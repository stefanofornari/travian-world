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

package ste.travian.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.swingx.JXDialog;
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
    private TravianWorldFrame mainWindow;
    private AllianceGroupsPanel allianceGroupsPanel;
    
    protected WorldController() {
        url = null;
        world = null;
        mainWindow = null;
        allianceGroupsPanel = null;
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
        new AllianceGroupsController().showMainWindow(mainWindow);
    }
    
    // ------------------------------------------------------ ChartMouseListener
    
    public void chartMouseClicked(ChartMouseEvent event) {
        
    }
    
    public void chartMouseMoved(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();
        if (entity == null) {
            mainWindow.statusMessage("");
            return;
        }
        if (entity instanceof XYItemEntity) {
            XYItemEntity itemEntity = (XYItemEntity)entity;
            WorldDataset dataset = (WorldDataset)itemEntity.getDataset();
            mainWindow.statusMessage(String.valueOf(dataset.getTile(itemEntity.getSeriesIndex(), itemEntity.getItem())));
        } else {
            mainWindow.statusMessage(String.valueOf(entity));
        }
    }

    public TravianWorldFrame getFrame() {
        return mainWindow;
    }

    public void showMainWindow() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainWindow = new TravianWorldFrame(WorldController.this);
                mainWindow.setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        //
        // Load the existing map
        //
        /*
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    try {
                        c.load();
                        showMap(c.getWorldPanel());
                    } catch (Exception e) {
                        error("Error creating the map", e);
                    }
                }
            }
        );
         */
        WorldController c = new WorldController();

        //
        // Tweaks for MacOS UI
        // Rememeber to add -Xdock:name="Travian world" to the command line...
        //
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "About Travian World");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
        // ---

        c.showMainWindow();

    }
}
