package com.UBikeFree;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.Map.GMap;
import com.Resource.EnvironmentSource;
import com.UBikeFree.R;
import com.google.android.gms.maps.MapFragment;
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;  


public class GmapStation extends FragmentActivity implements LocationListener  {

    private ScheduledExecutorService	obj_Scheduler;
    private ScheduledFuture				obj_SchedulerHandler;
    private GMap						obj_GMap;
    private EnvironmentSource			obj_Environment;
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_information);
        //Start initial the Environment
        this.obj_Environment=new EnvironmentSource(getResources().getXml(R.xml.resource));
        //End initial the Environment
        
        //Start initial Google Map
        this.obj_GMap=new GMap(this.obj_Environment,this);
        this.obj_GMap.MapDisplay(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap());
        //End initial Google Map
        
        //Start initial Scheduler
        this.obj_Scheduler =Executors.newSingleThreadScheduledExecutor();        
        //End initial Scheduler
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //Start initial Google Map
        this.obj_GMap.MapDisplay(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap());
        this.ScheduleMap();
        //End initial Google Map
    }
    
    protected void onStop(){    	
    	super.onStop();
    	this.obj_SchedulerHandler.cancel(true);
    }
    
    @Override
	public void onLocationChanged(Location arg0) {	//當地點改變時
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void onProviderDisabled(String arg0) {	//當GPS或網路定位功能關閉時
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void onProviderEnabled(String arg0) {	//當GPS或網路定位功能開啟
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {	//定位狀態改變
		// TODO Auto-generated method stub
 
	}    
    

  //Trigger for reloading
    private void ScheduleMap(){
    	Runnable obj_Task=new Runnable() {
    		public void run() {
    			runOnUiThread(new Runnable() {
    				public void run() {
                        Toast.makeText(getApplicationContext(), "Relaod from WebService", Toast.LENGTH_SHORT).show();
                        obj_GMap.StationDataReload(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap(),obj_Environment);
    				}
    			});
    		}
    	};
        this.obj_SchedulerHandler	=this.obj_Scheduler.scheduleAtFixedRate(obj_Task, Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerInitialDelay")), Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerTimeDelay")), TimeUnit.SECONDS);        
    }
    
}

