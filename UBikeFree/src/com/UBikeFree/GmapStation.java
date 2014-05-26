package com.UBikeFree;


import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;


import android.widget.Toast;


//import android.widget.Toast;
import com.Map.GMap;
import com.Resource.EnvironmentSource;
import com.StationInformation.UBStation;
import com.StationInformation.StationListAdapter.UBikeStationListAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.navdrawer.SimpleSideDrawer;


public class GmapStation extends FragmentActivity{
	private EnvironmentSource			obj_Environment;
	private GMap						obj_GMap;
    private ScheduledExecutorService	obj_Scheduler;
    private ScheduledFuture<?>			obj_SchedulerHandler; 
    
    private SimpleSideDrawer			sideMenu_UBikeStations;
    private ListView					listView_UBikeStations;
    private EditText					editText_searchStation;

    private UBikeStationListAdapter		adapter_UBikeStationList;
    
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
        this.getActionBar().setDisplayHomeAsUpEnabled(false);
        
        
        //set list view and list items
        //get list view
        listView_UBikeStations = (ListView)findViewById(R.id.ubike_list_view);
        //get input field
        editText_searchStation = (EditText)findViewById(R.id.input_search);
        //add items to list view
        adapter_UBikeStationList = new UBikeStationListAdapter(this, this.obj_GMap.getSortedStationList());        
        listView_UBikeStations.setTextFilterEnabled(true);
        listView_UBikeStations.setAdapter(adapter_UBikeStationList);
        
        //enable search functionality
        editText_searchStation.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
					GmapStation.this.adapter_UBikeStationList.getFilter().filter(s.toString());
					adapter_UBikeStationList.notifyDataSetChanged();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
        	
        });
        
        //set onItemClickListener
        listView_UBikeStations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				LatLng stationLocation = new LatLng(
												GmapStation.this.adapter_UBikeStationList
													.getItem(position)
													.getLat(),
												GmapStation.this.adapter_UBikeStationList
													.getItem(position)
													.getLng()
												);
				GmapStation.this.obj_GMap.MapCameraSetting(stationLocation);

				GmapStation.this.obj_GMap.showMarkerOption(GmapStation.this.adapter_UBikeStationList
															.getItem(position)
															.getName());
				sideMenu_UBikeStations.toggleRightDrawer();
			}
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        						//Start initial Google Map        
        this.ScheduleMap();		//End initial Google Map, and update adapter's list
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
    	case R.id.station_search:
    		Log.e("GMap.Station", "Got");
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
                        adapter_UBikeStationList.swapStationItems(obj_GMap.getSortedStationList());
                        //as soon as update the list, if the editor has any value, filter the list view
                        CharSequence s = editText_searchStation.getText();
                        if(s != null && s.length() > 0) {
                        	adapter_UBikeStationList.getFilter().filter(s.toString());
                        	adapter_UBikeStationList.notifyDataSetChanged();
                        }//end if
                        
                        //show toast
                        Toast.makeText(GmapStation.this, "更新站點資訊", Toast.LENGTH_SHORT).show();
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

