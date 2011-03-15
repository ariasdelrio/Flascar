package es.guillesoft.flascar.db;

import java.util.List;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.Context;

class FlashcardDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "flascar";
	public static final int V_ALFA = 1;
	public static final int V_BETA = 2;
	private static final int DB_VERSION = 7;

	private List<Entity> entities;
	
	FlashcardDBHelper( Context context, List<Entity> entities ) {
		super( context, DB_NAME, null, DB_VERSION );
		this.entities = entities;
	}

    @Override
    public void onCreate( SQLiteDatabase db ) {

    	Log.d("DB", "CREATE BEGIN");
    	for( Entity entity : entities ) entity.create( db );
        Log.d("DB", "CREATE END");
        	
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        	
      	Log.d("DB", "UPGRADE BEGIN");
      	for( Entity entity : entities ) entity.upgrade( db, oldVersion, newVersion );
        Log.d("DB", "UPGRADE END");
       
    }
                    
        
}