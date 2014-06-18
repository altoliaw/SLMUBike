package com.UBikeFree;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
//import android.app.ProgressDialog;
//import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
//import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import android.widget.Toast;
import com.Map.GMap;
import com.Resource.EnvironmentSource;
import com.StationInformation.StationListAdapter.UBikeStationListAdapter;
import com.UBikeFree.Dialog.TimerConfirmDialog;
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
    private String 						text = "";
    private ActionBar 					actionBar;
    private Timer 						watchTimer;    

    private TextView					countdownLabelTextView;
    
    private final Handler 				countdownLabelHandler 	= new Handler();
    private final Runnable				countdownLabelUpdate 	= new Runnable() {
    										public void run() {
    											countdownLabelTextView.setText(text);
    										}
    									};
    									
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);		        
        actionBar = this.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);        
        watchTimer = new Timer();        
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
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        
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
        
        //watch the TimerCalculate
        countdownLabelTextView = (TextView)findViewById(R.id.countdown_label);
        
        watchTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				text=MainActivity.obj_Timer.str_layOutTimeBuffer;
				countdownLabelHandler.post(countdownLabelUpdate);
				invalidateOptionsMenu();
			}
        
        }, 250, 1000);
        MainActivity.progress_Dialog.dismiss();
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        								//Start initial Google Map 
        this.obj_GMap.WakeUpCamera();	//Wake Up Camera for Map
        this.ScheduleMap();				//End initial Google Map, and update adapter's list        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.gmapstation_actions, menu);
    	MenuItem actionLabel = menu.findItem(R.id.action_countdown_label);
    	MenuItem stationLabel = menu.findItem(R.id.station_search);
    	stationLabel.setTitle("run");
    	actionLabel.setTitle(text);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	MenuItem actionLabel = menu.findItem(R.id.action_countdown_label);
    	actionLabel.setTitle(text);
    	
    	return super.onPrepareOptionsMenu(menu);
    }
    
    //on click actions on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.action_search:
    		sideMenu_UBikeStations.toggleRightDrawer();
    		return true;
    	case R.id.action_countdown_label:
    		
    		int state = MainActivity.obj_Timer.getState();
    		if(state == 0) {
    			MainActivity.obj_Timer.StartProcess();
    		} else {
    			TimerConfirmDialog.popupTimerConfirmDialog(GmapStation.this, MainActivity.obj_Timer);
    		}
    		return true;
    	case R.id.station_search:
    		this.obj_GMap.bool_IsShownNearStation=!(this.obj_GMap.bool_IsShownNearStation);
    		this.obj_GMap.StationDataReload(false);
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
                        obj_GMap.StationDataReload(true);
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

