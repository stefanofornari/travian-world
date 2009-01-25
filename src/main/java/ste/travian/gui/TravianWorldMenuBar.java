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
    
    private JMenu world;
    
    private WorldController controller;
    
    public TravianWorldMenuBar(WorldController controller) {
        super();
        
        this.controller = controller;
        
        JMenuItem m = null;
        
        world = new JMenu("World");
        world.add(m = new JMenuItem("Update map"));
        m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.META_MASK));
        m.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TravianWorldMenuBar.this.controller.updateWorld();
            }
        });
        world.add(m = new JMenuItem("Group alliances"));
        m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.META_MASK));
        m.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TravianWorldMenuBar.this.controller.showAllianceGroupsDialog();
            }
        });
        add(world);
    }
    
    

}
