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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.ChartProgressEvent;
import ste.travian.world.WorldChart;

/**
 *
 * @author ste
 */
public class WorldChartPanel extends ChartPanel {

    private final TravianWorldFrame mainWindow;

    public WorldChartPanel(TravianWorldFrame mainWindow, WorldChart chart) {
        super(chart.getChart(), true, true, true, true, true);
        this.mainWindow = mainWindow;
    }

    @Override
    public void chartProgress(ChartProgressEvent event) {

        if (event.getType() == ChartProgressEvent.DRAWING_STARTED) {
            mainWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        } else {
            mainWindow.setCursor(Cursor.getDefaultCursor());
        }
    }

}
