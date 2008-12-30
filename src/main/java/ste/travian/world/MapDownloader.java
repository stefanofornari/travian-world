/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ste.travian.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author ste
 */
public class MapDownloader {
    
    private URL mapURL;
    
    public MapDownloader(final String url) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        
        try {
            mapURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("'" + url + "' is not a valid URL (" + e + ")");
        }
    }
    
    /**
     * Retrieves the content of the URL and returns it as a String
     * 
     * @return the content of the URL as a String
     * 
     * @throws java.io.IOException
     */
    public String downloadMapAsString() throws IOException {
        StringBuffer map = new StringBuffer();
        
        BufferedReader r = this.downloadMapAsReader();
        String line = null;
        while ((line = r.readLine()) != null) {
            map.append(line);
        }
        
        r.close();
        
        return map.toString();
        
    }
    
    /**
     * Retrieves the content of the URL as an InputStream
     * @return the input stream from which read the content of the URL
     * 
     * @throws java.io.IOException
     */
    public InputStream downloadMapAsStream() throws IOException {
        return mapURL.openStream();
    }
    
    public BufferedReader downloadMapAsReader() throws IOException {
        return  new BufferedReader(
                    new InputStreamReader(
                        new GZIPInputStream(downloadMapAsStream())
                    )
                );
    }

}
