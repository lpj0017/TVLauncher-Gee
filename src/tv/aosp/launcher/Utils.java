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

import java.lang.reflect.Method;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

/**
 * Static utility methods shared between many classes
 * 
 * @author Alexander Gee 2012
 * 
 */

public class Utils {

	public static void startActivity(Context context, LauncherIcon icon) {

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClassName(icon.id, icon.className);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

		startActivitySafely(context, intent);
	}

	private static void startActivitySafely(Context context, Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "Activity Not Found", Toast.LENGTH_SHORT)
					.show();
		} catch (SecurityException e) {
			Toast.makeText(context, "Activity Not Found", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
	}

	
	/**
	 * Calculates the average colour of a bitmap in the RGB space
	 * 
	 * @param bmp - The bitmap to analyse
	 * @return the average colour
	 */
	public static int averageColour(Bitmap bmp) {
		
		int step = 4;
		int r = 0, g = 0, b = 0;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int pixels = 0;
		for (int x = 0; x < width; x+=step) {
			for (int y = 0; y < height; y+=step) {

				int pixel = bmp.getPixel(x, y);
				r += Color.red(pixel);
				g += Color.green(pixel);
				b += Color.blue(pixel);
				pixels++;
			}
		}
		r /= pixels;
		g /= pixels;
		b /= pixels;

		return Color.rgb(r, g, b);
	
	}

	/**
	 * Interpolates between two colours
	 * 
	 * @param c0
	 *            - Color 0
	 * @param c1
	 *            - Color 1
	 * @param ip
	 *            - percentage of colour 0 to show range 0-100
	 * @return
	 */
	public static int getColor(int c0, int c1, int ip) {

		float p = ip;
		p /= 100;

		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0), Color.red(c1), p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0), Color.blue(c1), p);
		return Color.argb(a, r, g, b);
	}

	private static int ave(int src, int dst, float p) {
		return src + java.lang.Math.round(p * (dst - src));
	}

	public static void showStatusBar(Context context) {
		try {
			Object service = context.getSystemService("statusbar");
			Class<?> statusbarManager = Class
					.forName("android.app.StatusBarManager");
			Method expand = statusbarManager.getMethod("expand");
			expand.invoke(service);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
