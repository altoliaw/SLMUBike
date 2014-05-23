//===========================================================
//	Author		:	Nick Liao
//	Date		:	20140514
//	Description	:	Map Setting
package com.Map;
import java.net.MalformedURLException;
import java.util.ArrayList;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMap {
																						//Google Map
	private Context					obj_ContextFromActivity;
	private Double 					db_Lat;
    private Double 					db_Lng;
    private	EnvironmentSource		obj_Environment;
	private GoogleMap				obj_GoogleMap;										//Google Map Variable    
	private LatLng					obj_TaipeiMap;										//Lat Lng Position
    private	Stations				obj_Station;										//Stations' Information Object    
    private	static	final	String 	ststr_Activity="GMap.java";
    
    private ArrayList<Circle> circles;
    
    																					//Construct of class
    public	GMap(EnvironmentSource obj_Environment, Context obj_ContextFromActivity, GoogleMap obj_GoogleMap){
    	this.obj_Environment						=obj_Environment;
    	this.obj_ContextFromActivity				=obj_ContextFromActivity;
    	this.obj_GoogleMap							=obj_GoogleMap;    	
    	try{
    		obj_Station					=new Stations(this.obj_Environment);
    		
    		
    	}
    	catch(MalformedURLException obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());    		
    	}
    	this.PositionSetting();
    	
    	//list for Circles
    	circles = new ArrayList<Circle>();
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
    			this.obj_GoogleMap.addMarker(obj_Mark);    			    		
    		}    		
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());
    	}       
    }
    
    																					//Position from GPS or pre-define
    private void PositionSetting(){
    	db_Lat						=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lat"));
    	db_Lng						=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lng"));		
		LocationListener obj_LocationListener = new LocationListener() {				
            public void onLocationChanged(Location obj_Location) {
            	db_Lat=obj_Location.getLatitude();
            	db_Lng=obj_Location.getLongitude();
            	try{
            		obj_TaipeiMap	=new LatLng(db_Lat, db_Lng);
            		MapCameraSetting();
            		Log.i("Lat",String.valueOf(db_Lat));
            		Log.i("Lng",String.valueOf(db_Lng));
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
    		Toast.makeText(this.obj_ContextFromActivity, "You can open GPS for precise locating.", Toast.LENGTH_SHORT).show();			
    	}
    	if(this.obj_TaipeiMap==null){
    		Log.i("LatOut",String.valueOf(db_Lat));
    		Log.i("LngOut",String.valueOf(db_Lng));
    		this.obj_TaipeiMap			=new LatLng(db_Lat	,db_Lng);
    	}
	}    
    																					//Trigger for reloading station information
    public void StationDataReload(){
    																					//Re-do the json package again
    	try{    		
    		this.obj_GoogleMap.clear();
    		this.obj_Station.update(this.obj_Environment);
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
    			this.obj_GoogleMap.addMarker(obj_Mark);    			    		
    		}
    		
    		//redraw circles
    		if(circles != null && circles.size() > 0) {
    			for(Circle circle : circles) {
    				this.obj_GoogleMap.addCircle(new CircleOptions()
    													.center(circle.getCenter())
    													.radius(circle.getRadius())
    													.strokeColor(circle.getStrokeColor())
    											);
    			}//end for loop
    		}//end if
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());
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
    
    public void drawCircle(LatLng location) {
    	Circle obj_circle = this.obj_GoogleMap.addCircle(new CircleOptions()
    									.center(location)
    									.radius(100)
    									.strokeColor(Color.RED));
    	
    	this.circles.add(obj_circle);
    }
    
    public void removeCircles() {
    	for(Circle circle : circles) {
    		circle.remove();
    	}
    	circles.clear();
    }
    
    public ArrayList<UBStation> getSortedStationList() {
    	return obj_Station.getSortedStationListByKey();
    }
}
