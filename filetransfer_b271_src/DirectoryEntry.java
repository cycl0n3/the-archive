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
import java.io.File;

public class DirectoryEntry extends FileSystemEntry {

    private LinkedList<DirectoryEntry> directories = new LinkedList<DirectoryEntry>();
    private LinkedList<FileEntry> files = new LinkedList<FileEntry>();
    
    public DirectoryEntry[] getDirectories() {
	return directories.toArray(new DirectoryEntry[directories.size()]);
    }

    public FileEntry[] getFiles() {
	return files.toArray(new FileEntry[files.size()]);
    }
   
    public void addDirectory(DirectoryEntry newDirectory) {
	directories.addLast(newDirectory);
    }
   
    public void addFile(FileEntry newFile) {
	files.addLast(newFile);
    }
    public boolean removeDirectory(DirectoryEntry newDirectory) {
	return directories.remove(newDirectory);
    }
    public boolean removeFile(FileEntry newFile) {
	return files.remove(newFile);
    }
    
    public String getPath() {
	if(parent == null)
	    return "";
	else {
	    String parentPath = parent.getPath();
	    if(parentPath.equals(""))
		return name;
	    else
		return parentPath + File.separator + name;
	}
    }

    /**
     * Creates this directory along with its subdirectory tree under the directory
     * rootDir.
     */
    public boolean makeTree(File rootDir) {
	return makeTree(this, rootDir);
    }
    private static boolean makeTree(DirectoryEntry current, File rootDir) {
	File thisDir = new File(rootDir, current.name);
	if(!(thisDir.exists() && thisDir.isDirectory()) && !thisDir.mkdirs())
	    return false;
	else {
	    for(DirectoryEntry de : current.directories) {
		if(!makeTree(de, thisDir))
		    return false;
	    }
	    return true;
	}
    }
}
