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

/* Unfinished material.. */
public class InterruptStreamTest {
    
    public static void main(String[] args) throws IOException {
	println("InterruptStreamTest");
	if(args[0].equalsIgnoreCase("server"))
	    server();
	else if(args[0].equalsIgnoreCase("client"))
	    client(args[1]);
	else
	    println("huhu");
    }
    public static void client(String serverAddress) throws IOException {
	println("client running");
	println("connecting to " + serverAddress);
	Socket s = new Socket(serverAddress, 9271);
	println("connected. transfering data to server. type a message to send it instantly.");
	
    }
    public static void server() throws IOException {
	println("server running");
	ServerSocket ss = new ServerSocket(9271);
	Socket s = ss.accept();
    }

    public static void println(String s) {
	System.out.println(s);
    }
}

class Package {
    public static final int MAX_SIZE = 4096;
    
    public byte[] data;
    
    public Package(int size) {
	if(size > MAX_SIZE || size < 0)
	    throw new IllegalArgumentException();
	data = new byte[size];
    }
}
class ExpressPackage {
    public static final int MAX_SIZE = 512;
    
    public byte[] data;
    
    public ExpressPackage(int size) {
	if(size > MAX_SIZE || size < 0)
	    throw new IllegalArgumentException();
	data = new byte[size];
    }
}

// class InterruptiblePackageOutputStream extends FilterOutputStream {
    
// }
// class InterruptiblePackageInputStream extends FilterInputStream {
//     private ExpressPackageHandler eph;
    
//     public InterruptiblePackageInputStream(InputStream is, ExpressPackageHandler eph) {
// 	this.eph = eph;
//     }
    
//     public int available() { return 0; }
//     public void close() {}
//     public void mark(int readlimit) {}
//     public boolean markSupported() { return false; }
//     public int read() {
// 	return is.read();
//     }
//     public int read(byte[] b) { return is.read(b, 0, b.length); }
//     public int read(byte[] b, int off, int len) { return is.read(b, off, len); }
//     public void reset() {}
//     public long skip(long n) { return 0; }
// }
