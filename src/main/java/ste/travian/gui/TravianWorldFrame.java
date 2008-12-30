/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    
    /** Creates new form TravianMapFrame2 */
    public TravianWorldFrame() {
        c = new WorldController(this);
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
        
        //
        // Load the existing map
        //
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    try {
                        c.load();
                        showMap(c.getWorldPanel());
                    } catch (Exception e) {
                        error("Error creating the map", e);
                    }
                }
            }
        );
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

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        //
        // Tweaks for MacOS UI
        //
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "About Travian World");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
        // ---
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TravianWorldFrame().setVisible(true);
            }
        });
    }    
}

