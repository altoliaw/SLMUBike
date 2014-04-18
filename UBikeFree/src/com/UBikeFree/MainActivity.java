package com.UBikeFree;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import com.example.ubikefree.R;
/*Timer import Start*/
import com.Timer.TimerCalculate;
/*Timer import end*/


public class MainActivity extends ActionBarActivity {
	/*Timer Start*/
	private Button start;
	TimerCalculate obj_Timer;
	private int state; // 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態
	/*Timer end*/		
	private Button obj_GmapStation;
	
	
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
        
        obj_GmapStation=(Button) findViewById(R.id.gmapstation);
        obj_GmapStation.setText("站點資訊");
        obj_GmapStation.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent obj_Intent=new Intent();
        		obj_Intent.setClass(MainActivity.this,GmapStation.class);
        		startActivity(obj_Intent);         		
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
