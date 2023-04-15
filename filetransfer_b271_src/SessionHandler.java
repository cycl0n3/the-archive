/*-
 * Copyright (C) 2006 Erik Larsson
 * 
 * All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

import java.util.*;
import java.net.*;
import java.io.*;
import java.security.*;

/**
 * <code>SessionHandler</code> handles a connection to another peer. It kicks
 * in after the handshaking is done, so it doesn't need to know which one
 * connected first. It only sees a socket, representing the connection between
 * the two peers.
 */
public class SessionHandler {
    private static final int MAXIMUM_PACKET_SIZE = 10240;
    private final Socket connection;
    private final InetAddress boundAddress;
    private boolean started = false;
    private final PackageSender packageSender;
    private final RequestHandler currentRequestHandler;
    private final SessionMonitor monitor;
    private final FileTransfer main;
    private static int currentID = 0;
    private Package currentlyProcessing = null;
    
    /**
     * Creates a SessionHandler and initalizes its variables. Invoking this constructor does not start its operation as
     * listener/sender. To start the listener and package sender threads <code>start()</code> has to be invoked.
     */
    public SessionHandler(FileTransfer main, Socket connection, SessionMonitor monitor) throws IOException {
	this.main = main;
	this.connection = connection;
	this.monitor = monitor;
	boundAddress = connection.getInetAddress();
	currentRequestHandler = new RequestHandler(connection.getInputStream());
	packageSender = new PackageSender(connection.getOutputStream());
    }
    
    public InetAddress getAddress() {
	return boundAddress;
    }
    
    public Package[] getQueuedPackages() {
	return packageSender.packageQueue.toArray(new Package[packageSender.packageQueue.size()]);
    }

    public Package getCurrentlyProcessing() {
	return currentlyProcessing;
    }

    /**
     * Launches the threads used to handle remote requests and to send data to the other peer.
     */
    public void start() throws IOException {
	//monitor.displayMessageLine("start called");
	if(!started) {
	    started = true;
	    new Thread(currentRequestHandler).start();
	    new Thread(packageSender).start();
	}
	else throw new RuntimeException("Can't start the same SessionHandler more than once");
    }

    public void disconnect() {
	try {
	    connection.close();
	} catch(Exception e) {}
    }

    /**
     * This method takes the files in the argument and creates a package to send to the other peer.
     * It then puts the package in the package queue for transfer.
     * @param files a list of files and/or directories to send to the other peer. the directories
     *              will be traversed recursively to include all the contents of the entire tree.
     * @return the identification number of the request, used to track its progress
     */
    public Upload sendFTBLOBv1(File[] files) {
	HashMap<FileSystemEntry, File> fileMap = new HashMap<FileSystemEntry, File>();
	DirectoryEntry root = createDirectoryTree(files, fileMap);
	return sendFTBLOBv1(fileMap, root);
    }
    public Upload sendFTBLOBv1(HashMap<FileSystemEntry, File> fileMap, DirectoryEntry root) {
	try {
	    LinkedList<FileEntry> fileList = new LinkedList<FileEntry>();
	    byte[] contentsHeader = generateHeader(root, fileList);
	    long totalJobSize = contentsHeader.length;
	    for(FileEntry f : fileList)
		totalJobSize += (4 + f.getLength() + 32);
	    
	    Package filePackage = new Package(currentID++, "FTBLOBv1", totalJobSize);
	    /* File Transfer Binary Large OBject version 1 (to indicate that this package is a 
	     * single large object that is sent over the connection, and not divided in smaller 
	     * packages). */
	    filePackage.segments.addLast(new ByteArraySegment(contentsHeader));
	    FileSegment firstFileSegment = null;
	    HashMap<FileSegment, FileEntry> segmentEntryMap = new HashMap<FileSegment, FileEntry>();
	    int i = 0;
	    for(FileEntry fe : fileList) {
		File f = fileMap.get(fe);
		if(f == null) {
		    monitor.printMessageLine("ERROR: fileMap not complete! Contact developer immediately and kick his ass!");
		    continue;
		}
		filePackage.segments.addLast(new IntSegment(i++));
		filePackage.segments.addLast(new LongSegment(fe.getLength()));
		filePackage.segments.addLast(new Marker(Marker.RESET_DIGEST));
		FileSegment fs = new FileSegment(f);
		if(firstFileSegment == null)
		    firstFileSegment = fs;
		segmentEntryMap.put(fs, fe);
		filePackage.segments.addLast(fs);
		filePackage.segments.addLast(new Marker(Marker.PRINT_DIGEST));
	    }
	    Upload ul = new Upload(filePackage.requestID, filePackage.getLength(), segmentEntryMap);
	    filePackage.uploadInstance = ul;
	    ul.signalCurrentCompleted(firstFileSegment);
	    packageSender.queuePackage(filePackage);
	    return ul;
	} catch(IOException ioe) { throw new RuntimeException(ioe); }
    }
    
    private void parseFTBLOBv2(DigestInputStream is, long frameSize) throws IOException {
	//BufferedInputStream bufIn = new BufferedInputStream(is);
	DataInputStream dataIn = new DataInputStream(is);
	int numberOfDirectories = dataIn.readInt();
	int numberOfFiles = dataIn.readInt();

	DirectoryEntry[] dirEntries = new DirectoryEntry[numberOfDirectories];
	FileEntry[] fileEntries = new FileEntry[numberOfFiles];
	
	for(int i = 0; i < dirEntries.length; ++i)
	    dirEntries[i] = new DirectoryEntry();
	
	for(int i = 0; i < fileEntries.length; ++i)
	    fileEntries[i] = new FileEntry();
	
	for(int i = 0; i < dirEntries.length; ++i) {
	    int dirIndex = dataIn.readInt();
	    if(dirIndex != i)
		monitor.printMessageLine("WARNING: Received directory entries have not been packed sequentially. " +
					 "(i=" + i + " dirIndex=" + dirIndex + ")");
	    String dirName = dataIn.readUTF(); //Currently ignored.
	    
	    int dirDataLength = dataIn.readInt();	    
	    byte[] dirData = new byte[dirDataLength];
	    
	    DataInputStream arrayStream = new DataInputStream(new ByteArrayInputStream(dirData));
	    boolean stop = false;
	    while(!stop) {
		try {
		    String name = arrayStream.readUTF();
		    long lastModified = arrayStream.readLong();
		    boolean isDirectory = arrayStream.readBoolean();
		    int index = arrayStream.readInt();
		    if(isDirectory) {
			dirEntries[index].setLastModified(lastModified);
			dirEntries[index].setName(name);
			dirEntries[dirIndex].addDirectory(dirEntries[index]);
		    }
		    else {
			long fileSize = arrayStream.readLong();
			fileEntries[index].setLastModified(lastModified);
			fileEntries[index].setName(name);
			fileEntries[index].setLength(fileSize);
			dirEntries[dirIndex].addFile(fileEntries[index]);
		    }
		}
		catch(EOFException ee) {
		    stop = true;
		}
	    }
	}
    }
    
    private void parseFTBLOBv1(DigestInputStream is, long frameSize) throws IOException {
	FileOutputStream currentFileOutputStream = null;
	try {
	    DataInputStream dataIn = new DataInputStream(is);
	    int numberOfDirectories = dataIn.readInt();
	    int numberOfFiles = dataIn.readInt();
	
	    DirectoryEntry[] dirEntries = new DirectoryEntry[numberOfDirectories];
	    FileEntry[] fileEntries = new FileEntry[numberOfFiles];
	    FileSystemEntry[] fsEntries = new FileSystemEntry[numberOfDirectories+numberOfFiles];
	
	    for(int i = 0; i < dirEntries.length; ++i)
		dirEntries[i] = new DirectoryEntry();
	
	    for(int i = 0; i < fileEntries.length; ++i)
		fileEntries[i] = new FileEntry();

	    // Bugg: När man skickar med tomma kataloger så skapas de inte på klientsidan. Kolla upp detta..
	    
	    // Read directory data
	    int currentFseIndex = 0;
	    for(int i = 0; i < dirEntries.length; ++i) {
		int dirIndex = dataIn.readInt()-1;
		if(dirIndex != i)
		    monitor.printMessageLine("WARNING: Received directory entries have not been packed sequentially. " +
					     "(i=" + i + " dirIndex=" + dirIndex + ")");
	    
		if(dirIndex == 0) //All other directories get their names set by the listings of their parent directory
		    dirEntries[dirIndex].setName("/");

		String dirName = dataIn.readUTF(); //Currently ignored
	    
		int dirDataLength = dataIn.readInt();	    
		byte[] dirData = new byte[dirDataLength];
		dataIn.readFully(dirData);
	    
		DataInputStream arrayStream = new DataInputStream(new ByteArrayInputStream(dirData));
		boolean stop = false;
		while(!stop) {
		    try {
			String name = arrayStream.readUTF();
			long lastModified = arrayStream.readLong();
			boolean isDirectory = arrayStream.readBoolean();
			int index = arrayStream.readInt();
			if(isDirectory) {
			    dirEntries[index].setLastModified(lastModified);
			    dirEntries[index].setName(name);
			    dirEntries[index].setParent(dirEntries[dirIndex]);
			    dirEntries[dirIndex].addDirectory(dirEntries[index]);
			    fsEntries[currentFseIndex++] = dirEntries[index];
			}
			else {
			    long fileSize = arrayStream.readLong();
			    fileEntries[index].setLastModified(lastModified);
			    fileEntries[index].setName(name);
			    fileEntries[index].setLength(fileSize);
			    fileEntries[index].setParent(dirEntries[dirIndex]);
			    dirEntries[dirIndex].addFile(fileEntries[index]);
			    fsEntries[currentFseIndex++] = fileEntries[index];
			}
		    }
		    catch(EOFException ee) {
			stop = true;
		    }
		}
	    }

	    // Ask user for confirmation before receiving files...
	    File saveDir = monitor.confirmReceiveFiles(dirEntries[0]);

	    // If user wants to save files...
	    if(saveDir != null) {
		long size = 0;
		for(FileEntry fe : fileEntries)
		    size += fe.getLength();
		Download dl = new Download(size, fileEntries.length);
		monitor.monitorDownload(dl);
		dirEntries[0].makeTree(saveDir);
	
		byte[] buffer = new byte[4097]; //Instead of 4096... for the cache ^^ (I'm a moron)
		boolean overwriteAll = false;
		boolean skipAll = false;
		
		int i = 0;
		for(int j = 0; j < fsEntries.length; ++j) {
		    FileSystemEntry fse = fsEntries[j];
		    if(fse instanceof DirectoryEntry) {
			DirectoryEntry de = (DirectoryEntry) fse;
			monitor.printMessageLine("Processing directory entry: " + de.getName());
			if(de.makeTree(saveDir))
			    monitor.printMessageLine("  Created directory.");
			else
			    monitor.printMessageLine("  FAILED TO CREATE DIRECTORY!");
		    }
		    else if(fse instanceof FileEntry) {
			FileEntry fe = (FileEntry) fse; //fileEntries[i];
			monitor.printMessageLine("Processing file entry: " + fe.getName() + " Size: " + fe.getLength() + " B");
		
			int entryNumber = dataIn.readInt();
			if(entryNumber != i)
			    monitor.printMessageLine("  INCONSISTENCY IN INDICES (array: " + i + " entry: " + entryNumber + ")");
	    
			long fileSize = dataIn.readLong();
			if(fileSize != fe.getLength())
			    monitor.printMessageLine("  INCONSISTENCY IN FILESIZES (array: " + fe.getLength() + 
						     " entry: " + fileSize + ")");

			dl.signalNextFile(fe.getName(), fileSize);
		
			if(skipAll) {
			    skipBytes(dataIn, fileSize);
			    skipBytes(dataIn, 32); //checksum
			    continue;
			}

			DirectoryEntry current = fe.getParent();
			String path = "";
			while(current.parent != null) {
			    path = current.name + File.separator + path;
			    current = current.parent;
			}
			File fileDirectory = new File(saveDir, path);
			if(!fileDirectory.exists())
			    fileDirectory.mkdirs();
			else if(!fileDirectory.isDirectory()) {
			    monitor.printMessageLine("  Output directory: " + fileDirectory);
			    monitor.printMessageLine("  Could not create directory, since a file of the same name was present!");
			    monitor.printMessageLine("  Skipping...");
			    skipBytes(dataIn, fileSize);
			    skipBytes(dataIn, 32); //checksum
			    continue;		    
			}
			File file = new File(fileDirectory, fe.getName());
			if(!overwriteAll && file.exists()) {
			    int status = monitor.confirmOverwriteFile(file);
			    if(status == SessionMonitor.OVERWRITE_ALL)
				overwriteAll = true;
			    else if(status == SessionMonitor.SKIP) {
				skipBytes(dataIn, fileSize);
				skipBytes(dataIn, 32); //checksum
				continue;
			    }
			    else if(status == SessionMonitor.CANCEL) {
				skipBytes(dataIn, fileSize);
				skipBytes(dataIn, 32); //checksum
				skipAll = true;
				continue;
			    }
			}
			monitor.printMessageLine("  Output file: " + file.getCanonicalPath());
			FileOutputStream fos;
			try {
			    currentFileOutputStream = new FileOutputStream(file);
			    fos = currentFileOutputStream;
			} catch(Exception e) {
			    monitor.printMessageLine("  Failed to open output file: " + file);
			    monitor.printMessageLine("  Skipping...");
			    skipBytes(dataIn, fileSize);
			    skipBytes(dataIn, 32); //checksum
			    continue;		    
			}

			int bytesWritten = 0;
			long totalBytesRead = 0;
			is.getMessageDigest().reset();
			int bytesRead = dataIn.read(buffer, 0, (totalBytesRead >= (fileSize-buffer.length)?
								(int)(fileSize-totalBytesRead):buffer.length));
			totalBytesRead += bytesRead;
			while(totalBytesRead < fileSize && bytesRead != -1) {
			    fos.write(buffer, 0, bytesRead);
			    bytesWritten += bytesRead;
			    dl.addCurrentCompletedBytes(bytesRead);
			    bytesRead = dataIn.read(buffer, 0, (totalBytesRead >= (fileSize-buffer.length)?
								(int)(fileSize-totalBytesRead):buffer.length));
			    totalBytesRead += bytesRead;
			}
			if(bytesRead == -1)
			    throw new RuntimeException("Reached end of stream unexpectedly...");
			else {
			    fos.write(buffer, 0, bytesRead);
			    bytesWritten += bytesRead;
			    fos.close();
			    monitor.printMessageLine("  Received " + SpeedUnitUtils.bytesToBinaryUnit(totalBytesRead) + " bytes");
			    dl.addCurrentCompletedBytes(bytesRead);

			    long remainingBufferLength = totalBytesRead-fileSize;
			    if(remainingBufferLength != 0) //As this should never happen, the check is unneccessary ;)
				monitor.printMessageLine("  CRITICAL ERROR! Assertion remainingBufferLength == 0 failed!" +
							 " remainingBufferLength==" + remainingBufferLength); 

			    byte[] calculatedDigest = is.getMessageDigest().digest();		    
			    byte[] receivedDigest = new byte[32];
			    dataIn.readFully(receivedDigest);
			    if(!Main.byteArraysEqual(calculatedDigest, receivedDigest)) {
				monitor.printMessageLine("  WARNING: Detected incorrect checksum for file " + fe.getName());
				monitor.printMessageLine("   Received digest: " + Main.byteArrayToHexString(receivedDigest));
				monitor.printMessageLine("   Calculated digest: " + Main.byteArrayToHexString(calculatedDigest));
				monitor.printMessageLine("  As the file is most certainly corrupted, you should request it to" +
							 " be retransmitted.");
			    }
			}
			++i;
		    }
		}
		dl.signalTransferComplete();
	    }
	    else {
		for(int i = 0; i < fileEntries.length; ++i) {
		    int entryNumber = dataIn.readInt();
		    long fileSize = dataIn.readLong();
		    monitor.printMessageLine("Skipping entry " + entryNumber + " with size " + fileSize + " B...");
		    if(fileSize < 0) {
			monitor.printMessageLine(" ERROR: fileSize waaay too large. can only handle filesizes < " + 0x8fffffffffffffffL);
			monitor.printMessageLine(" Program will probably freak out by now... close connection and reconnect.");
		    }
		
		    skipBytes(dataIn, fileSize);
		    skipBytes(dataIn, 32); // Digest
		}
	    }
	    //monitor.reportFileTransferProgress(-1, -1);
	}
	catch(IOException ioe) {
	    if(currentFileOutputStream != null)
		currentFileOutputStream.close();
	    throw ioe;
	}
    }
    
    private static void skipBytes(DataInputStream dis, long bytesToSkip) throws IOException {
	long bytesSkipped = 0;
	long skipSteps = bytesToSkip/0x7fffffffL;
	for(int j = 0; j < skipSteps; ++j) {
	    bytesSkipped = 0;
	    while(bytesSkipped < 0x7fffffff)
		bytesSkipped += dis.skipBytes(0x7fffffff);
	}
	int remainingBytes = (int)(bytesToSkip - skipSteps*0x7fffffff);
	bytesSkipped = 0;
	while(bytesSkipped < remainingBytes)
	    bytesSkipped += dis.skipBytes(remainingBytes);
    }

    /**
     * @return true if the frame data was valid, false if not.
     */
    private boolean processFrameData(String frameType, long frameSize, DigestInputStream in) {
	//digestStream.getMessageDigest().reset();
	try {
	    if(frameType.equals("$CC")) {
		handleControlCommand(frameSize, in);
		return true;
	    }
	    else if(frameType.equals("FTBLOBv1")) {
		parseFTBLOBv1(in, frameSize);
		return true;
	    }
	    else {
		long skippedBytes = 0;
		while(skippedBytes < frameSize)
		    skippedBytes += in.skip(frameSize);
		return false;
	    }
	} catch(IOException ioe) { ioe.printStackTrace(); return false; }
    }
    
    private void handleControlCommand(long frameSize, DigestInputStream in) throws IOException {
	DataInputStream dataIn = new DataInputStream(in);
	String command = dataIn.readUTF();
	if(command.equals("CLOSE")) {
	    try {
		connection.close();
	    } catch(IOException ioe) {}
	}
	else if(command.equals("INIT_FTBLOBv2")) {
	    // Initiate a BLOB-type file transfer
	    //BufferedInputStream bufIn = new BufferedInputStream(is);
	    //DataInputStream dataIn = new DataInputStream(is);
// 	    int numberOfDirectories = dataIn.readInt();
// 	    int numberOfFiles = dataIn.readInt();
// 	    //monitor.printMessageLine("numberOfDirectories=" + numberOfDirectories);
// 	    DirEntry[] dirEntries = new DirEntry[numberOfDirectories];
// 	    for(int i = 0; i < numberOfDirectories; ++i) {
// 		int dirIndex = dataIn.readInt();
// 		String dirName = dataIn.readUTF();
// 		int dirDataLength = dataIn.readInt();
// 		byte[] dirData = new byte[dirDataLength];
// 		dataIn.readFully(dirData);
// 		dirEntries[i] = new DirEntry(dirIndex, dirName, dirDataLength, dirData);
// 		//monitor.printMessageLine("Received dirEntry: " + dirEntries[i].toString());
// 	    }
// 	    ParsedDirectory[] directories = new ParsedDirectory[dirEntries.length];
// 	    for(int i = 0; i < directories.length; ++i)
// 		directories[i] = new ParsedDirectory(dirEntries[i].dirName, dirEntries[i].dirDataLength);
	
// 	    ParsedFileEntry[] fileEntryList = new ParsedFileEntry[numberOfFiles];
// 	    for(int i = 0; i < directories.length; ++i) {
// 		DataInputStream arrayStream = new DataInputStream(new ByteArrayInputStream(dirEntries[i].dirData));
// 		while(true) {
// 		    try {
// 			String name = arrayStream.readUTF();
// 			long lastModified = arrayStream.readLong();
// 			boolean isDirectory = arrayStream.readBoolean();
// 			int index = arrayStream.readInt();
// 			if(isDirectory)
// 			    directories[i].addDirectory(name, lastModified, directories[index]);
// 			else {
// 			    long fileSize = arrayStream.readLong();
// 			    fileEntryList[index] = directories[i].addFile(name, lastModified, index, fileSize);
// 			}
// 		    }
// 		    catch(EOFException ee) {
// 			break;
// 		    }
// 		}
// 	    }
	}
	    
    }

    private static byte[] generateHeader(DirectoryEntry dir, LinkedList<FileEntry> fileList) throws IOException {
	//Directory[] fullDirectoryList = findAllDirectoriesDFS(dir);
	int dirCount = countAllDirectoriesInTreeDFS(dir) + 1; //Where the extra directory is "/", rootdir
	int fileCount = countAllFilesInTreeDFS(dir);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);
	dos.writeInt(dirCount);
	dos.writeInt(fileCount);
	LinkedList<DirectoryEntry> directoryQueue = new LinkedList<DirectoryEntry>();
	directoryQueue.addLast(dir);
	int currentDirIndex = 1;
	int currentFileIndex = 0;
	int thisDirIndex = 1;
	while(!directoryQueue.isEmpty()) {
	    DirectoryEntry current = directoryQueue.removeFirst();
	    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
	    DataOutputStream dos2 = new DataOutputStream(baos2);
	    for(DirectoryEntry next : current.getDirectories()) {
		directoryQueue.addLast(next);
		dos2.writeUTF(next.getName());
		dos2.writeLong(next.getLastModified());
		dos2.writeBoolean(true); //Indikerar att det är en katalog
		dos2.writeInt(currentDirIndex++);
	    }
	    for(FileEntry f : current.getFiles()) {
		fileList.addLast(f);
		dos2.writeUTF(f.getName());
		dos2.writeLong(f.getLastModified());
		dos2.writeBoolean(false); //Indikerar att det inte är en katalog
		dos2.writeInt(currentFileIndex++);
		dos2.writeLong(f.getLength()); //Finns endast då entryt hänvisar till en fil
	    }
	    byte[] dirdata = baos2.toByteArray();
	    
	    dos.writeInt(thisDirIndex++);
// 	    if(current.getParent() != null)
	    dos.writeUTF(current.getName());
// 	    else
// 		dos.writeUTF("/");
	    dos.writeInt(dirdata.length);
	    dos.write(dirdata);
	}
	return baos.toByteArray();
    }

    private static int countAllDirectoriesInTreeDFS(DirectoryEntry dir) {
	int dirCount = 0;
	for(DirectoryEntry currentDir : dir.getDirectories()) {
	    ++dirCount;
	    dirCount += countAllDirectoriesInTreeDFS(currentDir);
	}
	return dirCount;
    }
    private static int countAllFilesInTreeDFS(DirectoryEntry dir) {
	int fileCount = 0;
	for(FileEntry currentFile : dir.getFiles())
	    ++fileCount;
	    
	for(DirectoryEntry currentDir : dir.getDirectories())
	    fileCount += countAllFilesInTreeDFS(currentDir);
	
	return fileCount;
    }
    
    /**
     * @param files the files used to generate the tree.
     * @param fileMap the map to store bindings to the actual File objects from the generated 
     *                FileSystemEntries. This parameter can safely be <code>null</code>.
     * @return the root directory for the tree.
     */
    public static DirectoryEntry createDirectoryTree(File[] files, Map<FileSystemEntry, File> fileMap) {
	DirectoryEntry dir = _createDirectoryTree(files, fileMap);
	dir.setParent(null);
	dir.setHidden(false);
	dir.setLastModified(0);
	dir.setReadOnly(false);
	dir.setName("/");
	return dir;
    }
    
    private static DirectoryEntry _createDirectoryTree(File[] files, Map<FileSystemEntry, File> fileMap) {
	DirectoryEntry currentDir = new DirectoryEntry();
	
	LinkedList<File> fileList = new LinkedList<File>();
	LinkedList<File> dirList = new LinkedList<File>();
	for(File f : files) {
	    if(f.isDirectory())
		dirList.addLast(f);
	    else if(f.isFile() && f.canRead())
		fileList.addLast(f);
	    else; //Ignore special files...
	}
	
	for(File f : fileList) {
	    FileEntry fe = new FileEntry();
	    if(fileMap != null)
		fileMap.put(fe, f);
	    fe.setParent(currentDir);
	    fe.setHidden(f.isHidden());
	    fe.setLength(f.length());
	    fe.setLastModified(f.lastModified());
	    fe.setReadOnly(!f.canWrite());
	    fe.setName(f.getName());
	    currentDir.addFile(fe);
	}

	for(File f : dirList) {
	    DirectoryEntry dir = _createDirectoryTree(f.listFiles(), fileMap);
	    if(fileMap != null)
		fileMap.put(dir, f);
	    dir.setParent(currentDir);
	    dir.setHidden(f.isHidden());
	    dir.setLastModified(f.lastModified());
	    dir.setReadOnly(!f.canWrite());
	    dir.setName(f.getName());
	    currentDir.addDirectory(dir);
	}
	return currentDir;
    }
    
//     private String readUTF8StringLeadingSize(InputStream is) {
// 	DataInputStream dis = new DataInputStream(is);
// 	int stringSize = dis.readShort() & 0xFFFF;
// 	byte[] utf8Data = new byte[stringSize];
// 	int bytesRead = 0;
// 	while(bytesRead < stringSize)
// 	    bytesRead += dis.read(utf8Data, bytesRead, stringSize-bytesRead);
// 	return new String(utf8Data, "UTF-8");
//     }

//     private void writeUTF8StringLeadingSize(OutputStream os, String s) {
// 	DataOutputStream dos = new DataOutputStream(os);
// 	byte[] data = s.getBytes("UTF-8");
// 	if(data.length > 0xFFFF)
// 	    throw new RuntimeException("Length of string is out of range");
// 	dos.writeShort(data.length & 0xFFFF);
//     }
    
    // Inner classes

   
    // Packages and segments
    
    public static class Package {
	public final int requestID;
	public final String identifier;
	public final long packageSize;
	public final LinkedList<Segment> segments = new LinkedList<Segment>();
	public long bytesSent = 0; // This should go...
	public Upload uploadInstance;
	
	public Package(int requestID, String identifier, long packageSize) {
	    this.requestID = requestID;
	    this.identifier = identifier;
	    this.packageSize = packageSize;
	}
	
	public long getLength() {
	    long result = 0;
	    for(Segment s : segments)
		result += s.getLength();
	    return result;
	}
    }

    public static abstract class Segment {
	public abstract long getLength();
	public abstract InputStream getInputStream() throws IOException;
	public abstract String getSegmentType();
    }
    
    public static class Marker extends Segment {
	
	public static final int RESET_DIGEST = 0;
	public static final int PRINT_DIGEST = 1;
	public int type;

	public Marker(int type) {
	    this.type = type;
	}
	public long getLength() { return 0; }
	public InputStream getInputStream() throws IOException {
	    throw new IOException("Not supported on Markers...");
	}
	
	public String getSegmentType() {
	    return "Marker Type " + type;
	}
    }
    
    public static class FileSegment extends Segment {
	private File f;
	private ByteCountInputStream byteCounter;
	public FileSegment(File f) {
	    this.f = f;
	}
	public long getLength() { return f.length(); }
	public InputStream getInputStream() throws IOException {
	    byteCounter = new ByteCountInputStream(new FileInputStream(f));
	    return byteCounter;
	}
	public long getBytesRead() {
	    if(byteCounter != null)
		return byteCounter.getBytesRead();
	    else
		return 0;
	}
	public String getSegmentType() {
	    return "FileSegment(" + f + ")";
	}
    }
    
    public static class ByteArraySegment extends Segment {
	protected byte[] data;
	protected ByteArraySegment() {}
	public ByteArraySegment(byte[] data) {
	    this.data = data;
	}
	public long getLength() { return data.length; }
	public InputStream getInputStream() throws IOException {
	    return new ByteArrayInputStream(data);
	}	
	public String getSegmentType() {
	    return "ByteArraySegment";
	}
    }

    public static class IntSegment extends ByteArraySegment {
	public IntSegment(int i) throws IOException {
	    ByteArrayOutputStream laban = new ByteArrayOutputStream();
	    DataOutputStream olav = new DataOutputStream(laban);
	    olav.writeInt(i);
	    data = laban.toByteArray();
	}
	public String getSegmentType() {
	    return "IntSegment";
	}
    }
    
    public static class LongSegment extends ByteArraySegment {
	public LongSegment(long i) throws IOException {
	    ByteArrayOutputStream laban = new ByteArrayOutputStream();
	    DataOutputStream olav = new DataOutputStream(laban);
	    olav.writeLong(i);
	    data = laban.toByteArray();
	}
	public String getSegmentType() {
	    return "LongSegment";
	}
    }
    
    /**
     * Class intended for running the thread that handles incoming packages.
     */
    private class RequestHandler implements Runnable {
	private boolean abort = false;
	private InputStream inStream;

	public RequestHandler(InputStream inStream) {
	    this.inStream = inStream;
	}

	public void run() {
	    //monitor.printMessageLine("requestHandler started");
	    int framesRead = 0;
	    byte[] frameTypeData = new byte[8];
	    DigestInputStream digestStream = null;
	    try {
		digestStream = new DigestInputStream(inStream, MessageDigest.getInstance("SHA-256"));
	    } catch(NoSuchAlgorithmException nsae) {
		nsae.printStackTrace();
		String msg = "Couldn't find an implementation of the SHA-256 algorithm.\nRequestHandler could not run()...";
		reportError(msg);
		main.reportGeneralError(msg);
		return;
	    }
	    DataInputStream dis = new DataInputStream(digestStream);
	    try {
		while(!abort) {
		    ++framesRead;
		    //digestStream.getMessageDigest().reset();
		    dis.readFully(frameTypeData);
		    String frameType = new String(frameTypeData, "US-ASCII");
		    long frameSize = dis.readLong();
		    if(!processFrameData(frameType, frameSize, digestStream)) {
			reportError("Incorrect checksum for frame " + framesRead + " frameType: " +
				    frameType + " frameSize: " + frameSize);
		    }
		    else
			reportError("Successfully received frame " + framesRead + " frameType: " +
				    frameType + " frameSize: " + frameSize);
		}
	    }
	    catch(Exception e) {
		//e.printStackTrace();
		monitor.signalConnectionLost();
	    }
	}
	
	public boolean equals(byte[] a, byte[] b) {
	    if(a.length != b.length)
		return false;
	    else {
		for(int i = 0; i < a.length; ++i) {
		    if(a[i] != b[i])
			return false;
		}
		return true;
	    }
	}

	public void reportError(String message) {
	    // This is only a stub, for testing.
	    System.err.println(message);
	}

    }

    /**
     * Class intended for running the thread that handles outgoing packages.
     */
    private class PackageSender implements Runnable {
	private boolean abort = false;
	public LinkedList<Package> packageQueue = new LinkedList<Package>();
	private LinkedList<Package> priorityPackages = new LinkedList<Package>();
	private BufferedOutputStream buf;
	private DigestOutputStream digestOut;
	private DataOutputStream out;
	
	public PackageSender(OutputStream out) {
	    buf = new BufferedOutputStream(out);
	    try {
		digestOut = new DigestOutputStream(buf, MessageDigest.getInstance("SHA-256"));
	    } catch(NoSuchAlgorithmException nsae) {
		nsae.printStackTrace();
		System.err.println("Couldn't find an implementation of the SHA-256 algorithm.");
		throw new RuntimeException();
	    }
	    
	    this.out = new DataOutputStream(digestOut);
	}
	
	public void queuePackage(Package p) {
	    synchronized(this) {
		packageQueue.addLast(p);
		notify();
	    }
	}
	
	public void run() {
	    //monitor.printMessageLine("PackageSender started");
	    byte[] readBuffer = new byte[4096];
	    while(!abort) {
		//monitor.printMessageLine("Checking for new packages...");
		synchronized(this) {
		    while(packageQueue.isEmpty() && priorityPackages.isEmpty()) {
			try {
			    //monitor.printMessageLine("No new package found... waiting.");
			    wait();
			} catch(InterruptedException ie) { ie.printStackTrace(); }
		    }
		}
		//monitor.printMessageLine("New package found! Transmitting...");
		try {
		    currentlyProcessing = packageQueue.removeFirst();
		    Package current = currentlyProcessing;
		    byte[] identifierBytes = current.identifier.getBytes("US-ASCII");
		    out.write(identifierBytes);
		    current.bytesSent += identifierBytes.length;
		    out.writeLong(current.packageSize);
		    current.bytesSent += 8;
		    for(Segment seg : current.segments) {
			if(seg instanceof Marker) {
			    Marker m = (Marker)seg;
			    if(m.type == Marker.RESET_DIGEST) {
				//monitor.printMessageLine("Resetting digest");
				digestOut.getMessageDigest().reset();
			    }
			    else if(m.type == Marker.PRINT_DIGEST) {
				byte[] digest = digestOut.getMessageDigest().digest();
				out.write(digest);
				//monitor.printMessageLine("PackageSender wrote digest of length " + digest.length);
				//monitor.printMessageLine("Send checksum: " + Main.byteArrayToHexString(digest));

			    }
			}
			else {
			    if(seg instanceof FileSegment)
				current.uploadInstance.signalCurrentCompleted((FileSegment)seg);
			    

			    InputStream is = seg.getInputStream();
			    long totalBytesRead = 0;
			    long segmentLength = seg.getLength();
			    int bytesRead = is.read(readBuffer, 0, (totalBytesRead >= (segmentLength-readBuffer.length)?
								    (int)(segmentLength-totalBytesRead):readBuffer.length));
			    totalBytesRead += bytesRead;
			    while(totalBytesRead < segmentLength && bytesRead != 1) {
				out.write(readBuffer, 0, bytesRead);
				current.bytesSent += bytesRead;
				bytesRead = is.read(readBuffer, 0, (totalBytesRead >= (segmentLength-readBuffer.length)?
								    (int)(segmentLength-totalBytesRead):readBuffer.length));
				totalBytesRead += bytesRead;
			    }
			    is.close();
			    if(bytesRead == -1)
				throw new RuntimeException("Reached end of stream unexpectedly...");
			    else {
				out.write(readBuffer, 0, bytesRead);
				current.bytesSent += bytesRead;
			    }


// 			    InputStream is = seg.getInputStream();
// 			    int bytesRead = is.read(readBuffer);
// 			    int totalBytesRead = bytesRead;
// 			    while(bytesRead == readBuffer.length) {
// 				out.write(readBuffer);
// 				current.bytesSent += bytesRead;
// 				bytesRead = is.read(readBuffer);
// 				totalBytesRead += bytesRead;
// 			    }
// 			    if(bytesRead != -1) {
// 				out.write(readBuffer, 0, bytesRead);
// 				current.bytesSent += bytesRead;
// 			    }
// 			    monitor.printMessageLine("PackageSender: Total bytes read: " + totalBytesRead);
			}
		    }
		    current.uploadInstance.signalTransferComplete();
		    buf.flush();
		}
		catch(IOException ioe) {
		    //ioe.printStackTrace();
		    //System.err.println("PANIC! SessionHandler.PackageSender exited unexpectedly!");
		    monitor.signalConnectionLost();
		    abort = true;
		}
		currentlyProcessing = null;
	    }
	}
    }
}
