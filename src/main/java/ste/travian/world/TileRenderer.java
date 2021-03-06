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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

/**
 * A renderer that draws a small dot at each data point for an {@link XYPlot}.
 */
public class TileRenderer extends AbstractXYItemRenderer 
                           implements XYItemRenderer, 
                                      Cloneable,
                                      PublicCloneable,
                                      Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -2764344339073566425L;
    
    /** The dot width. */
    private int dotWidth;
    
    /** The dot height. */
    private int dotHeight;
    
    /** 
     * The shape that is used to represent an item in the legend. 
     * 
     * @since 1.0.7
     */
    private transient Shape legendShape;

    /**
     * Constructs a new renderer.
     */
    public TileRenderer(int dotWidth, int dotHeight) {
        super();
        this.dotWidth = dotWidth;
        this.dotHeight = dotHeight;
        this.legendShape = new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0);
    }
    
    public TileRenderer() {
        this(1,1);
    }

    /**
     * Returns the dot width (the default value is 1).
     * 
     * @return The dot width.
     * 
     * @since 1.0.2
     * @see #setDotWidth(int)
     */
    public int getDotWidth() {
        return this.dotWidth;
    }
    
    /**
     * Sets the dot width and sends a {@link RendererChangeEvent} to all 
     * registered listeners.
     * 
     * @param w  the new width (must be greater than zero).
     * 
     * @throws IllegalArgumentException if <code>w</code> is less than one.
     * 
     * @since 1.0.2
     * @see #getDotWidth()
     */
    public void setDotWidth(int w) {
        if (w < 1) {
            throw new IllegalArgumentException("Requires w > 0.");
        }
        this.dotWidth = w;
        fireChangeEvent();
    }
    
    /**
     * Returns the dot height (the default value is 1).
     * 
     * @return The dot height.
     * 
     * @since 1.0.2
     * @see #setDotHeight(int)
     */
    public int getDotHeight() {
        return this.dotHeight;
    }
    
    /**
     * Sets the dot height and sends a {@link RendererChangeEvent} to all 
     * registered listeners.
     * 
     * @param h  the new height (must be greater than zero).
     * 
     * @throws IllegalArgumentException if <code>h</code> is less than one.
     * 
     * @since 1.0.2
     * @see #getDotHeight()
     */
    public void setDotHeight(int h) {
        if (h < 1) {
            throw new IllegalArgumentException("Requires h > 0.");
        }
        this.dotHeight = h;
        fireChangeEvent();
    }
    
    /**
     * Returns the shape used to represent an item in the legend.
     * 
     * @return The legend shape (never <code>null</code>).
     * 
     * @see #setLegendShape(Shape)
     * 
     * @since 1.0.7
     */
    public Shape getLegendShape() {
        return this.legendShape;   
    }
    
    /**
     * Sets the shape used as a line in each legend item and sends a 
     * {@link RendererChangeEvent} to all registered listeners.
     * 
     * @param shape  the shape (<code>null</code> not permitted).
     * 
     * @see #getLegendShape()
     * 
     * @since 1.0.7
     */
    public void setLegendShape(Shape shape) {
        if (shape == null) {
            throw new IllegalArgumentException("Null 'shape' argument.");   
        }
        this.legendShape = shape;
        fireChangeEvent();
    }

    /**
     * Draws the visual representation of a single data item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the area within which the data is being drawn.
     * @param info  collects information about the drawing.
     * @param plot  the plot (can be used to obtain standard color 
     *              information etc).
     * @param domainAxis  the domain (horizontal) axis.
     * @param rangeAxis  the range (vertical) axis.
     * @param dataset  the dataset.
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     * @param crosshairState  crosshair information for the plot 
     *                        (<code>null</code> permitted).
     * @param pass  the pass index.
     */
    public void drawItem(Graphics2D g2,
                         XYItemRendererState state,
                         Rectangle2D dataArea,
                         PlotRenderingInfo info,
                         XYPlot plot,
                         ValueAxis domainAxis,
                         ValueAxis rangeAxis,
                         XYDataset dataset,
                         int series,
                         int item,
                         CrosshairState crosshairState,
                         int pass) {

        // get the data point...
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double adjx = (this.dotWidth - 1) / 2.0;
        double adjy = (this.dotHeight - 1) / 2.0;
        if (Double.isNaN(y)) {
            return;
        }
        
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX = domainAxis.valueToJava2D(x, dataArea, 
                xAxisLocation) - adjx;
        double transY = rangeAxis.valueToJava2D(y, dataArea, yAxisLocation) 
                - adjy;
        
        g2.setPaint(getItemPaint(series, item));
        if (orientation == PlotOrientation.HORIZONTAL) {
            g2.fillRect((int) transY, (int) transX, this.dotHeight, 
                    this.dotWidth);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            g2.fillRect((int) transX, (int) transY, this.dotWidth, 
                    this.dotHeight);
        }

        int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
        int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
        updateCrosshairValues(crosshairState, x, y, domainAxisIndex, 
                rangeAxisIndex, transX, transY, orientation);
        
        // add an entity for the item, but only if it falls within the data
        // area...
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        int xx = (int)transX;
        int yy = (int)transY;
        if (orientation == PlotOrientation.HORIZONTAL) {
            xx = (int)transY;
            yy = (int)transX;
        }    
        if (entities != null && dataArea.contains(xx, yy)) {
            addEntity(entities, null, dataset, series, item, (int)xx, (int)yy);
        }

    }


    /**
     * Returns a legend item for the specified series.
     *
     * @param datasetIndex  the dataset index (zero-based).
     * @param series  the series index (zero-based).
     *
     * @return A legend item for the series (possibly <code>null</code>).
     */
    public LegendItem getLegendItem(int datasetIndex, int series) {

        // if the renderer isn't assigned to a plot, then we don't have a
        // dataset...
        XYPlot plot = getPlot();
        if (plot == null) {
            return null;
        }

        XYDataset dataset = plot.getDataset(datasetIndex);
        if (dataset == null) {
            return null;
        }

        LegendItem result = null;
        if (getItemVisible(series, 0)) {
            String label = getLegendItemLabelGenerator().generateLabel(dataset,
                    series);
            String description = label;
            String toolTipText = null;
            if (getLegendItemToolTipGenerator() != null) {
                toolTipText = getLegendItemToolTipGenerator().generateLabel(
                        dataset, series);
            }
            String urlText = null;
            if (getLegendItemURLGenerator() != null) {
                urlText = getLegendItemURLGenerator().generateLabel(
                        dataset, series);
            }
            Paint fillPaint = lookupSeriesPaint(series);
            result = new LegendItem(label, description, toolTipText, urlText, 
                    getLegendShape(), fillPaint);
            result.setSeriesKey(dataset.getSeriesKey(series));
            result.setSeriesIndex(series);
            result.setDataset(dataset);
            result.setDatasetIndex(datasetIndex);
        }

        return result;

    }
    
    /**
     * Tests this renderer for equality with an arbitrary object.  This method
     * returns <code>true</code> if and only if:
     * 
     * <ul>
     * <li><code>obj</code> is not <code>null</code>;</li>
     * <li><code>obj</code> is an instance of <code>TileRenderer</code>;</li>
     * <li>both renderers have the same attribute values.
     * </ul>
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TileRenderer)) {
            return false;
        }
        TileRenderer that = (TileRenderer) obj;
        if (this.dotWidth != that.dotWidth) {
            return false;
        }
        if (this.dotHeight != that.dotHeight) {
            return false;
        }
        if (!ShapeUtilities.equal(this.legendShape, that.legendShape)) {
            return false;
        }
        return super.equals(obj);    
    }
    
    /**
     * Returns a clone of the renderer.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException  if the renderer cannot be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream) 
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.legendShape = SerialUtilities.readShape(stream);
    }
    
    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeShape(this.legendShape, stream);
    }

}
