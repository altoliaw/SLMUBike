package com.example.ubikefree;

import java.util.ArrayList;
import java.util.HashMap;

import org.taipeitech.csie1623.UBIkeData.UBikeDataGetter;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;


/*Timer import Start*/
import com.Timer.TimerCalculate;
/*Timer import end*/
public class MainActivity extends ActionBarActivity {
	//Field:
	/*Timer Start*/
	private Button start;
	TimerCalculate obj_Timer;
	private int state; // 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態
	/*Timer end*/	
	
	private ListView uBikeStationList;
	private EditText uBikeStationSearch;
	private UBikeDataGetter uBikeDataGetter;
	private SimpleAdapter uBikeListAdapter;
	
	private ArrayList<String> uBikeStationNames;
	private ArrayList< HashMap<String, String> > listDataForUBikeListView;
	private final String UBIKE_LISTDATA_STATION_NAME_KEY = "station_name";
	private final String UBIKE_LISTDATA_FREE_OF_TOTAL_BIKES_KEY = "current_of_total_bikes";
	
	//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        
        /*Timer Start*/
        start = (Button)findViewById(R.id.start);        
        obj_Timer = new TimerCalculate(start);
        start.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		state = obj_Timer.getState();
        		if(state == 0)
        			obj_Timer.StartProcess();
        		else
        			timerConfirmDialog(obj_Timer);   
        	}        	
        });  
          
        /*Timer end*/
        
        uBikeStationNames = new ArrayList<String>();
        listDataForUBikeListView = new ArrayList< HashMap<String, String> >();

        
        //create a UBikeDataGetter
		uBikeDataGetter = new UBikeDataGetter();
        uBikeStationNames = uBikeDataGetter.getUBikeStationNames();
        setListDataForUBikeListView();
        
        
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
    
    protected void timerConfirmDialog(final TimerCalculate timer) {
    	
    	Builder stopCountingAlertDialog = new AlertDialog.Builder(this);
    	stopCountingAlertDialog.setTitle(R.string.timer_stop_alert_dialog_title);
    	DialogInterface.OnClickListener okClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//confirm function
				timer.StartProcess();
			}
		};
		DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Do nothing				
			}
		};
		stopCountingAlertDialog.setPositiveButton(R.string.confirm_button,okClick);
		stopCountingAlertDialog.setNegativeButton(R.string.cancel_button,cancelClick);
    	stopCountingAlertDialog.show();
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
