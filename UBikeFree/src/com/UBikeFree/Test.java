package com.UBikeFree;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;  
import android.location.LocationManager;

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;  

import android.util.Log;
import android.widget.Toast;

public class Test extends FragmentActivity {      
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> obj_beeperHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        scheduler =
                Executors.newSingleThreadScheduledExecutor();
        
        
     // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
 
    }
 
    private void makeUseOfNewLocation(Location location){
    	Log.i("Lat", String.valueOf(location.getLatitude()));
    	Log.i("Lng", String.valueOf(location.getLongitude()));    	
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
}
