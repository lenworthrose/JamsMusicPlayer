package com.jams.music.player.WidgetProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.jams.music.player.R;

public class LargeWidgetConfigActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private int mAppWidgetId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Dialog);
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED, new Intent());
		sharedPreferences = this.getSharedPreferences("com.jams.music.player", Context.MODE_PRIVATE);

		//Retrieve the id of the widget that called this activity.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras!=null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            					 AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_widget_color);
		builder.setCancelable(false);
		builder.setSingleChoiceItems(R.array.widget_color_options, -1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which==0) {
					//Light theme.
					sharedPreferences.edit().putString("" + mAppWidgetId, "LIGHT").commit();
				} else if (which==1) {
					//Dark theme.
					sharedPreferences.edit().putString("" + mAppWidgetId, "DARK").commit();
				}
				
				updateWidgetConfig();
			}
			
		});
		
		builder.create().show();
	}
	
	private void updateWidgetConfig() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(),
											R.layout.large_widget_layout);
											appWidgetManager.updateAppWidget(mAppWidgetId, views);
											
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		updateWidget();
		finish();
	}
	
	private void updateWidget() {
		try {
			Intent largeWidgetIntent = new Intent(this, LargeWidgetProvider.class);
			largeWidgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			int largeWidgetIds[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, LargeWidgetProvider.class));
			largeWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, largeWidgetIds);
			this.sendBroadcast(largeWidgetIntent);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}