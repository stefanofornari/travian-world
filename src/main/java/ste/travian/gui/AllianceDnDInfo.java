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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.JComponent;

public class AllianceDnDInfo implements Transferable, Serializable {

    final public static DataFlavor INFO_FLAVOR =
            new DataFlavor(AllianceDnDInfo.class, "Alliance");
    static DataFlavor flavors[] = {INFO_FLAVOR};
    private String name;

    public AllianceDnDInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object clone() {
        return new AllianceDnDInfo(name);
    }

    /**
     *
     * @return a string representation of this object
     *
     * @Override
     */
    public String toString() {
        return name;
    }

    // -------------------------------------------------------------- Transferable
    public boolean isDataFlavorSupported(DataFlavor df) {
        return df.equals(INFO_FLAVOR);
    }

    /** implements Transferable interface */
    public Object getTransferData(DataFlavor df)
            throws UnsupportedFlavorException, IOException {
        if (df.equals(INFO_FLAVOR)) {
            return this;
        } else {
            throw new UnsupportedFlavorException(df);
        }
    }

    /** implements Transferable interface */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    // -------------------------------------------------------------- Serializable
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
