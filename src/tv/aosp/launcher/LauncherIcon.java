package tv.aosp.launcher;

import android.graphics.Bitmap;

/**
 * Dataclass that knows the key info about an application's icon
 * @author Alexander Gee 2012
 *
 */

public class LauncherIcon implements Comparable<LauncherIcon>{
	String id;
	String name;
	Bitmap icon;

	public int averageColour;
	public String className;
	
	public LauncherIcon(Bitmap bmp, String id, String name,String className){
		
		this.id = id;
		this.icon = bmp;
		this.name = name;
		this.className = className;
	}

	@Override
	public int compareTo(LauncherIcon another) {
		return name.compareToIgnoreCase(another.name);
	}
}