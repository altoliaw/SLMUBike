package com.Map;


import android.content.res.XmlResourceParser;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.StationInformation.Stations;
import com.StationInformation.UBStation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.Resource.EnvironmentSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GMap {
	// Google Map
    private HashMap<String,Marker> obj_MarkInformation;
    private EnvironmentSource obj_Environment;
    private Stations obj_Station;
    
    public GMap(EnvironmentSource obj_Environment){
    	this.obj_Environment=obj_Environment;
    	try{
    		obj_Station =new Stations(this.obj_Environment);
    	}
    	catch(Exception obj_Ex){}
    }
    
    public void GetInitialMap(GoogleMap obj_GoogleMap){
    	try {
            // Loading map
            this.InitilizeMap(obj_GoogleMap);
        } 
        catch (Exception obj_ex) {
        	obj_ex.printStackTrace();
        }    	
    }
	
    
    private void InitilizeMap(GoogleMap obj_GoogleMap) {        
    	LatLng obj_TaipeiMap = new LatLng(Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lat")), Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lng")));//25.052401, 121.543915
    	obj_GoogleMap.setMyLocationEnabled(true);
    	obj_GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(obj_TaipeiMap, Float.parseFloat(this.obj_Environment.SearchValue("GMap/Zoom"))));
    	// ROSE color icon        	
    	try{        	
        	this.obj_MarkInformation=new HashMap<String,Marker>();
    		for(UBStation obj_UbiKeStation : obj_Station){
    			//.title(UBStationtemp.getName())
                //.snippet(("可借"+ UBStationtemp.getBikes()+ "空車位" + UBStationtemp.getEmptySlots()));
    			LatLng obj_Position=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
    			MarkerOptions obj_Mark = new MarkerOptions().position(obj_Position)
    									.title(obj_UbiKeStation.getName())
    									.snippet(("可借:"+ obj_UbiKeStation.getBikes()+ "/空車位:" + obj_UbiKeStation.getEmptySlots()))
    									.alpha(0.6F);	    			
    			String str_Key=obj_UbiKeStation.getId();	    			
    			// ROSE color icon
    			
    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))));
    			// adding marker
    			Marker obj_MarkObject=obj_GoogleMap.addMarker(obj_Mark);
    			this.obj_MarkInformation.put(str_Key,obj_MarkObject);	    			    		
    		}	    			    			    		    	
    	}
    	catch(Exception obj_Ex){}       
    }
    

    //Trigger for reloading
    public void ReloadData(GoogleMap obj_GoogleMap,EnvironmentSource obj_Environment){
    	// Re-do the json package again
    	try{
    		this.obj_Station.RetriveJsonData(obj_Environment);
    	}
    	catch(Exception obj_Ex){}
    	//search the HashMap
    	for(UBStation obj_UbiKeStation : this.obj_Station){
    		String str_Key=obj_UbiKeStation.getId();
    		if(this.obj_MarkInformation.containsKey(str_Key)==true){
    			Marker obj_MarkObject= this.obj_MarkInformation.get(str_Key);
    			obj_MarkObject.remove();
    			LatLng obj_Position=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
    			MarkerOptions obj_Mark = new MarkerOptions().position(obj_Position)
									.title(obj_UbiKeStation.getName())
									.snippet(("可借:"+ obj_UbiKeStation.getBikes()+ "/空車位:" + obj_UbiKeStation.getEmptySlots()))
									.alpha(0.6F);
    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))));    			
    			obj_MarkObject=obj_GoogleMap.addMarker(obj_Mark);
    			this.obj_MarkInformation.put(str_Key,obj_MarkObject);
    		}    		
    	}
    	 
    	
//    	Iterator obj_Iterate= obj_MarkInformation.entrySet().iterator(); 
//    	while (obj_Iterate.hasNext()) { 
//    	    Map.Entry entry = (Map.Entry) obj_Iterate.next();     			    
//    	    Marker obj_MarkObject = (Marker)entry.getValue();   	
//    	    //this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
//    	    obj_MarkObject.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));    	 
//    	}    
    }
    
    //Mark color 
    private float MarkColorPicker(Double Divisor,Double Dividend){
    	float flt_Color=BitmapDescriptorFactory.HUE_AZURE;
    	Double db_Result=0.0;
    	try{
    		if((Divisor+Dividend)==0.0){
    			db_Result=-1.0;    			
    		}
    		else{
    			db_Result=(Divisor/(Divisor+Dividend));
    		}
    	}
    	catch(Exception obj_Ex){
    		
    		
    	}
    	if(db_Result>=0  && db_Result < 0.2){
    		flt_Color=BitmapDescriptorFactory.HUE_RED;
    	}
    	else if(db_Result>=0.2  && db_Result < 0.4){
    		flt_Color=BitmapDescriptorFactory.HUE_ORANGE;   	
    	}
    	else if(db_Result>=0.4  && db_Result < 0.7){
    		flt_Color=BitmapDescriptorFactory.HUE_YELLOW;    	
    	}
    	else if(db_Result>=0.7  && db_Result <= 1){
    		flt_Color=BitmapDescriptorFactory.HUE_GREEN;    		
    	}
    	else{
    		flt_Color=BitmapDescriptorFactory.HUE_AZURE;    		
    	}
    	return flt_Color;    	
    } 
    
}
