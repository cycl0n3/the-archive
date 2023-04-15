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

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.LinkedList;
import java.security.*;

public class RateMyFilterStream {
    public static final String BACKSPACE79 = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
    
    public static void main(String[] args) throws IOException {
	testLinkStreamBandwidth();
    }
    
    public static void testMemoryBandwidth() {
	Random rnd = new Random();
	byte[] buffer = new byte[4096];
	byte[] buffer2 = new byte[buffer.length];
	rnd.nextBytes(buffer);
	
	//long time = 0;
	long lastTime = System.currentTimeMillis();
	long bytesSent = 0;
	long lastBytesSent = 0;
	while(true) {
	    System.arraycopy(buffer, 0, buffer2, 0, buffer.length);
	    bytesSent += buffer.length;
	    
	    long currentTime = System.currentTimeMillis();
	    if(currentTime-lastTime >= 1000) {
		printSpeed(System.out, bytesSent, lastBytesSent);
		lastTime = currentTime;
		lastBytesSent = bytesSent;
	    }
	}
    }
    public static void testLinkStreamBandwidth() throws IOException {
	LinkOutputStream los = new LinkOutputStream();
	LinkInputStream lis = new LinkInputStream(los);
	
	OutputStream topout = null;
	InputStream topin = null;
	if(false) {
	    MessageDigest digest_1 = null;
	    MessageDigest digest_2 = null;
	    try {
		digest_1 = MessageDigest.getInstance("SHA-256");
		digest_2 = MessageDigest.getInstance("MD5");
	    } catch(NoSuchAlgorithmException nsae) {
		nsae.printStackTrace();
		System.err.println("Couldn't find an implementation of the SHA-256 algorithm.");
		throw new RuntimeException();
	    }
	
	    topout = los;//new DigestOutputStream(los, digest_1);
	    topin = new DigestInputStream(lis, digest_2);
	}
	if(true) {
	    topout = los;
	    topin = new ByteCheckInputStream(lis);
	}

	Random rnd = new Random();
	byte[] buffer = new byte[4096];
	byte[] buffer2 = new byte[buffer.length];
	
	rnd.nextBytes(buffer);
	
 	long lastTime = System.currentTimeMillis();
	long bytesSent = 0;
	long lastBytesSent = 0;
	while(true) {
	    topout.write(buffer);
	    int bytesRead = 0;
	    while(bytesRead < buffer2.length)
		bytesRead += topin.read(buffer2, bytesRead, buffer2.length-bytesRead);
	    bytesSent += buffer2.length;
	    
	    long currentTime = System.currentTimeMillis();
	    if(currentTime-lastTime >= 1000) {
		printSpeed(System.out, bytesSent, lastBytesSent);
		lastTime = currentTime;
		lastBytesSent = bytesSent;
	    }
	}
    }
    public static void printSpeed(PrintStream ps, long currentBytesSent, long lastBytesSent) {
	long bytesPerSecond = currentBytesSent-lastBytesSent;
	ps.print(BACKSPACE79 + "Speed: " + SpeedUnitUtils.bytesToBinaryUnit(bytesPerSecond) + 
		 "/s / " + SpeedUnitUtils.bytesToDecimalBitUnit(bytesPerSecond) + "/s");
    }
}

class LinkInputStream extends InputStream {
    private LinkOutputStream los;
    public LinkInputStream(LinkOutputStream los) {
	this.los = los;
    }
    
    public int available() { return 0; }
    public void close() {}
    public void mark(int readlimit) {}
    public boolean markSupported() { return false; }
    public int read() {
	return los.read();
    }
    public int read(byte[] b) { return los.read(b, 0, b.length); }
    public int read(byte[] b, int off, int len) { return los.read(b, off, len); }
    public void reset() {}
    public long skip(long n) { return 0; }
}

class LinkOutputStream extends OutputStream {
    private final int bufferSize;
    private final int initialPoolSize;
    private final LinkedList<byte[]> useList;
    private final LinkedList<byte[]> bufferPool;
    private boolean isClosed;
    private int readPtr;
    private int writePtr;
    private byte[] readSegment;
    private byte[] writeSegment;
    
    public LinkOutputStream() {
	this(4097, 10);
    }
    public LinkOutputStream(int bufferSize, int initialPoolSize) {
	this.bufferSize = bufferSize;
	this.initialPoolSize = initialPoolSize;
	useList = new LinkedList<byte[]>();
	bufferPool = new LinkedList<byte[]>();
	incrementBufferPool();
	
	isClosed = false;
	readPtr = 0;
	writePtr = bufferSize;
    }
    
    public void incrementBufferPool() {
	System.err.println("incrementBufferPool()");
	for(int i = 0; i < initialPoolSize; ++i)
	    bufferPool.addLast(new byte[bufferSize]);
    }
    
    public int read() {
	byte[] ba = new byte[1];
	while(read(ba, 0, 1) != 1);
	return (int)ba[0];
    }
    public int read(byte[] b) {
	return read(b, 0, b.length);
    }
    public int read(byte[] b, int off, int len) {
	int currentOffset = off;
	int bytesLeftToRead = len;
	while(bytesLeftToRead > 0) {
	    //byte[] currentBuffer;
// 	    synchronized(useList) {
// 		if(useList.isEmpty()) {
// 		    useList.wait();
// 		}
// 	    }
	    if(bytesLeftToRead >= bufferSize-readPtr)
		readSegment = useList.removeFirst();
	    else
		readSegment = useList.getFirst();
	    
	    int y = readSegment.length-readPtr;
	    int bytesToRead = (y < bytesLeftToRead ?
			       y : bytesLeftToRead);
	    //System.err.println("b.length=" + readSegment.length + " currentSourceOffset=" + readPtr + " readSegment.length=" + b.length + " writePtr=" + currentOffset + " bytesToStore=" + bytesToRead);
	    System.arraycopy(readSegment, readPtr, b, currentOffset, bytesToRead);

	    if(bytesLeftToRead >= bufferSize-readPtr) {
		//System.err.println("returning " + readSegment);
		bufferPool.addFirst(readSegment);
	    }
	    currentOffset += bytesToRead;
	    bytesLeftToRead -= bytesToRead;
	    readPtr += bytesToRead;

	    if(writePtr == readPtr && writeSegment == readSegment) {
		writePtr = 0;
		readPtr = 0;
	    }
	    else if(readPtr == bufferSize)
		readPtr = 0;
	}
	return len;
    }
//     public int available() {
// 	return bufferSize*poolPtr + writePtr;
//     }
    
    public void close() throws IOException {
	if(!isClosed) { isClosed = true; }
	else throw new IOException("Stream already closed!");
    }
    public void flush() throws IOException {
	if(!isClosed) {}
	else throw new IOException("Stream closed!");
    }
    public void write(byte[] b) throws IOException {
	if(!isClosed) {
	    write(b, 0, b.length);
	}
	else throw new IOException("Stream closed!");
    }
    public void write(byte[] b, int off, int len) throws IOException {
	if(!isClosed) {
	    /* We want to write */
	    long bytesStored = 0;
	    int currentSourceOffset = off;

	    while(bytesStored < len) {
		int remainingBufferSpace = bufferSize-writePtr;
		if(remainingBufferSpace == 0) {
		    //Allocate a new buffer
		    if(bufferPool.isEmpty())
			incrementBufferPool();
		    useList.addLast(bufferPool.removeFirst());
		    writePtr = 0;
		    remainingBufferSpace = bufferSize;
		}
		writeSegment = useList.getLast();
		int leftToRead = len-(currentSourceOffset-off);
		int bytesToStore = (leftToRead < remainingBufferSpace ?
				    leftToRead : remainingBufferSpace); //Math.min(lef..., rem..);
		//System.err.println("b.length=" + b.length + " currentSourceOffset=" + currentSourceOffset + " writeSegment.length=" + writeSegment.length + " writePtr=" + writePtr + " bytesToStore=" + bytesToStore);
		System.arraycopy(b, currentSourceOffset, writeSegment, writePtr, 
				 bytesToStore);
		bytesStored += bytesToStore;
		writePtr += bytesToStore;
		currentSourceOffset += bytesToStore;
	    }
	}
	else throw new IOException("Stream closed!");
    }
    public void write(int b) throws IOException {
	if(!isClosed) {
	    byte[] ba = new byte[1];
	    ba[0] = (byte)b;
	    write(ba, 0, 1);
	}
	else throw new IOException("Stream closed!");
    }
}

class ByteCheckInputStream extends FilterInputStream {
    
    public static final byte CHECK = 81;

    public ByteCheckInputStream(InputStream is) {
	super(is);
    }
    
    public int read() throws IOException {
	int i = in.read();
	if((byte)i == CHECK);
	    //System.err.println("FOUND!");
	return i;
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
	int bytesRead = super.read(b, off, len);
	for(int i = off; i < len; ++i) {
	    byte be = b[i];
	    if(be == CHECK);
	    //System.err.println("FOUND!");
	}
	return bytesRead;
    }
}
