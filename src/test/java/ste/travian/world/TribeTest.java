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

import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class TribeTest extends TestCase {
    
    public TribeTest(String testName) {
        super(testName);
    }

    public void testGetName() {
        assertEquals(Tribe.TRIBE_ROMAN , Tribe.ROMAN.getName() );
        assertEquals(Tribe.TRIBE_TEUTON, Tribe.TEUTON.getName());
        assertEquals(Tribe.TRIBE_GAUL  , Tribe.GAUL.getName()  );
        assertEquals(Tribe.TRIBE_NATURE, Tribe.NATURE.getName());
        assertEquals(Tribe.TRIBE_NATARS, Tribe.NATARS.getName());
    }

    public void testGetTribeFromId() {
        assertEquals(Tribe.ROMAN , Tribe.getTribeFromId(1));
        assertEquals(Tribe.TEUTON, Tribe.getTribeFromId(2));
        assertEquals(Tribe.GAUL  , Tribe.getTribeFromId(3));
        assertEquals(Tribe.NATURE, Tribe.getTribeFromId(4));
        assertEquals(Tribe.NATARS, Tribe.getTribeFromId(5));

        try {
            Tribe.getTribeFromId(10);
            fail("10 is an invalid id!");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

}
