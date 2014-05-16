package com.StationInformation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Resource.EnvironmentSource;

public class Stations implements Iterable<UBStation>
{
    private URL url_;
    private TreeMap<String, UBStation> staMap_; 
    private Date obj_Now;

    public Stations() throws MalformedURLException{
    	//WebService的內容會因網址相同而有可能傳同樣的東西，所以必須要有改變
    	url_ = new URL("http://210.69.61.60:8080/you/gwjs_cityhall.json");      
        staMap_ = new TreeMap<String, UBStation>();
        update();
    }
    public Stations(EnvironmentSource obj_Environment)throws MalformedURLException{    	    	    	
        staMap_ = new TreeMap<String, UBStation>();
        update(obj_Environment);
    }

    public void RetriveJsonData(EnvironmentSource obj_Environment)throws MalformedURLException{
    	    	
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
                staMap_.put(sta.getArea() + "_" + sta.getId(), sta);
            } catch(Exception ex) {            	
            }
        }
    }
    
    public void update(EnvironmentSource obj_Environment)throws MalformedURLException{
    	obj_Now = new Date();
    	String str_Parameter	=new SimpleDateFormat("yyyyMMddHHmmss").format(obj_Now);
    	String str_URL			=obj_Environment.SearchValue("StationInformation/Url");
    	str_URL					+=("?ran="+str_Parameter);
        url_ 					=new URL(str_URL);
        JSONArray jsonData		=getJStations_();
        for (int i = 0; i < jsonData.length(); ++i) {
            try {
                JSONObject jStation		=jsonData.getJSONObject(i);
                UBStation sta			=new UBStation(jStation);
                staMap_.put(sta.getArea() + "_" + sta.getId(), sta);
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
    
    private <V> SortedMap<String, V> filterPrefix(SortedMap<String,V> baseMap, String prefix) {
        if(prefix.length() > 0) {
            char nextLetter = (char)(prefix.charAt(prefix.length() -1) + 1);
            String end = prefix.substring(0, prefix.length()-1) + nextLetter;
            return baseMap.subMap(prefix, end);
        }
        return baseMap;
    }
    
    public Set<Map.Entry<String, UBStation>> getStationsOfArea(String area) {
    	return filterPrefix(staMap_, area).entrySet();
    }
    
    public String[] getStationNames() {
    	ArrayList<String> names = new ArrayList<String>();
    	
    	for(UBStation station : staMap_.values()) {
    		names.add(station.getName());
    	}
    	
    	String[] nameArray = new String[names.size()];
    	for(int index = 0; index < names.size(); ++index) {
    		nameArray[index] = names.get(index);
    	}
    	
    	return nameArray;
    }
}

