/*
 * Copyright 2012 AOSP.TV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package tv.aosp.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tv.aosp.launcher.ClockView.ClockViewListener;
import tv.aosp.launcher.TrayView.TrayEventListener;
import tv.aosp.launcher.database.DBHelper;
import tv.aosp.launcher.database.DBHelper.DBListener;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is the main activity of the Launcher.
 * 
 * @author Alexander Gee 2012
 *
 */

public class Launcher extends Activity implements ClockViewListener, DBListener, TrayEventListener {


	List<LauncherIcon> packages;
	ClockView mClockView;
	TrayView tray;
	ViewGroup trayHolder;
	ViewGroup main;

	float displayScale;
	int screenWidth;
	int clockWidth;
	int iconWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		
		setContentView(R.layout.main);

		packages = getInstalledApps(false);

		calculateScreenParams();
		setupMainbar();

		setupTray();
		
		main = ((ViewGroup) findViewById(R.id.RelativeLayoutMain));

		DBHelper.registerDBListener(this);

	}

	private void setupTray() {

		
		
		int numColumns = screenWidth / iconWidth;

		
		trayHolder = ((ViewGroup) findViewById(R.id.FrameLayoutTray));

		tray = new TrayView(this);

		tray.setNumColumns(numColumns);

		tray.setTrayEventListener(this);
		
		tray.setIcons(packages);

		trayHolder.addView(tray);

	}

	private void calculateScreenParams() {

		displayScale = getResources().getDisplayMetrics().density;
		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();

		clockWidth = (int) (160 * displayScale);
		iconWidth = (int) (100 * displayScale);

	}

	private void setupMainbar() {

		

		populateMainbar();

		mClockView = new ClockView(this, this);
		((ViewGroup) findViewById(R.id.FrameLayoutClock)).addView(mClockView);

	}
	
	private void populateMainbar(){
		
		ViewGroup mainbar = (ViewGroup) findViewById(R.id.MainBar);
		
		mainbar.removeAllViews();

		int availableSpace = screenWidth - clockWidth;

		int numItems = availableSpace % iconWidth;

		DBHelper mDbHelper = new DBHelper(this);

		for (int i = 0; i < packages.size() && numItems>0; i++) {

			if (mDbHelper.checkFavouritePosition(packages.get(i).className) != DBHelper.NOT_FAVOURITE) {

				MainBarIcon icon = new MainBarIcon(this);

				icon.setLauncherIcon(packages.get(i));

				mainbar.addView(icon);
				
				numItems--;

			}
		}
	}


	private ArrayList<LauncherIcon> getInstalledApps(boolean getSysPackages) {
		ArrayList<LauncherIcon> res = new ArrayList<LauncherIcon>();
	
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(
				intent, 0);

		for (ResolveInfo info : resolves) {

			LauncherIcon newInfo = new LauncherIcon(
					((BitmapDrawable) info.loadIcon(getPackageManager()))
							.getBitmap(),
					info.activityInfo.packageName, info.activityInfo.loadLabel(
							getPackageManager()).toString(),
					info.activityInfo.name);
			

			res.add(newInfo);
		}
		
		Collections.sort(res);

		return res;
	}

	boolean trayOpen = false;

	@Override
	public void onTrayClicked() {
		invertTray();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && trayOpen) {
			closeTray();
			return true;
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	private void openTray() {

		trayHolder.setVisibility(View.VISIBLE);
		trayHolder.requestFocus();
		main.setBackgroundColor(Color.WHITE);
		mClockView.trayButton.setImageResource(R.drawable.down_selector);
		trayOpen = true;

	}

	private void closeTray() {
		trayHolder.setVisibility(View.GONE);
		main.setBackgroundColor(Color.TRANSPARENT);
		mClockView.trayButton.requestFocus();
		mClockView.trayButton.setImageResource(R.drawable.up_selector);
		trayOpen = false;

	}

	private void invertTray() {
		if (trayOpen)
			closeTray();
		else
			openTray();
	}

	@Override
	public void tableUpdated(String table) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Table updated rebuilding mainbar");
				populateMainbar();
			}
		});

	}

	@Override
	public void onAppLaunched() {
		//closeTray();
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		closeTray();
	}
}