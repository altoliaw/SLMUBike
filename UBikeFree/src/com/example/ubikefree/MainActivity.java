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

public class MainActivity extends ActionBarActivity {
	//
	private Button start, cancel;	
	private TextView tv;	
	private MyCoundDownTask mytask;
	private ListView uBikeStationList;
	private int state = 0 ; // 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態 
	private UBikeDataGetter uBikeDataGetter;
	private SimpleAdapter uBikeListAdapter;
	
	private ArrayList<String> uBikeStationNames;
	private ArrayList<String> uBikeStationFreeOfTotalBikes;
	private ArrayList< HashMap<String, String> > listDataForUBikeListView;
	private HashMap<String, JSONObject> uBikeJSONDataMap;
	/*private final String UBIKE_LISTDATA_STATION_NAME_KEY = "station_name";
	private final String UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY = "free_of_total_bikes";*/
	
	//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        uBikeStationNames = new ArrayList<String>();
        uBikeStationFreeOfTotalBikes = new ArrayList<String>();
        uBikeJSONDataMap = new HashMap<String, JSONObject>();
        listDataForUBikeListView = new ArrayList< HashMap<String, String> >();
        
        //create a UBikeDataGetter

		uBikeDataGetter = new UBikeDataGetter();

        
        grabUBikeStationsJSONData();
        uBikeStationNames = uBikeDataGetter.getUBikeStationNames();
        setUBikeStationFreeOfTotalBikes();
        setListDataForUBikeListView();
        
        // 
        tv = (TextView)findViewById(R.id.tv);       
        start = (Button)findViewById(R.id.start);    
        cancel = (Button)findViewById(R.id.cancel);
        uBikeStationList = (ListView)findViewById(R.id.ubike_list_view);
        
        //set list adapter
        uBikeListAdapter = new SimpleAdapter(
        							this,
        							listDataForUBikeListView,
        							android.R.layout.simple_list_item_2,
        							new String[] {	"name",
        											"bikes"},
        							new int[] {	android.R.id.text1,
        										android.R.id.text2});
        uBikeStationList.setAdapter(uBikeListAdapter);
        
        
        start.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		if ( state == 0 ) {
        	      startCountDown(1800000);   
                  cancel.setText("開始同站借車倒數");
                  state = 1 ;                  
        		} // if 
        	}        	
        });        	
        cancel.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		if ( state == 1 ) {
        		  cancel.setText("取消同站借車倒數");
        	      cancelCountDown();
        	      state = 2 ;
        		  startCountDown(900000);         		  
        		} // if
        		
        		else if ( state == 2 ) {    		  
        		  cancelCountDown();
        		  state = 0 ;
        		} // else
        	}        	
        });
        	//
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }
    //
    private void startCountDown( long initialtime ){
    	mytask = new MyCoundDownTask(initialtime, 1000);
    	mytask.start();
    }
    private void cancelCountDown(){
    	if (mytask != null){
    	  mytask.cancel();
    	  tv.setText("提早結束");
        }
    }
    private class MyCoundDownTask extends CountDownTimer {
    	public MyCoundDownTask(long millisInFuture, long countDownInterval) {
    	  super(millisInFuture, countDownInterval);
    	}
    	@Override
    	public void onTick(long millisUntilFinished) {
    		long minus = millisUntilFinished/60000 ;
    		long seconds = (millisUntilFinished%60000) /1000 ;
    	    tv.setText("剩下時間: " +minus + ":" + seconds );
    	}
    	@Override
    	public void onFinish() {
    	  tv.setText("時間到");
    	  }
    	}
//

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
    	
    	uBikeJSONDataMap = uBikeDataGetter.getUBikeJsonObjectsHashMapWithKeyStationName();
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
    			
    			numOfFreeBikes = uBikeJSONDataMap.get(name).getString(UBikeDataGetter.UBIKE_STATION_CURRENT_BIKES_KEY.toString());
    			numOfTotalBikes = uBikeJSONDataMap.get(name).getString(UBikeDataGetter.UBIKE_STATION_TOTAL_BIKES_KEY.toString());
    			
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
    		
    		map.put("name", uBikeStationNames.get(index));
    		map.put("bikes", uBikeStationFreeOfTotalBikes.get(index));
    		
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
