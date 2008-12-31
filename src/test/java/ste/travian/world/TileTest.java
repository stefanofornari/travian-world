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

import ste.travian.world.Tile;
import ste.travian.world.Tribe;
import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class TileTest extends TestCase {
    
    public static final int    ID         = 100                ;
    public static final int    X          = 200                ;
    public static final int    Y          = 300                ;
    public static final int    TID        = Tribe.ROMAN.getId();
    public static final Tribe  TRIBE      = Tribe.ROMAN        ;
    public static final int    VID        = 500                ;
    public static final String VILLAGE    = "the village"      ;
    public static final int    UID        = 600                ;
    public static final String PLAYER     = "the player"       ;
    public static final int    AID        = 700                ;
    public static final String ALLIANCE   = "the alliance"     ;
    public static final int    POPULATION = 800                ;
    
    public TileTest(String testName) {
        super(testName);
    }    
    
    public static Tile getDefaultTile() {
        return new Tile(
                       ID,
                       X, Y,
                       TID,
                       VID,
                       VILLAGE,
                       UID,
                       PLAYER,
                       AID,
                       ALLIANCE,
                       POPULATION
                   );
    }
    
    public void testContructorAndGetters() throws Throwable {
        Tile tile = getDefaultTile();
        
        assertEquals(ID, tile.getId());
        assertEquals(X, tile.getX());
        assertEquals(Y, tile.getY());
        assertEquals(Tribe.ROMAN,tile.getTribe());
        assertEquals(VID, tile.getVid());
        assertEquals(VILLAGE, tile.getVillage());
        assertEquals(UID,tile.getUid());
        assertEquals(PLAYER, tile.getPlayer());
        assertEquals(AID, tile.getAid());
        assertEquals(ALLIANCE,tile.getAlliance());
        assertEquals(POPULATION, tile.getPopulation());
        
        tile = new Tile(tile.getId(), tile.getX(), tile.getY());
        
        assertEquals(Tribe.NATURE, tile.getTribe());
    }
    
    public void testSetters() throws Throwable {
        Tile tile = getDefaultTile();
        
        tile.setAid(0);
        tile.setAlliance("");
        tile.setId(0);
        tile.setPlayer("");
        tile.setPopulation(0);
        tile.setTribe(Tribe.GAUL);
        tile.setUid(0);
        tile.setVid(0);
        tile.setVillage("");
        tile.setX(0);
        tile.setY(0);
        
        assertEquals(0, tile.getId());
        assertEquals(0, tile.getX());
        assertEquals(0, tile.getY());
        assertEquals(Tribe.GAUL.getId(),tile.getTribe().getId());
        assertEquals(0, tile.getVid());
        assertEquals(0, tile.getPopulation());
        assertEquals(0,tile.getUid());
        assertEquals(0, tile.getAid());
        assertEquals("", tile.getVillage());
        assertEquals("", tile.getPlayer());
        assertEquals("",tile.getAlliance());
    }
    
    /**
     *  This covers both versions of toString()
     */
    public void testToString() {
        final String S = "[100,200,300,Roman,500,the village,600,the player,700,the alliance,800]";
        
        Tile tile = getDefaultTile();
        
        assertEquals(S, tile.toString());
    }

}
