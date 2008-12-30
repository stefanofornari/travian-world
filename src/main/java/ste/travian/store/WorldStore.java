/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ste.travian.store;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import ste.travian.world.Tile;
import ste.travian.world.World;

/**
 *
 * @author ste
 */
public class WorldStore {

    public static final String WORLD_TABLE_NAME = "X_WORLD";
    public static final String SQL_CREATE_WORLD =
            "CREATE TABLE " +
            WORLD_TABLE_NAME +
            "(" +
            "id int NOT NULL," +
            "x smallint NOT NULL," +
            "y smallint NOT NULL," +
            "tid smallint  NOT NULL," +
            "vid int NOT NULL," +
            "village varchar(100) NOT NULL," +
            "uid int NOT NULL," +
            "player varchar(40) NOT NULL," +
            "vaid int NOT NULL," +
            "alliance varchar(40) NOT NULL," +
            "population smallint NOT NULL," +
            "PRIMARY KEY (id)" +
            ")";
    public static final String SQL_DELETE_ALL = "delete from " + WORLD_TABLE_NAME;
    public static final String SQL_GET_ALL = "select * from " + WORLD_TABLE_NAME;

    /**
     * Creates the world in the database.
     * 
     * @throws ste.travian.store.StoreException
     */
    public void createWorld() throws StoreException {
        StoreConnection sc = null;
        try {
            sc = new StoreConnection();

            sc.executeUpdate(SQL_CREATE_WORLD);

        } catch (Exception e) {
            throw new StoreException("Error in creating the world map", e);
        } finally {
            if (sc != null) {
                try {
                    sc.closeConnection();
                } catch (SQLException e) {
                //
                // there is nothing we can do...
                }
            }
        }
    }

    /**
     * Initializes the store. If the world tables do not exist yet, they are 
     * created.
     * 
     * @throws ste.travian.store.StoreException
     */
    public void initialize() throws StoreException {
        if (!isThereSchema()) {
            createWorld();
        }
    }

    /**
     * Update the travian world reading the SQL queries from the given
     * BufferedRead
     * 
     * @param in the reader from which read the SQL commands to execute
     * @throws ste.travian.store.StoreException
     */
    public void updateWorld(BufferedReader in) throws StoreException {
        if (in == null) {
            throw new IllegalArgumentException("in cannot be null");
        }

        StoreConnection sc = null;
        try {
            sc = new StoreConnection();

            //
            // First we need to delete everything
            //
            sc.executeUpdate(SQL_DELETE_ALL);

            String query = null;
            while ((query = in.readLine()) != null) {
                //
                // for some reason the table name is included within `, which
                // fail the execution of the query... let's take them out
                //
                System.out.println(query);
                sc.executeUpdate(query.replaceAll("`", ""));
            }

        } catch (Exception e) {
            throw new StoreException("Error in storing the world map", e);
        } finally {
            if (sc != null) {
                try {
                    sc.closeConnection();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * Retrieve the entire world from the storage
     * 
     * @return the World object representing the world retrieved from the storage
     * 
     * @throws ste.travian.store.StoreException in case of errors
     */
    public World getWorld() throws StoreException {
        World world = new World();
        
        StoreConnection sc = null;
        ResultSet rs = null;
        try {
            sc = new StoreConnection();

            //
            // First we need to delete everything
            //
            rs = sc.executeQuery(SQL_GET_ALL);

            Tile t = null;
            while (rs.next()) {
                t = getTile(rs);
                world.addTile(t);
            }

        } catch (Exception e) {
            throw new StoreException("Error in storing the world map", e);
        } finally {
            if (sc != null) {
                try {
                    sc.closeConnection();
                } catch (SQLException e) {
                //
                // there is nothing we can do...
                }
            }
        }
        
        return world;
    }

    /**
     * Determines if the store schema has been already created
     * 
     * @return true if the shema has been already created, false otherwise
     * 
     * @throws StoreException in case of errors
     */
    public boolean isThereSchema() throws StoreException {
        StoreConnection sc = null;
        ResultSet rs = null;
        try {
            sc = new StoreConnection();

            //
            // We cannot simply select the table by name because some db use
            // capital letters, other small letters...
            //
            rs = sc.getConnection().getMetaData().getTables(null, null, "%", new String[]{"TABLE"});

            boolean ret = false;
            while (!ret && rs.next()) {
                if (WORLD_TABLE_NAME.equalsIgnoreCase(rs.getString(3))) {
                    ret = true;
                }
            }

            return ret;

        } catch (SQLException e) {
            throw new StoreException("Error retrieving database metadata", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (sc != null) {
                try {
                    sc.closeConnection();
                } catch (SQLException e) {
                }
                sc = null;
            }
        }

    }

    // --------------------------------------------------------- Private methods
    private Tile getTile(ResultSet rs) throws SQLException {
        return new Tile(
                rs.getInt(1), // id
                rs.getInt(2), // x
                rs.getInt(3), // y
                rs.getInt(4), // tid
                rs.getInt(5), // vid
                rs.getString(6), // village
                rs.getInt(7), // uid
                rs.getString(8), // user
                rs.getInt(9), // aid
                rs.getString(10), // alliance
                rs.getInt(11) // population
                );
    }
}
