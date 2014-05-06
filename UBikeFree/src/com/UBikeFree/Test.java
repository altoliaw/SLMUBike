package com.UBikeFree;

import java.net.MalformedURLException;
import java.util.Iterator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.StationInformation.Stations;
import com.StationInformation.UBStation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Test extends FragmentActivity  {
 
    // Google Map
    private GoogleMap obj_GoogleMap;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        try {
            // Loading map
            initilizeMap();
            setUBikeStationInfoMaker() ;
 
        } catch (Exception obj_ex) {
        	obj_ex.printStackTrace();
        }
 
    }
 
 
    private void initilizeMap() {
        if (obj_GoogleMap == null) {
        	obj_GoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
        	LatLng obj_TaipeiMap = new LatLng(25.052401, 121.543915);//25.052401, 121.543915
        	obj_GoogleMap.setMyLocationEnabled(true);
        	obj_GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(obj_TaipeiMap, 13));
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        setUBikeStationInfoMaker() ;
    }
    /*
     * writen by LeoTsui @ntut
     * 將站點資訊放上googlemap
     */
    private void setUBikeStationInfoMaker() {
    	Stations MarkedStations;
		try {
			MarkedStations = new Stations();
			Iterator<UBStation> MarkedIterator ;
	    	MarkedIterator = MarkedStations.iterator() ;
	    	while ( MarkedIterator.hasNext() ) {
	    		UBStation UBStationtemp  = MarkedIterator.next() ;
	    		LatLng markedLatLng = new LatLng(UBStationtemp.getLat(), UBStationtemp.getLng());
	    		Marker ntut = obj_GoogleMap.addMarker(new MarkerOptions().position(markedLatLng)
	    				                                                 .title(UBStationtemp.getName())
	    				                                                 .snippet(("可借"+ UBStationtemp.getBikes()+ "空車位" + UBStationtemp.getEmptySlots())));
	    	} 
	    }
	    	catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
    }
}
