/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.catacombae.dua;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 *
 * @author erik
 */
public class PortableFileChooser {
    private class ObjectContainer<A> {
        A o;
    }

    public enum PortableFileChooserMode {
        FILES,
        DIRECTORIES,
    }

    private PortableFileChooserMode mode;

    public PortableFileChooser() {
        this.mode = PortableFileChooserMode.FILES;
    }

    public void setMode(PortableFileChooserMode mode) {
        if(mode == null)
            throw new IllegalArgumentException("'mode' is null");

        this.mode = mode;
    }

    public File show(final Frame parent) {
        if(!SwingUtilities.isEventDispatchThread()) {
            /* Make sure that we always run this code in the event dispatch
             * thread to avoid any race conditions. */
            try {
                final ObjectContainer<File> result =
                        new ObjectContainer<File>();
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        result.o = show(parent);
                    }
                });

                return result.o;
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            } catch(InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        String toolkitProperty = System.getProperty("awt.toolkit");
        if(toolkitProperty != null &&
                toolkitProperty.equals("apple.awt.CToolkit")) {
            String originalProperty =
                    System.getProperty("apple.awt.fileDialogForDirectories");
            if(mode == PortableFileChooserMode.DIRECTORIES)
                System.setProperty("apple.awt.fileDialogForDirectories",
                        "true");
            else
                System.setProperty("apple.awt.fileDialogForDirectories",
                        "false");

            FileDialog fd = new FileDialog(parent);
            fd.setMode(FileDialog.LOAD);
            fd.setVisible(true);

            String filename = fd.getFile();
            File f = (filename == null ? null :
                new File(new File(fd.getDirectory()), filename));

            if(originalProperty != null)
                System.setProperty("apple.awt.fileDialogForDirectories",
                        originalProperty);
            else
                System.clearProperty("apple.awt.fileDialogForDirectories");

            return f;
        }
        else {
            JFileChooser jfc = new JFileChooser();
            if(mode == PortableFileChooserMode.DIRECTORIES)
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            else
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            jfc.setMultiSelectionEnabled(false);
            if(jfc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
                return jfc.getSelectedFile();
            else
                return null;
        }
    }
}
