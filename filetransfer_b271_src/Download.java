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

public class Download implements Transfer {
    private boolean transferInitiated = false;
    private boolean transferComplete = false;
    
    private String currentFilename = "";
    private long currentCompletedBytes = 0;
    private long currentLength = 0;
    private long totalCompletedBytes = 0;
    private long totalLength;
    private long numberOfFiles;
    
    public Download(long totalLength, long numberOfFiles) {
	this.totalLength = totalLength;
	this.numberOfFiles = numberOfFiles;
    }
    
    public synchronized void signalNextFile(String filename, long currentLength) {
	this.totalCompletedBytes += this.currentCompletedBytes;
	
	this.currentFilename = filename;
	this.currentCompletedBytes = 0;
	this.currentLength = currentLength;
	transferInitiated = true;
    }
    
    public synchronized void addCurrentCompletedBytes(long bytes) {
	currentCompletedBytes += bytes;
    }
    
    public synchronized String getCurrentFilename() {
	if(!transferInitiated)
	    return "";
	else
	    return currentFilename;
    }
    
    public synchronized double getCurrentProgress() {
	if(!transferInitiated)
	    return 0;
	else
	    return ((double)getCurrentCompletedBytes())/getCurrentLength();
    }
    
    public synchronized long getCurrentCompletedBytes() {
	if(!transferInitiated)
	    return 0;
	else
	    return currentCompletedBytes;
    }
    
    public synchronized long getCurrentLength() {
	if(!transferInitiated)
	    return 1;
	else
	    return currentLength;
    }

    public synchronized double getTotalProgress() {
	if(!transferInitiated)
	    return 0.0;
	else
	    return ((double)getTotalCompletedBytes())/getTotalLength();
    }
    
    public synchronized long getTotalCompletedBytes() {
	if(!transferInitiated)
	    return 0;
	else
	    return (totalCompletedBytes + currentCompletedBytes);
    }
    
    public synchronized long getTotalLength() {
	if(!transferInitiated)
	    return 1;
	else
	    return totalLength;
    }

    public synchronized boolean transferComplete() {
	return transferComplete;
    }

    public synchronized void signalTransferComplete() {
	transferComplete = true;
    }
}
