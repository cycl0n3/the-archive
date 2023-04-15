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

/*
 * MainPanel.java
 *
 * Created on den 3 oktober 2006, 11:15
 */

package org.catacombae.dua;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.iharder.dnd.FileDrop;
import org.catacombae.dua.PortableFileChooser.PortableFileChooserMode;
import org.catacombae.dua.Result.Info;

/**
 *
 * @author  Erik
 */
public class MainPanel extends javax.swing.JPanel {
    private DecimalFormat progressFormatter = new DecimalFormat("0.00");
    private KeyListener keyListener;
    private PortableFileChooser jfc = new PortableFileChooser();
    private final DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;
    private final String calculateButtonIdleText, calculateButtonProcessingText;
    private boolean processing = false;
    private boolean abort = false;
    
    /** Creates new form MainPanel */
    public MainPanel(final Frame parentFrame) {
        initComponents();
        
        this.calculateButtonIdleText = calculateButton.getText();
        this.calculateButtonProcessingText = "Abort";
        this.treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("<empty>"), false);
        
        
        
	/* There seems to be a bug preventing the progress bar to function correctly if the range is
	   (Integer.MIN_VALUE, Integer.MAX_VALUE)... */
	progressBar.setMinimum(0);
	progressBar.setMaximum(Integer.MAX_VALUE);
	updateProgress(0.0);
	duTree.setModel(treeModel);
	keyListener = new KeyAdapter() {
                @Override
		public void keyPressed(KeyEvent ke) {
		    if(ke.getKeyCode() == KeyEvent.VK_G)
			gcWithInfo();
		}
	    };
	duTree.addKeyListener(keyListener);
        calculateButton.addKeyListener(keyListener);
	addKeyListener(keyListener);
	
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(!processing) {
                    jfc.setMode(PortableFileChooserMode.DIRECTORIES);
                    File f = jfc.show(parentFrame);
                    if(f != null)
                        startAnalysis(f);
                }
                else
                    abort = true;
            }
        });
        
        final JPopupMenu treeNodePopupMenu = new JPopupMenu();
        
        JMenuItem openItem = new JMenuItem("Open");
        if(FileOpener.isEnvironmentSupported()) {
            openItem.setEnabled(true);
            openItem.addActionListener(new OpenFileActionListener());
        }
        else
            openItem.setEnabled(false);
        treeNodePopupMenu.add(openItem);
        
        JMenuItem refreshSubtreeItem = new JMenuItem("Refresh subtree");
        refreshSubtreeItem.addActionListener(new RefreshSubtreeActionListener());
        treeNodePopupMenu.add(refreshSubtreeItem);

        duTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 || (e.getButton() == MouseEvent.BUTTON1 &&
                        (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0)) {
                    TreePath tp = duTree.getPathForLocation(e.getX(), e.getY());
                    
                    if(tp != null) {
                        Object nodeObj = tp.getLastPathComponent();
                        if(nodeObj instanceof DefaultMutableTreeNode) {
                            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) nodeObj;
                            if(!selectedNode.isLeaf()) {
                                Object userObj = selectedNode.getUserObject();
                                if(userObj instanceof Result.Info) {
                                    duTree.clearSelection();
                                    duTree.setSelectionPath(tp);
                                    duTree.requestFocus();

                                    treeNodePopupMenu.show(duTree, e.getX(), e.getY());
                                }
                            }
                        }
                        else {
                            throw new RuntimeException("Unexpected node type " +
                                    "in disk usage tree: " + nodeObj.getClass());
                        }
                    }
                }
            }
        });
        
        new FileDrop(this, new FileDrop.Listener() {
            public void filesDropped(File[] files) {
                if(!processing) {
                    // handle file drop
                    if(files.length == 1) {
                        if(files[0].isDirectory()) {
                            startAnalysis(files[0]);
                        }
                        else {
                            JOptionPane.showMessageDialog(MainPanel.this,
                                    "You can only drop directories.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if(files.length > 1) {
                        JOptionPane.showMessageDialog(MainPanel.this,
                                "You can only analyze one root at a time.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }   // end filesDropped
        }); // end FileDrop.Listener
    }

    private synchronized void setStatusText(String s) {
        statusField.setText(s);
        statusField.setCaretPosition(0);
    }
    
    public void updateDirProgress(final File f) {
        SwingUtilities.invokeLater(new Runnable() {
		public void run() {
                    setStatusText("Processing: " + f.getPath());
                    //System.err.print("!");
                }
        });
    }
    
    public boolean isAbortRequested() {
        return abort;
    }
    
    private void setTreeRoot(DefaultMutableTreeNode rootNode) {
        this.rootNode = rootNode;
        treeModel.setRoot(rootNode);
    }

    /** Starts the analysis thread and sets the UI elements to disabled. */
    public void startAnalysis(final File rootDir) {
	setUIEnabled(false);
	updateProgress(0.0);
	setTreeRoot(new DefaultMutableTreeNode("<building tree...>"));
	Runnable calcWorker = new Runnable() {
            public void run() {
                final Result r = doAnalysis(rootDir);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            if(r != null)
                                setTreeRoot(r.getRootNode());
                                
                            JScrollBar vsb = duTreeScroller.getVerticalScrollBar();
                            JScrollBar hsb = duTreeScroller.getHorizontalScrollBar();
                            vsb.setValue(vsb.getMinimum());
                            hsb.setValue(hsb.getMinimum());
                            setUIEnabled(true);
                            
                        }
                    });
                } catch(Exception e) {
                    reportThrowable(e);
                }
            }
        };
	
	Thread t = new Thread(calcWorker);
	t.start();
    }
    
    private Result doAnalysis(File rootDir) {
        try {
            updateProgress(0.0);
            final Result r = DiskUsageAnalyzer.gatherFiles(rootDir, MainPanel.this);
            updateProgress(1.0);
            
            return r;
        } catch(Throwable e) {
	    String stackTrace = "";
	    for(StackTraceElement ste : e.getStackTrace())
		stackTrace += ste.toString() + "\n";
	    JOptionPane.showMessageDialog(MainPanel.this, e.toString() + "\n" + stackTrace,
					  "Exception", JOptionPane.ERROR_MESSAGE);
            return null;
	}
    }
    
    private void recalculateParentSizes(Info info, long oldSize) {
        Info parentInfo = info.parentInfo;
        if(parentInfo != null) {
            long parentOldSize = parentInfo.dirSize;
            parentInfo.dirSize -= oldSize;
            parentInfo.dirSize += info.dirSize;
            treeModel.nodeChanged(parentInfo.node);
            recalculateParentSizes(parentInfo, parentOldSize);
        }
    }

    public KeyListener getKeyListener() { return keyListener; }

    private void setUIEnabled(boolean b) {
	//calculateButton.setEnabled(b);
        processing = !b;
            

        if(b) {
            calculateButton.setText(calculateButtonIdleText);
            abort = false;
            //System.err.print("Setting empty status text...");
            setStatusText("");
            //System.err.println("done.");
        }
        else {
            calculateButton.setText(calculateButtonProcessingText);
            if(abort) {
                JOptionPane.showMessageDialog(this, "INTERNAL ERROR: abort == true at " +
                        "setUIEnabled(false)\nReport this to developer.", "INTERNAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
                abort = false;
            }
        }
	duTree.setEnabled(b);
    }

    private void gcWithInfo() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true);
        gcWithInfo(ps);
        JOptionPane.showMessageDialog(this, new String(baos.toByteArray()), "Debug",
                JOptionPane.PLAIN_MESSAGE);
    }
    
    private void gcWithInfo(PrintStream ps) {
	ps.println();
	Runtime r = Runtime.getRuntime();		    
	{
	    long freeMemory = r.freeMemory(), maxMemory = r.maxMemory(), totalMemory = r.totalMemory();
	    long memoryUsage = totalMemory - freeMemory;
	    ps.println("Before gc:");
	    ps.println("  freeMemory(): " + freeMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(freeMemory) + ")");
	    ps.println("  maxMemory(): " + maxMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(maxMemory) + ")");
	    ps.println("  totalMemory(): " + totalMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(totalMemory) + ")");
	    ps.println("  Calculated memory usage: " + memoryUsage +
			       " (" + SpeedUnitUtils.bytesToBinaryUnit(memoryUsage) + ")");
	}
	r.gc();
	{
	    long freeMemory = r.freeMemory(), maxMemory = r.maxMemory(), totalMemory = r.totalMemory();
	    long memoryUsage = totalMemory - freeMemory;
	    ps.println("After gc:");
	    ps.println("  freeMemory(): " + freeMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(freeMemory) + ")");
	    ps.println("  maxMemory(): " + maxMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(maxMemory) + ")");
	    ps.println("  totalMemory(): " + totalMemory + " (" + SpeedUnitUtils.bytesToBinaryUnit(totalMemory) + ")");
	    ps.println("  Calculated memory usage: " + memoryUsage +
			       " (" + SpeedUnitUtils.bytesToBinaryUnit(memoryUsage) + ")");
	}
        
        ps.println();
        ps.println("abort=" + abort);
        ps.println("processing=" + processing);
    }
    
    public void updateProgress(final double relativeProgress) {
// 	System.out.println("updateProgress(" + relativeProgress + ");");
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
// 		    System.out.println("  Minimum: " + progressBar.getMinimum());
// 		    System.out.println("  Maximum: " + progressBar.getMaximum());
		    long range = progressBar.getMaximum() - (long)progressBar.getMinimum();
// 		    System.out.println("  Range: " + range);
// 		    System.out.println("  relativeProgress*range: " + relativeProgress*range);
		    long progress = progressBar.getMinimum() + (long)(relativeProgress*range);
// 		    System.out.println("  Progress: " + progress);
		    progressBar.setValue((int)progress);
		    progressBar.setString(progressFormatter.format(relativeProgress*100) + "%");
		}
	    });
    }
    
    private void reportThrowable(Throwable t) {
        t.printStackTrace();
        StringBuilder sb = new StringBuilder("Unhandled exception in application:\n\n");
        sb.append(t.toString());
        int i = 0;
        for(StackTraceElement ste : t.getStackTrace()) {
            if(i < 10) {
                sb.append(ste.toString());
                ++i;
            }
            else {
                sb.append("<...breaking at " + i + " stack trace entries for readability...>");
                break;
            }
        }
        sb.append("\n\nPlease check System.err output for possibly more detailed information.");
        JOptionPane.showMessageDialog(this, sb.toString(), "Unhandled exception",
                JOptionPane.ERROR_MESSAGE);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        duTreeScroller = new javax.swing.JScrollPane();
        duTree = new javax.swing.JTree();
        duTreeLabel = new javax.swing.JLabel();
        calculateButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        statusFieldWrapper = new javax.swing.JPanel();
        statusField = new javax.swing.JTextField();

        setOpaque(false);

        duTreeScroller.setViewportView(duTree);

        duTreeLabel.setText("Disk usage tree:");

        calculateButton.setText("Select root and calculate...");

        progressBar.setString("0.0%");
        progressBar.setStringPainted(true);

        statusFieldWrapper.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        statusField.setEditable(false);
        statusField.setAutoscrolls(false);
        statusField.setBorder(null);
        statusField.setCaretPosition(0);
        statusField.setFocusable(false);
        statusField.setOpaque(false);

        org.jdesktop.layout.GroupLayout statusFieldWrapperLayout = new org.jdesktop.layout.GroupLayout(statusFieldWrapper);
        statusFieldWrapper.setLayout(statusFieldWrapperLayout);
        statusFieldWrapperLayout.setHorizontalGroup(
            statusFieldWrapperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        );
        statusFieldWrapperLayout.setVerticalGroup(
            statusFieldWrapperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusFieldWrapper, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(calculateButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(duTreeScroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .add(duTreeLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(duTreeLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(duTreeScroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(calculateButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusFieldWrapper, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton calculateButton;
    public javax.swing.JTree duTree;
    private javax.swing.JLabel duTreeLabel;
    private javax.swing.JScrollPane duTreeScroller;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField statusField;
    private javax.swing.JPanel statusFieldWrapper;
    // End of variables declaration//GEN-END:variables
    
    private class OpenFileActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object obj = duTree.getSelectionPath().getLastPathComponent();
                if(obj instanceof DefaultMutableTreeNode) {
                    Object userObj = ((DefaultMutableTreeNode) obj).getUserObject();
                    if(userObj instanceof Result.Info) {
                        Info info = (Info) userObj;
                        try {
                            FileOpener.openFile(info.file);
                        } catch(IOException ex) {
                            JOptionPane.showMessageDialog(MainPanel.this,
                                    "Could not open directory:\n  \"" + info.file.getPath() + "\"");
                        }
                    }
                    else {
                        throw new RuntimeException("Unexepcted user object type in tree: " +
                                userObj.getClass());
                    }
                }
                else {
                    throw new RuntimeException("Unexpected node type in tree: " + obj.getClass());
                }
            } catch(Throwable t) {
                reportThrowable(t);
            }
        }
    }
    
    private class RefreshSubtreeActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object[] tp = duTree.getSelectionPath().getPath();
                if(tp != null && tp.length > 0) {
                    Object nodeObj = tp[tp.length - 1];
                    Object parentNodeObj = null;
                    if(tp.length > 1) {
                        parentNodeObj = tp[tp.length - 2];
                    }
                    if(nodeObj instanceof DefaultMutableTreeNode) {
                        final DefaultMutableTreeNode selectedNode =
                                (DefaultMutableTreeNode) nodeObj;

                        final DefaultMutableTreeNode parentNode;
                        final Result.Info parentInfo;
                        if(parentNodeObj != null) {
                            if(parentNodeObj instanceof DefaultMutableTreeNode) {
                                parentNode =
                                        (DefaultMutableTreeNode) parentNodeObj;
                                Object parentNodeUserObj = parentNode.getUserObject();
                                if(parentNodeUserObj instanceof Result.Info) {
                                    parentInfo = (Result.Info) parentNodeUserObj;
                                }
                                else {
                                    throw new RuntimeException("Unexpected parent " +
                                            "node user object type in disk usage " +
                                            "tree: " + parentNodeUserObj.getClass());
                                }
                            }
                            else {
                                throw new RuntimeException("Unexpected parent " +
                                        "node type in disk usage tree: " +
                                        parentNodeObj.getClass());
                            }
                        }
                        else {
                            parentNode = null;
                            parentInfo = null;
                        }

                        if(!selectedNode.isLeaf()) {
                            Object userObj = selectedNode.getUserObject();
                            if(userObj instanceof Result.Info) {
                                final Result.Info userInfo = (Result.Info) userObj;

                                if(!userInfo.file.exists()) {
                                    if(parentNode != null) {
                                        long oldSize = userInfo.dirSize;
                                        userInfo.dirSize = 0;
                                        recalculateParentSizes(userInfo, oldSize);
                                        treeModel.removeNodeFromParent(selectedNode);
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(MainPanel.this,
                                                "Root node no longer exists!",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else {
                                    setUIEnabled(false);

                                    DefaultMutableTreeNode[] childrenToRemove =
                                            new DefaultMutableTreeNode[selectedNode.getChildCount()];
                                    for(int i = 0; i < selectedNode.getChildCount(); ++i) {
                                        TreeNode curChild = selectedNode.getChildAt(i);
                                        if(curChild instanceof DefaultMutableTreeNode) {
                                            childrenToRemove[i] = (DefaultMutableTreeNode) curChild;
                                        }
                                        else {
                                            throw new RuntimeException("Invalid node type: " +
                                                    curChild.getClass());
                                        }
                                    }
                                    for(DefaultMutableTreeNode child : childrenToRemove) {
                                        treeModel.removeNodeFromParent(child);
                                    }
                                    selectedNode.setUserObject("<building tree...>");
                                    Runnable r = new Runnable() {
                                        public void run() {
                                            final Result r = doAnalysis(userInfo.file);
                                            SwingUtilities.invokeLater(new Runnable() {
                                                public void run() {
                                                    DefaultMutableTreeNode n =
                                                            r.getRootNode();
                                                    final Object userInfoObj =
                                                            n.getUserObject();

                                                    final Result.Info infoObj;
                                                    if(userInfoObj instanceof Result.Info) {
                                                        infoObj = (Result.Info) userInfoObj;
                                                    }
                                                    else {
                                                        throw new RuntimeException("Unexpected user object type: " +
                                                                userInfoObj.getClass());
                                                    }

                                                    infoObj.parentInfo = parentInfo;
                                                    
                                                    if(infoObj.parentInfo == null) {
                                                        setTreeRoot(n);
                                                    }
                                                    else {
                                                        int oldIndex =
                                                                treeModel.getIndexOfChild(parentNode,
                                                                selectedNode);
                                                        treeModel.removeNodeFromParent(selectedNode);

                                                        treeModel.insertNodeInto(n,
                                                                parentNode,
                                                                oldIndex);
                                                        recalculateParentSizes(infoObj, userInfo.dirSize);
                                                    }
                                                    
                                                    duTree.expandPath(new TreePath(n.getPath()));
                                                    
                                                    
                                                    setUIEnabled(true);
                                                }
                                            });
                                        }
                                    };
                                    new Thread(r).start();
                                }
                            }
                            else {
                                throw new RuntimeException("Refresh subtree invoked " +
                                        "for non-Result.Info user object type: " +
                                        userObj.getClass());
                            }
                        }
                        else {
                            throw new RuntimeException("Refresh subtree invoked " +
                                    "for leaf node!");
                        }
                    }
                    else {
                        throw new RuntimeException("Unexpected node type " +
                                "in disk usage tree: " + nodeObj.getClass());
                    }
                }
            } catch(Throwable t) {
                reportThrowable(t);
            }
        }
    }
}
