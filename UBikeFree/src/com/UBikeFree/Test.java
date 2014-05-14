package com.UBikeFree;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationListener;  

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;  

import android.util.Log;
import android.widget.Toast;

public class Test extends FragmentActivity implements LocationListener {      
	private ScheduledExecutorService scheduler;
	private ScheduledFuture obj_beeperHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        scheduler =
                Executors.newSingleThreadScheduledExecutor();               
 
    }
 
 
    
 
    @Override
    protected void onResume() {
        super.onResume();
        obj_beeperHandle=
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");                
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "It works", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 0, 5, TimeUnit.SECONDS);
       
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	Log.i("Stop", "close");
    	obj_beeperHandle.cancel(true);
    	
    }




	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
