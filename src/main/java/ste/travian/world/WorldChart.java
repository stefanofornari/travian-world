/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
