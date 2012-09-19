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

import tv.aosp.launcher.database.DBHelper;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * An icon that appears on the main bar of the launcher
 * @author Alexander Gee 2012
 *
 */

public class MainBarIcon extends LinearLayout implements OnLongClickListener {

	TextView mTextView;
	
	public MainBarIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public MainBarIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	Context mContext;

	public MainBarIcon(Context context) {
		super(context);
		init(context);
	}
	
	
	private void init(Context context){
		
		inflate(context, R.layout.main_bar_icon, this);
		
		mContext = context;
		
		setFocusable(true);
		
		setOnLongClickListener(this);
		
	}

	LauncherIcon mLauncherIcon;
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if(gainFocus){
			setBackgroundColor(mLauncherIcon.averageColour);
			mTextView.setVisibility(View.VISIBLE);
		}else{
			setBackgroundColor(Color.TRANSPARENT);
			mTextView.setVisibility(View.GONE);
		}
	}
	
	public void setLauncherIcon(LauncherIcon icon){
		
		
		mLauncherIcon = icon;
		
		((ImageView)findViewById(R.id.ImageViewIcon)).setImageBitmap(icon.icon);
		
		mTextView = ((TextView)findViewById(R.id.TextViewName));
		
		mTextView.setText(icon.name);
		//setBackgroundColor(icon.averageColour);
		
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.startActivity(mContext, mLauncherIcon);
			}
		});
	}
	
	
	//Handle our long clicks
		@Override
		public boolean onLongClick(View v) {
			
			DBHelper mDbHelper = new DBHelper(mContext);
			mDbHelper.setFavouritePosition(mLauncherIcon.className, DBHelper.NOT_FAVOURITE);
			
			return true;
		}
	
}
