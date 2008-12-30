/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.gui;

import java.io.IOException;
import java.net.URL;
import javax.swing.JPanel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import ste.travian.TravianException;
import ste.travian.store.WorldStore;
import ste.travian.world.MapDownloader;
import ste.travian.world.World;
import ste.travian.world.WorldChart;
import ste.travian.world.WorldDataset;

/**
 * Controller for actions that have to do with the World
 * @author ste
 */
public class WorldController
implements ChartMouseListener {
    
    private World world;
    private URL url;
    private TravianWorldFrame frame;
    
    protected WorldController(TravianWorldFrame frame) {
        this.frame = frame;
        url = null;
        world = null;
    }
    
    /**
     * Loads a travian map from the given URL. Note that if the store does not
     * have the world table, we create it.
     * 
     * @param url the url of the map
     * 
     * @throws ste.travian.TravianException in case of any error
     */
    public void load(URL url) throws TravianException {
        //
        // Let's initialize the store so that if it does not contain the needed 
        // tables they get created.
        //
        WorldStore store = new WorldStore();
        store.initialize();
        
        MapDownloader downloader = new MapDownloader(url.toExternalForm());
        
        try {
            store.updateWorld(downloader.downloadMapAsReader());
        } catch (IOException e) {
            throw new TravianException("Error reading the map from " + url, e);
        }
        
        this.url = url;
        world = store.getWorld();
    }
    
    /**
     * Loads a travian map from the store.
     * 
     * @throws ste.travian.TravianException in case of any error
     */
    public void load() throws TravianException {
        //
        // Let's initialize the store so that if it does not contain the needed 
        // tables they get created.
        //
        WorldStore store = new WorldStore();
        store.initialize();
        
        world = store.getWorld();
    }
    
    /**
     * Returns the JPanel containing the chart of the map
     * 
     * @return the JPanel containing the chart of the map
     */
    public JPanel getWorldPanel() {
        WorldChart chart = new WorldChart("Travian world", world);
        
        ChartPanel panel = new ChartPanel(
            chart.getChart(),
            true,
            true,
            true,
            true,
            true
        );
        
        panel.addChartMouseListener(this);
        
        return panel;
    }
    
    /**
     * Returns last world loaded
     * 
     * @return the world
     * 
     * @throws TravianException if no world has never been loaded
     */
    public World getWorld() throws TravianException {
        if (world == null) {
            throw new TravianException("No world has been loaded yet, use load(<uri>) first");
        }
        return world;
    }
    
    public void showAllianceGroupsDialog() {
        new AllianceGroupsDialog(frame).setVisible(true);
    }
    
    // ------------------------------------------------------ ChartMouseListener
    
    public void chartMouseClicked(ChartMouseEvent event) {
        
    }
    
    public void chartMouseMoved(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();
        if (entity == null) {
            frame.statusMessage("");
            return;
        }
        if (entity instanceof XYItemEntity) {
            XYItemEntity itemEntity = (XYItemEntity)entity;
            WorldDataset dataset = (WorldDataset)itemEntity.getDataset();
            frame.statusMessage(String.valueOf(dataset.getTile(itemEntity.getSeriesIndex(), itemEntity.getItem())));
        } else {
            frame.statusMessage(String.valueOf(entity));
        }
    }

    public TravianWorldFrame getFrame() {
        return frame;
    }

    public void setFrame(TravianWorldFrame frame) {
        this.frame = frame;
    }
}
