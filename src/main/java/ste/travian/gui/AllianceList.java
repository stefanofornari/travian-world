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

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.decorator.SortOrder;

/**
 * This is a JList that just defines the methods required to support DnD with
 * the alliance groups tree.
 *
 * @author ste
 */
public class AllianceList
        extends org.jdesktop.swingx.JXList
        implements DragGestureListener, DropTargetListener, DragSourceListener {

    DragSource ds;

    /**
     * Creates an AllianceList.
     */
    public AllianceList() {
        super(new DefaultListModel());

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(-1);
        setFixedCellWidth(90);
        
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

        DropTarget dropTarget = new DropTarget(this, this);
    }

    /**
     * Returns the selected alliance or null if no alliance is selected.
     * This is intended to be used also for DnD operations by the TransferHandler.
     *
     * @return the seleceted alliance or null
     */
    public AllianceDnDInfo getSelectedAlliance() {
        return (AllianceDnDInfo) getSelectedValue();
    }

    /**
     * Adds an alliance to the list respecting the alphabetical order.
     *
     * @param info the alliance to add in the form of a DnDable object
     *
     * @return the position the item has been inserted
     */
    public int addAlliance(AllianceDnDInfo info) {

        DefaultListModel model = (DefaultListModel) getModel();

        int i = 0;
        String alliance = null;
        for (; i<model.size(); ++i) {
            alliance = String.valueOf(model.elementAt(i));
            if (alliance.compareTo(info.getName())>0) {
                break;
            }
        }
        model.add(i, info);

        return i;
    }

    // ----------------------------------------------------- DragGestureListener
    public void dragGestureRecognized(DragGestureEvent e) {
        System.out.println("Drag Gesture Recognized!");
        Transferable t = getSelectedAlliance();
        ds.startDrag(e, DragSource.DefaultMoveDrop, t, this);
    }

    // ------------------------------------------------------ DragSourceListener

    public void dragEnter(DragSourceDragEvent e) {
        System.out.println("Drag Enter");
    }

    public void dragExit(DragSourceEvent e) {
        System.out.println("Drag Exit");
    }

    public void dragOver(DragSourceDragEvent e) {
        System.out.println("Drag Over");
    }

    public void dragDropEnd(DragSourceDropEvent e) {
        if (e.getDropSuccess()) {
            ((DefaultListModel)getModel()).removeElementAt(getSelectedIndex());
        }
    }

    public void dropActionChanged(DragSourceDragEvent e) {
        System.out.println("Drop Action Changed");
    }
    
    // ------------------------------------------------------ DropTargetListener

    /**
     * DropTargetListener interface method - What we do when drag is released
     */
    public void drop(DropTargetDropEvent e) {
        try {
            Transferable tr = e.getTransferable();

            //flavor not supported, reject drop
            if (!tr.isDataFlavorSupported(AllianceDnDInfo.INFO_FLAVOR)) {
                e.rejectDrop();
                return;
            }

            int pos = addAlliance(
                (AllianceDnDInfo)tr.getTransferData(AllianceDnDInfo.INFO_FLAVOR)
            );

            setSelectedIndex(pos);
            ensureIndexIsVisible(pos);

            e.getDropTargetContext().dropComplete(true);
        } catch (Exception ex) {
            e.rejectDrop();
        }
    }

    /** DropTargetListener interface method */
    public void dragEnter(DropTargetDragEvent e) {
    }

    /** DropTargetListener interface method */
    public void dragExit(DropTargetEvent e) {
    }

    /** DropTargetListener interface method */
    public void dragOver(DropTargetDragEvent e) {
        e.acceptDrag(DnDConstants.ACTION_MOVE);

    }

    /** DropTargetListener interface method */
    public void dropActionChanged(DropTargetDragEvent e) {
    }
}
