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

import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class ConnectionMonitorPanel extends JPanel implements SessionMonitor {
    private final String address;
    private final MainWindow parent;
    private int indexInParent;
    private boolean initialized = false;
    private boolean connectionLost = false;
    public static boolean displayBitRate = true;
    public static boolean displayByteRate = false;
    private final ConnectedPanel connectedPanel;
    private final JLabel connectingLabel = new JLabel("Connecting...", SwingConstants.CENTER);
    private final CardLayout layout;
    private SessionHandler currentHandler = null;
    private TransferMonitor currentTransferMonitor;
    private PackageQueueWindow pqWindow;// = new PackageQueueWindow(this);
    //private Vector<OutgoingFileTransfer> outgoingFileTransfers = new Vector<OutgoingFileTransfer>();
    
    /** Just used for layout-testing */
    ConnectionMonitorPanel() {
	address = null;
	parent = null;
	this.layout = null;
	setLayout(new BorderLayout());
	setBorder(new EmptyBorder(10, 10, 10, 10));
	connectedPanel = new ConnectedPanel();
	
	setOpaque(false);
	add(connectedPanel, BorderLayout.CENTER);

	//pqWindow = new PackageQueueWindow(this);
	pqWindow = null;
    }

    public ConnectionMonitorPanel(String address, MainWindow parent, int indexInParent) {
	this.address = address;
	this.parent = parent;
	this.indexInParent = indexInParent;
	this.layout = new CardLayout();
	setLayout(layout);
	setBorder(new EmptyBorder(10, 10, 10, 10));
	connectedPanel = new ConnectedPanel();
// 	connectedPanel.addSendFileListener(new ActionListener() {
// 		public void actionPerformed(ActionEvent e) {
// 		    sendFileSignaled();
// 		}
// 	    });
	connectedPanel.addSendFilesListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    sendFilesSignaled();
		}
	    });
	//connectedPanel.setVisible(false);
	setOpaque(false);
	add(connectedPanel, "connectedPanel");
	add(connectingLabel, "connectingLabel");
	layout.show(this, "connectingLabel");
	currentTransferMonitor = new TransferMonitor();
	currentTransferMonitor.start();
	//pqWindow = new PackageQueueWindow(this); //initialized later, when a ConnectionAttempt is successful
    }

    public ConnectionMonitorPanel(Socket s, MainWindow parent, int indexInParent) throws IOException {
	this.currentHandler = new SessionHandler(parent.getFileTransfer(), s, this);
	this.initialized = true;
	this.address = s.getInetAddress().getHostName();
	this.parent = parent;
	this.indexInParent = indexInParent;
	this.layout = new CardLayout();
	setLayout(new BorderLayout());
	setBorder(new EmptyBorder(10, 10, 10, 10));
	connectedPanel = new ConnectedPanel();
// 	connectedPanel.addSendFileListener(new ActionListener() {
// 		public void actionPerformed(ActionEvent e) {
// 		    sendFileSignaled();
// 		}
// 	    });
	connectedPanel.addSendFilesListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    sendFilesSignaled();
		}
	    });
	setOpaque(false);
	add(connectedPanel, BorderLayout.CENTER);
	currentHandler.start();
	currentTransferMonitor = new TransferMonitor();
	currentTransferMonitor.start();
	pqWindow = new PackageQueueWindow(this, true);
    }
    
    /**
     * Should be called prior to removal from the graphical layout. Performs cleanup operations.
     * Currently only one; closing the associated PackageQueueWindow.
     */
    public void removed() {
	if(pqWindow != null)
	    pqWindow.setVisible(false);
    }

    public MainWindow getMainWindow() {
	return parent;
    }
    
    public PackageQueueWindow getPackageQueueWindow() {
	return pqWindow;
    }
    
//     public SessionHandler getCurrentHandler() {
// 	return currentHandler;
//     }
    
    public InetAddress getAddress() {
	if(currentHandler == null)
	    return null;
	else
	    return currentHandler.getAddress();
    }

    public SessionHandler getSessionHandler() {
	return currentHandler;
    }
    
    public void disconnect() {
	currentHandler.disconnect();
    }
    
    public synchronized void signalConnectionLost() {
	if(!connectionLost) {
	    connectionLost = true;
	    displayConnectionLostMessage(this, currentHandler.getAddress());
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			parent.removeTab(ConnectionMonitorPanel.this);
		    }
		});
	}
    }

    public static void displayConnectionLostMessage(final Component parentComponent, final InetAddress address) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    JOptionPane.showMessageDialog(parentComponent, 
						  "Connection to " + address.getHostName() 
						  + "/" + address.getHostAddress() + " was lost.", 
						  "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	    });
	
    }    

    /**
     * @return the directory where the received files are to be save.
     */
    public File confirmReceiveFiles(final DirectoryEntry root) {
	//final ObjectContainer<Integer> oc = new ObjectContainer<Integer>();
	final ObjectContainer<File> saveDirContainer = new ObjectContainer<File>();
	final StringBuilder message = new StringBuilder();
	final int maxEntries = 10;
	
	LinkedList<DirectoryEntry> visitQueue = new LinkedList<DirectoryEntry>();
	visitQueue.addLast(root);
	int entriesAdded = 0;
	while(!visitQueue.isEmpty()) {
	    DirectoryEntry current = visitQueue.removeFirst();
	    for(DirectoryEntry de : current.getDirectories()) {
		visitQueue.addLast(de);
	    }
	    for(FileEntry fe : current.getFiles()) {
		if(entriesAdded >= maxEntries) {
		    visitQueue.clear();
		    ++entriesAdded;
		    break;
		}
		String currentPath = current.getPath();
		if(!currentPath.equals("")) {
		    message.append(currentPath);
		    message.append(File.separator);
		}
		message.append(fe.getName());
		message.append("\n");
		++entriesAdded;
	    }
	}
	if(entriesAdded == 1)
	    message.insert(0, " is requesting to send a file to you:\n");
	else if(entriesAdded == 0)
	    message.append(" is requesting to send an empty directory structure to you.\n");
	else if(entriesAdded > maxEntries) 
	    message.insert(0, " is requesting to send some files to you, including:\n");
	else
	    message.insert(0, " is requesting to send the following files to you:\n");
	
	message.insert(0, address);
	
	if(entriesAdded > maxEntries)
	    message.append("...\n");
	message.append("Will you accept the transfer?");
	
	Runnable r = new Runnable() {
		public void run() {
		    parent.setActiveTab(ConnectionMonitorPanel.this);
		    parent.setVisible(true);
		    int res = JOptionPane.showConfirmDialog(ConnectionMonitorPanel.this, 
							    message.toString(), "Recieve confirmation", 
							    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if(res == JOptionPane.YES_OPTION) {
			JFileChooser saveDirChooser = parent.getSaveDirChooser();
			if(saveDirChooser.showSaveDialog(ConnectionMonitorPanel.this) == JFileChooser.APPROVE_OPTION)
			    saveDirContainer.object = saveDirChooser.getSelectedFile();
		    }
		    //oc.object = res;
		}
	    };
	try {
	    SwingUtilities.invokeAndWait(r);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	if(saveDirContainer.object != null && saveDirContainer.object.isDirectory())
	    return saveDirContainer.object; //oc.object.intValue() == JOptionPane.YES_OPTION;
	else return null;
    }

    public int confirmOverwriteFile(final File file) {
	final ObjectContainer<Integer> oc = new ObjectContainer<Integer>();
	Runnable r = new Runnable() {
		public void run() {
		    parent.requestFocus();
		    int result = JOptionPane.showConfirmDialog(ConnectionMonitorPanel.this,
							       "Attempting to recieve a file that already exists:\n" + 
							       file + "\nDo you want to overwrite the existing file?", 
							       "Overwrite confirmation", JOptionPane.YES_NO_CANCEL_OPTION,
							       JOptionPane.QUESTION_MESSAGE);
		    oc.object = result;
		}
	    };
	try {
	    SwingUtilities.invokeAndWait(r);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	int result = oc.object.intValue();
	if(result == JOptionPane.YES_OPTION)
	    return OVERWRITE;
	else if(result == JOptionPane.NO_OPTION)
	    return SKIP;
	else if(result == JOptionPane.CANCEL_OPTION)
	    return CANCEL;
	else return SKIP;
    }
    
//     public void reportUploadStarted(Upload ul) {
// 	currentUploadMonitor.startMonitoring(ul);
//     }
//     public void reportUploadFinished(Upload ul) {}
//     public void reportDownloadStarted(Download ul) {}
//     public void reportDownloadFinished(Download ul) {}
    
//     public void reportFileTransferProgress(final long bytesCompleted, final long totalBytes) {
// 	// I'm NOT using invokeLater here, even though I should. I have concluded though, that
// 	// the lag introduced when using invokeLater is not esthetic, and the methods setText,
// 	// setValue don't appear to do any harm when executed outside the event loop context.
// 	if(bytesCompleted == -1 && totalBytes == -1) {
// 	    connectedPanel.setDownloadProgress(0);
// 	    connectedPanel.setDownloadStatusText("Idle...");
// 	}
// 	else {
// 	    final double value = ((double)bytesCompleted)/totalBytes;
// 	    connectedPanel.setDownloadProgress(value);
// 	    connectedPanel.setDownloadStatusText(SpeedUnitUtils.bytesToBinaryUnit(bytesCompleted) + " / " + SpeedUnitUtils.bytesToBinaryUnit(totalBytes));
// 	}
//     }
    
    public void printMessageLine(String s) {
	printMessage(s + System.getProperty("line.separator"));
    }
    public void printMessage(String s) {
	connectedPanel.printMessage(s);
    }

    public void initialize() {
	if(!initialized) {
	    initialized = true;
	    new Thread(new ConnectionAttempt()).start();
	    //new ConnectionAttempt().start();
	}
    }
    
    public void setIndexInParent(int newIndex) {
	indexInParent = newIndex;
    }

    public void oldSendFilesSignaled() {
	File[] selectedFiles = getFilesFromUser(this);
	if(selectedFiles != null) {
	    if(selectedFiles.length > 0)
		sendFiles(selectedFiles);
	    else
		JOptionPane.showMessageDialog(this, "No files found.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    public void sendFilesSignaled() {
	if(parent.useOldSendFilesDialog())
	    oldSendFilesSignaled();
	else {
	    SelectFilesDialog sfd = new SelectFilesDialog(this);
	    sfd.setVisible(true);
	}
    }

    public File[] getFilesFromUser(Component parentComponent) {
	JFileChooser sendFileChooser = parent.getSendFilesChooser();
	if(sendFileChooser.showOpenDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
	    return sendFileChooser.getSelectedFiles();
	}
	else
	    return null;
    }
    
    public void sendFiles(File[] files) {
	Upload ul = currentHandler.sendFTBLOBv1(files);
	currentTransferMonitor.startMonitoring(ul);
    }
    public void sendFilesPreprocessed(HashMap<FileSystemEntry, File> fileMap, DirectoryEntry root) {
	Upload ul = currentHandler.sendFTBLOBv1(fileMap, root);
	currentTransferMonitor.startMonitoring(ul);
    }
    
    public void monitorDownload(Download dl) {
	currentTransferMonitor.startMonitoring(dl);
    }

    /** The word projected is probably not suitable, but I needed to pick another
     * name than preferred, since that had already been spoken for... */
    public static Dimension getProjectedSize() {
	return new ConnectedPanel().getPreferredSize();
    }
    
    private class TransferMonitor {
	private static final int POLL_INTERVAL = 50; //ms
	private boolean abort = false;
	private boolean monitorUploads = false;
	private LinkedList<Upload> monitoredUploads = new LinkedList<Upload>();
	private boolean monitorDownloads = false;
	private Download monitoredDownload = null;
	private int uploadMeasurementPointer = 0;
	private long[] uploadSpeedMeasurements;
	private long[] uploadTimeStamps;
	private int downloadMeasurementPointer = 0;
	private long[] downloadSpeedMeasurements;
	private long[] downloadTimeStamps;
	
	public TransferMonitor() {
	    resetUploadMeasurements();
	    resetDownloadMeasurements();
	}
	public void resetUploadMeasurements() {
	    uploadSpeedMeasurements = new long[200];
	    uploadTimeStamps = new long[uploadSpeedMeasurements.length];
	    for(int i = 0; i < uploadSpeedMeasurements.length; ++i) {
		uploadSpeedMeasurements[i] = -1;
		uploadTimeStamps[i] = -1;
	    }
	}

	public void resetDownloadMeasurements() {
	    downloadSpeedMeasurements = new long[200];
	    downloadTimeStamps = new long[downloadSpeedMeasurements.length];
	    for(int i = 0; i < downloadSpeedMeasurements.length; ++i) {
		downloadSpeedMeasurements[i] = -1;
		downloadTimeStamps[i] = -1;
	    }
	}
	
	public synchronized void start() {
	    new Thread(new Runnable() {
		    public void run() {
			mainLoop();
		    }
		}).start();
	}
	
	public synchronized void stop() {
	    abort = true;
	    notify();
	    try {
		wait();
	    } catch(InterruptedException ie) {}
	}
	
	public void mainLoop() {
	    String uploadFilename = null;
	    String downloadFilename = null;
	    long lastUploadSpeedUpdate = 0;
	    long lastDownloadSpeedUpdate = 0;
	    while(!abort) {
		Transfer ul = null;
		try {
		    synchronized(this) {
			ul = monitoredUploads.getFirst();
			// This code should not be neccessary now
// 			if(ul != null && ul.transferComplete()) {
// 			    Upload removed = null;
// 			    try { removed = monitoredUploads.removeFirst(); }
// 			    catch(NoSuchElementException nsee) {
// 				nsee.printStackTrace();
// 			    }
// 			    if(removed != ul)
// 				System.err.println("WARNING: ConnectionMonitorPanel.TransferMonitor.mainLoop() ul != removed");

// 			    ul = null;
// 			    ul = monitoredUploads.getFirst();
// 			}
		    }
		} catch(NoSuchElementException nsee) {}
		Transfer dl = monitoredDownload;
		
		if(monitorUploads && ul != null) {
		    if(!ul.transferComplete()) {
			String currentFilename = ul.getCurrentFilename();
			long currentLength = ul.getCurrentLength();
			double currentProgress = ul.getCurrentProgress();
			long currentCompleted = ul.getCurrentCompletedBytes();
			long currentTimeStamp = System.currentTimeMillis();

			String statusText = "file: " + currentFilename + " (" +
			    SpeedUnitUtils.bytesToBinaryUnit(currentCompleted) + " / " +
			    SpeedUnitUtils.bytesToBinaryUnit(currentLength) + ")";

			if(uploadFilename != currentFilename) {
			    resetUploadMeasurements();
			    uploadFilename = currentFilename;
			}

			uploadMeasurementPointer = (uploadMeasurementPointer + 1) % uploadSpeedMeasurements.length;
			uploadSpeedMeasurements[uploadMeasurementPointer] = currentCompleted;
			uploadTimeStamps[uploadMeasurementPointer] = currentTimeStamp;

			statusText = "Sending " + statusText;// + " " + getUploadSpeed();
			connectedPanel.setUploadProgress(currentProgress);
			connectedPanel.setUploadStatusText(statusText);
			
			if(currentTimeStamp-lastUploadSpeedUpdate > 200) {
			    lastUploadSpeedUpdate = currentTimeStamp;
			    connectedPanel.setUploadSpeedText(getUploadSpeed());
			    //When monitoring uploads, we'd better also update the PackageQueueWindow...
			    pqWindow.refresh();
			}
		    }
		    else {
			synchronized(this) {
			    connectedPanel.setUploadProgress(0);
			    connectedPanel.setUploadStatusText("Transfer complete!");
			    monitorUploads = false;
			    try {
				Upload ou = monitoredUploads.removeFirst();
				if(ul != ou)
				    System.err.println("WARNING: ConnectionMonitorPanel.TransferMonitor.mainLoop() ul != ou");
			    }
			    catch(NoSuchElementException nsee) {
				nsee.printStackTrace();
			    }
			}
		    }
		}
		if(monitorDownloads && dl != null) {
		    if(!dl.transferComplete()) {
			String currentFilename = dl.getCurrentFilename();
			long currentLength = dl.getCurrentLength();
			double currentProgress = dl.getCurrentProgress();
			long currentCompleted = dl.getCurrentCompletedBytes();
			long currentTimeStamp = System.currentTimeMillis();
		
			String statusText = "file: " + currentFilename + " (" + 
			    SpeedUnitUtils.bytesToBinaryUnit(currentCompleted) + " / " + 
			    SpeedUnitUtils.bytesToBinaryUnit(currentLength) + ")";

			if(downloadFilename != currentFilename) {
			    resetDownloadMeasurements();
			    downloadFilename = currentFilename;
			}

			downloadMeasurementPointer = (downloadMeasurementPointer + 1) % downloadSpeedMeasurements.length;
			downloadSpeedMeasurements[downloadMeasurementPointer] = currentCompleted;
			downloadTimeStamps[downloadMeasurementPointer] = currentTimeStamp;
			
			statusText = "Receiving " + statusText;
			connectedPanel.setDownloadProgress(currentProgress);
			connectedPanel.setDownloadStatusText(statusText);

			if(currentTimeStamp-lastDownloadSpeedUpdate > 200) {
			    lastDownloadSpeedUpdate = currentTimeStamp;
			    connectedPanel.setDownloadSpeedText(getDownloadSpeed());
			}
		    }
		    else {
			synchronized(this) {
			    connectedPanel.setDownloadProgress(0);
			    connectedPanel.setDownloadStatusText("Transfer complete!");
			    monitorDownloads = false;
			    monitoredDownload = null;
			}
		    }
		}
		synchronized(this) {
		    if(!abort) {
			try {
			    wait(50);
			} catch(InterruptedException ie) {}
		    }
		}
	    }
	    synchronized(this) {
		notify();
	    }
	}
	
	public synchronized void startMonitoring(Upload ul) {
	    monitoredUploads.addLast(ul);
	    monitorUploads = true;
	}
	public synchronized void startMonitoring(Download dl) {
	    monitoredDownload = dl;
	    monitorDownloads = true;
	}

// 	public String getSomething(Transfer t) {}

	public String getUploadSpeed() {
	    int firstPointer = (uploadMeasurementPointer+1)%uploadSpeedMeasurements.length;
	    int lastPointer = uploadMeasurementPointer;
	    while(uploadSpeedMeasurements[firstPointer] == -1 || uploadTimeStamps[firstPointer] == -1) {
		firstPointer = (firstPointer+1)%uploadSpeedMeasurements.length;
		if(lastPointer == firstPointer) {
		    if(displayByteRate && displayBitRate)
			return SpeedUnitUtils.bytesToDecimalBitUnit(0) + "/s" +
			    " / " + SpeedUnitUtils.bytesToBinaryUnit(0) + "/s";
		    else if(displayByteRate)
			return SpeedUnitUtils.bytesToBinaryUnit(0) + "/s";
		    else if(displayBitRate)
			return SpeedUnitUtils.bytesToDecimalBitUnit(0) + "/s";
		    else throw new RuntimeException("Boolean variables fucktup");
		}
	    }
	    long firstMeasurement = uploadSpeedMeasurements[firstPointer];
	    long lastMeasurement = uploadSpeedMeasurements[lastPointer];
	    long firstTimeStamp = uploadTimeStamps[firstPointer];
	    long lastTimeStamp = uploadTimeStamps[lastPointer];

	    long elapsedTime = lastTimeStamp-firstTimeStamp;
	    long transferedBytes = lastMeasurement-firstMeasurement;
	    double elapsedSeconds = elapsedTime/1000.0;

	    if(displayByteRate && displayBitRate)
		return SpeedUnitUtils.bytesToDecimalBitUnit(transferedBytes/elapsedSeconds) + "/s" +
		    " / " + SpeedUnitUtils.bytesToBinaryUnit(transferedBytes/elapsedSeconds) + "/s";
	    else if(displayByteRate)
		return SpeedUnitUtils.bytesToBinaryUnit(transferedBytes/elapsedSeconds) + "/s";
	    else if(displayBitRate)
		return SpeedUnitUtils.bytesToDecimalBitUnit(transferedBytes/elapsedSeconds) + "/s";
	    else throw new RuntimeException("Boolean variables fucktup");
	}
	
	public String getDownloadSpeed() {
	    int firstPointer = (downloadMeasurementPointer+1)%downloadSpeedMeasurements.length;
	    int lastPointer = downloadMeasurementPointer;
	    while(downloadSpeedMeasurements[firstPointer] == -1 || downloadTimeStamps[firstPointer] == -1) {
		firstPointer = (firstPointer+1)%downloadSpeedMeasurements.length;
		if(lastPointer == firstPointer) {
		    if(displayByteRate && displayBitRate)
			return SpeedUnitUtils.bytesToDecimalBitUnit(0) + "/s" +
			    " / " + SpeedUnitUtils.bytesToBinaryUnit(0) + "/s";
		    else if(displayByteRate)
			return SpeedUnitUtils.bytesToBinaryUnit(0) + "/s";
		    else if(displayBitRate)
			return SpeedUnitUtils.bytesToDecimalBitUnit(0) + "/s";
		    else throw new RuntimeException("Boolean variables fucktup");
		}
	    }
	    long firstMeasurement = downloadSpeedMeasurements[firstPointer];
	    long lastMeasurement = downloadSpeedMeasurements[lastPointer];
	    long firstTimeStamp = downloadTimeStamps[firstPointer];
	    long lastTimeStamp = downloadTimeStamps[lastPointer];

	    long elapsedTime = lastTimeStamp-firstTimeStamp;
	    long transferedBytes = lastMeasurement-firstMeasurement;
	    double elapsedSeconds = elapsedTime/1000.0;

	    if(displayByteRate && displayBitRate)
		return SpeedUnitUtils.bytesToDecimalBitUnit(transferedBytes/elapsedSeconds) + "/s" +
		    " / " + SpeedUnitUtils.bytesToBinaryUnit(transferedBytes/elapsedSeconds) + "/s";
	    else if(displayByteRate)
		return SpeedUnitUtils.bytesToBinaryUnit(transferedBytes/elapsedSeconds) + "/s";
	    else if(displayBitRate)
		return SpeedUnitUtils.bytesToDecimalBitUnit(transferedBytes/elapsedSeconds) + "/s";
	    else throw new RuntimeException("Boolean variables fucktup");
	}
    }

    private static class ConnectedPanel extends JPanel {
	private final JPanel uploadPanel;
	private final JPanel downloadPanel;
	//private final JButton sendFileButton;
	private final JButton sendFilesButton;
	private final JLabel downloadStatusLabel;
	private final JLabel uploadStatusLabel;
	private final JProgressBar downloadProgressBar;
	private final JProgressBar uploadProgressBar;
	private final JTextArea messageArea;
	private final JScrollPane messageScroller;
	private String uploadStatusText = "Idle...";
	private String uploadSpeedText = null;
	private String downloadStatusText = "Idle...";
	private String downloadSpeedText = null;
	
	public ConnectedPanel() {
	    setOpaque(false);
	    setLayout(new BorderLayout());
	    
	    uploadPanel = new JPanel();
	    uploadPanel.setOpaque(false);
	    uploadPanel.setBorder(new TitledBorder("Upload status"));
	    uploadPanel.setLayout(new BorderLayout(5, 5));
	    
	    downloadPanel = new JPanel();
	    downloadPanel.setOpaque(false);
	    downloadPanel.setBorder(new TitledBorder("Download status"));
	    downloadPanel.setLayout(new BorderLayout(5, 5));
	    
	    //sendFileButton = new JButton("Send file");

	    sendFilesButton = new JButton("Send files...");
	    
	    downloadStatusLabel = new JLabel("", SwingConstants.CENTER);
	    downloadStatusLabel.setOpaque(false);
	    downloadStatusLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
	    refreshDownloadStatusLabel();
	    
	    uploadStatusLabel = new JLabel("", SwingConstants.CENTER);
	    uploadStatusLabel.setOpaque(false);
	    uploadStatusLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
	    refreshUploadStatusLabel();
	    
	    downloadProgressBar = new JProgressBar();
	    downloadProgressBar.setOpaque(false);
	    downloadProgressBar.setMinimum(0);
	    downloadProgressBar.setMaximum(0x7fffffff);

	    uploadProgressBar = new JProgressBar();
	    uploadProgressBar.setOpaque(false);
	    uploadProgressBar.setMinimum(0);
	    uploadProgressBar.setMaximum(0x7fffffff);
	    
	    messageArea = new JTextArea(10, 80);
	    //messageArea.setOpaque(false);
	    messageArea.setFont(downloadStatusLabel.getFont());
	    messageArea.setLineWrap(true);
	    messageArea.setWrapStyleWord(true);
	    messageArea.setEditable(false);
	    messageArea.setBackground(Color.WHITE);
	    
	    messageScroller = new JScrollPane(messageArea);
	    messageScroller.setOpaque(false);
	    messageScroller.setBorder(new TitledBorder("Status messages"));
	    //new BevelBorder(BevelBorder.RAISED));
	    
	    JPanel sendFileButtonWrapper = new JPanel();
	    sendFileButtonWrapper.setOpaque(false);
	    sendFileButtonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
// 	    sendFileButtonWrapper.add(sendFileButton);
	    sendFileButtonWrapper.add(sendFilesButton);
	    
	    uploadPanel.add(sendFileButtonWrapper, BorderLayout.NORTH);
	    uploadPanel.add(uploadProgressBar, BorderLayout.CENTER);
	    uploadPanel.add(uploadStatusLabel, BorderLayout.SOUTH);
	    
	    downloadPanel.add(downloadProgressBar, BorderLayout.NORTH);
	    downloadPanel.add(downloadStatusLabel, BorderLayout.CENTER);

// 	    add(uploadPanel, BorderLayout.NORTH);
// 	    add(downloadPanel, BorderLayout.CENTER);
// 	    add(messageScroller, BorderLayout.SOUTH);

	    setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 1.0;
	    gbc.anchor = GridBagConstraints.WEST;
// 	    gbc.gridwidth = 1;
// 	    gbc.gridheight = 3;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    add(uploadPanel, gbc);
	    gbc.gridy = 1;
	    add(downloadPanel, gbc);
	    gbc.gridy = 2;
	    gbc.weighty = 1.0;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(messageScroller, gbc);

// 	    System.out.println("uploadPanel.getPreferredSize()=" + uploadPanel.getPreferredSize());
// 	    System.out.println("uploadPanel.getMinimumSize()=" + uploadPanel.getMinimumSize());
// 	    System.out.println("uploadPanel.getMaximumSize()=" + uploadPanel.getMaximumSize());
// 	    System.out.println("downloadPanel.getPreferredSize()=" + downloadPanel.getPreferredSize());
// 	    System.out.println("downloadPanel.getMinimumSize()=" + downloadPanel.getMinimumSize());
// 	    System.out.println("downloadPanel.getMaximumSize()=" + downloadPanel.getMaximumSize());
// 	    System.out.println("messageScroller.getPreferredSize()=" + messageScroller.getPreferredSize());
// 	    System.out.println("messageScroller.getMinimumSize()=" + messageScroller.getMinimumSize());
// 	    System.out.println("messageScroller.getMaximumSize()=" + messageScroller.getMaximumSize());
	}
	public synchronized void printMessage(String s) {
	    messageArea.append(s);
	    //messageArea.setCaretPosition(connectedPanel.messageArea.getText().length());
	    //messageArea.append(LINE_SEPARATOR + "Private message from " + sourceNick + ": " + message);
	    messageArea.invalidate();
	    messageArea.validate();
	    messageScroller.invalidate();
	    messageScroller.validate();
	    messageScroller.getVerticalScrollBar().setValue(messageScroller.getVerticalScrollBar().getMaximum());
	}
	public void setDownloadProgress(final double value) {
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			downloadProgressBar.setValue((int)(0x7fffffff*value));
		    }
		});
	}
	public void setDownloadStatusText(String text) {
	    downloadStatusText = text;
	    refreshDownloadStatusLabel();
	}
	public void setDownloadSpeedText(String text) {
	    downloadSpeedText = text;
	    refreshDownloadStatusLabel();
	}
	private void refreshDownloadStatusLabel() {
	    String text = downloadStatusText;
	    if(downloadSpeedText != null)
		text += " " + downloadSpeedText;
	    
	    final String statusLabelText = text;
	    if(!statusLabelText.equals(downloadStatusLabel.getText())) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    downloadStatusLabel.setText(statusLabelText);
			}
		    });
	    }
	}
	public void setUploadProgress(final double value) {
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			uploadProgressBar.setValue((int)(0x7fffffff*value));
		    }
		});
	}
	public void setUploadStatusText(String text) {
	    uploadStatusText = text;
	    refreshUploadStatusLabel();
	}
	public void setUploadSpeedText(String text) {
	    uploadSpeedText = text;
	    refreshUploadStatusLabel();
	}
	private void refreshUploadStatusLabel() {
	    String text = uploadStatusText;
	    if(uploadSpeedText != null)
		text += " " + uploadSpeedText;
	    
	    final String statusLabelText = text;
	    if(!statusLabelText.equals(uploadStatusLabel.getText())) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    uploadStatusLabel.setText(statusLabelText);
			}
		    });
	    }
	}
// 	public void addSendFileListener(ActionListener al) {
// 	    sendFileButton.addActionListener(al);
// 	}
	public void addSendFilesListener(ActionListener al) {
	    sendFilesButton.addActionListener(al);
	}
    }
    
    private class ConnectionAttempt implements Runnable {
	public void run() {
	    //parent.setTitleAt(indexInParent, "Connecting to " + address);
	    //System.err.println("Connecting to " + address);
	    Socket target = null;
	    try {
		//System.err.println("Connecting to " + address);

		String[] addressComponents = address.split(":");
		if(addressComponents.length == 2) {
		    int port = 0;
		    boolean validPort;
		    try {
			port = Integer.parseInt(addressComponents[1]);
			if(port >= 0 && port <= 65535)
			    validPort = true;
			else
			    validPort = false;
		    } catch(NumberFormatException nfe) { validPort = false; }
		    if(validPort)
			target = new Socket(addressComponents[0], port);
		    else
			target = new Socket(address, ConnectionListener.DEFAULT_PORT_NUMBER);
		}
		else
		    target = new Socket(address, ConnectionListener.DEFAULT_PORT_NUMBER);//3632);
 		//System.err.print("construct1");
		target.getOutputStream().write(ConnectionListener.CLIENT_APPLICATION_SIGNATURE);
		byte[] serverSignature = new byte[32];
 		//System.err.print("\b2");
		target.getInputStream().read(serverSignature);
 		//System.err.print("\b3");
		if(Main.byteArraysEqual(serverSignature, ConnectionListener.SERVER_APPLICATION_SIGNATURE)) {
		    currentHandler = new SessionHandler(parent.getFileTransfer(), target, ConnectionMonitorPanel.this);
		    currentHandler.start();
		    pqWindow = new PackageQueueWindow(ConnectionMonitorPanel.this, false);
		
		    //System.err.print("\b4");
		    SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
				layout.show(ConnectionMonitorPanel.this, "connectedPanel");
			    }
			});
		    return;
		}
		else {
		    SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
				JOptionPane.showMessageDialog(ConnectionMonitorPanel.this, address + " refused your connection.", "Error", JOptionPane.ERROR_MESSAGE);
				parent.removeTab(ConnectionMonitorPanel.this);
			    }
			});
		}
		    
		//System.err.println("\b5");
		//System.err.println("ConnectionAttempt thread finished gracefully...");
	    }
	    catch(final UnknownHostException uhe) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    JOptionPane.showMessageDialog(ConnectionMonitorPanel.this, "Could not resolve hostname " + uhe.getMessage() + ".", "Error", JOptionPane.ERROR_MESSAGE);
			    parent.removeTab(ConnectionMonitorPanel.this);
			}
		    });
		//uhe.printStackTrace();
	    }
	    catch(ConnectException ce) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    JOptionPane.showMessageDialog(ConnectionMonitorPanel.this, "Connection attempt timed out.", "Error", JOptionPane.ERROR_MESSAGE);
			    parent.removeTab(ConnectionMonitorPanel.this);
			}
		    });
	    }
	    catch(final Exception e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    JOptionPane.showMessageDialog(ConnectionMonitorPanel.this, "Unhandled exception:\n" + e + "\nChoose the menu option \"Show system messages\" for more details.", "Error", JOptionPane.ERROR_MESSAGE);
			    parent.removeTab(ConnectionMonitorPanel.this);
			}
		    });
		e.printStackTrace();
	    }
	    if(target != null) {
		try {
		    target.close();
		} catch(Exception e) {}
	    }
	}
    }
}
