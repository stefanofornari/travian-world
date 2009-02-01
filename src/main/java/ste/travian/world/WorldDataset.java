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
package ste.travian.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author ste
 */
public class WorldDataset 
implements XYDataset {
    
    private List<List<Tile>> data;
    private List<String> keys;
    private Map<String, ArrayList<String>> allianceGroups;
    private List<DatasetChangeListener> listeners;
    private DatasetGroup group;
    
    public WorldDataset() {
        initialize();
    }
    
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the order of the domain (or X) values returned by the dataset.
     */
    public DomainOrder getDomainOrder() {
        return DomainOrder.NONE;
    }
    
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the number of items in a series.
     */
    public int getItemCount(int series) {
        checkSeries(series);
        
        return data.get(series).size();
   
    }
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the x-value for an item within a series
     */
    public double getXValue(int series, int item) {
        return getTile(series, item).getX();
    }
    
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the x-value for an item within a series.
     */
    public double getYValue(int series, int item) {
        return (double)getTile(series, item).getY();
    }
    
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the x-value for an item within a series
     */
    public Number getX(int series, int item) {
        return new Integer((int)getXValue(series, item));
    }
    
    /**
     * @see org.jfree.data.xy.XYDataset
     * 
     * @return the x-value for an item within a series.
     */
    public Number getY(int series, int item) {
        return new Integer((int)getYValue(series, item));
    }
    
    /**
     * @see org.jfree.data.general.SeriesDataset
     * 
     * @return the number of series in the dataset
     */
    public int getSeriesCount() {
        return data.size();
    }
    
    /**
     * @see org.jfree.data.general.SeriesDataset
     * 
     * @return the key for the series
     */
    public Comparable getSeriesKey(int series) {
        checkSeries(series);
        
        return keys.get(series);
    }
    
    /**
     * @see org.jfree.data.general.SeriesDataset
     * 
     * @return the key for the series
     */
    public int indexOf(Comparable key) {
        if (allianceGroups != null) {
            int i = 0;
            for(String groupName: allianceGroups.keySet()) {
                if (allianceGroups.get(groupName).contains(key)) {
                    return i;
                }
                ++i;
            }
        }

        return 0;
    }
    
    /**
     * @see org.jfree.data.general.Dataset
     * @param listener
     */
    public void addChangeListener(DatasetChangeListener listener) {
        checkListener(listener);
        
        listeners.add(listener);
    }
    
    /**
     * @see org.jfree.data.general.Dataset
     * @param listener
     */
    public void removeChangeListener(DatasetChangeListener listener) {
        checkListener(listener);
        
        listeners.remove(listener);
    }
    
    /**
     * @see org.jfree.data.general.Dataset
     * 
     * @return
     */
    public DatasetGroup getGroup() {
        return group;
    }

    /**
     * @see org.jfree.data.general.Dataset
     * 
     * @param group
     */
    public void setGroup(DatasetGroup group) {
        this.group = group;
    }
    
    /**
     * Returns the tile corresponding to the given series and item
     * 
     * @param series
     * @param item
     * 
     * @returnthe tile corresponding to the given series and item
     */
    public Tile getTile(int series, int item) {
        checkSeriesItem(series, item);
        
        return data.get(series).get(item);
    }
    
    /**
     * Initializes the dataset with the values in the world map
     * 
     * @param world a World object containing the map
     */
    public void initialize(World world) {
        if (world.isEmpty()) {
            //
            // just a shortcut if there is nothing to do...
            //
            return;        
        }

        clearSeries();

        allianceGroups = world.getAllianceGroups();
        if (allianceGroups != null) {
            for(String groupName: allianceGroups.keySet()) {
                keys.add(groupName);
                data.add(new ArrayList<Tile>());
            }
        }
        
        for (int y=world.getMinY(); y<=world.getMaxY(); ++y) {
            for (int x=world.getMinX(); x<=world.getMaxY(); ++x) {
                Tile t = world.getTile(x, y);
                if (t != null) {
                    int allianceIndex = indexOf(t.getAlliance());
                    data.get(allianceIndex).add(t);
                }
            }
        }
    }
    
    public String[] getKeys() {
        return keys.toArray(new String[keys.size()]);
    }

    public DatasetChangeListener[] getChangeListeners() {
        return listeners.toArray(new DatasetChangeListener[listeners.size()]);
    }

            
            
    // ------------------------------------------------------- Protected methods
    
    /**
     * Checks if a given series parameter is good
     * 
     * @param series the value to check
     * 
     * @throws java.lang.IllegalArgumentException the given series in not acceptable
     */
    protected void checkSeries(int series) throws IllegalArgumentException {
        if ((series<0) || (series>=data.size())) {
            throw new IllegalArgumentException("series (" + series + ") must be >=0 and <" + data.size());
        }
    }
    
    /**
     * Checks if a given series,item parameters are good
     * 
     * @param series the series value to check
     * @param item the item value to check
     * 
     * @throws java.lang.IllegalArgumentException the given series,item are not 
     *         acceptable for this WorldDataset
     */
    protected void checkSeriesItem(int series, int item) throws IllegalArgumentException {
        checkSeries(series);
        if ((item<0) || (item>=data.get(series).size())) {
            throw new IllegalArgumentException("item (" + item + ") must be >=0 and <" + data.size());
        }
    }
    
    /**
     * Checks if a given DatasetChangeListener is not null
     * 
     * @param listener value to check
     * 
     * @throws java.lang.IllegalArgumentException if listener is invalid
     */
    protected void checkListener(DatasetChangeListener listener) 
    throws IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("listener must be not null");
        }
    }
    
    // --------------------------------------------------------- Private methods
    
    /**
     * Initialize the dataset with default (and empty) values
     * 
     */
    private void initialize() {
        initializeSeries();
        initializeKeys();
  
        listeners = new ArrayList<DatasetChangeListener>();
        group = null;
    }
    
    /**
     * Initializes the series keys
     * 
     */
    private void initializeKeys() {
        keys = new ArrayList<String>();       
    }
    
    /**
     * Initializes the dat series
     */
    private void initializeSeries() {
        data = new ArrayList<List<Tile>>();
    }
    
    /**
     * Remove all items in each series
     */
    private void clearSeries() {
        Iterator<List<Tile>> i = data.iterator();
        while(i.hasNext()) {
            i.next().clear();
        }
    }
    
}
