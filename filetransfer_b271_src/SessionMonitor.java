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

import java.net.InetAddress;
import java.io.File;

/**
 * All classes implementing the metods of this interface have to be aware of the
 * threading issue that could arise from altering swing-components through these
 * methods. They may be called in a highly threaded environment, so as often as
 * you can, use SwingUtilities.invokeLater and SwingUtilites.invokeAndWait when
 * altering swing-components.
 */
public interface SessionMonitor {
    public static final int SKIP = 234;
    public static final int CANCEL = 9288;
    public static final int OVERWRITE = 22881;
    public static final int OVERWRITE_ALL = 2488;
    public InetAddress getAddress();
    public SessionHandler getSessionHandler();
    public void signalConnectionLost();
    public File confirmReceiveFiles(DirectoryEntry root);
    public void printMessageLine(String s);
    public void printMessage(String s);
    //public void displayWarning(String lines...);
    //public void reportFileTransferProgress(long bytesCompleted, long totalBytes);
    public int confirmOverwriteFile(File file);
    public void disconnect();
    public void monitorDownload(Download dl);
}
