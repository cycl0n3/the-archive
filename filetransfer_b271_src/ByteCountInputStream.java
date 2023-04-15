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

import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

public class ByteCountInputStream extends FilterInputStream {
    private long bytesRead;

    public ByteCountInputStream(InputStream in) {
	super(in);
	bytesRead = 0;
    }
    public int read() throws IOException {
	int i = super.read();
	++bytesRead;
	return i;
    }
    public int read(byte[] b) throws IOException {
	int i = super.read(b);
	bytesRead += i;
	return i;
    }
    public int read(byte[] b, int off, int len) throws IOException {
	int i = super.read(b, off, len);
	bytesRead += i;
	return i;
    }
    public long skip(long n) throws IOException {
	long l = super.skip(n);
	bytesRead += l;
	return l;
    }
    public long getBytesRead() {
	return bytesRead;
    }
}
