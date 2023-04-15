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

import gui.SelectFilesPanel;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class SelectFilesDialog extends JDialog {
    public final ConnectionMonitorPanel cmp;
    public DefaultListModel listModel;
    public SelectFilesPanel selectFilesPanel;
    private DirectoryEntry packageRoot;
    private HashMap<FileSystemEntry, File> packageFileMap;
    
    public SelectFilesDialog(ConnectionMonitorPanel cmp) {
	super(cmp.getMainWindow(), "Select files to send", true);
	this.cmp = cmp;

	selectFilesPanel = new SelectFilesPanel();
 	listModel = new DefaultListModel();
	packageRoot = new DirectoryEntry();
	packageRoot.setParent(null);
	packageRoot.setHidden(false);
	packageRoot.setLastModified(0);
	packageRoot.setReadOnly(false);
	packageRoot.setName("/");
	packageFileMap = new HashMap<FileSystemEntry, File>();
	
	selectFilesPanel.fileList.setModel(listModel);
	setupListeners();
	
	add(selectFilesPanel, BorderLayout.CENTER);
	
	pack();
	setLocationRelativeTo(null);
    }

    public void setupListeners() {
	selectFilesPanel.addButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    addButtonActionPerformed();
		}
	    });
	selectFilesPanel.removeButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    removeButtonActionPerformed();
		}
	    });
	selectFilesPanel.sendButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    sendButtonActionPerformed();
		}
	    });
	selectFilesPanel.cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    cancelButtonActionPerformed();
		}
	    });

	selectFilesPanel.addKeyListener(new KeyAdapter() {
		public void keyTyped(KeyEvent ke) {
		    if(ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			setVisible(false);
		}
	    });
    }
    
    public void refreshListModel() {
	listModel.clear();
	System.out.println("refreshListModel");
	for(DirectoryEntry de : packageRoot.getDirectories()) {
	    System.out.println(" adding: " + de);
	    refreshListModel(listModel, de);
	}
	for(FileEntry fe : packageRoot.getFiles()) {
	    System.out.println(" adding: " + fe);
	    refreshListModel(listModel, fe);
	}
    }
    private static void refreshListModel(DefaultListModel listModel, FileSystemEntry fse) {
	listModel.addElement(fse);
	
    	if(fse instanceof DirectoryEntry) {
	    DirectoryEntry de = (DirectoryEntry) fse;
	    for(DirectoryEntry child : de.getDirectories())
		refreshListModel(listModel, child);
	    for(FileEntry child : de.getFiles())
		refreshListModel(listModel, child);
	}
    }
    
//     public void addFilesToPackage(File[] files) {
// 	for(File file : files)
// 	    listModel.addElement(file);
//     }
    // This method does not automatically refresh the list model...
    private void addFilesToPackage(File[] files) {
	DirectoryEntry currentRoot = SessionHandler.createDirectoryTree(files, packageFileMap);
	for(DirectoryEntry de : currentRoot.getDirectories()) {
	    packageRoot.addDirectory(de);
	    de.setParent(packageRoot);
	}
	for(FileEntry fe : currentRoot.getFiles()) {
	    packageRoot.addFile(fe);
	    fe.setParent(packageRoot);
	}
    }
    // This method does not automatically refresh the list model...
    private boolean removeEntryFromPackage(FileSystemEntry fse) {
	DirectoryEntry parent = fse.getParent();
	if(fse instanceof FileEntry) {
	    boolean removeStatus = parent.removeFile((FileEntry) fse);
	    if(removeStatus && packageFileMap.remove(fse) == null)
		JOptionPane.showMessageDialog(this, "packageFileMap not sychronized with rest of tree! (FileEntry)", "Warning", JOptionPane.WARNING_MESSAGE);
	    return removeStatus;
	}
	else if(fse instanceof DirectoryEntry) {
	    boolean removeStatus = parent.removeDirectory((DirectoryEntry) fse);
	    if(removeStatus && packageFileMap.remove(fse) == null)
		JOptionPane.showMessageDialog(this, "packageFileMap not sychronized with rest of tree! (DirectoryEntry)", "Warning", JOptionPane.WARNING_MESSAGE);
	    return removeStatus;
	}
	else
	    return false;
    }
    public void addButtonActionPerformed() {
	File[] selectedFiles = cmp.getFilesFromUser(this);
	if(selectedFiles != null) {
	    if(selectedFiles.length > 0) {
		addFilesToPackage(selectedFiles);
		refreshListModel();
	    }
	    else
		JOptionPane.showMessageDialog(this, "No files found.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    public void removeButtonActionPerformed() {
	if(!selectFilesPanel.fileList.isSelectionEmpty()) {
	    // Get selection
	    int selectionMode = selectFilesPanel.fileList.getSelectionMode();
	    Object[] selectedValues;
	    switch(selectionMode) {
	    case ListSelectionModel.SINGLE_INTERVAL_SELECTION:
	    case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:
		selectedValues = selectFilesPanel.fileList.getSelectedValues();
		break;
	    case ListSelectionModel.SINGLE_SELECTION:
	    default:
		selectedValues = new Object[1];
		selectedValues[0] = selectFilesPanel.fileList.getSelectedValue();
	    }
	    
	    // Translate the Object objects from the selection into their correct types.
	    LinkedList<FileEntry> fileEntries = new LinkedList<FileEntry>();
	    LinkedList<DirectoryEntry> directoryEntries = new LinkedList<DirectoryEntry>();
	    for(Object o : selectedValues) {
		if(o instanceof FileEntry)
		    fileEntries.add((FileEntry) o);
		else if(o instanceof DirectoryEntry)
		    directoryEntries.add((DirectoryEntry) o);
	    }

	    // Remove the entries
	    for(FileEntry fe : fileEntries) {
		if(!removeEntryFromPackage(fe))
		    JOptionPane.showMessageDialog(this, "Could not remove file  \"" + fe.toString() + "\"...", "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	    for(DirectoryEntry de : directoryEntries) {
		if(!removeEntryFromPackage(de))
		    JOptionPane.showMessageDialog(this, "Could not remove directory \"" + de.toString() + "\"...", "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	    if(selectedValues.length != (fileEntries.size()+directoryEntries.size()))
		JOptionPane.showMessageDialog(this, "Encountered unexpected types in file list...", "Warning", JOptionPane.WARNING_MESSAGE);

	    // Refresh
	    refreshListModel();
	}
	else
	    JOptionPane.showMessageDialog(this, "Could not find element to remove", "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void sendButtonActionPerformed() {
// 	Object[] fileObjects = listModel.toArray();
// 	File[] selectedFiles = new File[fileObjects.length];
// 	for(int i = 0; i < fileObjects.length; ++i) {
// 	    Object o = fileObjects[i];
// 	    if(o instanceof File)
// 		selectedFiles[i] = (File)o;
// 	    else
// 		throw new RuntimeException("Encountered unexpected object in listModel");
// 	}
	if(packageRoot.getFiles().length > 0 || packageRoot.getDirectories().length > 0) {
	    setVisible(false);
	    cmp.sendFilesPreprocessed(packageFileMap, packageRoot);
	}
	else
	    JOptionPane.showMessageDialog(this, "No files to send.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public void cancelButtonActionPerformed() {
	setVisible(false);
    }

    public static File[] resolveTree(File[] roots) {
	LinkedList<File> fileList = new LinkedList<File>();
	if(roots != null) {
	    for(File f : roots) {
		if(f.isDirectory())
		    fileList.addAll(_resolveTree(f));
		else if(f.isFile())
		    fileList.add(f);
	    }
	}
	return fileList.toArray(new File[fileList.size()]);
    }
    private static Collection<File> _resolveTree(File root) {
	LinkedList<File> fileList = new LinkedList<File>();
	File[] roots = root.listFiles();
	if(roots != null) {
	    for(File f : roots) {
		if(f.isDirectory())
		    fileList.addAll(_resolveTree(f));
		else if(f.isFile())
		    fileList.add(f);
	    }
	}
	return fileList;
    }
//     protected static DirectoryEntry createDirectoryTree(File[] files, Map<FileSystemEntry, File> fileMap) {
// 	DirectoryEntry dir = _createDirectoryTree(files, fileMap);
// 	dir.setParent(null);
// 	dir.setHidden(false);
// 	dir.setLastModified(0);
// 	dir.setReadOnly(false);
// 	dir.setName("/");
// 	return dir;
//     }
    
//     private static DirectoryEntry _createDirectoryTree(File[] files, Map<FileSystemEntry, File> fileMap) {
// 	DirectoryEntry currentDir = new DirectoryEntry();
	
// 	LinkedList<File> fileList = new LinkedList<File>();
// 	LinkedList<File> dirList = new LinkedList<File>();
// 	for(File f : files) {
// 	    if(f.isDirectory())
// 		dirList.addLast(f);
// 	    else if(f.isFile() && f.canRead())
// 		fileList.addLast(f);
// 	    else; //Ignore special files...
// 	}
	
// 	for(File f : fileList) {
// 	    FileEntry fe = new FileEntry();
// 	    if(fileMap != null)
// 		fileMap.put(fe, f);
// 	    fe.setParent(currentDir);
// 	    fe.setHidden(f.isHidden());
// 	    fe.setLength(f.length());
// 	    fe.setLastModified(f.lastModified());
// 	    fe.setReadOnly(!f.canWrite());
// 	    fe.setName(f.getName());
// 	    currentDir.addFile(fe);
// 	}

// 	for(File f : dirList) {
// 	    DirectoryEntry dir = _createDirectoryTree(f.listFiles(), fileMap);
// 	    if(fileMap != null)
// 		fileMap.put(dir, f);
// 	    dir.setParent(currentDir);
// 	    dir.setHidden(f.isHidden());
// 	    dir.setLastModified(f.lastModified());
// 	    dir.setReadOnly(!f.canWrite());
// 	    dir.setName(f.getName());
// 	    currentDir.addDirectory(dir);
// 	}
// 	return currentDir;
//     }
}
