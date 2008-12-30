/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.world;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author ste
 */
public class TileToolTipGenerator
implements XYToolTipGenerator {
    public TileToolTipGenerator() {
        
    }
    
    /**
     * 
     */
    public String generateToolTip(XYDataset dataset, int series, int item) {
        Tile tile = ((WorldDataset)dataset).getTile(series, item);
        if (tile != null) {
            return tile.toString(
                "<html>"                           +
                "<center><b>%6$s</b></center><hr>" +
                "<b>Position:</b> (%2$d,%3$d)<br>" +
                "<b>Tribe:</b> %4$s<br>"           +
                "<b>User:</b> %8$s<br>"            +
                "<b>Alliance:</b> %10$s<br>"       +
                "<b>Population:</b> %11$d"         +
                "</html>"
            );
        }
        
        return "null";

    }
}
