package com.UBikeFree.ShowcaseView;

import android.app.Activity;
import android.view.View;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.PointTarget;
import com.espian.showcaseview.targets.ViewTarget;


public class ShowcaseViewManager {

	/**
	 * Add a ShowcaseView with a View as target, and text for title and details
	 * 
	 * @param activity where ShowcaseView should show over
	 * @param view target view
	 * @param title
	 * @param details
	 * @return ShowcaseView
	 */
	public static ShowcaseView setUpShowcaseViewTargetOnView(Activity activity,
															 View view,
															 String title,
															 String details) {
		
		//create target
		ViewTarget target = new ViewTarget(view);
		return ShowcaseView.insertShowcaseView(target, activity, title, details);
	}
	
	/**
	 * Add a ShowcaseView with a location as target, and text for title and details
	 * 
	 * @param activity ShowcaseView should show over
	 * @param x target dimension x
	 * @param y target dimension y
	 * @param title
	 * @param details
	 * @return
	 */
	public static ShowcaseView setUpShowcaseViewTargetOnLocation(Activity activity,
																 int x,
																 int y,
																 String title,
																 String details) {
		
		//create target
		PointTarget target = new PointTarget(x, y);
		return ShowcaseView.insertShowcaseView(target, activity, title, details);
	}
}
