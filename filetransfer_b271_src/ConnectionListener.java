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

/*
 * 256-bitars (32 bytes) signatur som måste stämma för att två peers ska känna igen protokollet:
 *
 * Byte för byte (hexadecimal):
 * 0xD8, 0xB1, 0x9B, 0xC8, 0xB6, 0x71, 0x8A, 0x4D, 0x97, 0x0F, 0x67, 0xF6, 0x27, 0x8B, 0x67, 0x47,
 * 0xB7, 0x3D, 0xEA, 0x29, 0x89, 0xE4, 0xF3, 0x12, 0x92, 0xDC, 0x21, 0x44, 0xDB, 0xDA, 0x37, 0x2B
 *
 * Byte för byte (decimal, signed byte, java style...):
 * -40, -79, -101, -56, -74, 113, -118, 77, -105, 15, 103, -10, 39, -117, 103, 71, -73, 61, -22,
 * 41, -119, -28, -13, 18, -110, -36, 33, 68, -37, -38, 55, 43
 *
 * Som en lång hexadecimal sträng:
 * 0xD8B19BC8B6718A4D970F67F6278B6747B73DEA2989E4F31292DC2144DBDA372B
 *
 * Denna sträng måste alltid vara det första som skickas mellan två klienter.
 *
 * Denna sträng svarar sedan servern med:
 *
 * Byte för byte (hexadecimal):
 * 0x83, 0x82, 0x3A, 0xDD, 0x1D, 0x46, 0xA6, 0x3A, 0xC8, 0xEF, 0x75, 0x16, 0x97, 0x45, 0x3F, 0x1E,
 * 0xD3, 0xC6, 0x73, 0xC5, 0x9D, 0x8C, 0x5B, 0x48, 0x14, 0xDE, 0xD5, 0x3A, 0x4B, 0x67, 0xBD, 0xC5
 *
 * Byte för byte (decimal, signed byte, java style...):
 * -125, -126, 58, -35, 29, 70, -90, 58, -56, -17, 117, 22, -105, 69, 63, 30, -45, -58, 115, -59,
 * -99, -116, 91, 72, 20, -34, -43, 58, 75, 103, -67, -59
 *
 * Som en lång hexadecimal sträng:
 * 0x83823ADD1D46A63AC8EF751697453F1ED3C673C59D8C5B4814DED53A4B67BDC5
 */

import java.util.*;
import java.net.*;
import java.io.IOException;

public class ConnectionListener {
    public static final byte[] CLIENT_APPLICATION_SIGNATURE = { -40, -79, -101, -56, -74, 113, -118, 77, 
								-105, 15, 103, -10, 39, -117, 103, 71, -73, 
								61, -22, 41, -119, -28, -13, 18, -110,
								-36, 33, 68, -37, -38, 55, 43 };
    public static final byte[] SERVER_APPLICATION_SIGNATURE = { -125, -126, 58, -35, 29, 70, -90, 58, -56, 
								-17, 117, 22, -105, 69, 63, 30, -45, -58, 
								115, -59, -99, -116, 91, 72, 20, -34, -43,
								58, 75, 103, -67, -59 };
    public static final int DEFAULT_PORT_NUMBER = 3632;
    private int portNumber = DEFAULT_PORT_NUMBER;
    private byte[] receivedSignature = new byte[32];
//     private boolean abort = false;
//     private boolean listening = false;
//     private LinkedList<SessionHandler> handlers = new LinkedList<SessionHandler>();
    private Listener listener = null;
//     private Thread listenThread = null;
//     private ServerSocket ss = null;
    private FileTransfer main;

    public ConnectionListener(FileTransfer main) {
	this.main = main;
    }
    
    public void setListenPort(int newPortNumber) {
	portNumber = newPortNumber;
    }
    public int getListenPort() {
	return portNumber;
    }
    
    public synchronized boolean startListening() {
	if(listener != null && listener.isRunning())
	    return false;
	else {
	    listener = new Listener();
	    synchronized(listener) {
		if(!listener.isRunning()) {
		    listener.start();
		    return true;
		}
		else
		    return false;
	    }
	}
// 	if(listenThread == null) {
// 	    abort = false;
// 	    listener = new Listener();
// 	    listenThread = new Thread(listener);
// 	    listenThread.start();
// 	    listening = true;
// 	    return true;
// 	}
// 	else {
// 	    listening = false;
// 	    return false;
// 	}
    }
    public synchronized void stopListening() {
	// - Check if listener is running, and if it is:
	//   - Send abort signal
	//   - Wait for confirm signal
	if(listener != null) {
	    synchronized(listener) {
		if(listener.isRunning()) {
		    listener.abort();
		}
	    }
	}
	    
	
	
// 	abort = true;
// 	if(ss != null) {
// 	    try {
// 		ss.close();
// 		ss = null;
// 	    } catch(Exception e) {
// 		e.printStackTrace();
// 	    }
// 	}
// 	synchronized(listener) {
// 	    try {
// 		listener.wait();
// 	    } catch(InterruptedException ie) {
// 		throw new RuntimeException(ie);
// 	    }
// 	}
    }

    public synchronized boolean isListening() {
	return listener.isRunning();
    }

//     public void addHandler(SessionHandler sh) {
// 	handlers.addLast(sh);
//     }
    private class Listener implements Runnable {
	private boolean running = false;
	private boolean abort = false;
	private ServerSocket ss;
	
	public synchronized boolean isRunning() {
	    return running;
	}

	public synchronized void start() {
	    if(!running) {
		new Thread(this).start();
		try {
		    while(!running) {
			wait();
		    }
		} catch(InterruptedException ie) {
		    throw new RuntimeException(ie);
		}
	    }
	}
	
	public synchronized void abort() {
	    if(running) {
		abort = true;
		try {
		    ss.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		    /* If an exception occurs here, shall we still set the abort flag and wait
		     * for receipt? It might hang... let's just throw it.
		     */
		    throw new RuntimeException(ioe);
		}
		try {
		    while(running) {
			wait();
		    }
		} catch(InterruptedException ie) {
		    throw new RuntimeException(ie);
		}
	    }
	}
	
	public void run() {
	    synchronized(this) {
		running = true;
		notify();
	    }
	    //System.err.println("ConnectionListener running");
	    main.setStatus("Not listening for incoming connections.");
	    try {
		ss = new ServerSocket(portNumber);
		main.setStatus("Listening for incoming connections at port " + portNumber + "...");
		main.setListenStatus(true);
		while(!abort) {
		    try {
			final Socket s = ss.accept();
			new SwingWorker() {
			    public Object construct() {
				synchronized(Listener.this) {
				    //System.err.println("ConnectionListener accepted a connection. Checking for signature...");
				    try {
					s.getInputStream().read(receivedSignature);
					if(Main.byteArraysEqual(receivedSignature, CLIENT_APPLICATION_SIGNATURE)) {
					    //System.err.println("Signature correct. Establishing connection...");
					    if(main.confirmAcceptConnection(s)) {
						try {
						    s.getOutputStream().write(SERVER_APPLICATION_SIGNATURE);
						    main.newIncomingConnectionSignaled(s);
						} catch(IOException se) {
						    ConnectionMonitorPanel.displayConnectionLostMessage(main.getMainWindow(), s.getInetAddress());
						}
					    }
					    else
						s.close();
					}
					else {
					    //System.err.println("Incorrect application signature. Connection ignored.");
					    s.close();
					}
				    } catch(IOException se) {
					// A disconnection has occurred. No point in making a fuzz about it...
				    } catch(Exception e) {
					e.printStackTrace();
					System.err.println("Something went wrong when trying to handle an incoming connection. ConnectionListener runs unaffected.");
				    }
				}
				return null;
			    }
			}.start();
		    }
		    catch(Exception e) {
			if(!abort)
			    e.printStackTrace();
		    }
		}
	    }
	    catch(Exception e) {
		//e.printStackTrace();
		//System.err.println("Couldn't create server socket for port 3632");
		main.signalListenFailed();
		ss = null;
		//stopListening();
	    }
	    //System.err.println("ConnectionListener exited");
	    main.setStatus("Not listening for incoming connections.");
	    main.setListenStatus(false);
	    synchronized(this) {
		running = false;
		notify();
// 		listening = false;
// 		listener = null;
// 		listenThread = null;
// 		notify();
	    }
	}
    }

}
