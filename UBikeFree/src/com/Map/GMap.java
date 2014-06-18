//===========================================================
//	Author		:	Nick Liao
//	Date		:	20140514
//	Description	:	Map Setting
package com.Map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.Resource.EnvironmentSource;
import com.StationInformation.Stations;
import com.StationInformation.UBStation;
import com.StationInformation.NearStationObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GMap {
																						//Google Map
	private Context					obj_ContextFromActivity;
	private Double 					db_Lat;
    private Double 					db_Lng;
    private	EnvironmentSource		obj_Environment;
	private GoogleMap				obj_GoogleMap;										//Google Map Variable    
	private LatLng					obj_TaipeiMap;										//Lat Lng Position
    private	Stations				obj_Station;										//Stations' Information Object
    private boolean 				bool_IsMoveCamera;
    public  boolean					bool_IsShownNearStation;
    private	static	final	String 	ststr_Activity="GMap.java";
    
    
    private ArrayList<NearStationObject>		obj_NearStationList;
    private ArrayList<Marker> markers;//record markers
    private ArrayList<Marker> 					obj_LessInformationMarkers;//record markers
    private String lastClickedStation;//record the last time clicked marker, for re-shown after updated the map
    
    																					//Construct of class
    public	GMap(EnvironmentSource obj_Environment, Context obj_ContextFromActivity, GoogleMap obj_GoogleMap){
    	this.bool_IsMoveCamera						=true;
    	this.bool_IsShownNearStation				=false;
    	this.obj_Environment						=obj_Environment;
    	this.obj_ContextFromActivity				=obj_ContextFromActivity;
    	this.obj_GoogleMap							=obj_GoogleMap;
    	try{
    		obj_Station								=new Stations(this.obj_Environment);
    	}
    	catch(MalformedURLException obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());    		
    	}
    	this.PositionSetting();

    	//list for Markers
    	markers = new ArrayList<Marker>();
    	obj_LessInformationMarkers	=new ArrayList<Marker>();
    	
    	//override onMarkerClickListener
    	//record the last clicked marker
    	//for showing it since updated the map
    	obj_GoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				lastClickedStation = marker.getTitle();
				marker.showInfoWindow();
				return true;
			}
    	});
    }
    
    public void MapDisplay(){
    	try {																			//Load Map into Fragment
            this.MapSetting();
            
        } 
        catch (Exception obj_Ex) {
        	//obj_ex.printStackTrace();
        	Log.e(ststr_Activity,obj_Ex.getMessage());
        }    	
    }	
    																					//Map Setting: default Lat Lng are 25.052401, 121.543915
    private void MapSetting() {
    	try{        	
    		this.MapCameraSetting();
    		for(UBStation 		obj_UbiKeStation 	: obj_Station){    			
    			LatLng			obj_Position		=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
    			MarkerOptions 	obj_Mark			=new MarkerOptions().position(obj_Position)
    																	.title(obj_UbiKeStation.getName())
    																	.snippet(("可借："+ obj_UbiKeStation.getBikes()+ "空車位：" + obj_UbiKeStation.getEmptySlots()))
    																	.alpha(Float.parseFloat(this.obj_Environment.SearchValue("GMap/Alpha")));    				    			    
    			
    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(
    					this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
    			));
    																					//Adding markers for station
    			Marker tempMarker = this.obj_GoogleMap.addMarker(obj_Mark);
    			markers.add(tempMarker); 
    			obj_LessInformationMarkers.add(tempMarker);
    		}
    		this.MarkListener();    		
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());
    	}       
    }
    
    																					//Position from GPS or pre-define
    private void PositionSetting(){
    	db_Lat						=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lat"));
    	db_Lng						=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lng"));
    	if(this.obj_TaipeiMap==null){
    		this.obj_TaipeiMap						=new LatLng(db_Lat	,db_Lng);
    	}
    	
																						//If no GPS or network, It will not work in Listener.    	
		LocationListener obj_LocationListener 		= new LocationListener() {				
			public void onLocationChanged(Location obj_Location) {            	
            	db_Lat=obj_Location.getLatitude();
            	db_Lng=obj_Location.getLongitude();
            	try{
            		obj_TaipeiMap					=new LatLng(db_Lat, db_Lng);
            		if(bool_IsMoveCamera==true){
            			MapCameraSetting();
            			bool_IsMoveCamera			=false;
            		}
            		//Calculate the Nearest Markers
            		if(bool_IsShownNearStation ==true){
            			NearestStation(obj_TaipeiMap);
            		}
            		
//            		Log.i("FetchNowLatInFirstTime",String.valueOf(db_Lat));
//            		Log.i("FetchNowLngInFirstTime",String.valueOf(db_Lng));
            	}
            	catch(Exception obj_Ex){
            		Log.e(ststr_Activity,obj_Ex.getMessage());
            	}
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
		};
		LocationManager	obj_LocationManger			=(LocationManager)(this.obj_ContextFromActivity.getSystemService(Context.LOCATION_SERVICE));
    	if (obj_LocationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){    		
              obj_LocationManger.requestLocationUpdates(	LocationManager.NETWORK_PROVIDER, 
            		  										Long.parseLong(this.obj_Environment.SearchValue("GMap/SeneorRetrivalTime")), 
            		  										Float.parseFloat(this.obj_Environment.SearchValue("GMap/SeneorRetrivalDistance")), 
            		  										obj_LocationListener
            );              
    	}
    	else{
    		Toast.makeText(this.obj_ContextFromActivity, "請打開您的GPS服務", Toast.LENGTH_SHORT).show();			
    	}
	}    
    																					//Trigger for reloading station information
    public void StationDataReload(boolean bool_Update){ 
    	if(bool_Update==true){
    		markers.clear();    	
    	}
    																					//Re-do the json package again
    	try{    		
    		this.obj_GoogleMap.clear();
    		if(bool_Update==true){
    			this.obj_Station.update(this.obj_Environment);
    		}
    		for(UBStation 		obj_UbiKeStation 	: this.obj_Station){    			
    			LatLng			obj_Position		=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
    			MarkerOptions 	obj_Mark			=new MarkerOptions().position(obj_Position)
    																	.title(obj_UbiKeStation.getName())
    																	.snippet(("可借："+ obj_UbiKeStation.getBikes()+ "空車位：" + obj_UbiKeStation.getEmptySlots()))
    																	.alpha(Float.parseFloat(this.obj_Environment.SearchValue("GMap/Alpha")));    				    			    
    			
    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(
    					this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
    			));
    																					//Adding markers for station
    			Marker tempMarker = this.obj_GoogleMap.addMarker(obj_Mark);
    			if(bool_Update==true){
    				markers.add(tempMarker);
    			}
    		}
    		
    		//re-show clicked station marker
    		if(bool_Update==true){
    			showMarkerOption(lastClickedStation);
    		}
    		//re-show the nearest markers
    		if(this.bool_IsShownNearStation==true){
    			NearestStation(this.obj_TaipeiMap);    			
    		}
    	}
    	catch(Exception obj_Ex){
    		//Log.e(ststr_Activity,obj_Ex.getMessage());
    		Log.e("GMap.StationDataReload", "Exception" + obj_Ex);
    	}    						
    }
    
    																					//Mark color picker
    private float MarkColorPicker(Double Divisor, Double Dividend){
    	float	flt_Color		=BitmapDescriptorFactory.HUE_AZURE;
    	Double	db_Result		=0.0;
    	try{
    		if((Divisor+Dividend)==0.0){
    			db_Result=-1.0;    			
    		}
    		else{
    			db_Result=(Divisor/(Divisor+Dividend));
    		}
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());
    	}
																						//Define different color to different amount of bikes    	
    	if(db_Result>=0	&&	db_Result<0.2){
    		flt_Color	=BitmapDescriptorFactory.HUE_RED;
    	}
    	else if(db_Result>=0.2	&&	db_Result<0.4){
    		flt_Color	=BitmapDescriptorFactory.HUE_ORANGE;   	
    	}
    	else if(db_Result>=0.4	&&	db_Result<0.7){
    		flt_Color	=BitmapDescriptorFactory.HUE_YELLOW;    	
    	}
    	else if(db_Result>=0.7	&&	db_Result<=1){
    		flt_Color	=BitmapDescriptorFactory.HUE_GREEN;    		
    	}
    	else{
    		flt_Color	=BitmapDescriptorFactory.HUE_AZURE;    		
    	}
    	return flt_Color;    	
    }
    
    public void WakeUpCamera(){
    	this.bool_IsMoveCamera					=true;    	
    }
    																					//Define Camera
    private void MapCameraSetting(){
    	this.obj_GoogleMap.setMyLocationEnabled(true);    	
    	this.obj_GoogleMap.moveCamera(	CameraUpdateFactory.newLatLngZoom(obj_TaipeiMap,
    								Float.parseFloat(this.obj_Environment.SearchValue("GMap/Zoom"))));    	
    }
    
    public void MapCameraSetting(LatLng positionOnMap){
    	this.obj_GoogleMap.setMyLocationEnabled(true);    	
    	this.obj_GoogleMap.moveCamera(	CameraUpdateFactory.newLatLngZoom(positionOnMap,
    								Float.parseFloat(this.obj_Environment.SearchValue("GMap/Zoom")))); 
    }
    
    
    public String[] getStationNames() {
    	return obj_Station.getStationNames();
    }
    
    public Float[] getStationLongitudes() {
    	return obj_Station.getStationLongitudes();
    }
    
    public Float[] getStationLatitudes() {
    	return obj_Station.getStationLatitudes();
    }
    
    public ArrayList<UBStation> getStationList() {
    	return obj_Station.getStationList();
    }
    
    
    public void showMarkerOption(String stationName) {
    	
    	if(markers.size() > 0) {
    	
	    	for(Marker marker : markers) {
	    		if(marker.getTitle().compareTo(stationName)==0) {
	    			marker.showInfoWindow();
	    			lastClickedStation = stationName;
	    			break;
	    		}
	    	}
    	}
    }
    
    public ArrayList<UBStation> getSortedStationList() {
    	return obj_Station.getSortedStationListByKey();
    }
    
    
    private void MarkListener(){
    	this.obj_GoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
    	    @Override 
    	    public boolean onMarkerClick(Marker marker) {
    	        //  Take some action here
    	    	marker.showInfoWindow();
    	    	
    	    	Log.i("Marklat",String.valueOf(marker.getPosition().latitude)+String.valueOf(marker.getPosition().longitude));
    	        return true;
    	    }
    	    
    	    
    	});
    }
    
   

    public void NearestStation(LatLng obj_Position){ 
    	this.obj_NearStationList=null;
    	this.obj_NearStationList=new ArrayList<NearStationObject>();
    	if(this.obj_LessInformationMarkers.size()>0){
    		LatLng obj_MarkPosition;
    		for(Marker marker : this.obj_LessInformationMarkers) {
    			obj_MarkPosition	=marker.getPosition();
    			double db_Distance	=Math.abs(obj_Position.latitude-obj_MarkPosition.latitude)+Math.abs(obj_Position.longitude-obj_MarkPosition.longitude);
    			if(db_Distance<0.05){
    				this.obj_NearStationList.add(new NearStationObject(marker,db_Distance));
    				//Log.i("information",String.valueOf(db_Distance));    				
    			}
    			else{
    				//Log.i("information",String.valueOf(db_Distance));      				
    			}
	    	}
    		//sort the Nearest TopK Station
    		Collections.sort(this.obj_NearStationList,new Comparator<NearStationObject>(){	
    				@Override
	    			public int compare(NearStationObject obj_Object1, NearStationObject obj_Object2) {
	    		        return Double.compare(obj_Object1.db_Distance,obj_Object2.db_Distance);
	    		    }
    		});
    		this.obj_GoogleMap.clear();
    		for(UBStation 		obj_UbiKeStation 	: this.obj_Station){
	    		for(int i=0;(i<this.obj_NearStationList.size()&& i<5);i++){
	    			NearStationObject obj_NearStation		=this.obj_NearStationList.get(i);
	    			if(obj_UbiKeStation.getName().equals(obj_NearStation.obj_StationMarkers.getTitle())){
		    			LatLng			obj_Pos				=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
		    			MarkerOptions 	obj_Mark			=new MarkerOptions().position(obj_Pos)
		    																	.title(obj_UbiKeStation.getName())
		    																	.snippet(("可借："+ obj_UbiKeStation.getBikes()+ "空車位：" + obj_UbiKeStation.getEmptySlots()))
		    																	.alpha(Float.parseFloat(this.obj_Environment.SearchValue("GMap/Alpha")));    				    			    
		    			
		    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(
		    					this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
		    			));
		    			this.obj_GoogleMap.addMarker(obj_Mark);
	    			}
    			}    																				       		
    		}    		
    	}   	
    } 
}
