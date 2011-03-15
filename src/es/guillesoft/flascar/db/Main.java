package es.guillesoft.flascar.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class Main extends Entity {
	
	public static final String URI_ROOT = "flascar";
	public static final String URI_PATTERN = URI_ROOT;
	
	public static final String TABLE = "flascar";
    
    public static final String ID = "_id";
    public static final String CURRENT_CARDFILE = "current_cardfile";

    private static HashMap<String, String> PMAP = new HashMap<String, String>();
    static {
    	PMAP.put( ID, ID );
    	PMAP.put( CURRENT_CARDFILE, CURRENT_CARDFILE );
    }
	
    public Main( String authority, String baseCT ) {
		super( authority, baseCT );
	}

    public Uri getUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT );
    }
    
	public String getContentType( Uri uri ) {
		return getBaseCT() + URI_ROOT;
	}
	
    public void create( SQLiteDatabase db ) {
    	
		db.execSQL(
	              "create table " + TABLE + " (" +	
	              ID + " integer primary key autoincrement, " +
	              CURRENT_CARDFILE + " integer);" );
		
		ContentValues initialValues = new ContentValues();
    	initialValues.put( ID,  1 );
    	db.insert( TABLE, null, initialValues );
    	
    }
    
    public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
    	db.execSQL( "drop table if exists " + TABLE );
    	create( db );
    }
 
	public Uri insert( SQLiteDatabase db, Uri uri, ContentValues initialValues )
		throws IllegalArgumentException, SQLException {

		throw new IllegalArgumentException( "Main: insert not allowed" );
		
	}
	
	public int delete( SQLiteDatabase db, Uri uri, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException {
			
		throw new IllegalArgumentException( "Main: delete not allowed" );
		
	}
	
	public Cursor query( SQLiteDatabase db, Uri uri, 
			String[] projection, String selection, String[] selectionArgs, String sortOrder)
		throws IllegalArgumentException, SQLException {	
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		
		qb.setTables( TABLE );
		qb.setProjectionMap( PMAP );
				
		Log.d( "query", qb.buildQuery( projection, selection, selectionArgs, groupBy, null, sortOrder, null));
		Cursor c = qb.query( db, projection, selection, selectionArgs, groupBy, null, sortOrder );
		
		return c;
		    
	}
		
	public int update( SQLiteDatabase db, Uri uri, ContentValues values, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException {
		
		return db.update( TABLE, values, where, whereArgs);
		
	}
}