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

import ste.travian.world.MapDownloader;
import java.io.BufferedReader;
import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author ste
 */
public class MapDownloaderTest extends TestCase {
        
    
    public static final String MAP_FILE = "src/test/resources/sql/map.sql.gz";
    
    private String mapURL;
    
    public MapDownloaderTest(String testName) throws Exception {
        super(testName);
        
        File f = new File(MAP_FILE);
        
        mapURL = "file://" + f.getAbsolutePath();
    }            

    public void testDownloadAsString() throws Throwable {
        try {
            MapDownloader downloader = new MapDownloader(null);
            fail("only not null url must be accepted");
            downloader = new MapDownloader("none://wrongurl");
            fail("only valid url must be accepted");
        } catch (IllegalArgumentException e) {
            // OK
        }
        MapDownloader downloader = new MapDownloader(mapURL);
        
        String map = downloader.downloadMapAsString();
        
        if (map.indexOf("Boondocks") < 0) {
            fail("map does not contain 'Boondocks'");
        }
    }
    
    public void testDownloadAsReader() throws Throwable {
        MapDownloader downloader = new MapDownloader(mapURL);
        
        BufferedReader map = downloader.downloadMapAsReader();
        
        String line = null;
        
        line = map.readLine();
        if (line.indexOf("horim2004") < 0) {
            fail("'horim2004' not found in map!");
        }
        
        line = map.readLine(); line = map.readLine();
        if (line.indexOf("myla") < 0) {
            fail("'myla' not found in map!");
        }
        
        line = map.readLine(); line = map.readLine(); line = map.readLine();
        if (line.indexOf("Shark") < 0) {
            fail("'Shark' not found in map!");
        }
        
        line = map.readLine(); line = map.readLine(); line = map.readLine();
        if (line.indexOf("Orc Hunter") < 0) {
            fail("'Orc Hunter' not found in map!");
        }
    }
    
    public void testDownloadKO() throws Throwable {
        boolean err = false;
        try {
          MapDownloader downloader = new MapDownloader(mapURL + ".no");
          downloader.downloadMapAsStream();
        } catch (Throwable t) {
            err = true;
        }
        
        assertEquals(true, err);
    }
}
