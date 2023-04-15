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

import java.util.Map;

public class Upload implements Transfer {
    private boolean transferInitiated = false;
    private boolean transferComplete = false;
    private int requestID;
    private long size;
    private long bytesSent;
    //private String currentFilename;
    private FileEntry currentEntry;
    private SessionHandler.FileSegment currentSegment;
    private Map<SessionHandler.FileSegment, FileEntry> segmentMap;
    
    public Upload(int requestID, long size, Map<SessionHandler.FileSegment, FileEntry> segmentMap) {
	this.requestID = requestID;
	this.size = size;
	this.segmentMap = segmentMap;
	bytesSent = 0;
    }
    
    public synchronized void signalCurrentCompleted(SessionHandler.FileSegment newFile) {
	if(!transferInitiated)
	    transferInitiated = true;
	else
	    bytesSent += getCurrentLength();
	
	currentSegment = newFile;
	currentEntry = segmentMap.get(currentSegment);
    }
    
    public synchronized void signalTransferComplete() {
	transferComplete = true;
    }
    
    public synchronized boolean transferComplete() {
	return transferComplete;
    }
    
    public synchronized String getCurrentFilename() {
	if(transferInitiated)
	    return currentEntry.getName();
	else
	    return "";
    }
    
    public synchronized double getCurrentProgress() {
	if(transferInitiated)
	    return ((double)getCurrentCompletedBytes())/getCurrentLength();
	else
	    return 0.0;
    }
    
    public synchronized long getCurrentCompletedBytes() {
	if(transferInitiated)
	    return currentSegment.getBytesRead();
	else
	    return 0;
    }
    
    public synchronized long getCurrentLength() {
	if(transferInitiated)
	    return currentEntry.getLength();
	else
	    return 0;
    }
    
    public synchronized double getTotalProgress() {
	if(transferInitiated)
	    return ((double)getTotalCompletedBytes())/getTotalLength();
	else
	    return 0.0;
    }
    
    public synchronized long getTotalCompletedBytes() {
	if(transferInitiated)
	    return bytesSent+getCurrentCompletedBytes();
	else
	    return 0;
    }
    
    public synchronized long getTotalLength() {
	return size;
    }
}
