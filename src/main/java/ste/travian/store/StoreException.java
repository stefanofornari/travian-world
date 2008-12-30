/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.store;

import ste.travian.TravianException;

/**
 * This Exception represents a WorldStore exception.
 * 
 * @author ste
 */
public class StoreException extends TravianException {
    public StoreException (String msg, Throwable t) {
        super(msg, t);
    }
}
