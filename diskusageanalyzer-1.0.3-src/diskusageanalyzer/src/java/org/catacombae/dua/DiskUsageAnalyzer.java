/*-
 * Copyright (C) 2007 Erik Larsson
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

package org.catacombae.dua;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class DiskUsageAnalyzer {
    public static final String VERSION = "1.0.3";
    
    public static Result gatherFiles(File f, MainPanel mp) throws IOException {
	if(f.isDirectory()) {
	    f = f.getCanonicalFile();
	    File[] files = f.listFiles();
	    Result r = new Result(f);
	    gatherFilesInternal(files, r, mp, 0.0, 1.0);
            r.finalizeResult();
	    return r;
	}
	else
	    throw new RuntimeException("Root point must be a directory");
    }
    
    private static void gatherFilesInternal(File[] files, Result r, MainPanel mp,
            double lowLimit, double highLimit) throws IOException {
        if(mp.isAbortRequested())
            return;
        
	double increment = (highLimit - lowLimit) / files.length;
	for(int i = 0; i < files.length; ++i) {
            if(mp.isAbortRequested())
                return;
	    File f = files[i];
	    double newLowLimit = lowLimit + i*increment;
	    double newHighLimit = lowLimit + (i+1)*increment;
	    mp.updateProgress(newLowLimit);

	    if(f.isDirectory() && f.getCanonicalPath().equals(f.getAbsolutePath())) {
                mp.updateDirProgress(f);
		r.startDirectory(f);
		File[] newFiles = f.listFiles();
		if(newFiles != null)
		    gatherFilesInternal(newFiles, r, mp, newLowLimit, newHighLimit);
		else
                    System.out.println("WARNING: " + f.getAbsolutePath() +
                            " claims to be a directory but can't be listed. (Access denied?)");
                r.endDirectory(f);
	    }
	    else {
		r.addFile(f);
	    }
	}
    }
    
    public static void main(String[] args) {
        // When on a Mac, it is nice to have the program behave in a mac-way.
        if(System.getProperty("os.name").equalsIgnoreCase("mac os x")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            //System.setProperty("com.apple.mrj.application.apple.menu.about.name",
            //        "TypherTransfer");
        }

       	try {
	    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	    /*
	     * Description of look&feels:
	     *   http://java.sun.com/docs/books/tutorial/uiswing/misc/plaf.html
	     */
	}
	catch(Exception e) {
	    //It's ok. Non-critical.
	}

        createMainWindow();
    }

    private static void createMainWindow() {

        JFrame mainFrame = new JFrame("Catacombae DiskUsageAnalyzer " +
                DiskUsageAnalyzer.VERSION);

        final MainPanel mp = new MainPanel(mainFrame);

        if(System.getProperty("os.name").equalsIgnoreCase("mac os x")) {

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            JMenuItem newWindowItem = new JMenuItem("New window");

            newWindowItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    createMainWindow();
                }
            });
            newWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

            fileMenu.add(newWindowItem);
            menuBar.add(fileMenu);

            mainFrame.setJMenuBar(menuBar);
        }

	mainFrame.add(mp, BorderLayout.CENTER);
	mainFrame.pack();
	mainFrame.setLocationRelativeTo(null);
	mainFrame.setVisible(true);
	mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	mainFrame.addKeyListener(mp.getKeyListener()); // To listen for debug commands when UI is disabled
    }
}
