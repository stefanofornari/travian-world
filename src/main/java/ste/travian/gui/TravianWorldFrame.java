/*
 * Copyright (C) 2008
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * "Derived from Travian world" logo. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Derived from Travian world".
 */

package ste.travian.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import ste.travian.TravianException;

/**
 *
 * @author ste
 */
public class TravianWorldFrame extends JFrame {
    
    private JButton mapLoadButton;
    private JPanel urlPanel;
    private JLabel urlLabel;
    private JTextField urlMapText;
    private JTextField statusText;
    
    private WorldController c;

    
    /** Creates new form TravianWorldFrame */
    public TravianWorldFrame(WorldController controller) {
        c = controller;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        
        urlLabel = new JLabel();
        urlMapText = new JTextField();
        mapLoadButton = new JButton();
        urlPanel = new JPanel();
        statusText = new JTextField();

        setLayout(new BorderLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Travian Map");

        urlPanel.setLayout(new FlowLayout());
        urlLabel.setText("url:");
        urlPanel.add(urlLabel);

        urlMapText.setText("http://s4.travian.com/map.sql.gz");
        urlMapText.setPreferredSize(new Dimension(300, 22));
        urlPanel.add(urlMapText);

        mapLoadButton.setText("Load");
        mapLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapLoadButtonActionPerformed(evt);
            }
        });
        urlPanel.add(mapLoadButton);
        getContentPane().add(urlPanel, BorderLayout.PAGE_START);
        getContentPane().add(statusText, BorderLayout.PAGE_END);
        
        setJMenuBar(new TravianWorldMenuBar(c));
        
        pack();
    }


    private void mapLoadButtonActionPerformed(ActionEvent evt) {
        //
        // If no URL is provide do nothing...
        //
        String urlText = urlMapText.getText();
        if (urlText.length() == 0) {
            return;
        }
        
        try {
            c.load(new URL(urlText));
        } catch (MalformedURLException e) {
            error("Invalid map URL: " + urlText, e);
            return;
        } catch (TravianException e) {
            error("Error loading the map from " + urlText, e);
            return;
        }
        
        showMap(c.getWorldPanel());
    }
    
    public void showMap(JPanel worldPanel) {
        getContentPane().add(worldPanel, BorderLayout.CENTER);
        pack();
    }
    
    public void statusMessage(String msg) {
        statusText.setText(msg);
    }
    
    public void error(final String msg, final Throwable t) {
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    Object[] message = new Object[1];
                    String string;

                    if (msg != null) {
                        string = msg + "\n" + t.getMessage();
                    } else {
                        string = t.getMessage();
                    }

                    Object[] options = { "Dismiss", "Stack trace" };

                    // Show the MODAL dialog
                    int selected = JOptionPane.showOptionDialog(
                                       TravianWorldFrame.this,
                                       new String[] {string}, 
                                       "Error",
                                       JOptionPane.YES_NO_OPTION, 
                                       JOptionPane.ERROR_MESSAGE,
                                       null, 
                                       options, 
                                       options[0]
                    
                    );

                    if (selected == 1) {
                        showStackTrace(msg, t);
                    }
                }
            }
        
        );
    }

    public WorldController getController() {
        return c;
    }

    // ---------------------------------------------------------- Private mthods

    private void showStackTrace(final String msg, final Throwable t) {
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);

                    JTextArea text = new JTextArea(sw.toString(), 60, 80);
                    JScrollPane stext = new JScrollPane(text);
                    stext.setPreferredSize(new Dimension(600, 300));
                    text.setCaretPosition(0);
                    text.setEditable(false);

                    Object[] message = new Object[2];
                    String string;

                    if (msg != null) {
                        string = msg + "\n" + t.getMessage();
                    } else {
                        string = t.getMessage();
                    }

                    message[0] = string;
                    message[1] = stext;

                    // Show the MODAL dialog
                    JOptionPane.showMessageDialog(
                        TravianWorldFrame.this, 
                        message, 
                        "Stack trace",
                        JOptionPane.ERROR_MESSAGE
                    
                    );
                }
            }
        
        );
    }
}

