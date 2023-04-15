/*-
 * Copyright (C) Erik Larsson
 *
 * All rights reserved.
 */
package org.catacombae.dua;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Erik Larsson
 */
public class FileOpener {
    /**
     * Tells whether or not the currently running operating environment (i.e. the combination of
     * your operating system and Java platform version/implementation) is supported by FileOpener.
     * 
     * @return whether or not the currently running operating environment is supported by
     * FileOpener.
     */
    public static boolean isEnvironmentSupported() {
        return (Java6Specific.isJava6OrHigher() && Java6Specific.canOpen()) ||
                System.getProperty("os.name").toLowerCase().startsWith("windows") ||
                System.getProperty("os.name").toLowerCase().startsWith("mac os x");
    }
    
    /**
     * Attempts to perform a platform specific "open file" operation.
     * 
     * @param f the file to open.
     * @throws java.io.IOException if anything goes wrong while opening the file.
     * @throws java.lang.UnsupportedOperationException if the implementation does not support the
     * currently running operating environment (check first with isEnvironmentSupported()).
     */
    public static void openFile(File f) throws IOException, UnsupportedOperationException {
        IOException finalException = null;
	if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
	    final String finalCommand = "cmd.exe /c start \"FileOpener\" \"" + f.getAbsolutePath() + "\"";
            try {
                Process p = Runtime.getRuntime().exec(finalCommand);
                return;
            } catch(IOException ioe) {
                finalException = ioe;
            }
        }
        
	if(System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
	    final String[] finalCommand = new String[] { "open", f.getAbsolutePath() };
            try {
                Process p = Runtime.getRuntime().exec(finalCommand);
                return;
            } catch(IOException ioe) {
                finalException = ioe;
            }
        }
        
        if(Java6Specific.isJava6OrHigher() && Java6Specific.canOpen()) {
            try {
                Java6Specific.openFile(f);
                return;
            } catch(IOException ioe) {
                finalException = ioe;
            }
        }
        
        if(finalException != null)
            throw finalException;
        else
            throw new UnsupportedOperationException("File open operation not supported in this " +
                    "operating environment!");
    }
}
