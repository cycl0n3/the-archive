/*-
 * Copyright (C) 2007 Erik Larsson
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

package org.catacombae.dua;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;

public class Result {
    private static final DecimalFormat resultFormatter = new DecimalFormat("0.0");
    //private long totalSize = 0;
    private LinkedList<Info> dirStack = new LinkedList<Info>();
    
    public static class Info implements Comparable<Info> {
	//public String name;
        public final File file;
        public Info parentInfo;
	public long dirSize = 0;
	/** Size of current dir excluding subdirectories. (i.e. only '.') */
	public Info dotInfo;
	public final DefaultMutableTreeNode node;
	private LinkedList<Info> children = new LinkedList<Info>();

	public Info(/*String name, */File file, Info parentInfo) {
	    //this.name = name;
            this.file = file;
            this.parentInfo = parentInfo;
	    this.node = new DefaultMutableTreeNode(this);
	    
            if(parentInfo != null)
                parentInfo.children.addLast(this);	    
	}
        
        @Override
	public String toString() {
	    String percentageString = "";
	    if(parentInfo != null && parentInfo.dirSize != 0)
		percentageString = (dirSize*100/parentInfo.dirSize)+"% ";
            
            String nameString;
            if(parentInfo == null)
                nameString = file.getAbsolutePath();
            else if(dotInfo != null)
                nameString = file.getName();
            else
                nameString = ".";
            return percentageString + nameString + " (" + SpeedUnitUtils.bytesToBinaryUnit(dirSize, resultFormatter) + ")";
        }
	
	public int compareTo(Info i) {
	    //if(dotInfo == null && i.dotInfo != null)
	    //return -1;
	    
	    long res = dirSize - i.dirSize;
            if(res < 0) return 1; // reverse order
            else if(res > 0) return -1; // reverse order
            else return 0;
	}
	
	public void finalizeInfo() {
	    Collections.sort(children);
	    for(Info i : children)
		node.add(i.node);
	    children = null;
	}
   }
    
    public Result(File root) {
	String name = root.getAbsolutePath();
	if(name.trim().equals("")) {
// 	    System.out.println("name is nothing... file: \"" + root + "\"");
	    try {
		name = root.getCanonicalPath();
// 		System.out.println("new name is \"" + name + "\"");
	    } catch(Exception e) { e.printStackTrace(); throw new RuntimeException(e); }
	}
// 	else
// 	    System.out.println("Good root name: \"" + name + "\"");
	Info rootNode = new Info(root, null);
	dirStack.addLast(rootNode);
	rootNode.dotInfo = new Info(root, rootNode);
        rootNode.children.addLast(rootNode.dotInfo);
    }
    public Info getRoot() {
	if(dirStack.size() > 1) {
	    System.out.println("WARNING: Parsed tree contains the following nodes:");
	    for(Info i : dirStack)
		System.out.println("  " + i.toString());
	}
	Info i = dirStack.getFirst();
	return i;
    }
    public DefaultMutableTreeNode getRootNode() {
	if(dirStack.size() > 1) {
	    System.out.println("WARNING: Parsed tree contains the following nodes:");
	    for(Info i : dirStack)
		System.out.println("  " + i.toString());
	}
	Info i = dirStack.getFirst();
	return i.node;
    }
    
    public void startDirectory(File f) {
	Info n = new Info(f, dirStack.getLast());
	dirStack.addLast(n);
	n.dotInfo = new Info(f, n);
	n.children.addLast(n.dotInfo);
        //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(n);
        //nodeStack.getLast().add(newNode);
	//nodeStack.addLast(newNode);
    }
    public void endDirectory(File f) {
	Info i = dirStack.removeLast();
	i.finalizeInfo();
        dirStack.getLast().dirSize += i.dirSize;
    }
    public void addFile(File f) {
	dirStack.getLast().dirSize += f.length();
	dirStack.getLast().dotInfo.dirSize += f.length();
    }
    
    /** Must be called before getRootNode, or root node won't have any children. */
    public void finalizeResult() {
        dirStack.getLast().finalizeInfo();
    }
}
