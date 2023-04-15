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

import java.security.*;
import java.io.*;

public class SHA256Digest {
    public static void main(String[] args) throws IOException {
	File inputFile = new File(args[0]);
	FileInputStream inStream = new FileInputStream(inputFile);
	DigestInputStream digestStream;
	try {
	    digestStream = new DigestInputStream(inStream, MessageDigest.getInstance("SHA-256"));
	} catch(NoSuchAlgorithmException nsae) {
	    nsae.printStackTrace();
	    System.err.println("Couldn't find an implementation of the SHA-256 algorithm.");
	    return;
	}
	digestStream.getMessageDigest().reset();
	byte[] buffer = new byte[4096];
	int totalBytesRead = 0;
	long fileSize = inputFile.length();
	while(totalBytesRead < fileSize)
	    totalBytesRead += digestStream.read(buffer);
	System.out.println(byteArrayToHexString(digestStream.getMessageDigest().digest()));
    }
    public static String byteArrayToHexString(byte[] array) {
	String result = "0x";
	for(byte b : array) {
	    result += Integer.toHexString(b & 0xFF);
	}
	return result;
    }
}
