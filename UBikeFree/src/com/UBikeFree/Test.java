package com.UBikeFree;

import com.UBikeFree.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.TextView;
import android.view.View.OnClickListener;

import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigLocator;
import org.igfay.util.*;

public class Test extends FragmentActivity  {
 
    // Google Map
    private GoogleMap obj_GoogleMap;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
//        TextView obj_Text= (TextView)findViewById(R.id.textView1);
//        JFigLocator obj_Locator=new JFigLocator("D:\\adt-bundle-windows-x86_64-20140321\\eclipse\\WorkSpace\\UBikeFree\\src\\com\\Resource\\resource.xml");
//        obj_Locator.setConfigLocation("file");
//        try{
//	    	JFig.initialize(obj_Locator);
//	    	String str_Text=JFig.getInstance().getValue("Countdown", "Min30");
//	    	obj_Text.setText(str_Text);
//        }
//        catch(Exception obj_Ex){
//        	
//        	
//        }

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
