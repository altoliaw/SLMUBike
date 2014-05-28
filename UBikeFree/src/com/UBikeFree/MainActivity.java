package com.UBikeFree;

import java.util.concurrent.Callable;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
//import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;















/*Timer import Start*/
import com.Timer.TimerCalculate;
/*Timer import end*/
import com.UBikeFree.R;


public class MainActivity extends ActionBarActivity {
	/*Timer Start*/
	private Button start;
	public static TimerCalculate obj_Timer;
	private int state; // 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態
	/*Timer end*/		
	/*Gmap Start*/
	private Button obj_GmapStation;
	/*Gmap end*/
	/*Test start*/
	private Button obj_Test;
	/*Test end*/

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
        obj_Timer = new TimerCalculate(start,getResources().getXml(R.xml.resource));
        start.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		state = obj_Timer.getState();
        		if(state == 0) {
        			obj_Timer.StartProcess();
        		    obj_Timer.setAlertTime(300);
                    obj_Timer.setAlert(new AlertCallback());
        		} else {
        			timerConfirmDialog(obj_Timer);
        		}
        	}        	
        });  
          
        /*Timer end*/
        /*Gmap Start*/
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
        /*Gmap end*/
       
        /*Test Start*/
        /*obj_Test=(Button)findViewById(R.id.test);
        obj_Test.setText("測試用");
        obj_Test.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent obj_Intent =new Intent();
        		obj_Intent.setClass(MainActivity.this, Test.class);
        		startActivity(obj_Intent);         		
        	}        	
        });*/
        /*Test end*/
        
        
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
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
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * auther by leotsui
     * 把tesk給Backgroung執行
     */
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true) ;
    }
    

    /*
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }
    private class AlertCallback implements Callable<Void> {
        @Override
        public Void call() {
            final Vibrator vibrator;
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{5000, 1000}, 30);
            final MediaPlayer mp = new MediaPlayer();
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            try {
                mp.prepare();
            } catch(Exception ex) {
                
            }
            mp.start();
            Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
            alertDialog.setTitle("UBikeFree");
            alertDialog.setMessage("請準備環車");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    vibrator.cancel();
                    mp.stop();
                }
            });
            alertDialog.show();
            return null;
        }
    }
}
