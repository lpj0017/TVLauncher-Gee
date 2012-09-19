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


package tv.aosp.launcher.database;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper to create the tables required to support the launcher.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    static final String DB_NAME = "launcher_apps";
    static final int DB_VERSION = 2;
    public static final String CATEGORIES_TABLE = "categories";
    public static final String APPS_TABLE = "apps";
    public static final String FAVORITES_TABLE = "favourites";

	public static final int NOT_FAVOURITE = -1;

    public DBHelper(final Context context) { //
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.execSQL("create table " + CATEGORIES_TABLE + " (_id INTEGER PRIMARY KEY autoincrement, name TEXT)");
     //   db.execSQL("create table " + APPS_TABLE + " (packageName TEXT, category_id INT)");
     //   db.execSQL("insert into " + CATEGORIES_TABLE + "(name) values ('Apps')");
        db.execSQL("create table " + FAVORITES_TABLE + " (class TEXT PRIMARY KEY, position INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       
    }
    
    
    public int checkFavouritePosition(String classname){
    	
    	SQLiteDatabase db = getReadableDatabase();
    	
    	Cursor result = db.query(FAVORITES_TABLE, new String[]{"position"}, "class = ?", new String[]{classname}, null, null, null);
    	
    	if(result.moveToNext()){
    		int pos = result.getInt(result.getColumnIndex("position"));
    		db.close();
    		return pos;
    	}else{
    		db.close();
    		return NOT_FAVOURITE;
    	}
    	
    }
    
  public void setFavouritePosition(String classname, int position){
    	
    	SQLiteDatabase db = getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	
    	values.put("class", classname);
    	values.put("position", position);
    	
    	if (db.update(FAVORITES_TABLE, values, "class = ?", new String[]{classname})==0)
    	{
    		db.insert(FAVORITES_TABLE, null, values);
    	}
    	
    	
    	db.close();
    	
    	informListenersTableUpdated(FAVORITES_TABLE);
    	
    }
  
  
  private void informListenersTableUpdated(String table){
	  for(WeakReference<DBListener> listenerRef : mDBListeners){
		  
		  DBListener listener = listenerRef.get();
		  if(listener!=null){
			  listener.tableUpdated(table);
		  }
	  }
  }
  
  public static interface DBListener{
	  
	  public void tableUpdated(String table);
  }

  private static List<WeakReference<DBListener>> mDBListeners = new ArrayList<WeakReference<DBListener>>();
    
  
  public static void registerDBListener(DBListener listener){
	  mDBListeners.add(new WeakReference<DBHelper.DBListener>(listener));
  }
}
