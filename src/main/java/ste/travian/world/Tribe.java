/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    private int tid;
    
    Tribe(int tid) {
        this.tid = tid;
    }
    
    public int getId() {
        return tid;
    }
    
    public String getName() {
        if (tid == 1) {
            return "Roman";
        } else if (tid == 2) {
            return "Teuton";
        } else if (tid == 3) {
            return "Gaul";
        } else if (tid == 4) {
            return "Nature";
        } else if (tid == 5) {
            return "Natars";
        }
        
        return "";
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
