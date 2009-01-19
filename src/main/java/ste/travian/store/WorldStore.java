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

    /**
     * Creates the world in the database.
     * 
     * @throws ste.travian.store.StoreException
     */
    public void createWorld() throws StoreException {
        StoreConnection sc = null;
        try {
            sc = new StoreConnection();

            sc.executeUpdate(Q.SQL_CREATE_WORLD);
            sc.executeUpdate(Q.SQL_CREATE_ALLIANCE);
            sc.executeUpdate(Q.SQL_CREATE_GROUP);
            sc.executeUpdate(Q.SQL_CREATE_ALLIANCE_GROUP);
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
            sc.executeUpdate(Q.SQL_DELETE_ALL_WORLD);
            sc.executeUpdate(Q.SQL_DELETE_ALL_ALLIANCE);

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
            sc.executeUpdate(Q.SQL_INSERT_ALL_ALLIANCES);

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
            rs = sc.executeQuery(Q.SQL_GET_ALL_WORLD);

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
                if (Q.WORLD_TABLE_NAME.equalsIgnoreCase(rs.getString(3))) {
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
            rs = sc.executeQuery(Q.SQL_GET_ALL_ALLIANCES);

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
            rs = sc.executeQuery(Q.SQL_GET_REST_OF_THE_WORLD_ALLIANCES);

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
            rs = sc.executeQuery(Q.SQL_GET_ALL_ALLIANCE_GROUPS);

            while (rs.next()) {
                groupName = rs.getString(1);
                group = ret.get(groupName);
                if (group == null) {
                    group = new ArrayList<String>();
                    ret.put(groupName, group);
                }
                group.add(rs.getString(2));
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

    public void updateAllianceGroups(Map<String, ArrayList<String>> groups)
    throws StoreException {
        StoreConnection sc = null;
        try {
            sc = new StoreConnection();

            ArrayList<String> group = null;

            //
            // First remove all existing groups
            //
            sc.executeUpdate(Q.SQL_DELETE_ALL_ALLIANCE_GROUP);
            sc.executeUpdate(Q.SQL_DELETE_ALL_GROUP);

            int groupId = 0;
            for (String groupName: groups.keySet()) {
                if (!REST_OF_THE_WORLD_GROUP.equals(groupName)) {
                    addGroup(sc, ++groupId, groupName);
                    for (String alliance: groups.get(groupName)) {
                        addAllianceGroup(sc, groupId, alliance);
                    }
                }
            }
        } catch (SQLException e) {
            throw new StoreException("Error retrieving alliance groups", e);
        } finally {
            if (sc != null) {
                try { sc.closeConnection(); } catch (SQLException e) {}
                sc = null;
            }
        }
    }

    // --------------------------------------------------------- Private methods

    /**
     * Insert an alliance group.
     * 
     * @param c the store connection
     * @param group the group name
     * @return the id of the new group
     * 
     * @throws java.sql.SQLException
     */
    private void addGroup(StoreConnection c, int id, String group)
    throws SQLException {
        c.executeUpdate(
            Q.SQL_ADD_NEW_GROUP,
            new Object[] { new Integer(id), group }
        );
    }

    /**
     * Add the given group of alliances. Since the database links must be done
     * through the ids, we need to look up the alliances from the alliance table.
     *
     * @param c the store connection
     * @param group the group name
     * @param alliance the alliance name
     * 
     * @throws java.sql.SQLException in case of database error
     */
    private void addAllianceGroup(StoreConnection c, int group, String alliance)
    throws SQLException {
        c.executeUpdate(
            Q.SQL_ADD_NEW_GROUP_ALLIANCE,
            new Object[] { new Integer(group), alliance }
        );

    }

    /**
     * Return a Tile object from the data of the given result set
     *
     * @param rs the result set
     * @return the newly created tile
     *
     * @throws java.sql.SQLException in case of database error
     */
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
