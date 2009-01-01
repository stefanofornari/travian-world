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

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import ste.travian.world.Tile;
import ste.travian.world.World;

/**
 *
 * @author ste
 */
public class WorldStore {

    public static final String REST_OF_THE_WORLD_GROUP = "Rest of the world";
    public static final String WORLD_TABLE_NAME = "X_WORLD";
    public static final String ALLIANCE_TABLE_NAME = "X_ALLIANCE";
    public static final String GROUP_TABLE_NAME = "X_GROUP";
    public static final String ALLIANCE_GROUP_TABLE_NAME = "X_ALLIANCE_GROUP";

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
            "aid int NOT NULL," +
            "alliance varchar(40) NOT NULL," +
            "population smallint NOT NULL," +
            "PRIMARY KEY (id)" +
            ")";
    
    public static final String SQL_CREATE_ALLIANCE  =
            "CREATE TABLE "                         +
            ALLIANCE_TABLE_NAME                     +
            "(id int primary key, name varchar(40))";

    public static final String SQL_CREATE_GROUP      =
            "CREATE TABLE "                          +
            GROUP_TABLE_NAME                         +
            "(id int primary key, name varchar(100))";

    public static final String SQL_CREATE_ALLIANCE_GROUP =
            "CREATE TABLE "                              +
            ALLIANCE_GROUP_TABLE_NAME                    +
            "(aid int, gid int, primary key(aid, gid))"  ;

    public static final String SQL_DELETE_ALL_WORLD = "delete from " + WORLD_TABLE_NAME;
    public static final String SQL_DELETE_ALL_ALLIANCE = "delete from " + ALLIANCE_TABLE_NAME;
    public static final String SQL_DELETE_ALL_GROUP = "delete from " + GROUP_TABLE_NAME;
    public static final String SQL_DELETE_ALL_ALLIANCE_GROUP_TABLE_NAME =
            "delete from " + ALLIANCE_GROUP_TABLE_NAME;

    public static final String SQL_GET_ALL_WORLD =
            "select * from " + WORLD_TABLE_NAME  ;
    public static final String SQL_GET_ALL_ALLIANCES = 
            " select * from "                        +
            ALLIANCE_TABLE_NAME                      +
            " order by name"                         ;
    public static final String SQL_GET_ALL_ALLIANCE_GROUPS =
            "select g.name, a.name from "                  +
            GROUP_TABLE_NAME                               +
            " g, "                                         +
            ALLIANCE_TABLE_NAME                            +
            " a, "                                         +
            ALLIANCE_GROUP_TABLE_NAME                      +
            " ag where "                                   +
            "g.id=ag.gid and a.id=ag.aid "                 +
            "order by 1"                                   ;
    public static final String SQL_GET_REST_OF_THE_WORLD_ALLIANCES =
            "select name from "                                    +
            ALLIANCE_TABLE_NAME                                    +
            " where id not in ("                                   +
            "select distinct aid from "                            +
            ALLIANCE_GROUP_TABLE_NAME                              +
            ") order by 1"                                         ;


    public static final String SQL_INSERT_ALL_ALLIANCES =
        "insert into "                                  +
        ALLIANCE_TABLE_NAME                             +
        "("                                             +
        "select distinct aid, alliance from "           +
        WORLD_TABLE_NAME                                +
        ")"                                             ;


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
            sc.executeUpdate(SQL_CREATE_ALLIANCE);
            sc.executeUpdate(SQL_CREATE_GROUP);
            sc.executeUpdate(SQL_CREATE_ALLIANCE_GROUP);
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
            sc.executeUpdate(SQL_DELETE_ALL_WORLD);
            sc.executeUpdate(SQL_DELETE_ALL_ALLIANCE);

            String query = null;
            while ((query = in.readLine()) != null) {
                //
                // for some reason the table name is included within `, which
                // fail the execution of the query... let's take them out
                //
                sc.executeUpdate(query.replaceAll("`", ""));
            }
            //
            // Let's extract the alliances
            //
            sc.executeUpdate(SQL_INSERT_ALL_ALLIANCES);

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
            rs = sc.executeQuery(SQL_GET_ALL_WORLD);

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

    /**
     * Returns the alliances as stored in the database.
     *
     * @return the alliances alliances in a Map<Integer, String> form (id, name)
     *
     * @throws ste.travian.store.StoreException
     */
    public Map<Integer, String> readAlliances() throws StoreException {
        Map<Integer, String> ret =  new HashMap<Integer, String>();

        StoreConnection sc = null;
        ResultSet rs = null;
        try {
            sc = new StoreConnection();

            //
            // We cannot simply select the table by name because some db use
            // capital letters, other small letters...
            //
            rs = sc.executeQuery(SQL_GET_ALL_ALLIANCES);

            while (rs.next()) {
                ret.put(new Integer(rs.getInt(1)), rs.getString(2));
            }

            return ret;

        } catch (SQLException e) {
            throw new StoreException("Error retrieving alliances", e);
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

    /**
     * Returns the groups of alliances as stored in the backend.
     *
     * @return the groups of alliances in a Map<String, String[]> form
     *
     *
     * @throws ste.travian.store.StoreException
     */
    public Map<String, ArrayList<String>> readAllianceGroups() throws StoreException {
        Map<String, ArrayList<String>> ret =
            new TreeMap<String, ArrayList<String>>();

        StoreConnection sc = null;
        ResultSet rs = null;
        try {
            sc = new StoreConnection();

            String groupName = null;
            ArrayList<String> group = null;

            //
            // First get the alliances not in any group
            //
            rs = sc.executeQuery(SQL_GET_REST_OF_THE_WORLD_ALLIANCES);

            groupName = REST_OF_THE_WORLD_GROUP;
            group = new ArrayList<String>();

            ret.put(groupName, group);
            while (rs.next()) {
                group.add(rs.getString(1));
            }
            rs.close(); rs = null;

            //
            // Now let's get the groups
            //
            rs = sc.executeQuery(SQL_GET_ALL_ALLIANCE_GROUPS);

            while (rs.next()) {
                groupName = rs.getString(1);
                if (!ret.containsKey(groupName)) {
                    group = new ArrayList<String>();
                    ret.put(groupName, group);
                }
                group.add(rs.getString(1));
            }

            return ret;

        } catch (SQLException e) {
            throw new StoreException("Error retrieving alliance groups", e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) {}
                rs = null;
            }
            if (sc != null) {
                try { sc.closeConnection(); } catch (SQLException e) {}
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
