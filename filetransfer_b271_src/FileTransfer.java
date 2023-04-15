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

import java.util.Hashtable;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.security.NoSuchAlgorithmException;

public class FileTransfer {
    private final MainWindow mainWindow;
    private final SystemMessageWindow systemMessageWindow;
    private ConnectionListener listener;
    //private Hashtable<ConnectionMonitorPanel, PackageQueueWindow> packageQueueWindows;
    public static final PrintStream stdout = System.out;
    public static final PrintStream stderr = System.err;
    
    public FileTransfer() {
	mainWindow = new MainWindow(this);
	systemMessageWindow = new SystemMessageWindow();
	System.setErr(systemMessageWindow.getErrorStream());
	System.setOut(systemMessageWindow.getOutputStream());
	listener = new ConnectionListener(this);
	//packageQueueWindows = new Hashtable<ConnectionMonitorPanel, PackageQueueWindow>();
	mainWindow.setVisible(true);
	listener.startListening();
	checkForUpdates();
    }

    public void terminateProgram() {
	System.exit(0);
    }
    
    public MainWindow getMainWindow() {
	return mainWindow;
    }
    
    public boolean confirmQuit() {
	return JOptionPane.showConfirmDialog(mainWindow, "Are you sure you want to quit?", 
					     "Exit confirmation", JOptionPane.YES_NO_OPTION, 
					     JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public void terminateProgramWithConfirmation() {
	// Executed in event dispatch thread
	if(confirmQuit())
	    terminateProgram();
    }
    
    public void newConnectionSignaled() {
	// Executed in event dispatch thread
	String address = JOptionPane.showInputDialog(mainWindow, "Please type the address of the peer\n" +
						     "to whom you wish to connect:", "Enter peer address", 
						     JOptionPane.PLAIN_MESSAGE);
	if(address != null)
	    mainWindow.addConnectionTab(address);
	
    }
    public void exitSignaled() {
	// Executed in event dispatch thread
	terminateProgram();
    }
    public void aboutSignaled() {
	// Executed in event dispatch thread
	String message = "";
	message += Main.APPLICATION_NAME + " " + Main.VERSION + "\n";
	message += "Build #" + BuildNumber.BUILD_NUMBER + "\n";
	message += Main.COPYRIGHT_MESSAGE + "\n";
	for(String notice : Main.NOTICES) {
	    message += notice + "\n";
	    // System.out.println("Message now: " + message);
	}
	message += "\n Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version");
	message += "\n Architecture: " + System.getProperty("os.arch");
	message += "\n Virtual machine: " + System.getProperty("java.vm.vendor") + " " + 
	    System.getProperty("java.vm.name") + " "  + System.getProperty("java.vm.version");
	JOptionPane.showMessageDialog(mainWindow, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    public void newIncomingConnectionSignaled(final Socket s) throws IOException {
	// Not executed in event dispatch thread (need to invokeLater/invokeAndWait)
	final ObjectContainer<IOException> oc = new ObjectContainer<IOException>();
	try {
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			try {
			    mainWindow.addConnectionTab(s);
			} catch(IOException ioe) {
			    oc.object = ioe;
			}
		    }
		});
	} catch(InvocationTargetException ite) {
	    Throwable cause = ite.getCause();
	    if(cause instanceof IOException) {
		throw (IOException)cause;
	    }
	    else if(cause instanceof RuntimeException) {
		throw (RuntimeException)cause;
	    }
	    else ite.printStackTrace();
	} catch(InterruptedException ie) {
	    ie.printStackTrace();
	}
	if(oc.object != null)
	    throw oc.object;
    }

    public boolean confirmAcceptConnection(final Socket s) {
	final ObjectContainer<Integer> oc = new ObjectContainer<Integer>();
	Runnable r = new Runnable() {
		public void run() {
		    mainWindow.setVisible(true);
		    int res = JOptionPane.showConfirmDialog(mainWindow, "A computer with ip " + 
							    s.getInetAddress().getHostName() + "/" + 
							    s.getInetAddress().getHostAddress() + 
							    " is trying to connect to your computer.\nAccept connection?", 
							    "Connection confirmation", JOptionPane.YES_NO_OPTION, 
							    JOptionPane.QUESTION_MESSAGE);
		    oc.object = res;
		}
	    };
	try {
	    SwingUtilities.invokeAndWait(r);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	return oc.object.intValue() == JOptionPane.YES_OPTION;
    }

    public void signalListenFailed() {
	Runnable r = new Runnable() {
		public void run() {
		    JOptionPane.showMessageDialog(mainWindow, "Could not open server socket for port " + 
						  getListenPort() + "...\n" +
						  "You will not be able to accept any incoming connections.\n" + 
						  "(You seem to have another server listening on that port already. Maybe\n" +
						  "another running instance of this program?)", 
						  "Error", JOptionPane.ERROR_MESSAGE);
		}
	    };
	SwingUtilities.invokeLater(r);
    }

    public void setListenStatus(final boolean b) {
	Runnable r = new Runnable() {
		public void run() {
		    mainWindow.setListenStatus(b);
		}
	    };
	SwingUtilities.invokeLater(r);
    }
    
    public void reportGeneralError(final String message) {
	Runnable r = new Runnable() {
		public void run() {
		    JOptionPane.showMessageDialog(mainWindow, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	    };
	SwingUtilities.invokeLater(r);
    }
    public void showPackageQueueSignaled() {
	ConnectionMonitorPanel activeMonitor = mainWindow.getActiveTab();
	if(activeMonitor == null) {
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			JOptionPane.showMessageDialog(mainWindow, "No active connections.", 
						      "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});
	}
	else {
	    PackageQueueWindow pqw = activeMonitor.getPackageQueueWindow();
// 	    if(pqw == null) {
// 		pqw = new PackageQueueWindow(activeMonitor);
// 		packageQueueWindows.put(activeMonitor, pqw);
// 	    }
	    pqw.setVisible(true);
	}
	
    }
    public void disconnectSignaled() {
	ConnectionMonitorPanel activeMonitor = mainWindow.getActiveTab();
	if(activeMonitor == null) {
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			JOptionPane.showMessageDialog(mainWindow, "No active connections.", 
						      "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});
	}
	else {
	    activeMonitor.disconnect();
	}
    }
    public void showSystemMessagesSignaled() {
	systemMessageWindow.setVisible(true);
    }
    public void printMessageLine(String s) {
	systemMessageWindow.printMessageLine(s);
    }
    public void printMessage(String s) {
	systemMessageWindow.printMessage(s);
    }
    
    public void checkForUpdates() {
	try {
	    boolean completed = true;
	    final String[] baseURLs = { "http://www.typhontools.cjb.net/filetransfer/", "http://hem.bredband.net/unsound/filetransfer/" };
	    URL versionfile;
	    InputStream versionfileStream;
// 	    final String applicationName;
// 	    final String majorVersionID;
// 	    final long latestBuildNumber;
// 	    final String latestBuildURL;
// 	    final String sha256digest;
	    for(String baseURL : baseURLs) {
		try {
		    versionfile = new URL(baseURL + "latest-version.txt");
		    versionfileStream = versionfile.openStream();
		    BufferedReader versionfileIn = new BufferedReader(new InputStreamReader(versionfileStream));
		    final String applicationName = versionfileIn.readLine();
		    final String majorVersionID = versionfileIn.readLine();
		    final long latestBuildNumber = Long.parseLong(versionfileIn.readLine());
		    final String latestBuildURL = versionfileIn.readLine();
		    final String sha256digest = versionfileIn.readLine();
		    if(sha256digest.trim().length() != 64)
			continue;
		 
		    System.out.println("Retrieved the following information from the version URL: " + versionfile);
		    System.out.println("  " + applicationName);
		    System.out.println("  " + majorVersionID);
		    System.out.println("  " + latestBuildNumber);
		    System.out.println("  " + latestBuildURL);
		    System.out.println("  " + sha256digest);
		    if(latestBuildNumber > BuildNumber.BUILD_NUMBER) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				    int res = JOptionPane.showConfirmDialog(mainWindow, "There is a new version of " +
									    Main.APPLICATION_NAME + " available!\n" +
									    "The new version is identified by the following " +
									    "parameters:\n" +
									    "Application name: " + applicationName + "\n" +
									    "Version: " + majorVersionID + "\n" +
									    "Build number: " + latestBuildNumber + "\n" +
									    "URL: " + latestBuildURL + "\n" +
									    "Do you want to upgrade automatically?\n" + 
									    "(The safest way to upgrade is to fetch the new version" + 
									    " from " + Main.WEBPAGE_URL + " and install manually...)",
									    "New version available", JOptionPane.YES_NO_OPTION, 
									    JOptionPane.QUESTION_MESSAGE);
				    if(res == JOptionPane.YES_OPTION)
					upgradeApplication(latestBuildURL, hexStringToByteArray(sha256digest));
				}
			    });
		    }
		    completed = true;
		    break;
		} catch(UnknownHostException e) {
		} catch(UnknownServiceException e) {
		} catch(SocketTimeoutException e) {
		}
	    }
	    if(!completed)
		System.out.println("WARNING: Could not contact the version URL...");
	}
	catch(Exception e) { e.printStackTrace(); } //Non-critical..
    }
		
    public void upgradeApplication(String url, byte[] referenceDigest) {
	try {
	    URL newJar = new URL(url);
	    File targetfile;
	    String classPath = System.getProperty("java.class.path");
	    String[] paths = classPath.split(System.getProperty("path.separator"));
	    System.err.println("classPath = " + classPath);
	    String jarPath = null;
	    for(String curPath : paths) {
		System.err.println("processing: " + curPath);
		curPath = curPath.trim();
		if(curPath.endsWith(".jar")) {
		    if(jarPath == null)
			jarPath = curPath;
		    else {
			JOptionPane.showMessageDialog(mainWindow, "More than one JAR-file found, could not determine which " +
						      "to replace.\nInstall update manually from " + Main.WEBPAGE_URL, "Error",
						      JOptionPane.ERROR_MESSAGE);
			return;
		    }
		}
	    }
	
	    File targetFile = new File(jarPath);
	    if(jarPath == null || !targetFile.exists() || !targetFile.isFile()) {
		System.err.println("jarPath == " + jarPath);
		System.err.println("targetFile.exists() == " + targetFile.exists());
		System.err.println("targetFile.isFile() == " + targetFile.isFile());
		JOptionPane.showMessageDialog(mainWindow, "Could not find the JAR-file to replace...\n" +
					      "Install update manually from " + Main.WEBPAGE_URL, "Error",
					      JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    String canonicalTargetPath = targetFile.getCanonicalPath();
	    System.err.println("canonicalTargetPath = " + canonicalTargetPath);

	    int iter8 = 1;
	    File tempFile = new File(canonicalTargetPath + ".new");
	    while(tempFile.exists())
		tempFile = new File(canonicalTargetPath + ".new" + iter8++);
	    System.err.println("tempFile.canonicalTargetPath = " + tempFile.getCanonicalPath());
	    iter8 = 1;
	    File backupFile = new File(canonicalTargetPath + ".build" + BuildNumber.BUILD_NUMBER);
	    while(backupFile.exists())
		backupFile = new File(canonicalTargetPath + ".build" + BuildNumber.BUILD_NUMBER + "_" + iter8++);
	    System.err.println("backupFile.canonicalTargetPath = " + backupFile.getCanonicalPath());
	    
	    if(tempFile.createNewFile() && backupFile.createNewFile()) {
		int res = JOptionPane.showConfirmDialog(mainWindow, "Located application JAR at\n" +
							canonicalTargetPath +"\nReally overwrite?", "Overwrite confirmation",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(res == JOptionPane.YES_OPTION) {	    
		    FileOutputStream fos = null;
		    try {
			fos = new FileOutputStream(tempFile);
		    } catch(FileNotFoundException fnfe) {
			errorMessage("Update failed", "Application update failed!\nCould not open temporary file...");
			tempFile.delete();
			backupFile.delete();
			return;
		    }
		    DigestInputStream urlIn = null;
		    try {
			urlIn = new DigestInputStream(newJar.openStream(), MessageDigest.getInstance("SHA-256"));
			byte[] buffer = new byte[4096];
			int bytesRead = urlIn.read(buffer);
			while(bytesRead != -1) {
			    fos.write(buffer, 0, bytesRead);
			    bytesRead = urlIn.read(buffer);
			}
			byte[] myDigest = urlIn.getMessageDigest().digest();
			fos.close();
			urlIn.close();
			if(!byteArraysEqual(myDigest, referenceDigest)) {
			    errorMessage("Update failed", "The SHA-256 checksum for the downloaded file is incorrect! Can not upgrade...");
			    tempFile.delete();
			    backupFile.delete();
			    return;
			}
			if(fileCopy(targetFile, backupFile)) {
			    if(fileCopy(tempFile, targetFile)) {
				tempFile.delete();
				infoMessage("Update complete", "Application update complete!\nRestart application " +
					    "as soon as possible.\nA backup of your previous JAR-file has been placed at\n" +
					    backupFile.getCanonicalPath() + "\nYou may safely remove it at will.");
			    }
			    else {
				//fileCopy(backupFile, targetFile);
				errorMessage("Update failed", "Could not store new JAR-file...");
				tempFile.delete();
				backupFile.delete();
			    }	
			}
			else {
			    errorMessage("Update failed", "Could not backup existing JAR-file...");
			    tempFile.delete();
			    backupFile.delete();
			}
		    } catch(IOException ioe) {
			errorMessage("Update failed", "Application update failed!\nCould not retrieve update from web...");
			backupFile.delete();
			tempFile.delete();
			return;
		    } catch(NoSuchAlgorithmException nsae) {
			errorMessage("Update failed", "Could not find algorithm for SHA-256...\nTry a different Java " +
				     "Runtime Environment.");
			backupFile.delete();
			tempFile.delete();
			return;			
		    }
		}
		else {
		    backupFile.delete();
		    tempFile.delete();
		}
	    }
	    else {
		errorMessage("Update failed", "Application update failed!\nCould not create temporary file...");
	    }
	}
	catch(IOException ioe) {
	    errorMessage("Update failed", "Unknown error. Check system messages...");
	    ioe.printStackTrace();
	}
    }

    public void errorMessage(String message) {
	errorMessage("Error", message);
    }
    public void errorMessage(String title, String message) {
	JOptionPane.showMessageDialog(mainWindow, message, title, JOptionPane.ERROR_MESSAGE);
    }
    public void infoMessage(String message) {
	infoMessage("Information", message);
    }
    public void infoMessage(String title, String message) {
	JOptionPane.showMessageDialog(mainWindow, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean fileCopy(File source, File target) {
	try {
	    FileInputStream bin = new FileInputStream(source);
	    FileOutputStream bout = new FileOutputStream(target);
	    byte[] buffer = new byte[4096];
	    int bytesRead = bin.read(buffer);
	    while(bytesRead != -1) {
		bout.write(buffer, 0, bytesRead);
		bytesRead = bin.read(buffer);
	    }
	    bin.close();
	    bout.close();
	    return true;
	} catch(IOException ioe) {
	    return false;
	}
    }

    public static byte[] hexStringToByteArray(String s) {
	char[] sc = s.toCharArray();
	if(sc.length % 2 != 0)
	    throw new IllegalArgumentException();
	
	byte[] targetArray = new byte[sc.length/2];
	for(int i = 0; i < sc.length/2; ++i) {
	    for(int j = i*2; j < i*2+2; ++j) {
		char c = sc[j];
		byte b;
		switch(c) {
		case '0':
		    b = 0x0;
		    break;
		case '1':
		    b = 0x1;
		    break;
		case '2':
		    b = 0x2;
		    break;
		case '3':
		    b = 0x3;
		    break;
		case '4':
		    b = 0x4;
		    break;
		case '5':
		    b = 0x5;
		    break;
		case '6':
		    b = 0x6;
		    break;
		case '7':
		    b = 0x7;
		    break;
		case '8':
		    b = 0x8;
		    break;
		case '9':
		    b = 0x9;
		    break;
		case 'A':
		case 'a':
		    b = 0xA;
		    break;
		case 'B':
		case 'b':
		    b = 0xB;
		    break;
		case 'C':
		case 'c':
		    b = 0xC;
		    break;
		case 'D':
		case 'd':
		    b = 0xD;
		    break;
		case 'E':
		case 'e':
		    b = 0xE;
		    break;
		case 'F':
		case 'f':
		    b = 0xF;
		    break;
		default:
		    throw new IllegalArgumentException();
		}
		targetArray[i] |= (b << (j%2 == 0 ? 4 : 0)) & (j%2 == 0 ? 0xF0 : 0x0F);
	    }
	}
	return targetArray;
    }

    public boolean byteArraysEqual(byte[] a, byte[] b) {
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

    public void setStatus(final String s) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    mainWindow.setStatus(s);
		}
	    });
    }

    public void setListeningForConnections(boolean b) {
	if(b && !listener.isListening()) {
	    listener.startListening();
	}
	else if(!b && listener.isListening()) {
	    listener.stopListening();
	}
    }
    
    public void setListenPort(int port) {
	listener.setListenPort(port);
    }
    public int getListenPort() {
	return listener.getListenPort();
    }
}
