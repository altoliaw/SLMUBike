package com.UBikeFree;

import com.UBikeFree.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.TextView;
import android.view.View.OnClickListener;


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
    }
 
}
