//===========================================================
//	Author		:	Nick Liao
//	Date		:	20140514
//	Description	:	Map Setting
package com.Map;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

import android.content.Context;
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
    																					//Construct of class
    public	GMap(EnvironmentSource obj_Environment, Context obj_ContextFromActivity, GoogleMap obj_GoogleMap){
    	this.obj_Environment						=obj_Environment;
    	this.obj_ContextFromActivity				=obj_ContextFromActivity;
    	this.obj_GoogleMap							=obj_GoogleMap;    	
    	try{
    		obj_Station					=new Stations(this.obj_Environment);
    		
    		//測試從TreeMap取得相同區域的站點
    		int[] stationNum = {	obj_Station.getStationsOfArea("信義區").size(),
    								obj_Station.getStationsOfArea("中山區").size(),
    								obj_Station.getStationsOfArea("松山區").size(),
    								obj_Station.getStationsOfArea("南港區").size(),
    								obj_Station.getStationsOfArea("內湖區").size(),
    								obj_Station.getStationsOfArea("大安區").size(),
    								obj_Station.getStationsOfArea("中正區").size(),
    								obj_Station.getStationsOfArea("萬華區").size(),
    								obj_Station.getStationsOfArea("文山區").size(),
    								obj_Station.getStationsOfArea("士林區").size(),
    								obj_Station.getStationsOfArea("大同區").size(),
    								obj_Station.getStationsOfArea("北投區").size(),
    								obj_Station.getStationsOfArea("汐止區").size(),
    								obj_Station.getStationsOfArea("新店區").size()
    								};
    		
    		int total = 0;
    		for(int num : stationNum) {
    			total += num;
    		}
    		Toast.makeText(obj_ContextFromActivity,
    						"信義區:"+stationNum[0]+"\n" +
    						"中山區："+stationNum[1]+"\n" +
    						"松山區："+stationNum[2]+"\n" +
    						"南港區："+stationNum[3]+"\n" +
    						"內湖區："+stationNum[4]+"\n" +
    						"大安區："+stationNum[5]+"\n" +
    						"中正區："+stationNum[6]+"\n" +
    						"萬華區："+stationNum[7]+"\n" +
    						"文山區："+stationNum[8]+"\n" +
    						"士林區："+stationNum[9]+"\n" +
    						"大同區："+stationNum[10]+"\n" +
    						"北投區："+stationNum[11]+"\n" +
    						"汐止區："+stationNum[12]+"\n" +
    						"新店區："+stationNum[13]+"\n" +
    						"Total："+total,
    						Toast.LENGTH_LONG).show();
    		//測試結束
    		//結果：數量正確
    	}
    	catch(MalformedURLException obj_Ex){
    		Log.e(ststr_Activity,obj_Ex.getMessage());    		
    	}
    	this.PositionSetting();
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
    
    
    public String[] getStationNames() {
    	return obj_Station.getStationNames();
    }
}
