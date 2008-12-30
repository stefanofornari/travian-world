/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.store;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class represents a connection to the data store; the configuration for
 * the connection is taken from a file config.properties in the classpath
 * or from the system properties:
 * 
 * jdbc.driver   : JDBC driver to use
 * jdbc.url      : JDBC url
 * jdbc.user     : JDB user
 * jdbc.password : JDBC password
 * 
 * 
 * @author ste
 */
public class StoreConnection {
    
    public static final String PROP_JDBC_DRIVER   = "jdbc.driver"  ;
    public static final String PROP_JDBC_URL      = "jdbc.url"     ;
    public static final String PROP_JDBC_USER     = "jdbc.user"    ;
    public static final String PROP_JDBC_PASSWORD = "jdbc.password";
    
    private Connection c;
    
    /**
     * Creates a new StoreConnection and makes sure all necessary parameters
     * are specified
     */
    public StoreConnection() {
        //
        // Connectivity information are read from the db.properties file 
        // or from the System properties
        //
        Properties config = new Properties();
        
        InputStream is = getClass().getResourceAsStream("/config.properties");
        
        if (is != null) {
            try {
                config.load(is);
                System.getProperties().putAll(config);
            } catch (IOException e) {
                //
                // do nothing, we will use the system properties
                //
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    //
                    // there is nothing meaningful I can do...
                    //
                }
            }
        }
        
        Properties system = System.getProperties();
        
        String value = system.getProperty(PROP_JDBC_DRIVER);
        if ((value == null) || (value.length() == 0)) {
            throw new IllegalArgumentException("The property " + PROP_JDBC_DRIVER + " must be specified in config.properties or with -D on the command line");
        }
        
        value = system.getProperty(PROP_JDBC_URL);
        if ((value == null) || (value.length() == 0)) {
            throw new IllegalArgumentException("The property " + PROP_JDBC_URL + " must be specified in config.properties or with -D on the command line");
        }
        
        value = system.getProperty(PROP_JDBC_USER);
        if ((value == null) || (value.length() == 0)) {
            throw new IllegalArgumentException("The property " + PROP_JDBC_USER + " must be specified in config.properties or with -D on the command line");
        }
        
        value = system.getProperty(PROP_JDBC_PASSWORD);
        if (value == null) {
            throw new IllegalArgumentException("The property " + PROP_JDBC_PASSWORD + " must be specified in config.properties or with -D on the command line");
        }
        
        c = null;
    }
    
    /**
     * Returns a previously created connection if available or creates a new one.
     *
     * @return the connection
     * 
     * @throws java.sql.SQLException
     */
    public Connection getConnection() throws SQLException {
        if (c != null) {
            return c;
        }
       
        try {
            Class.forName(getDriver());
        } catch (Exception e) {
            throw new SQLException("Error loading the driver " + getDriver() + ": " + e);
        }
        
        return (c = DriverManager.getConnection(getUrl(), getUser(), getPassword()));
    }
    
    /**
     * Closes the current connection if open
     * 
     * @throws java.sql.SQLException
     */
    public void closeConnection() throws SQLException {
        if (c != null) {
            if (!c.isClosed()) {
                c.close(); 
            }
            c = null;
        }
    }
    
    public void executeUpdate(String sql) throws SQLException {
        Connection c = getConnection();
        
        Statement s = null;
        try {
            s = c.createStatement();
            
            s.executeUpdate(sql);
        } finally {
            if (s != null) {
                try { s.close(); } catch (Exception e) {}
            }
        }
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        return getConnection().createStatement().executeQuery(sql);
    }
    
    // --------------------------------------------------------- Private methods

    private String getDriver() {
        return System.getProperties().getProperty(PROP_JDBC_DRIVER);
    }
    
    private String getUrl() {
        return System.getProperties().getProperty(PROP_JDBC_URL);
    }
    
    private String getUser() {
        return System.getProperties().getProperty(PROP_JDBC_USER);
    }
    
    private String getPassword() {
        return System.getProperties().getProperty(PROP_JDBC_PASSWORD);
    }
}
