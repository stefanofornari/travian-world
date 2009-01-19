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

/**
 * Just a collection of queries...
 *
 * @author ste
 */
public class Q {
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
    public static final String SQL_DELETE_ALL_ALLIANCE_GROUP = "delete from " + ALLIANCE_GROUP_TABLE_NAME;

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
            "order by 1, 2"                                   ;
    public static final String SQL_GET_REST_OF_THE_WORLD_ALLIANCES =
            "select name from "                                    +
            ALLIANCE_TABLE_NAME                                    +
            " where id not in ("                                   +
            "select distinct aid from "                            +
            ALLIANCE_GROUP_TABLE_NAME                              +
            ") order by 1"                                           ;

    public static final String SQL_INSERT_ALL_ALLIANCES =
        "insert into "                                  +
        ALLIANCE_TABLE_NAME                             +
        "("                                             +
        "select distinct aid, alliance from "           +
        WORLD_TABLE_NAME                                +
        ")"                                             ;

    public static final String SQL_ADD_NEW_GROUP =
        "insert into "                           +
        GROUP_TABLE_NAME                         +
        " values(?, ?)"                          ;

    public static final String SQL_ADD_NEW_GROUP_ALLIANCE =
        "insert into "                                    +
        ALLIANCE_GROUP_TABLE_NAME                         +
        " (select id, ? from "                            +
        ALLIANCE_TABLE_NAME                               +
        " where name=?)"                                  ;
}
