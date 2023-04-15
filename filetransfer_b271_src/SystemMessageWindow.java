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

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class SystemMessageWindow extends JFrame {
    public JTextArea messageArea;
    public JScrollPane messageScroller;

    public SystemMessageWindow() {
	super("System messages");
	messageArea = new JTextArea(25, 80);
	messageArea.setEditable(false);
	messageArea.setLineWrap(true);
	
	messageScroller = new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	add(messageScroller, BorderLayout.CENTER);
	
	pack();
	setLocationRelativeTo(null);
    }

    public void printMessageLine(String s) {
	printMessage(s + System.getProperty("line.separator"));
    }
    public synchronized void printMessage(final String s) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    messageArea.append(s);
		    messageArea.invalidate();
		    messageArea.validate();
		    messageScroller.invalidate();
		    messageScroller.validate();
		    messageScroller.getVerticalScrollBar().setValue(messageScroller.getVerticalScrollBar().getMaximum());
		}
	    });
    }   
    
    public PrintStream getErrorStream() {
	return new PrintStream(new DebugStream(), true);
    }
    public PrintStream getOutputStream() {
	return new PrintStream(new DebugStream(), true);
    }
    
    private class DebugStream extends OutputStream {
// 	StringBuffer buffer;
// 	JTextArea textArea;
// 	JRadioButton associatedButton;
	
// 	public DebugStream(StringBuffer buffer, JTextArea textArea, JRadioButton associatedButton) {
// 	    this.buffer = buffer;
// 	    this.textArea = textArea;
// 	    this.associatedButton = associatedButton;
// 	}
	public void write(byte[] b) throws IOException {
	    String s = new String(b);
	    printMessage(s);
// 	    buffer.append(s);
// 	    if(associatedButton.isSelected())
// 		textArea.append(s+"");
	}
	public void write(byte[] b, int off, int len) throws IOException {
	    byte[] c = new byte[len-off];
	    System.arraycopy(b, off, c, 0, len);
// 	    for(int i = off; i < len; ++i)
// 		c[i-off] = b[i];
	    String s = new String(c);
	    printMessage(s);
// 	    buffer.append(s);
// 	    if(associatedButton.isSelected())
// 		textArea.append(s+"");
	}
	public void write(int b) throws IOException {
	    char c = (char)b;
	    String s = "" + c;
	    printMessage(s);	    
// 	    buffer.append(c);
// 	    if(associatedButton.isSelected())
// 		textArea.append(c+"");
	}
    }    
}
