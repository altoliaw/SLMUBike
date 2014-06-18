package com.UBikeFree.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import com.Timer.TimerCalculate;
import com.UBikeFree.R;

public class TimerConfirmDialog {

	public static void popupTimerConfirmDialog(Activity activity, final TimerCalculate timer) {
    	
    	Builder stopCountingAlertDialog = new AlertDialog.Builder(activity);
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
}
