/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ste.travian.world;

/**
 * This class encapsulates all information about a tile.
 * 
 * The meaning of each field is as follows (see <a href="http://help.travian.us/index.php?type=faq&mod=230">here</a>
 * for the latest update):
 * 
 * <table>
 * <th><td>Column</td><td>Explanation</td></th>
 * <tr><td>id</td><td>Number of the field, starts in the top left corner at the coordinate (-400|400) and ends in the bottom right corner at (400|-400)</td></tr>
 * <th><td>x</td><td>X-Coordinate</td></tr>
 * <th><td>y</td><td>y-Coordinate</td></tr>
 * <th><td>tid</td><td>The tribe number. 1 = Roman, 2 = Teuton, 3 = Gaul, 4 = Nature and 5 = Natars</td></tr>
 * <th><td>vid</td><td>Village number</td></tr>
 * <th><td>village</td><td>Village name</td></tr>
 * <th><td>uid</td><td>Player number also known as User-ID</td></tr>
 * <th><td>player</td><td>Player name</td></tr>
 * <th><td>aid</td><td>Alliance number</td></tr>
 * <th><td>alliance</td><td>Alliance name</td></tr>
 * <th><td>population</td><td>The village's number of inhabitants</td></tr>
 * </table>
 * 
 * 
 * @author ste
 */
public class Tile {

    private int id;
    private int x;
    private int y;
    private Tribe tribe;
    private int vid;
    private String village;
    private int uid;
    private String player;
    private int aid;
    private String alliance;
    private int population;

    /**
     * Createas a tile given all its properties
     * 
     * @param id
     * @param x
     * @param y
     * @param tid
     * @param vid
     * @param village
     * @param uid
     * @param player
     * @param aid
     * @param alliance
     * @param population
     */
    public Tile(
            int id,
            int x,
            int y,
            int tid,
            int vid,
            String village,
            int uid,
            String player,
            int aid,
            String alliance,
            int population) {
        this(
          id, 
          x, y, 
          Tribe.getTribeFromId(tid), 
          vid, village, 
          uid, player, 
          aid, alliance, 
          population
        );
    }
    
    /**
     * Createas a tile given all its properties
     * 
     * @param id
     * @param x
     * @param y
     * @param tid
     * @param vid
     * @param village
     * @param uid
     * @param player
     * @param aid
     * @param alliance
     * @param population
     */
    public Tile(
            int id,
            int x,
            int y,
            Tribe tribe,
            int vid,
            String village,
            int uid,
            String player,
            int aid,
            String alliance,
            int population) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tribe = tribe;
        this.vid = vid;
        this.village = village;
        this.uid = uid;
        this.player = player;
        this.aid = aid;
        this.alliance = alliance;
        this.population = population;
    }
    
    /**
     * Creates a nature unoccupied tile at the given position.
     * 
     * @param id tile id
     * @param x  tile x coordinate
     * @param y  tile y coordinate
     */
    public Tile(int id, int x, int y) {
        this(id, x, y, Tribe.NATURE, 0, "", 0, "", 0, "", 0);
    }
    
    // ---------------------------------------------------------- Public methods
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAlliance() {
        return alliance;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
    
    public String toString() {
        return toString("[%d,%d,%d,%s,%d,%s,%d,%s,%d,%s,%d]");        
    }
    
    /**
     * Returns a string representation of this Tile formatted with the given
     * format string. The fields are accessed by arguments in the following 
     * order:
     *   id, x, y, tribe, vid, village, uid, player, aid, alliance, population
     * 
     * @param format a format string (as used by String.format())
     * 
     * @return the string representation of this Tile
     */
    public String toString(final String format) {
        return String.format(
            format,
            id,
            x, y,
            tribe.getName(),
            vid, village,
            uid, player,
            aid, alliance,
            population
        );
    }
}
