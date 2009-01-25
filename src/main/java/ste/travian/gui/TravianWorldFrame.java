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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDialog;
import ste.travian.TravianException;

/**
 *
 * @author ste
 */
public class TravianWorldFrame extends JFrame {

    public static final String PROPERTY_COLLAPTION_STATE = "collapsed";
    
    private JLabel statusText;
    
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
        statusText = new JLabel();

        setLayout(new BorderLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Travian Map");

        getContentPane().add(statusText, BorderLayout.PAGE_END);
        
        setJMenuBar(new TravianWorldMenuBar(c));
        
        pack();
    }

    public void showMap(final JPanel worldPanel) {
        getContentPane().add(worldPanel, BorderLayout.CENTER);
        pack();
    }
    
    public void statusMessage(String msg) {
        statusText.setText(msg);
    }

    public WorldController getController() {
        return c;
    }

    public void error(final String msg, final Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);

        JLabel icon = new JLabel();
        icon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));

        JLabel message = new JLabel();
        if (msg != null) {
            message.setText(msg + " ");
        } else {
            message.setText(t.getMessage() + " ");
        }

        JTextArea text = new JTextArea(sw.toString(), 60, 80);
        text.setCaretPosition(0);
        text.setEditable(false);
        JScrollPane stext = new JScrollPane(text);
        stext.setPreferredSize(new Dimension(500, 200));

        Box content = Box.createHorizontalBox();
        JXDialog dialog = new JXDialog(content);

        JXCollapsiblePane cp = new JXCollapsiblePane(new BorderLayout());
        cp.setAnimated(false);
        cp.addPropertyChangeListener(new CollapseListener(dialog));
        cp.add(stext, BorderLayout.CENTER);

        // get the built-in toggle action
        Action toggleAction = cp.getActionMap().
            get(JXCollapsiblePane.TOGGLE_ACTION);

        // use the collapse/expand icons from the JTree UI
        toggleAction.putValue(
            JXCollapsiblePane.COLLAPSE_ICON,
            UIManager.getIcon("Tree.expandedIcon")
        );
        toggleAction.putValue(
           JXCollapsiblePane.EXPAND_ICON,
           UIManager.getIcon("Tree.collapsedIcon")
        );

        cp.setCollapsed(true);

        JButton toggle = new JButton (toggleAction);
        toggle.setText("");
        toggle.setSize(new Dimension(40,40));

        Box messagePanel = Box.createHorizontalBox();
        messagePanel.add(message);
        messagePanel.add(toggle);

        JPanel exceptionPanel = new JPanel(new BorderLayout());
        exceptionPanel.add(messagePanel, BorderLayout.PAGE_START);
        exceptionPanel.add(cp, BorderLayout.PAGE_END);

        icon.setAlignmentY(TOP_ALIGNMENT);
        exceptionPanel.setAlignmentY(TOP_ALIGNMENT);
        content.add(icon);
        content.add(Box.createRigidArea(new Dimension(5, 5)));
        content.add(exceptionPanel);


        // Show the MODAL dialog

        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(TravianWorldFrame.this);
    }

    private class CollapseListener 
        implements PropertyChangeListener {
        private JDialog dialog;

        public CollapseListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (PROPERTY_COLLAPTION_STATE.equals(e.getPropertyName())) {
                dialog.pack();
            }
        }

    }

}

