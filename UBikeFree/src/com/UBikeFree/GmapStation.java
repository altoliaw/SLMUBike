package com.UBikeFree;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


//import android.widget.Toast;
import com.Map.GMap;
import com.Resource.EnvironmentSource;
import com.google.android.gms.maps.MapFragment;
import com.navdrawer.SimpleSideDrawer;


public class GmapStation extends FragmentActivity{
	private EnvironmentSource			obj_Environment;
	private GMap						obj_GMap;
    private ScheduledExecutorService	obj_Scheduler;
    private ScheduledFuture<?>			obj_SchedulerHandler; 
    
    private SimpleSideDrawer			sideMenu_UBikeStations;
    private ListView					listView_UBikeStations;
    private EditText					editText_searchStation;
    private ArrayAdapter<String>		adapter_UBikeStationList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_information);
        																							//Start initial the Environment
        this.obj_Environment		=new EnvironmentSource(getResources().getXml(R.xml.resource));
        																							//End initial the Environment
        
        																							//Start initial Google Map
        this.obj_GMap				=new GMap(	this.obj_Environment,
        										this,((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap()        										
        );
        this.obj_GMap.MapDisplay();
        																							//End initial Google Map
        
        //Start initial Scheduler
        this.obj_Scheduler =Executors.newSingleThreadScheduledExecutor();        
        //End initial Scheduler
        
        //side menu
        //initialize simple side drawer
        sideMenu_UBikeStations = new SimpleSideDrawer(this);
        sideMenu_UBikeStations.setRightBehindContentView(R.layout.side_menu);
        
        //enable display home as up
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //remove auto-focus of input
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        //set list view and list items
        //get list view
        listView_UBikeStations = (ListView)findViewById(R.id.ubike_list_view);
        //get input field
        editText_searchStation = (EditText)findViewById(R.id.input_search);
        //add items to list view
        adapter_UBikeStationList = new ArrayAdapter<String>(
        									this,
        									R.layout.list_item,
        									R.id.ubstation_name,
        									this.obj_GMap.getStationNames());
        listView_UBikeStations.setAdapter(adapter_UBikeStationList);
        
        //enable search functionality
        editText_searchStation.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// When user changed the Text
				GmapStation.this.adapter_UBikeStationList.getFilter().filter(s);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
        	
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        																							//Start initial Google Map        
        this.ScheduleMap();
        																							//End initial Google Map
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.gmapstation_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    //on click actions on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.action_search:
    		sideMenu_UBikeStations.toggleRightDrawer();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    protected void onStop(){    	
    	super.onStop();
    	this.obj_SchedulerHandler.cancel(true);
    }
    

    //Trigger for reloading
    private void ScheduleMap(){
    	Runnable obj_Task=new Runnable() {
    		public void run() {
    			runOnUiThread(new Runnable() {
    				public void run() {
                        //Toast.makeText(getApplicationContext(), "Relaod from WebService", Toast.LENGTH_SHORT).show();
                        obj_GMap.StationDataReload();
                        //reset list items
                        adapter_UBikeStationList = new ArrayAdapter<String>(
                        		GmapStation.this,
								R.layout.list_item,
								R.id.ubstation_name,
								obj_GMap.getStationNames());
                        adapter_UBikeStationList.notifyDataSetChanged();
    				}
    			});
    		}
    	};
    	this.obj_SchedulerHandler	=this.obj_Scheduler.scheduleAtFixedRate(	obj_Task,
        																		Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerInitialDelay")),
        																		Long.parseLong(this.obj_Environment.SearchValue("GMap/TriggerTimeDelay")), 
        																		TimeUnit.SECONDS
        );        
    }
    
}

