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

import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import ste.travian.TravianException;
import ste.travian.store.WorldStore;

/**
 *
 * @author ste
 */
public class AllianceGroupsController {
    
    private AllianceGroupsDialog mainWindow;

    public void showMainWindow(TravianWorldFrame frame) {
        mainWindow = new AllianceGroupsDialog(frame, this);

        populateAllianceGroupsTree();

        mainWindow.setVisible(true);
    }

    public void populateAllianceGroupsTree() {
        WorldStore store = new WorldStore();
        try {
            store.initialize();
            Map<String, ArrayList<String>> groups = store.readAllianceGroups();
            mainWindow.initializeAllianceGroups(groups);
        } catch (Exception e) {
            ((TravianWorldFrame)mainWindow.getParent()).error("Error accessing the store", e);
        }
    }

    public void updateAllianceGroups(Map<String,ArrayList<String>> groups) {
        WorldStore store = new WorldStore();
        try {
            store.initialize();
            store.updateAllianceGroups(groups);
        } catch (Exception e) {
            ((TravianWorldFrame)mainWindow.getParent()).error("Error accessing the store", e);
        }
    }

}
