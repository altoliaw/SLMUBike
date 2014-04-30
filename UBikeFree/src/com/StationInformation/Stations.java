package com.StationInformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.lang.Iterable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Stations implements Iterable<UBStation>
{
    private final URL url_;
    private TreeMap<String, UBStation> staMap_;

    public Stations() throws MalformedURLException
    {
    	url_ = new URL("http://210.69.61.60:8080/you/gwjs_cityhall.json");
        staMap_ = new TreeMap<String, UBStation>();
    }

    public Iterator<UBStation> iterator()
    {
        return staMap_.values().iterator();
    }

    public UBStation get(String key)
    {
        return staMap_.get(key);
    }

    public void update()
    {
        JSONArray jsonData = getJStations_();
        for (int i = 0; i < jsonData.length(); ++i) {
            try {
            	JSONObject jStation = jsonData.getJSONObject(i);
                UBStation sta = new UBStation(jStation);
                staMap_.put(sta.getId(), sta);
            } catch(Exception ex) {

            }
        }
    }

    private InputStream getJStream_() throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url_.openConnection();
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream(); 
    }

    private String StreamToString_(InputStream is) throws IOException
    {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int nbRead;
        byte[] buffer = new byte[1024];
        while ((nbRead = is.read(buffer)) != -1) {
          baos.write(buffer, 0, nbRead);
        }
        
        return new String(baos.toByteArray(), "UTF-8");
    }

    private JSONArray getJStations_()
    {
        try {
            InputStream jStream = getJStream_();
            String jString = StreamToString_(jStream);
            JSONObject jsonData = new JSONObject(jString);
            return jsonData.getJSONArray("retVal");
        } catch (Exception ex) {
            return new JSONArray();
        }
    }
}

