package com.UBikeFree;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  
import android.util.Log;
import android.widget.Toast;

public class Test extends FragmentActivity  {      
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "It works", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 10, 10, TimeUnit.SECONDS);
 
    }
 
 
    
 
    @Override
    protected void onResume() {
        super.onResume();
       
    }
    
}
