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

import java.awt.Container;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.swingx.JXDialog;

/**
 * Extends a JXDialog with a controller object
 * 
 * @author ste
 */
public class AllianceGroupsDialog extends JXDialog {

    private AllianceGroupsController controller;

    /**
     * Creates the dialog to organize alliances in groups.
     *
     * @param frame the owner of this dialog
     * @param c the controller for this dialog
     * 
     */
    public AllianceGroupsDialog(JFrame frame, AllianceGroupsController c) {
        super(frame, new AllianceGroupsPanel());
        
        if (c == null) {
            throw new IllegalArgumentException("c cannot be null");
        }
        controller = c;

        setModal(true);
        setTitle("Alliance groups selection");

        pack();
    }

    /**
     *
     * @return the controller associated to this dialog
     */
    public AllianceGroupsController getController() {
        return controller;
    }

    /**
     * Returns the controller of the dialog containing the given JComponent
     *
     * @param c the component of which we want the dialog controller
     *
     * @return the dialog controller; if the component is not in a AllianceGroupsDialog
     *         container, am IllegalArgumentExceptoion is thrown
     *
     * @throws IllegalArgumentException if c is not in a AllianceGroupsDialog
     */
    public static AllianceGroupsController getController(JComponent c) {
        Container container = c.getRootPane().getParent();

        if (container instanceof AllianceGroupsDialog) {
            return ((AllianceGroupsDialog)container).getController();
        }

        throw new IllegalArgumentException("The given component is not in a AllianceGroupsDialog");
    }

    /**
     * Initialize the alliance groups tree and the alliance lists with the given
     * content.
     *
     * @param groups the allaince groups
     */
    public void initializeAllianceGroups(Map<String, ArrayList<String>> groups) {
        AllianceGroupsPanel allianceGroupsPanel = (AllianceGroupsPanel)content;
        AllianceGroupsTree tree = allianceGroupsPanel.getTree();
        JList restOfTheWorldList = allianceGroupsPanel.getRestOfTheWorldList();

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Groups");
        DefaultMutableTreeNode groupNode = null;
        DefaultMutableTreeNode allianceNode = null;
        AllianceDnDInfo info = null;
        for(String group: groups.keySet()) {
            if (!AllianceGroupsPanel.REST_OF_THE_WORLD_GROUP.equals(group)) {
                groupNode = new DefaultMutableTreeNode(group);
                groupNode.setAllowsChildren(true);
                root.add(groupNode);
            }
            for (String alliance: groups.get(group)) {
                info = new AllianceDnDInfo(alliance);
                if (AllianceGroupsPanel.REST_OF_THE_WORLD_GROUP.equals(group)) {
                    ((DefaultListModel)restOfTheWorldList.getModel()).addElement(info);
                } else {
                    allianceNode = new DefaultMutableTreeNode(alliance);
                    allianceNode.setAllowsChildren(false);
                    allianceNode.setUserObject(info);
                    groupNode.add(allianceNode);
                }
            }
        }
        model.setRoot(root);
    }
}
