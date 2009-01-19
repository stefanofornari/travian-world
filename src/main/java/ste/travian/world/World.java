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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ste
 */
public class World {

    protected HashMap<String, Tile> map;
    
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    
    private List<String> alliances;
    private boolean needAlliancesSort;

    private Map<String,ArrayList<String>> allianceGroups;

    public World() {
        map = new HashMap<String, Tile>();
        maxX = Integer.MIN_VALUE;
        maxY = Integer.MIN_VALUE;
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        
        alliances = new ArrayList<String>();
        needAlliancesSort = false;
        allianceGroups = new HashMap<String, ArrayList<String>>();
    }

    /**
     * Adds a new Tile to the world.
     * 
     * @param tile the tile to add; ignored if null
     */
    public void addTile(Tile tile) {
        if (tile == null) {
            return;
        }
        
        int x = tile.getX();
        int y = tile.getY();

        map.put(createKey(x, y), tile);
        
        if (x > maxX) {
            maxX = x;
        }
        
        if (x < minX) {
            minX = x;
        }
        
        if (y > maxY) {
            maxY = y;
        }
        
        if (y < minY) {
            minY = y;
        }
        
        if (alliances.indexOf(tile.getAlliance())<0) {
            alliances.add(tile.getAlliance());
            needAlliancesSort = true;
        }
    }

    /**
     * Returns the tile at the given coordinates
     * 
     * @param x
     * @param y
     * 
     * @return the tile at (x,y)
     */
    public Tile getTile(int x, int y) {
        return map.get(createKey(x, y));
    }
    
    /**
     * 
     * @return the max X cooridinate of the world
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * 
     * @return the min X coordinate of the world
     */
    public int getMinX() {
        return minX;
    }
    
    /**
     * 
     * @return the max Y cooridinate of the world
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * 
     * @return the min Y coordinate of the world
     */
    public int getMinY() {
        return minY;
    }
    
    /**
     * Does the map contain any tail?
     * 
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    /**
     * Returns all alliances in the world
     * 
     * @return all alliances in the world
     */
    public String[] getAlliances() {
        if (needAlliancesSort) {
            Collections.sort(alliances);
            needAlliancesSort = false;
        }
        return alliances.toArray(new String[alliances.size()]);
    }

    /**
     * Sets alliance groups
     * 
     * @param groups - can be null
     */
    public void setAllianceGroups(Map<String,ArrayList<String>> groups) {
        allianceGroups = groups;
    }

    /**
     * Gets the alliance groups
     *
     * @return the alliance groups
     */
    public Map<String,ArrayList<String>> getAllianceGroups() {
        return allianceGroups;
    }

    // ----------------------------------------------------------- Private methods
    /**
     * Create the key to use to add a tile into the map starting from a Tile 
     * object.
     * 
     * @param tile the tile for which we want the key
     * 
     * @return the generated key
     */
    private String createKey(Tile tile) {
        return createKey(tile.getX(), tile.getY());
    }

    /**
     * Create the key to use to add a tile into the map. The key is generated by
     * the tile's coordinates as follows:
     * 
     *  (x),(y)
     * 
     * @param x
     * @param y
     * 
     * @return the generated key
     */
    private String createKey(int x, int y) {
        return x + "," + y;
    }
}
