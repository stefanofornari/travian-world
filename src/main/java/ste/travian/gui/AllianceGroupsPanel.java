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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class AllianceGroupsPanel extends JPanel {

    private AllianceGroupsTree tree;
    private JList restOfTheWorldList;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public AllianceGroupsPanel() {
        super(new BorderLayout());

        tree = new AllianceGroupsTree();
        add(new JScrollPane(tree), BorderLayout.LINE_START);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton button = new JButton("+");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addGroup();
            }
        });
        panel.add(button);
        panel.add(new JButton("-"));
        panel.setPreferredSize(new Dimension(40, 100));
        add(panel, BorderLayout.CENTER);

        restOfTheWorldList = new AllianceList();
        JScrollPane pane = new JScrollPane(restOfTheWorldList);
        pane.setPreferredSize(new Dimension(200, 300));
        add(pane, BorderLayout.LINE_END);
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        TreeModel model = tree.getModel();
        ((DefaultMutableTreeNode)model.getRoot()).removeAllChildren();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                ((DefaultTreeModel)tree.getModel()).removeNodeFromParent(currentNode);
                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    public void addGroup() {
        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New group");
        newNode.setAllowsChildren(true);

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        DefaultMutableTreeNode rootNode =
            (DefaultMutableTreeNode)model.getRoot();

        int nChildren = rootNode.getChildCount(), i = 0;
        for (; i<nChildren; ++i) {
            node = (DefaultMutableTreeNode)rootNode.getChildAt(i);
            if (String.valueOf(node.getUserObject()).compareTo(String.valueOf(newNode.getUserObject())) > 0) {
                break;
            }
        }
        model.insertNodeInto(newNode, rootNode, i);
        tree.scrollPathToVisible(new TreePath(newNode.getPath()));

    }

    public AllianceGroupsTree getTree() {
        return tree;
    }

    public JList getRestOfTheWorldList() {
        return restOfTheWorldList;
    }
}
