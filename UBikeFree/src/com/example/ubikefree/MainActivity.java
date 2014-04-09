package com.example.ubikefree;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.taipeitech.csie1623.UBIkeData.UBikeDataGetter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*Timer import Start*/
import com.Timer.*;
/*Timer import end*/
public class MainActivity extends ActionBarActivity {
	//Field:
	/*Timer Start*/
	private Button start;
	TimerCalculate obj_Timer;
	private int state = 0 ; // 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態
	/*Timer end*/	
	
	private ListView uBikeStationList;
	 
	private UBikeDataGetter uBikeDataGetter;
	private SimpleAdapter uBikeListAdapter;
	
	private ArrayList<String> uBikeStationNames;
	private ArrayList<String> uBikeStationFreeOfTotalBikes;
	private ArrayList< HashMap<String, String> > listDataForUBikeListView;
	private HashMap<String, JSONObject> uBikeJSONDataMap;
	private final String UBIKE_LISTDATA_STATION_NAME_KEY = "station_name";
	private final String UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY = "free_of_total_bikes";
	
	//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*Timer Start*/
        start = (Button)findViewById(R.id.start);        
        obj_Timer=new TimerCalculate(start);
        start.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		obj_Timer.StartProcess();
        	}        	
        });  
          
        /*Timer end*/
        
        
        //create a UBikeDataGetter
        uBikeDataGetter = new UBikeDataGetter();
        setUBikeStationNames();
        setUBikeStationFreeOfTotalBikes();
        setListDataForUBikeListView();                     
        uBikeStationList = (ListView)findViewById(R.id.ubike_list_view);
        
        //set list adapter
        /*uBikeListAdapter = new SimpleAdapter(
        							this,
        							listDataForUBikeListView,
        							android.R.layout.simple_list_item_2,
        							new String[] {	UBIKE_LISTDATA_STATION_NAME_KEY,
        											UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY},
        							new int[] {	android.R.id.text1,
        										android.R.id.text2});
        uBikeStationList.setAdapter(uBikeListAdapter);*/
        
        
              	
       
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
    public void grabUBikeStationsJSONData() {
    	
    	try{
    		uBikeJSONDataMap = uBikeDataGetter.getUBikeJsonObjectsHashMapWithKeyStationName();
    	}
    	catch(JSONException jsonException) {
    		jsonException.printStackTrace();
    	}
    }
    

    private void setUBikeStationNames() {
    	
    	int numOfUBikeStations;
    	numOfUBikeStations = uBikeDataGetter.getNumOfUBikeStations();
    	
    	for(int index = 0; index < numOfUBikeStations; ++index) {
    		try {
    			uBikeStationNames.add(uBikeDataGetter.getUBikeStationName(index));
    		}
    		catch(JSONException jsonException) {
    			jsonException.printStackTrace();
    		}
    	}//end for loop
    }
    
    /**
     * dependent on uBikeJSONDataMap and uBikeStationNames
     */
    private void setUBikeStationFreeOfTotalBikes() {
    	
    	String numberOfFreeOfTotalBikes = "";
    	
    	for(String name : uBikeStationNames) {
    		
    		try {
    			String numOfFreeBikes;
    			String numOfTotalBikes;
    			
    			numOfFreeBikes = uBikeJSONDataMap.get(name).getString(UBikeDataGetter.UBIKE_STATION_CURRENT_BIKES_KEY);
    			numOfTotalBikes = uBikeJSONDataMap.get(name).getString(UBikeDataGetter.UBIKE_STATION_TOTAL_BIKES_KEY);
    			
    			numberOfFreeOfTotalBikes = numOfFreeBikes + "/" + numOfTotalBikes;
    		}
    		catch(JSONException jsonException) {
    			jsonException.printStackTrace();
    		}
    		
    		uBikeStationFreeOfTotalBikes.add(numberOfFreeOfTotalBikes);
    	}//end for loop
    }
    
    private void setListDataForUBikeListView() {
    	
    	HashMap<String, String> map = new HashMap<String, String>();
    	
    	for(int index = 0; index < uBikeStationNames.size(); ++index) {
    		
    		map.put(UBIKE_LISTDATA_STATION_NAME_KEY, uBikeStationNames.get(index));
    		map.put(UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY, uBikeStationFreeOfTotalBikes.get(index));
    		
    		listDataForUBikeListView.add(map);
    	}//end for loop
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
