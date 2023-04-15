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

import java.net.Socket;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainWindow extends JFrame {
    private boolean oldSendFiles = false;
    private boolean noTabs = true;
    
    private FileTransfer fileTransfer;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JCheckBoxMenuItem listenItem;
    private JPanel backgroundPanel;
    private JTabbedPane connectionMonitors;
    private JPanel statusField;
    private JLabel statusLabel;
    private final JFileChooser sendFilesChooser;
    private final JFileChooser saveDirChooser;
    
    public MainWindow(FileTransfer fileTransfer) {
	super(Main.APPLICATION_NAME + " " + Main.VERSION /*+ " " + Main.POST_VERSION_STRING*/);
	this.fileTransfer = fileTransfer;
	
	sendFilesChooser = new JFileChooser();
	sendFilesChooser.setMultiSelectionEnabled(true);
	sendFilesChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

	saveDirChooser = new JFileChooser(System.getProperty("user.dir"));
	saveDirChooser.setMultiSelectionEnabled(false);
	saveDirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
	saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	saveDirChooser.setDialogTitle("Select target directory");
	saveDirChooser.setApproveButtonText("Select");

	initComponents();
    }
    
    public boolean useOldSendFilesDialog() {
	return oldSendFiles;
    }

    public JFileChooser getSendFilesChooser() {
	return sendFilesChooser;
    }

    public JFileChooser getSaveDirChooser() {
	return saveDirChooser;
    }

    public FileTransfer getFileTransfer() {
	return fileTransfer;
    }
    
    private void initComponents() {
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    fileTransfer.terminateProgramWithConfirmation();
		}
	    });

	setUpMenus();

	backgroundPanel = new JPanel();
	connectionMonitors = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	
	connectionMonitors.addTab("yada", new ConnectionMonitorPanel());

	statusField = new JPanel();
	statusField.setLayout(new BorderLayout());
	statusField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	statusLabel = new JLabel("Starting...");
	statusField.add(statusLabel, BorderLayout.CENTER);

	getContentPane().add(connectionMonitors, BorderLayout.CENTER);
	getContentPane().add(statusField, BorderLayout.SOUTH);
	
	//setSize(720, 405); //Widescreen 16:9 is da future ;)
	pack();
	setLocationRelativeTo(null);
	connectionMonitors.removeAll();
	connectionMonitors.addTab("No connections open", new JLabel("No connections open", SwingConstants.CENTER));
    }

    private void setUpMenus() {
	menuBar = new JMenuBar();
	fileMenu = new JMenu("File");
	JMenuItem newConnectionItem = new JMenuItem("New connection");
	newConnectionItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.newConnectionSignaled();
		}
	    });
	newConnectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	//newConnectionItem.setMnemonic(KeyEvent.VK_N);
	//newConnectionItem.setDisplayedMnemonicIndex(1);
	fileMenu.add(newConnectionItem);

	
	JMenuItem showSystemMessagesItem = new JMenuItem("Show system messages");
	showSystemMessagesItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.showSystemMessagesSignaled();
		}
	    });
	fileMenu.add(showSystemMessagesItem);
	showSystemMessagesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

	// System.getProperty("mrj.version") is deprecated, sadly. The fact that os.name reads "mac os x" does not automatically mean that the apple java extensions are there. what if we use a vm from another company under mac os x???
	if(!System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
	    JMenuItem exitItem = new JMenuItem("Exit");
	    exitItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			fileTransfer.exitSignaled();
		    }
		});
	    exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    fileMenu.add(exitItem);
	}
	else {
	    MacSpecifics.registerQuitHandler(new MacSpecifics.QuitHandler() {
		    public boolean acceptQuit() {
			return fileTransfer.confirmQuit();
		    }
		});
	}
	
	/*
	JMenuItem Item = new JMenuItem("");
	Item.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.Signaled();
		}
	    });
	*/
	
	// Start of connectionMenu
	JMenu connectionMenu = new JMenu("Connection");
	JMenuItem showPackageQueueItem = new JMenuItem("Show package queue");
	showPackageQueueItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.showPackageQueueSignaled();
		}
	    });
	JMenuItem disconnectItem = new JMenuItem("Disconnect");
	disconnectItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.disconnectSignaled();
		}
	    });
	disconnectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

	connectionMenu.add(showPackageQueueItem);
	connectionMenu.add(disconnectItem);
	// End of connectionMenu


	// Start of option menu
	JMenu viewMenu = new JMenu("Options");

	JMenu speedUnitChooser = new JMenu("Speed units");
	final JRadioButtonMenuItem bits = new JRadioButtonMenuItem("bit/s");
	final JRadioButtonMenuItem bytes = new JRadioButtonMenuItem("B/s");
	final JRadioButtonMenuItem bitsAndBytes = new JRadioButtonMenuItem("bit/s / B/s");
	speedUnitChooser.add(bits);
	speedUnitChooser.add(bytes);
	speedUnitChooser.add(bitsAndBytes);

	bits.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(bits.isSelected()) {
			ConnectionMonitorPanel.displayBitRate = true;
			ConnectionMonitorPanel.displayByteRate = false;
		    }
		}
	    });
	bytes.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(bytes.isSelected()) {
			ConnectionMonitorPanel.displayBitRate = false;
			ConnectionMonitorPanel.displayByteRate = true;
		    }
		}
	    });
	bitsAndBytes.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(bitsAndBytes.isSelected()) {
			ConnectionMonitorPanel.displayBitRate = true;
			ConnectionMonitorPanel.displayByteRate = true;
		    }
		}
	    });

	// Default to display byterate
	bytes.setSelected(true);
	ConnectionMonitorPanel.displayBitRate = false;
	ConnectionMonitorPanel.displayByteRate = true;	
	
	ButtonGroup bg = new ButtonGroup();
	bg.add(bits);
	bg.add(bytes);
	bg.add(bitsAndBytes);

	listenItem = new JCheckBoxMenuItem("Listen for incoming connections");
	listenItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.setListeningForConnections(listenItem.isSelected());
		}
	    });
	listenItem.setSelected(true);
	listenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

	JMenuItem setPortItem = new JMenuItem("Override incoming port number");
	setPortItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ChangePortDialog cpd = new ChangePortDialog(MainWindow.this, fileTransfer);
		    cpd.setVisible(true);
		}
	    });
	
	final JMenuItem oldSendFilesItem = new JCheckBoxMenuItem("Use old \"Send files...\" dialog");
	oldSendFilesItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    oldSendFiles = oldSendFilesItem.isSelected();
		}
	    });
	oldSendFilesItem.setSelected(false);

	viewMenu.add(speedUnitChooser);
	viewMenu.add(listenItem);
	viewMenu.add(setPortItem);
	viewMenu.add(oldSendFilesItem);

	helpMenu = new JMenu("Help");
	JMenuItem aboutItem = new JMenuItem("About...");
	aboutItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fileTransfer.aboutSignaled();
		}
	    });

	helpMenu.add(aboutItem);
	
	menuBar.add(fileMenu);
	menuBar.add(connectionMenu);
	menuBar.add(viewMenu);
	menuBar.add(helpMenu);

	setJMenuBar(menuBar);
    }
    
    public void setListenStatus(boolean status) {
	listenItem.setSelected(status);
    }
    
    public synchronized void addConnectionTab(String address) {
	//System.out.println("Entered addConnectionTab(String)");
	ConnectionMonitorPanel cmp;
	if(noTabs) {
	    noTabs = false;
	    cmp = new ConnectionMonitorPanel(address, this, 0);
	    connectionMonitors.setComponentAt(0, cmp);
	    connectionMonitors.setTitleAt(0, "[C] " + address);
	}
	else {
	    int thisIndex = connectionMonitors.getTabCount();
	    cmp = new ConnectionMonitorPanel(address, this, thisIndex);
	    connectionMonitors.addTab("[C] " + address, cmp);
	}
	//System.out.println("intializing..");
	cmp.initialize();
	//System.out.println("Exited addConnectionTab(String)");
    }
    public synchronized void addConnectionTab(Socket s) throws IOException {
	ConnectionMonitorPanel cmp;
	if(noTabs) {
	    noTabs = false;
	    cmp = new ConnectionMonitorPanel(s, this, 0);
	    connectionMonitors.setComponentAt(0, cmp);
	    connectionMonitors.setTitleAt(0, "[S] " + s.getInetAddress().getHostName());
	}
	else {
	    cmp = new ConnectionMonitorPanel(s, this, connectionMonitors.getTabCount());
	    connectionMonitors.addTab("[S] " + s.getInetAddress().getHostName(), cmp);
	}
    }
    
    /**
     * @return the active ConnectionMonitorPanel, if any. null if the active tab was not instanceof
     *         ConnectionMonitorPanel (which should mean that no active connections were found).
     */
    public synchronized ConnectionMonitorPanel getActiveTab() {
	Component c = connectionMonitors.getSelectedComponent();
	if(c instanceof ConnectionMonitorPanel)
	    return (ConnectionMonitorPanel)c;
	else
	    return null;
    }
    public synchronized void setActiveTab(ConnectionMonitorPanel cmp) {
	connectionMonitors.setSelectedComponent(cmp);
    }
    public synchronized void setTitle(ConnectionMonitorPanel cmp, String title) {
	connectionMonitors.setTitleAt(connectionMonitors.indexOfComponent(cmp), title);
    }
    public synchronized void removeTab(ConnectionMonitorPanel cmp) {
	cmp.removed();
	if(connectionMonitors.getTabCount() == 1) {
	    connectionMonitors.setComponentAt(0, new JLabel("No connections open", SwingConstants.CENTER));
	    connectionMonitors.setTitleAt(0, "No connections open");
	    noTabs = true;
	}
	else {
	    connectionMonitors.remove(cmp);
	    // Obsolete
	    for(int i = 0; i < connectionMonitors.getTabCount(); ++i) {
		Component c = connectionMonitors.getComponent(i);
		if(c instanceof ConnectionMonitorPanel)
		    ((ConnectionMonitorPanel)c).setIndexInParent(i);
	    }
	}
    }

    public void setStatus(String s) {
	statusLabel.setText(s);
    }
}
