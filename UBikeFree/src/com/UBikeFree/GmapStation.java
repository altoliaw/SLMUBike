package com.UBikeFree;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.Map.GMap;
import com.Resource.EnvironmentSource;
import com.UBikeFree.R;
import com.google.android.gms.maps.MapFragment;

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  
import android.widget.Toast;

public class GmapStation extends FragmentActivity  {
 
    private ScheduledExecutorService obj_Scheduler;
    private GMap obj_GMap;
    private EnvironmentSource obj_Environment;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_information);
        //Start initial the Environment
        this.obj_Environment=new EnvironmentSource(getResources().getXml(R.xml.resource));
        //End initial the Environment
        
        //Start initial Google Map
        this.obj_GMap=new GMap(this.obj_Environment);
        this.obj_GMap.GetInitialMap(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap());
        //End initial Google Map
        
        //Start initial Scheduler
        this.obj_Scheduler =Executors.newSingleThreadScheduledExecutor();
        this.ReloadData();
        //End initial Scheduler
    }
 
 


    //Trigger for reloading
    public void ReloadData(){
    	Runnable obj_Task=new Runnable() {
            public void run() {                
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Relaod the JSON", Toast.LENGTH_SHORT).show();
                        obj_GMap.ReloadData(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap(),obj_Environment);
                    }
                });
            }
        };
    	this.obj_Scheduler.scheduleAtFixedRate(obj_Task, Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerInitialDelay")), Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerTimeDelay")), TimeUnit.SECONDS);    
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //Start initial Google Map
        this.obj_GMap.GetInitialMap(((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap());
        //End initial Google Map
    }
    
    
}

