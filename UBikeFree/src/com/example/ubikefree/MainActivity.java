package com.example.ubikefree;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;
import android.view.View.OnClickListener;
import android.os.CountDownTimer;

public class MainActivity extends ActionBarActivity {
	//
	private Button start, cancel;	
	private TextView tv;	
	private MyCoundDownTask mytask;
	private int state = 0 ; // 0:�٨S�p��  1:30�����p�ɪ��A  2:15�����p�ɪ��A 
	//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 
        tv = (TextView)findViewById(R.id.tv);       
        start = (Button)findViewById(R.id.start);    
        cancel = (Button)findViewById(R.id.cancel);
        start.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		if ( state == 0 ) {
        	      startCountDown(1800000);   
                  cancel.setText("�}�l�P���ɨ��˼�");
                  state = 1 ;                  
        		} // if 
        	}        	
        });        	
        cancel.setOnClickListener(new OnClickListener() {        	
        	@Override        	
        	public void onClick(View v) {
        		if ( state == 1 ) {
        		  cancel.setText("�����P���ɨ��˼�");
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
    	  tv.setText("��������");
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
    	    tv.setText("�ѤU�ɶ�: " +minus + ":" + seconds );
    	}
    	@Override
    	public void onFinish() {
    	  tv.setText("�ɶ���");
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
