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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author ste
 */
public class WorldChart {
    
    public static final String LABEL_X_AXIS = "X";
    public static final String LABEL_Y_AXIS = "Y";

    JFreeChart chart;
    World world;

    public WorldChart(final String title,
            final World world) {
        if (world == null) {
            throw new IllegalArgumentException("world cannot be null!");
        }

        this.world = world;

        createWorldPlot(title, createDataset());

    }
    
    /**
     * Returns the chart as created in the constructor
     * 
     * @return the created chart
     */
    public JFreeChart getChart() {
        return chart;
    }

    // --------------------------------------------------------- Private methods
    /**
     * Create the JFreeChart world chart
     * 
     * @param title world title
     * @param dataset world data set
     * 
     */
    private void createWorldPlot(String title,  XYDataset dataset) {

        NumberAxis xAxis = new NumberAxis(LABEL_X_AXIS);
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(LABEL_Y_AXIS);
        yAxis.setAutoRangeIncludesZero(false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

        TileRenderer renderer = new TileRenderer(3, 3);
        renderer.setBaseToolTipGenerator(new TileToolTipGenerator());
        
        plot.setRenderer(renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }
    
    /**
     * Creates a XYDataset suitable to plot the map
     * 
     * @return the created dataset
     */
    private XYDataset createDataset() {
        assert (world != null) : "world must not be null when createDataset is called";
        
        WorldDataset dataset = new WorldDataset();
        dataset.setKeys(new String[] {"War Hero"});
        dataset.initialize(world);
        
        return dataset;
    }
}
