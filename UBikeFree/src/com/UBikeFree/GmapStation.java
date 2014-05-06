package com.UBikeFree;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.UBikeFree.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.StationInformation.Stations;
import com.StationInformation.UBStation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;

public class GmapStation extends FragmentActivity  {
 
    // Google Map
    private GoogleMap obj_GoogleMap;
    private HashMap<String,Marker> obj_MarkInformation;
    private final ScheduledExecutorService Obj_Trigger =Executors.newSingleThreadScheduledExecutor();

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_information);

        try {
            // Loading map
            this.InitilizeMap();
 
        } catch (Exception obj_ex) {
        	obj_ex.printStackTrace();
        }
 
    }
 
 
    private void InitilizeMap() {
        if (obj_GoogleMap == null) {
        	obj_GoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.gmap)).getMap();
        	LatLng obj_TaipeiMap = new LatLng(25.052401, 121.543915);//25.052401, 121.543915
        	obj_GoogleMap.setMyLocationEnabled(true);
        	obj_GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(obj_TaipeiMap, 13));
        	// ROSE color icon        	
        	try{
	        	Stations obj_Station =new Stations();
	        	this.obj_MarkInformation=new HashMap<String,Marker>();
	    		for(UBStation obj_UbiKeStation : obj_Station){
	    			//.title(UBStationtemp.getName())
                    //.snippet(("可借"+ UBStationtemp.getBikes()+ "空車位" + UBStationtemp.getEmptySlots()));	    			//
	    			LatLng obj_Position=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
	    			MarkerOptions obj_Mark = new MarkerOptions().position(obj_Position)
	    									.title(obj_UbiKeStation.getName())
	    									.snippet(("可借:"+ obj_UbiKeStation.getBikes()+ "/空車位:" + obj_UbiKeStation.getEmptySlots()))
	    									.alpha(0.6F);	    			
	    			String str_Key=String.valueOf(obj_UbiKeStation.getLat())+"/"+String.valueOf(obj_UbiKeStation.getLng());	    			
	    			// ROSE color icon
	    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	    			// adding marker
	    			Marker obj_MarkObject=obj_GoogleMap.addMarker(obj_Mark);
	    			this.obj_MarkInformation.put(str_Key,obj_MarkObject);	    			    		
	    		}
	    		//this.ReloadData();	    		
        	}
        	catch(Exception obj_Ex){}
        }
    }
    
    //Trigger for reloading
    public void ReloadData(){
    	Runnable obj_Task=new Runnable(){
    		public void run(){
//    			Iterator obj_Iterate= obj_MarkInformation.entrySet().iterator(); 
//    			while (obj_Iterate.hasNext()) { 
//    			    Map.Entry entry = (Map.Entry) obj_Iterate.next(); 
//    			    String str_Key = (String)entry.getKey(); 
//    			    Marker obj_MarkerObject = (Marker)entry.getValue();
//    			    String str_Id=obj_MarkerObject.getId();
//    			    obj_MarkerObject.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//    			} 
    			System.out.println("beep");
    		}    		
    	};
    	Obj_Trigger.schedule(obj_Task, 5, TimeUnit.SECONDS);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.InitilizeMap();
    }
    
    
}

