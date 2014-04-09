//Name:				File timer calculate
//Designer: 		Nick & 
//Date:				20140409
//Description:		
package com.Timer;


import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TimerCalculate {
	private String str_messageBuffer;
	private Button obj_startButton;		
	private MyCoundDownTask obj_mytask;
	private int int_stateValue ;// 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態 
	public TimerCalculate(Button obj_startButton){
		//constructor
		this.obj_startButton=obj_startButton;				
		this.int_stateValue=0;
		this.str_messageBuffer="按一下開始計時";
		this.obj_startButton.setText(this.str_messageBuffer);
	}
	
	private void startCountDown( long initialtime ){
    	this.obj_mytask = new MyCoundDownTask(initialtime, 1000);
    	this.obj_mytask.start();
    }
    private void cancelCountDown(){    	
    	if (this.obj_mytask != null){
    		this.obj_mytask.cancel();
        }
    }
    
    public void StartProcess(){
    	if ( this.int_stateValue == 0 ) {
    		this.str_messageBuffer="30分鐘借車倒數:\n";
    		startCountDown(1800000);   
  	   		obj_startButton.setText(this.str_messageBuffer);
            this.int_stateValue = 1 ;                  
  		} 
    	
    	else if ( int_stateValue == 1 ) {
    		this.str_messageBuffer="同站借車倒數:\n";
    		obj_startButton.setText(this.str_messageBuffer);
  	      	cancelCountDown();
  	      	int_stateValue = 2 ;
  	      	startCountDown(900000);         		  
  		} 
  		
  		else if ( int_stateValue == 2 ) {    		  
  			cancelCountDown();
  			this.str_messageBuffer="按一下開始計時\n";
  			obj_startButton.setText(this.str_messageBuffer);
  			int_stateValue = 0 ;
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
		    String str_layOutMessageBuffer="";
		    str_layOutMessageBuffer=str_messageBuffer+(minus+ ":" + seconds);
		    obj_startButton.setText(str_layOutMessageBuffer);		    
		}
		@Override
		public void onFinish() {
			String str_layOutMessageBuffer="逾期!";
			obj_startButton.setText(str_layOutMessageBuffer);
		}
	}	
}


