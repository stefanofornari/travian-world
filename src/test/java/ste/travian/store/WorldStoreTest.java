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
package ste.travian.store;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import junit.framework.TestCase;
import ste.travian.world.MapDownloader;
import ste.travian.world.MapDownloaderTest;
import ste.travian.world.Tile;
import ste.travian.world.World;

/**
 *
 * @author ste
 */
public class WorldStoreTest extends TestCase {
    
    public static final String SQL_COUNT_TILES = 
        "select count(*) from " + WorldStore.WORLD_TABLE_NAME;
    public static final String SQL_GET_TILE = 
        "select * from " + WorldStore.WORLD_TABLE_NAME + " where id=801";
    public static final String SQL_DROP_WORLD_TABLE = 
        "drop table " + WorldStore.WORLD_TABLE_NAME;
    
    private boolean worldCreated; // has the store been already created?
    private boolean worldUpdated; // has the store been already updated?
    
    public WorldStoreTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        Properties system = System.getProperties();
        
        system.put(StoreConnection.PROP_JDBC_DRIVER,   "org.hsqldb.jdbcDriver");
        system.put(StoreConnection.PROP_JDBC_URL,      "jdbc:hsqldb:mem:test" );
        system.put(StoreConnection.PROP_JDBC_USER,     "sa"                   );
        system.put(StoreConnection.PROP_JDBC_PASSWORD, ""                     );
        
        worldCreated = worldUpdated = false;
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        
        StoreConnection sc = new StoreConnection();
        
        sc.executeUpdate("SHUTDOWN");
        
        Properties system = System.getProperties();
        
        system.remove(StoreConnection.PROP_JDBC_DRIVER  );
        system.remove(StoreConnection.PROP_JDBC_URL     );
        system.remove(StoreConnection.PROP_JDBC_USER    );
        system.remove(StoreConnection.PROP_JDBC_PASSWORD);
    }
    
    /**
     * This test covers:
     * 
     * - isThereSchema()
     * - initialize()
     * - createWorld()
     * 
     * We assume the database is clean
     * 
     * @throws java.lang.Exception
     */
    public void testInitializeAndCreateWorld() throws Exception {
        WorldStore store = new WorldStore();
        
        assertFalse(store.isThereSchema());
                
        store.initialize();
        
        assertTrue(store.isThereSchema());
        
        //
        // if we initialize again, we should not have any errror
        //
        store.initialize();
        assertTrue(store.isThereSchema());
        
        World w = store.getWorld();
        
        assertTrue(w.isEmpty());
        
        worldCreated = true;
    }

    public void testUpdateWorld() throws Exception {
        String mapUrl = "file://" + new File(MapDownloaderTest.MAP_FILE).getAbsolutePath();
        
        MapDownloader mapReader = new MapDownloader(mapUrl);
        
        if (!worldCreated) {
            testInitializeAndCreateWorld();
            assertEquals(true, worldCreated);
        }
        
        WorldStore world = new WorldStore();
        world.updateWorld(mapReader.downloadMapAsReader());
        
        StoreConnection c = null;
        ResultSet rs = null;
        try {
            c = new StoreConnection();
            
            rs = c.executeQuery(SQL_COUNT_TILES);
            
            assertEquals(true, rs.next());
            assertEquals(10, rs.getInt(1));
            
            rs.close(); rs = null;
            
            rs = c.executeQuery(SQL_GET_TILE);
            
            assertEquals(true, rs.next());
            assertEquals("2 Shark", rs.getString(6));
            
            worldUpdated = true;
            
        } finally {
            if (rs != null) {
                rs.close(); rs = null;
            }
            if (c != null) {
                c.closeConnection(); c = null;
            }
        }
    }

    public void testGetWorld() throws Exception {
        if (!worldUpdated) {
            testUpdateWorld();
            assertEquals(true, worldUpdated);
        }
        
        WorldStore store = new WorldStore();
        World world = store.getWorld();
       
        assertEquals(400, world.getMaxX());
        assertEquals(400, world.getMaxY());
        assertEquals(-399, world.getMinX());
        assertEquals(396, world.getMinY());
        
        Tile tile = world.getTile(290, 397);
        
        assertEquals(3094, tile.getId());
    }

    public void testAlliances() throws Exception {
        if (!worldUpdated) {
            testUpdateWorld();
            assertEquals(true, worldUpdated);
        }

        WorldStore store = new WorldStore();
        Map<Integer,String> alliances = store.readAlliances();

        //
        // Let's check a couple of values...
        //
        assertTrue(alliances.containsKey(new Integer(539)));
        assertEquals("T[SD]", alliances.get(new Integer(539)));

        assertTrue(alliances.containsKey(new Integer(539)));
        assertEquals("Berserk+", alliances.get(new Integer(436)));
    }

    //
    // TODO: we now test the read only; we need to add write also
    //
    public void testAllianceGroups() throws Exception {
        if (!worldUpdated) {
            testUpdateWorld();
            assertEquals(true, worldUpdated);
        }

        WorldStore store = new WorldStore();
        Map<String,ArrayList<String>> groups = store.readAllianceGroups();

        assertEquals(1, groups.size());
        assertTrue(groups.containsKey(WorldStore.REST_OF_THE_WORLD_GROUP));
        
        ArrayList<String> group = groups.get(WorldStore.REST_OF_THE_WORLD_GROUP);
        assertEquals(6, group.size());

        //
        // Let's check some values
        //
        assertTrue(group.contains("Berserk+"));
        assertTrue(group.contains("T[SD]"));
    }

}
