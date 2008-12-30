/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
