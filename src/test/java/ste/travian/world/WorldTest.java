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
package ste.travian.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ste.travian.world.Tile;
import ste.travian.world.World;
import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class WorldTest extends TestCase {
    
    public static final Tile[] TILES = new Tile[] {
        new Tile(1,  1,  1, 1, 1, "v01", 1, "u01", 1, "a01", 10),
        new Tile(2, -1, -1, 2, 2, "v02", 1, "u01", 1, "a01", 20),
        new Tile(3, -1,  1, 3, 3, "v03", 2, "u02", 2, "a02", 30),
        new Tile(4,  1, -1, 4, 4, "v04", 3, "u01", 2, "a02", 40)
    };
    
    public WorldTest(String testName) {
        super(testName);
    }

    public void testAddGetTile() {
        Tile tile = TileTest.getDefaultTile();
        World w = new World();
        w.addTile(tile);
        
        Tile tile2 = w.getTile(tile.getX(), tile.getY());
        
        assertEquals(tile.getId(), tile2.getId());
        
        w.addTile(null); // it must work with no errors
    }
    
    public void testWorldExtension() {
        World w = new World();
        
        w.addTile(new Tile(1, -10, 15));
        w.addTile(new Tile(2, 0, 20));
        w.addTile(new Tile(3, 5, 0));
        w.addTile(new Tile(4, 15, 15));
        
        assertEquals(15, w.getMaxX());
        assertEquals(-10, w.getMinX());
        assertEquals(20, w.getMaxY());
        assertEquals(0, w.getMinY());
    }
    
    public void testIsEmpty() {
        World w = new World();
        
        assertTrue(w.isEmpty());
        
        w.addTile(new Tile(1, 0, 0));
        
        assertFalse(w.isEmpty());
    }
    
    public void testGetAlliances() {
        World w = new World();
        
        for (Tile t: TILES) {
            w.addTile(t);
        }
        
        String[] alliances = w.getAlliances();
        
        assertEquals(2, alliances.length);
        assertEquals(TILES[0].getAlliance(), alliances[0]);
        assertEquals(TILES[3].getAlliance(), alliances[1]);
    }

    public void testSetGetAllianceGroups() {
        World w = new World();

        w.setAllianceGroups(null);
        assertNull(w.getAllianceGroups());

        Map<String,ArrayList<String>> GROUPS = new HashMap<String,ArrayList<String>>();
        ArrayList<String> alliances = null;

        alliances = new ArrayList<String>();
        alliances.add("**B-A**");
        alliances.add("T[SD]");

        GROUPS.put("group1", alliances);

        alliances = new ArrayList<String>();
        alliances.add("+SPQRIII");
        alliances.add("Berserk+");

        GROUPS.put("group2", alliances);

        alliances = new ArrayList<String>();
        alliances.add("~COLONY~");

        GROUPS.put("group3", alliances);

        w.setAllianceGroups(GROUPS);

        Map<String,ArrayList<String>> groups = w.getAllianceGroups();

        for (String groupName: GROUPS.keySet()) {
            assertNotNull(alliances = groups.get(groupName));
            int i = 0;
            for (String alliance: GROUPS.get(groupName)) {
                assertEquals(alliance, alliances.get(i++));
            }
        }
    }

}
