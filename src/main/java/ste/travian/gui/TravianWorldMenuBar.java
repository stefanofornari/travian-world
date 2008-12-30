/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author ste
 */
public class TravianWorldMenuBar extends JMenuBar {
    
    private JMenu file;
    private JMenu world;
    private JMenu help;
    
    private WorldController controller;
    
    public TravianWorldMenuBar(WorldController controller) {
        super();
        
        this.controller = controller;
        
        JMenuItem m = null;
        
        file = new JMenu("File");
        file.add(m = new JMenuItem("Update map"));
        m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.META_MASK));

        world = new JMenu("World");
        world.add(m = new JMenuItem("Group alliances"));
        m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.META_MASK));
        m.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TravianWorldMenuBar.this.controller.showAllianceGroupsDialog();
            }
        });
        
        add(file);
        add(world);
    }
    
    

}
