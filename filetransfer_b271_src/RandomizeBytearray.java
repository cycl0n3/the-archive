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

import java.util.Random;

public class RandomizeBytearray {
    public static void main(String[] args) {
// 	System.out.println(0x7fffffff);
// 	System.out.println(0x7fffffffffffffffL+1);
// 	System.out.println(0x7fffffffffffffffL/0x7fffffffL);
	byte[] array = new byte[Integer.parseInt(args[0])];
	Random rand = new Random(System.nanoTime());
	rand.nextBytes(array);
	for(byte b : array)
	    System.out.print(toHexString(b) + ", ");
	System.out.println();
	for(byte b : array)
	    System.out.print(b + ", ");
	System.out.println();
    }
    public static String toHexString(byte s) {
	StringBuffer result = new StringBuffer(4);
	result.append("0x");
	for(int i = 0; i < 2; i++) {
	    int currentChar = (s >> 4*(1-i)) & 0x0000000F;
	    if(currentChar < 10)
		result.append((int)((s >> 4*(1-i)) & 0x0000000F));
	    else
		result.append((char)(currentChar-10+65));
	}
	return result.toString();
    }
}
