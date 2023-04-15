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

import java.text.DecimalFormat;

public class Main {
    public static final String APPLICATION_NAME = "TypherTransfer";
    public static final String VERSION = "0.13";
    //public static final String POST_VERSION_STRING = "";
    public static final String COPYRIGHT_MESSAGE = "Copyright \u00A9 2005-2006 Erik Larsson";
    public static final String[] NOTICES = { "", 
					     "Using the library swing-layout (https://swing-layout.dev.java.net/)", 
					     "    Copyright (C) 2005-2006 Sun Microsystems, Inc.", 
					     "    Licensed under the Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)" };
    public static final String WEBPAGE_URL = "http://www.typhontools.cjb.net/";
    
    public static void main(String[] args) {
	System.setProperty("swing.aatext", "true"); //Antialiased text
       	try {
	    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	    /*
	     * Beskrivning av olika look&feels:
	     *   http://java.sun.com/docs/books/tutorial/uiswing/misc/plaf.html
	     */
	}
	catch(Exception e) {
	    //Gick det inte? jaja, ingen fara
	}
	new FileTransfer();
    }
        
    public static String byteArrayToHexString(byte[] array) {
	String result = "0x";
	for(byte b : array) {
	    result += Integer.toHexString(b & 0xFF);
	}
	return result;
    }
    public static boolean byteArraysEqual(byte[] a, byte[] b) {
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
}
