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

import java.awt.Cursor;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
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
    private TravianWorldFrame mainWindow;
    private AllianceGroupsPanel allianceGroupsPanel;

    private final ExecutorService executor;
    
    protected WorldController() {
        world = null;
        mainWindow = null;
        allianceGroupsPanel = null;

        executor = Executors.newCachedThreadPool();
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

        WorldUpdateWorker worker =
            new WorldUpdateWorker(mainWindow, store, downloader);
        worker.execute();
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
        
        WorldChartPanel panel = new WorldChartPanel(mainWindow, chart);
        
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

    /**
     * Shows the dialog to edit alliance groups
     */
    public void showAllianceGroupsDialog() {
        new AllianceGroupsController().showMainWindow(mainWindow);
    }

    /**
     * Shows the dialog to edit the map URL.
     */
    public void updateWorld() {
        MapUrlPanel mapUrlPanel = new MapUrlPanel();

        int result = JOptionPane.showOptionDialog(
            mainWindow,
            mapUrlPanel,
            mainWindow.getTitle(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] {"Download", "Cancel"},
            null
            );

        if (result != JOptionPane.NO_OPTION) {
            try {
                load(mapUrlPanel.getUrl());
                load();
            } catch (Exception e) {
                mainWindow.error(e.getMessage(), e);
            }
        }
    }

    public void showMainWindow() {
        mainWindow = new TravianWorldFrame(WorldController.this);
        mainWindow.setVisible(true);

        showMap();
    }

    /**
     * Shows the map chart
     */
    public void showMap() {
        mainWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        executor.execute(
            new Runnable() {
                public void run() {
                    try {
                        load();
                        mainWindow.showMap(getWorldPanel());
                        //
                        // do not move the line below, it needs to be here so
                        // that all components will have the wait cursor set
                        //
                        mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    } catch (Exception e) {
                        mainWindow.setCursor(Cursor.getDefaultCursor());
                        mainWindow.error("Error creating the map", e);
                    }
                }
            }
        );
    }
    
    // ------------------------------------------------------ ChartMouseListener
    
    public void chartMouseClicked(ChartMouseEvent event) {
        
    }
    
    public void chartMouseMoved(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();
        if (entity == null) {
            //
            // NOTE: we set the text to a space instead of an empty string to
            // avoid flickering of the UI (the JLabel disappeas when thee is no
            // text)
            //
            mainWindow.statusMessage(" ");
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

    // -------------------------------------------------------------------- main

    public static void main(String[] args) throws Exception {
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
