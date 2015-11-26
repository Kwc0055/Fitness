package com.kennethwcox.fitness;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;


/**
 * Only required if you want the capability to capture screen-shots during the crash.
 */
public class ReportingActivity extends AppCompatActivity
{
	private static Activity sForegroundInstance;
	
	public static Activity getForegroundInstance()
	{
		return sForegroundInstance;
	}
	
	protected void onPause()
	{
		super.onPause();
		sForegroundInstance = null;
	}
	
	protected void onResume()
	{
		super.onResume();
		sForegroundInstance = this;
	}
}
