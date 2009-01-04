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

/**
 *
 * @author ste
 */
public enum Tribe {
    ROMAN(1), 
    TEUTON(2), 
    GAUL(3), 
    NATURE(4), 
    NATARS(5);

    public static final String TRIBE_ROMAN  = "Roman" ;
    public static final String TRIBE_TEUTON = "Teuton";
    public static final String TRIBE_GAUL   = "Gaul"  ;
    public static final String TRIBE_NATURE = "Nature";
    public static final String TRIBE_NATARS = "Natars";
    
    private int tid;
    
    Tribe(int tid) {
        this.tid = tid;
    }
    
    public int getId() {
        return tid;
    }
    
    public String getName() {
        assert (tid>=1 && tid<=5): "invalid tid: " + tid;
        
        if (tid == 1) {
            return TRIBE_ROMAN;
        } else if (tid == 2) {
            return TRIBE_TEUTON;
        } else if (tid == 3) {
            return TRIBE_GAUL;
        } else if (tid == 4) {
            return TRIBE_NATURE;
        } else if (tid == 5) {
            return TRIBE_NATARS;
        }
        
        throw new IllegalStateException("tid not in rage: " + tid);
    }
    
    public static Tribe getTribeFromId(int tid) throws IllegalArgumentException {
        if (tid == 1) {
            return ROMAN;
        } else if (tid == 2) {
            return TEUTON;
        } else if (tid == 3) {
            return GAUL;
        } else if (tid == 4) {
            return NATURE;
        } else if (tid == 5) {
            return NATARS;
        }
        
        throw new IllegalArgumentException("id must be between 1 and 5 (it is " + tid + ")");
    }

}
