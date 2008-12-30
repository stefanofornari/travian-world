/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.world;

import ste.travian.world.Tile;
import ste.travian.world.World;
import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class WorldTest extends TestCase {
    
    public static final Tile[] TILES = new Tile[] {
        new Tile(1, 1, 1, 1, 1, "v01", 1, "u01", 1, "a01", 10),
        new Tile(2, -1, -1, 2, 2, "v02", 1, "u01", 1, "a01", 20),
        new Tile(3, -1, 1, 3, 3, "v03", 2, "u02", 2, "a02", 30),
        new Tile(4, 1, -1, 4, 4, "v04", 3, "u01", 2, "a02", 40)
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

}
