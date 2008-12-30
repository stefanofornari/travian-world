/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
