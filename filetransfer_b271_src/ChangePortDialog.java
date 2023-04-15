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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.DefaultStyledDocument; 
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;

public class ChangePortDialog extends JDialog {
    private JPanel backgroundPanel;
    private ButtonGroup buttonGroupOne;
    private JRadioButton defaultPortButton;
    private JRadioButton customPortButton;
    private JTextField customPortField;
    private JButton okButton;
    private JButton cancelButton;

    public ChangePortDialog(Frame owner, final FileTransfer ft) {
	super(owner, "Change port number", true);
	backgroundPanel = new JPanel();
	buttonGroupOne = new ButtonGroup();
	defaultPortButton = new JRadioButton("Default port (" + ConnectionListener.DEFAULT_PORT_NUMBER + ")");
	customPortButton = new JRadioButton("Custom port:");
	customPortField = new JTextField(new DefaultStyledDocument() {
		private int minValue = 0;
		private int maxValue = 65535;
		public void insertString(int offs, String str, AttributeSet a)
		    throws BadLocationException {
		    try {
			if(!str.equals("")) {
			    int currentValue = Integer.parseInt(super.getText(0, super.getLength()) + str);
			    if(currentValue <= maxValue && currentValue >= minValue)
				super.insertString(offs, str, a);
			    else
				Toolkit.getDefaultToolkit().beep();
			}
		    }
		    catch(Exception e) {
			Toolkit.getDefaultToolkit().beep();
		    }
		}
	    }, "", 5);
	okButton = new JButton("Ok");
	cancelButton = new JButton("Cancel");

	okButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int port;
		    if(defaultPortButton.isSelected())
			port = ConnectionListener.DEFAULT_PORT_NUMBER;
		    else if(customPortButton.isSelected()) {
			String newPort = customPortField.getText();
			if(newPort.length() < 1) {
			    JOptionPane.showMessageDialog(ChangePortDialog.this, "Custom port field empty!", "Error", JOptionPane.ERROR_MESSAGE);
			    return;
			}
			else
			    port = Integer.parseInt(customPortField.getText());
		    }
		    else
			throw new RuntimeException("No radio button selected. (Strange)");
		    ft.setListeningForConnections(false);
		    ft.setListenPort(port);
		    ft.setListeningForConnections(true);
		    setVisible(false);
		}
	    });
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    setVisible(false);
		}
	    });

	buttonGroupOne.add(defaultPortButton);
	buttonGroupOne.add(customPortButton);

	int currentPort = ft.getListenPort();
	if(currentPort == ConnectionListener.DEFAULT_PORT_NUMBER) {
	    defaultPortButton.setSelected(true);
	    defaultPortButton.requestFocusInWindow();
	}
	else {
	    customPortField.setText("" + currentPort);
	    customPortButton.setSelected(true);
	    customPortButton.requestFocusInWindow();
	}

	backgroundPanel.setLayout(new GridBagLayout());
	backgroundPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.VERTICAL;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 0;
	gbc.gridy = 0;
	backgroundPanel.add(defaultPortButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 1;
	backgroundPanel.add(customPortButton, gbc);
	gbc.gridx = 1;
	gbc.gridy = 1;
	backgroundPanel.add(customPortField, gbc);
	gbc.gridx = 0;
	gbc.gridy = 2;
	backgroundPanel.add(okButton, gbc);
	gbc.gridx = 1;
	gbc.gridy = 2;
	backgroundPanel.add(cancelButton, gbc);

	getContentPane().add(backgroundPanel, BorderLayout.CENTER);

	pack();
	setResizable(false);
	setLocationRelativeTo(null);
	
    }
}
