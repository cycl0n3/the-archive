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

public abstract class FileSystemEntry {
    protected DirectoryEntry parent;
    protected boolean hidden;
    protected boolean readOnly;
    protected long lastModified;
    protected String name;
   
    protected FileSystemEntry() {
	hidden = false;
	readOnly = false;
	lastModified = 0;
	name = "";
    }
    
    public void setParent(DirectoryEntry de) {
	parent = de;
    }
    
    public DirectoryEntry getParent() {
	return parent;
    }
    
    public boolean getHidden() {
	return hidden;
    }
   
    public void setHidden(boolean newHidden) {
	hidden = newHidden;
    }
   
    public boolean getReadOnly() {
	return readOnly;
    }
   
    public void setReadOnly(boolean newReadOnly) {
	readOnly = newReadOnly;
    }
   
    public long getLastModified() {
	return lastModified;
    }
   
    public void setLastModified(long newLastModified) {
	lastModified = newLastModified;
    }
   
    public String getName() {
	return name;
    }
   
    public void setName(String newName) {
	name = newName;
    }

    public String toString() {
	DirectoryEntry parent = getParent();
	if(parent != null && parent.getParent() != null)
	    return parent.toString() + "/" + getName();
	else
	    return getName();
    }
}
