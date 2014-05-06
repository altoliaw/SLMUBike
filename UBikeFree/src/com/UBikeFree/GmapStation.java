package com.UBikeFree;
import com.UBikeFree.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.StationInformation.Stations;
import com.StationInformation.UBStation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class GmapStation extends FragmentActivity  {
 
    // Google Map
    private GoogleMap obj_GoogleMap;
 
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
	    		for(UBStation obj_UbiKeStation : obj_Station){
	    			String str_Information=obj_UbiKeStation.getName() +":"+obj_UbiKeStation.getBikes()+"/"+obj_UbiKeStation.getEmptySlots();
	    			MarkerOptions obj_Marker = new MarkerOptions().position(new LatLng(obj_UbiKeStation.getLat(),obj_UbiKeStation.getLng()))
	    									.title(str_Information);
	    			// ROSE color icon
	    			obj_Marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	    			// adding marker
	    			obj_GoogleMap.addMarker(obj_Marker);
	    		}
        	}
        	catch(Exception obj_Ex){}
        }
    }
    
 
    @Override
    protected void onResume() {
        super.onResume();
        this.InitilizeMap();
    }
 
}

