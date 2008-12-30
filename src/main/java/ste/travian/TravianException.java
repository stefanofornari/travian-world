/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian;

/**
 *
 * @author ste
 */
public class TravianException extends Exception {
    public TravianException(String msg) {
        super(msg);
    }
    
    public TravianException (String msg, Throwable t) {
        super(msg, t);
    }
}
