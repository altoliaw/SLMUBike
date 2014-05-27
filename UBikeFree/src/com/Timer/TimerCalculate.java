//Name:				File timer calculate
//Designer: 		Nick & 
//Date:				20140409
//Description:		
package com.Timer;

import com.Resource.EnvironmentSource;

import android.content.res.XmlResourceParser;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimerCalculate {
	private String str_messageBuffer;
	public String str_layOutTimeBuffer;
	private Button obj_startButton;		
	private MyCoundDownTask obj_mytask;
	private EnvironmentSource obj_Environment;
	private int int_stateValue ;// 0:還沒計時  1:30分鐘計時狀態  2:15分鐘計時狀態 
	public TimerCalculate(Button obj_startButton,XmlResourceParser obj_MyXml ){
		//constructor
		this.obj_startButton=obj_startButton;				
		this.int_stateValue=0;
		this.str_messageBuffer="按一下開始計時";
		this.obj_startButton.setText(this.str_messageBuffer);
		this.obj_Environment=new EnvironmentSource(obj_MyXml);
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
    		//this.str_messageBuffer="30分鐘借車倒數:"+this.obj_Environment.SearchValue("Countdown/Min30")+"\n";
    		//startCountDown(1800000); 
    		startCountDown(Long.parseLong(this.obj_Environment.SearchValue("Countdown/Min30")));   
  	   		obj_startButton.setText(this.str_messageBuffer);
            this.int_stateValue = 1 ;                  
  		} 
    	
    	else if ( int_stateValue == 1 ) {
    		this.str_messageBuffer="同站借車倒數:\n";
    		obj_startButton.setText(this.str_messageBuffer);
  	      	cancelCountDown();
  	      	int_stateValue = 2 ;
  	      	startCountDown(Long.parseLong(this.obj_Environment.SearchValue("Countdown/Min15")));
  	      	//startCountDown(900000);         		  
  		} 
  		
  		else if ( int_stateValue == 2 ) {    		  
  			cancelCountDown();
  			this.str_messageBuffer="按一下開始計時\n";
  			//clear str_layOutTimeBuffer
  			str_layOutTimeBuffer = "";
  			obj_startButton.setText(this.str_messageBuffer);
  			int_stateValue = 0 ;
  		}
    
    }
    
    public int getState() {
    	return this.int_stateValue;
    }
    
	private class MyCoundDownTask extends CountDownTimer {
		public MyCoundDownTask(long millisInFuture, long countDownInterval) {
		    	  super(millisInFuture, countDownInterval);
		}
		@Override
		public void onTick(long millisUntilFinished) {
		    //long minus = millisUntilFinished/60000 ;
			//long seconds = (millisUntilFinished%60000) /1000 ;
		    long minus = millisUntilFinished/(Long.parseLong(obj_Environment.SearchValue("TimeDefinition/Min")));
		    long seconds = (millisUntilFinished%(Long.parseLong(obj_Environment.SearchValue("TimeDefinition/Min")))) /(Long.parseLong(obj_Environment.SearchValue("TimeDefinition/Sec"))) ;
		    String str_layOutMessageBuffer="";
		    str_layOutTimeBuffer = minus+ ":" + seconds;
		    str_layOutMessageBuffer=str_messageBuffer+str_layOutTimeBuffer;
		    obj_startButton.setText(str_layOutMessageBuffer);		    
		}
		@Override
		public void onFinish() {
			String str_layOutMessageBuffer="逾期!";
			obj_startButton.setText(str_layOutMessageBuffer);
		}
	}	
}


