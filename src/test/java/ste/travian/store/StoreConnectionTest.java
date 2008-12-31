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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;
import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class StoreConnectionTest extends TestCase {
    
    private final String DRIVER   = "org.postgresql.Driver";
    private final String URL      = "jdbc:postgresql://localhost/travian";
    private final String USER     = "travian";
    private final String PASSWORD = "password";
    
    public StoreConnectionTest(String testName) {
        super(testName);
    }

    public void testGetConnection() throws Exception {
        //
        // With no parameters it must fail
        //
        try {
            StoreConnection sc = new StoreConnection();
            fail("With no parameters it must fail!");
        } catch (IllegalArgumentException e) {
            //
            // this is good!
            //
        }
        
        //
        // TODO: loading config.properties
        //
        Properties p = System.getProperties();
        
        p.put(StoreConnection.PROP_JDBC_DRIVER, DRIVER);
        p.put(StoreConnection.PROP_JDBC_URL, URL);
        p.put(StoreConnection.PROP_JDBC_USER, USER);
        try {
            new StoreConnection();
            fail("missing property test failed (object created with missing parameters)");
        } catch (IllegalArgumentException e) {
            // ok
        }
        p.put(StoreConnection.PROP_JDBC_PASSWORD, "");  // empty password is allowed
        try {
            new StoreConnection();
        } catch (IllegalArgumentException e) {
            fail("empty passwords should be allowed");
        }
        p.put(StoreConnection.PROP_JDBC_PASSWORD, PASSWORD);
        
        StoreConnection c = new StoreConnection();
        
        Class cl = c.getClass();
        Method m = cl.getDeclaredMethod("getDriver", new Class[0]);
        m.setAccessible(true);
        String value = (String)m.invoke(c);
        assertEquals(DRIVER, value);
        
        m = cl.getDeclaredMethod("getUrl", new Class[0]); m.setAccessible(true);
        value = (String)m.invoke(c);
        assertEquals(URL, value);
        
        m = cl.getDeclaredMethod("getUser", new Class[0]); m.setAccessible(true);
        value = (String)m.invoke(c);
        assertEquals(USER, value);
        
        m = cl.getDeclaredMethod("getPassword", new Class[0]); m.setAccessible(true);
        value = (String)m.invoke(c);
        assertEquals(PASSWORD, value);
        
    }

    //
    // TODO: Enable this test!!!
    //
    public void _testGetAnCloseConnection() throws Exception {
    }

}
