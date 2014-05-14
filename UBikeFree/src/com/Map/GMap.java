//===========================================================
//	Author		:	Nick Liao
//	Date		:	20140514
//	Description	:	Map Setting
package com.Map;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
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

public class GMap {
																						// Google Map
    private	HashMap<String,Marker>	obj_MarkInformation;								//All stations' marks information
    private	EnvironmentSource		obj_Environment;
    private	Stations				obj_Station;										// Stations' Information Object
    private Context					obj_ContextFromActivity;
    private LatLng					obj_TaipeiMap;										//Lat Lng Position
    private	static	final	String 	ststr_Activity="GMap.java";
    
    																					//Construct of class
    public	GMap(EnvironmentSource obj_Environment, Context obj_ContextFromActivity){
    	this.obj_Environment			=obj_Environment;
    	this.obj_ContextFromActivity	=obj_ContextFromActivity;    	
    	try{
    		obj_Station					=new Stations(this.obj_Environment);
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());    		
    	}
    	this.GPSPosition();
    }
    
    public void MapDisplay(GoogleMap obj_GoogleMap){
    	try {																			//Load Map into Fragment
            this.MapSetting(obj_GoogleMap);
        } 
        catch (Exception obj_Ex) {
        	//obj_ex.printStackTrace();
        	Log.e(ststr_Activity,obj_Ex.getMessage());
        }    	
    }
	
    																					//Map Setting: default Lat Lng are 25.052401, 121.543915
    private void MapSetting(GoogleMap obj_GoogleMap) {    	
    	obj_GoogleMap.setMyLocationEnabled(true);    	
    	obj_GoogleMap.moveCamera(	CameraUpdateFactory.newLatLngZoom(obj_TaipeiMap,
    								Float.parseFloat(this.obj_Environment.SearchValue("GMap/Zoom"))));    	        
    	try{        	
        	this.obj_MarkInformation				=new HashMap<String,Marker>();
    		for(UBStation 		obj_UbiKeStation 	: obj_Station){    			
    			LatLng			obj_Position		=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
    			MarkerOptions 	obj_Mark			=new MarkerOptions().position(obj_Position)
    																	.title(obj_UbiKeStation.getName())
    																	.snippet(("可借:"+ obj_UbiKeStation.getBikes()+ "/空車位:" + obj_UbiKeStation.getEmptySlots()))
    																	.alpha(Float.parseFloat(this.obj_Environment.SearchValue("GMap/AlphaBefore")));	    			
    			String			str_Key				=obj_UbiKeStation.getId();	    			    	
    			
    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(
    					this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
    			));
    																					//Adding markers for station
    			Marker	obj_MarkObject				=obj_GoogleMap.addMarker(obj_Mark);
    			this.obj_MarkInformation.put(str_Key,obj_MarkObject);	    			    		
    		}	    			    			    		    	
    	}
    	catch(Exception obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());
    	}       
    }
    
    																					//Position from GPS or pre-define
    private void GPSPosition(){
    	LocationManager	obj_GPS			=(LocationManager)(this.obj_ContextFromActivity.getSystemService(Context.LOCATION_SERVICE));    	
    	Double 			db_Lng	;														//紀錄經度
		Double 			db_Lat	;														//紀錄緯度 		
    	if (obj_GPS.isProviderEnabled(LocationManager.GPS_PROVIDER) || obj_GPS.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
    																					//使用GPS定位座標
    		Location obj_Location 	= obj_GPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		
    		if(obj_Location != null) {
    			db_Lng 				=obj_Location.getLongitude();						//取得經度
    			db_Lat 				=obj_Location.getLatitude();						//取得緯度        		
    		}
    		else {
    			Toast.makeText(this.obj_ContextFromActivity, "You can open GPS for precise locating.", Toast.LENGTH_SHORT).show();
       			db_Lng 				=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lng"));					
    			db_Lat 				=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lat"));     		    			
    		}
    	}
    	else{
    		Toast.makeText(this.obj_ContextFromActivity, "You can open GPS for precise locating.", Toast.LENGTH_SHORT).show();
   			db_Lng 					=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lng"));					
			db_Lat 					=Double.parseDouble(this.obj_Environment.SearchValue("GMap/Lat"));   		    		
    	}
    	if(this.obj_TaipeiMap==null){
    		this.obj_TaipeiMap			=new LatLng(db_Lat	,db_Lng);
    	}
	}
    
    																					//Trigger for reloading station information
    public void StationDataReload(GoogleMap obj_GoogleMap,EnvironmentSource obj_Environment){
    																					//Re-do the json package again
    	try{
    		//this.obj_Station.update(obj_Environment);
    	}
    	catch(Exception obj_Ex){}
    	for(UBStation	obj_UbiKeStation	:	this.obj_Station){
    		String		str_Key				=obj_UbiKeStation.getId();
    		if(this.obj_MarkInformation.containsKey(str_Key)==true){
    			this.obj_MarkInformation.remove(str_Key);
    		}
    		
    	}
    																					//Search the HashMap for station information
//    	for(UBStation	obj_UbiKeStation	:	this.obj_Station){
//    		String		str_Key				=obj_UbiKeStation.getId();
//    		if(this.obj_MarkInformation.containsKey(str_Key)==true){
//    			Marker obj_MarkObject		=this.obj_MarkInformation.get(str_Key);
//    			obj_MarkObject.remove();
//    			LatLng obj_Position			=new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng());
//    			MarkerOptions obj_Mark		=new MarkerOptions().position(obj_Position)
//																.title(obj_UbiKeStation.getName())
//																.snippet(("可借:"+ obj_UbiKeStation.getBikes()+ "/空車位:" + obj_UbiKeStation.getEmptySlots()))
//																.alpha(Float.parseFloat(this.obj_Environment.SearchValue("GMap/AlphaAfter")));
//    			obj_Mark.icon(BitmapDescriptorFactory.defaultMarker(
//    							this.MarkColorPicker(Double.parseDouble(obj_UbiKeStation.getBikes()), Double.parseDouble(obj_UbiKeStation.getEmptySlots()))
//    			));    			
//    			obj_MarkObject				=obj_GoogleMap.addMarker(obj_Mark);
//    			this.obj_MarkInformation.put(str_Key,obj_MarkObject);
//    		}    		
//    	}    	
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
    
}
