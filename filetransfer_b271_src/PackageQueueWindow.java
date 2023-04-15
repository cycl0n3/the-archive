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

import java.util.*;
import java.net.InetAddress;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class PackageQueueWindow extends JFrame {
    private SessionMonitor sm;
    private PackageQueuePanel pqp;
    private JButton refreshButton;
    private JLabel currentlyProcessingLabel;
    
    public PackageQueueWindow(SessionMonitor sm, boolean server) {
	this.sm = sm;
	InetAddress address = sm.getAddress();
	String hostName = address.getHostName();
	String hostAddress = address.getHostAddress();
	setTitle("Outbound packages for " + (server?"[S] ":"[C] ") + (hostName.trim().equals("")?"":hostName + " / ") + hostAddress);

	currentlyProcessingLabel = new JLabel("No transfer in progress...", SwingConstants.CENTER);
	pqp = new PackageQueuePanel();
	refreshButton = new JButton("Refresh");
	refreshButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    refresh();
		}
	    });
	JPanel refreshButtonPanel = new JPanel();
	refreshButtonPanel.add(refreshButton);

	add(currentlyProcessingLabel, BorderLayout.NORTH);
	add(pqp, BorderLayout.CENTER);
	//add(refreshButtonPanel, BorderLayout.SOUTH);
	pack();
	setLocationRelativeTo(null);
					      
    }
    public void setVisible(boolean b) {
	super.setVisible(b);
	refresh();
    }
    public void refresh() {
	if(isVisible()) {
	    pqp.refreshList();
	    refreshCurrentlyProcessing();
	}
    }
    public void refreshCurrentlyProcessing() {
	SessionHandler.Package p = sm.getSessionHandler().getCurrentlyProcessing();
	if(p != null) {
	    String text = "Currently processing package " + p.requestID + " of type " + p.identifier + 
		". Size: " + SpeedUnitUtils.bytesToBinaryUnit(p.packageSize) + " Progress: " + SpeedUnitUtils.bytesToBinaryUnit(p.bytesSent) + " (" + (100*p.bytesSent/p.packageSize) + "%)";
	    if(!text.equals(currentlyProcessingLabel.getText()))
		currentlyProcessingLabel.setText(text);
	}
	else {
	    final String noTransfer = "No transfer in progress...";
	    if(!noTransfer.equals(currentlyProcessingLabel.getText()))
		currentlyProcessingLabel.setText(noTransfer);
	}
    }


    private class PackageQueuePanel extends JPanel {
	private final JTable userTable;
	private final DefaultTableModel tableModel;
	private final JScrollPane scrollPane;
	private final Vector<String> columnNames = new Vector<String>();

	public PackageQueuePanel() {
	    columnNames.add("Package ID");
	    columnNames.add("Type");
	    columnNames.add("Size");

	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	    tableModel = new DefaultTableModel(10, 3) {
		    public boolean isCellEditable(int row, int col) {
			return false;
			//return col > 0;
		    }
		};
	    userTable = new JTable(tableModel);
	    userTable.setCellSelectionEnabled(false);
	    userTable.setColumnSelectionAllowed(false);
	    userTable.setRowSelectionAllowed(true);
	
	    scrollPane = new JScrollPane(userTable);

	    // Snabbt litet hack nedan. Fixa höjden till 200.
	    //userTable.setPreferredScrollableViewportSize(new Dimension(userTable.getPreferredScrollableViewportSize().width, 200));

	    add(scrollPane, BorderLayout.NORTH);

	    scrollPane.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent evt) {
			userTable.clearSelection();
		    }
		});
	
	    refreshList();
	}
    
	public void refreshList() {
	    SessionHandler.Package[] packages = sm.getSessionHandler().getQueuedPackages();
	    Vector<Vector<String>> rowData = new Vector<Vector<String>>();
	    for(SessionHandler.Package pkg : packages) {
		Vector<String> newItems = new Vector<String>();
		newItems.add(pkg.requestID + "");
		newItems.add(pkg.identifier);
		newItems.add(pkg.packageSize + "");
		rowData.add(newItems);
	    }
	    boolean vectorChanged = false;
	    Vector oldDataVector = tableModel.getDataVector();
	    if(oldDataVector.size() != rowData.size())
		vectorChanged = true;
	    else {
		for(int i = 0; i < oldDataVector.size(); ++i) {
		    Object o = oldDataVector.get(i);
		    if(o instanceof Vector) {
			Vector v1 = (Vector) o;
			Vector<String> v2 = rowData.get(i);
			if(v1.size() != v2.size()) {
			    vectorChanged = true;
			    break;
			}
			for(int j = 0; j < v1.size(); ++j) {
			    Object o2 = v1.get(j);
			    if(o2 instanceof String) {
				String s = (String) o2;
				if(!s.equals(v2.get(j))) {
				    vectorChanged = true;
				    break;
				}
			    }
			}
		    }
		}
	    }
	    if(vectorChanged) {
		//System.err.println("Change in data vector detected!");
		tableModel.setDataVector(rowData, columnNames);
	    }
	}
    }
}
