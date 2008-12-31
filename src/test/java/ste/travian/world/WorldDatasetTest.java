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

import java.util.Arrays;
import junit.framework.TestCase;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

/**
 *
 * @author ste
 */
public class WorldDatasetTest extends TestCase {
    
    private final World WORLD = new World();
    
    public WorldDatasetTest(String testName) {
        super(testName);
          
        WORLD.addTile(WorldTest.TILES[0]);
        WORLD.addTile(WorldTest.TILES[1]);
        WORLD.addTile(WorldTest.TILES[2]);
        WORLD.addTile(WorldTest.TILES[3]);        
    }

    public void testSanityCheck() {
        WorldDataset instance = new WorldDataset();
        
        instance.initialize(WORLD);
        
        try {
            instance.getItemCount(-1);
            fail("series must not be <0");
        } catch (IllegalArgumentException e) {
            // OK
        }
        
        try {
            instance.getItemCount(10);
            fail("series must not be >=" + instance.getSeriesCount());
        } catch (IllegalArgumentException e) {
            // OK
        }
        
        try {
            instance.getX(0, -1);
            fail("item must not be <0");
        } catch (IllegalArgumentException e) {
            // OK
        }
        
        try {
            instance.getX(0, 10);
            fail("item must not be >=" + instance.getItemCount(0));
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
    
    public void testInitialize() {
        WorldDataset instance = new WorldDataset();
        
        assertEquals(1, instance.getSeriesCount());
        assertEquals(0, instance.getChangeListeners().length);
        assertNull(instance.getGroup());
        
        instance.initialize(WORLD);
        
        assertEquals(4, instance.getItemCount(0));
        assertEquals(1, instance.getKeys().length);
    }
    
    public void testSetKeys() {
        String[] KEYS = {
            WorldTest.TILES[0].getAlliance(),
            WorldTest.TILES[2].getAlliance()
        };
        
        WorldDataset instance = new WorldDataset();
        
        instance.setKeys(KEYS);
        instance.initialize(WORLD);
        
        assertEquals(WorldDataset.DEFAULT_KEY, instance.getKeys()[0]);
        assertEquals(KEYS[0], instance.getKeys()[1]);
        assertEquals(KEYS[1], instance.getKeys()[2]);
        
        instance.setKeys(Arrays.asList(KEYS));
        instance.initialize(WORLD);
        
        assertEquals(WorldDataset.DEFAULT_KEY, instance.getKeys()[0]);
        assertEquals(KEYS[0], instance.getKeys()[1]);
        assertEquals(KEYS[1], instance.getKeys()[2]);
    }
    
    public void testGetTile() {
        WorldDataset instance = new WorldDataset();
        
        instance.initialize(WORLD);
        
        Tile t = instance.getTile(0, 1);
        assertEquals(WorldTest.TILES[3].getId(), t.getId());
        
        try {
            instance.getTile(-1, 0);
            fail("series cannot be <0 but getTile passes");
        } catch (IllegalArgumentException e) {}; // ok
        
        try {
            instance.getTile(0, -1);
            fail("item cannot be <0 but getTile passes");
        } catch (IllegalArgumentException e) {}; // ok
        
        try {
            instance.getTile(10, 0);
            fail("series cannot be > getSeriesCount()-1 but getTile passes");
        } catch (IllegalArgumentException e) {}; // ok
        
        try {
            instance.getTile(0, 10);
            fail("series cannot be > getItemCount()-1 but getTile passes");
        } catch (IllegalArgumentException e) {}; // ok
    }
    
    public void testGetDomainOrder() {
        WorldDataset instance = new WorldDataset();
        
        assertEquals(DomainOrder.NONE, instance.getDomainOrder());
    }

    public void testGetSeriesCount() {
        WorldDataset instance = new WorldDataset();
        
        instance.initialize(WORLD);
        
        assertEquals(1, instance.getSeriesCount());
        
        instance.setKeys(new String[] {"a02"});
        
        assertEquals(2, instance.getSeriesCount());
        
        instance.setKeys(new String[] {"a01", "a02"});
        
        assertEquals(3, instance.getSeriesCount());
    }

    public void testGetSeriesKey() {
        WorldDataset instance = new WorldDataset();
        
        instance.initialize(WORLD);
                
        instance.setKeys(new String[] {"a02"});
        
        assertEquals(WorldDataset.DEFAULT_KEY, instance.getSeriesKey(0));
        assertEquals(WorldTest.TILES[3].getAlliance(), instance.getSeriesKey(1));
    }

    public void testIndexOf() {
        WorldDataset instance = new WorldDataset();
        
        instance.setKeys(new String[] {"a02"});
        instance.initialize(WORLD);
        
        assertEquals(0, instance.indexOf(WorldDataset.DEFAULT_KEY));
        assertEquals(1, instance.indexOf(WorldTest.TILES[3].getAlliance()));
    }

    public void testAddRemoveChangeListener() {
        WorldDataset instance = new WorldDataset();
        
        instance.initialize(WORLD);
        
        assertEquals(0, instance.getChangeListeners().length);
        
        DatasetChangeListener l;
        instance.addChangeListener(l = new DatasetChangeListener() {
            public void datasetChanged(DatasetChangeEvent event) {}
        });
        
        assertEquals(1, instance.getChangeListeners().length);
        
        instance.removeChangeListener(l);
        
        assertEquals(0, instance.getChangeListeners().length);
    }
    
    public void testGetX() {
        WorldDataset instance = new WorldDataset();
        
        instance.setKeys(new String[] {"a01"});
        instance.initialize(WORLD);
        //
        // note that tiles are inserted from minX to maxX and minY to maxY
        //
        assertEquals(WorldTest.TILES[1].getX(), (int)instance.getXValue(instance.indexOf(WorldTest.TILES[1].getAlliance()), 0));
        assertEquals(new Integer(WorldTest.TILES[1].getX()), instance.getX(instance.indexOf(WorldTest.TILES[1].getAlliance()), 0));
    }
    
    public void testGetY() {
        WorldDataset instance = new WorldDataset();
        
        instance.setKeys(new String[] {"a02"});
        instance.initialize(WORLD);
        //
        // note that tiles are inserted from minX to maxX and minY to maxY
        //
        assertEquals(WorldTest.TILES[2].getY(), (int)instance.getYValue(instance.indexOf(WorldTest.TILES[2].getAlliance()), 1));
        assertEquals(new Integer(WorldTest.TILES[2].getY()), instance.getY(instance.indexOf(WorldTest.TILES[2].getAlliance()), 1));
    }

    //
    // not used by jfreechart
    //
    public void _testGetGroup() {
    }

    //
    // not used by jfreechart
    //
    public void testSetGroup() {

    }

    

}
