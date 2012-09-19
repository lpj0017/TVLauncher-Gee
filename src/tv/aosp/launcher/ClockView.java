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

import java.text.SimpleDateFormat;
import java.util.Date;

import tv.aosp.launcher.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A digital clock view that also holds icons for the message and app tray.
 * 
 *
 */

public class ClockView extends FrameLayout {

	TextView time;
	Typeface roboto;
	SimpleDateFormat sdf;
	
	ImageView trayButton;

	int lastMinute = -1;
	
	
	public static interface ClockViewListener{
		
		public void onTrayClicked();
		
	}
	
	ClockViewListener mClockViewListener;
	
	public ClockView(Context context,ClockViewListener listener) {
		super(context);

		mClockViewListener = listener;
		
		sdf = new SimpleDateFormat("hh:mma");

		addView(inflate(context, R.layout.clock, null));
		roboto = Typeface.createFromAsset(getResources().getAssets(),"roboto-medium.ttf");
		time = ((TextView) findViewById(R.id.TextViewTime));
		time.setTypeface(roboto);
		
		findViewById(R.id.imageViewNotifications).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showStatusBar(getContext());
			}
		});
		
		findViewById(R.id.imageViewDrawer).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mClockViewListener!=null)
					mClockViewListener.onTrayClicked();
			}
		});
		
		trayButton = (ImageView) findViewById(R.id.imageViewDrawer);
		
		this.setWillNotDraw(false);
	}


	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Date now = new Date();
		int currentMinute = now.getMinutes();
		if (currentMinute == lastMinute) {

			postInvalidateDelayed(200);
			// System.out.println("No update needed");
			return;
		}
		lastMinute = currentMinute;

		time.setText(sdf.format(now));
		
		postInvalidateDelayed(200);
	}
}
