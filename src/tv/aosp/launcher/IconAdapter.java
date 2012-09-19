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

import java.util.List;

import tv.aosp.launcher.TrayView.TrayEventListener;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class IconAdapter extends BaseAdapter {
    private Context mContext;

    List<LauncherIcon> mLauncherIcons;
    TrayEventListener mTrayEventListener;
    
    public IconAdapter(Context c,List<LauncherIcon> icons,TrayEventListener listener) {
        mContext = c;
        mLauncherIcons = icons;
        mTrayEventListener = listener;
    }

    public int getCount() {
        return mLauncherIcons.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	TrayIcon icon;
        if (convertView == null) { 
        	icon = new TrayIcon(mContext);
        	icon.setLauncherIcon(mLauncherIcons.get(position));
        	icon.setTrayEventListener(mTrayEventListener);
  
        } else {
        	icon = (TrayIcon) convertView;
        	icon.setLauncherIcon(mLauncherIcons.get(position));

        }
        
        return icon;
    }

   
}