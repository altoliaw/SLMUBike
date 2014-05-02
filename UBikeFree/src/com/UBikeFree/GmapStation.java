package com.UBikeFree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.StationInformation.UBikeDataGetter;
public class GmapStation extends ActionBarActivity {	
	
	private ListView uBikeStationList;
	private EditText uBikeStationSearch;
	private UBikeDataGetter uBikeDataGetter;
	private SimpleAdapter uBikeListAdapter;
	
	private ArrayList<String> uBikeStationNames;
	private ArrayList< HashMap<String, String> > listDataForUBikeListView;
	private final String UBIKE_LISTDATA_STATION_NAME_KEY = "station_name";
	private final String UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY = "current_of_total_bikes";
	
	
	private Timer refreshStationInfoTimer;
	//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_information);

        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        
        
        
        uBikeStationNames = new ArrayList<String>();
        listDataForUBikeListView = new ArrayList< HashMap<String, String> >();

        
        //create a UBikeDataGetter
		//uBikeDataGetter = new UBikeDataGetter();
        //uBikeStationNames = uBikeDataGetter.getUBikeStationNames();
        //setListDataForUBikeListView();
        setUBikeListViewFromDataGetter();
        
        uBikeStationSearch = (EditText)findViewById(R.id.input_search);
        uBikeStationList = (ListView)findViewById(R.id.ubike_list_view);
        
        //set list adapter
        uBikeListAdapter = new SimpleAdapter(
        							this,
        							listDataForUBikeListView,
        							android.R.layout.simple_list_item_2,
        							new String[] {	UBIKE_LISTDATA_STATION_NAME_KEY,
        											UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY},
        							new int[] {	android.R.id.text1,
        										android.R.id.text2});
        uBikeStationList.setAdapter(uBikeListAdapter);
        
        uBikeStationSearch.addTextChangedListener(new TextWatcher() {
        	
        	@Override
        	public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        		
        		// When user changed the Text
        		uBikeListAdapter.getFilter().filter(cs);
        	}
        	@Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                         
            }
        });
              	
       
        	//
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 
     */
    private void setListDataForUBikeListView() {
    	
    	HashMap<String, String> mapOfCurrentOfTotalBikes = new HashMap<String, String>();
    	mapOfCurrentOfTotalBikes = uBikeDataGetter.getUBikeCurrentOfTotalBikesWithKeyStationName();
    	
    	for(int index = 0; index < uBikeStationNames.size(); ++index) {
    		HashMap<String, String> map = new HashMap<String, String>();
    		
    		map.put(UBIKE_LISTDATA_STATION_NAME_KEY, uBikeStationNames.get(index));
    		map.put(UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY, mapOfCurrentOfTotalBikes.get(uBikeStationNames.get(index)));
    		
    		listDataForUBikeListView.add(map);
    	}//end for loop
    }

    public void setUBikeListViewFromDataGetter() {
    	uBikeDataGetter = new UBikeDataGetter();
        uBikeStationNames = uBikeDataGetter.getUBikeStationNames();
        setListDataForUBikeListView();
    }
    
    public void updateUBikeListView() {
    	listDataForUBikeListView.clear();
    	setUBikeListViewFromDataGetter();
    	
    	uBikeListAdapter = new SimpleAdapter(
				this,
				listDataForUBikeListView,
				android.R.layout.simple_list_item_2,
				new String[] {	UBIKE_LISTDATA_STATION_NAME_KEY,
								UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY},
				new int[] {	android.R.id.text1,
							android.R.id.text2});
    	uBikeListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	refreshStationInfoTimer = new Timer();
    	refreshStationInfoTimer.schedule(new TimerTask() {
    		
    		@Override
    		public void  run() {
    			runOnUiThread(new Runnable() {
    				public void run() {
    					updateUBikeListView();
    				}
    			});
    		}
    	}, 0, 300000);//updates each 5 minutes
    }
    
    @Override
    public void onPause() {
    	refreshStationInfoTimer.cancel();
    	super.onPause();
    }
    
    /**
     * A placeholder fragment containing a simple view.
     *
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

}

