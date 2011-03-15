package es.guillesoft.flascar.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class Box extends Entity {
    
	public static final String URI_ROOT = "box";
	public static final String URI_PATTERN = URI_ROOT + "/*";
	public static final String URI_PLAIN = "plain";
	
	public static final String TABLE = "box";
	
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String EXPIRATION = "expiration";
    
    private static HashMap<String, String> PMAP = new HashMap<String, String>();
    static {
    	PMAP.put( ID, ID );
    	PMAP.put( NAME, NAME );
    	PMAP.put( EXPIRATION, EXPIRATION );
    }

    public Box( String authority, String baseCT ) {
		super( authority, baseCT );
	}

    public Uri getUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + URI_PLAIN );
    }

    public String getContentType( Uri uri ) {
		return getBaseCT() + URI_ROOT + "." + URI_PLAIN;
	}
	
	public void create( SQLiteDatabase db ) {
    	
		db.execSQL(
	              "create table " + TABLE + " (" +	
	              ID + " integer primary key autoincrement, " +
	              NAME + " text not null, " +
	              EXPIRATION + " text not null);" );
		
    	ContentValues initialValues = new ContentValues();
    	initialValues.put( ID,  1 );
    	initialValues.put( NAME,  "Caja 1D" );
    	initialValues.put( EXPIRATION,  "-1 days" );
    	db.insert( TABLE, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  2 );
    	initialValues.put( NAME,  "Caja 3D" );
    	initialValues.put( EXPIRATION,  "-3 days" );
    	db.insert( TABLE, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  3 );
    	initialValues.put( NAME,  "Caja 1S" );
    	initialValues.put( EXPIRATION,  "-7 days" );
    	db.insert( TABLE, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  4 );
    	initialValues.put( NAME,  "Caja 1M" );
    	initialValues.put( EXPIRATION,  "-1 months" );
    	db.insert( TABLE, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  5 );
    	initialValues.put( NAME,  "Caja 3M" );
    	initialValues.put( EXPIRATION,  "-3 months" );
    	db.insert( TABLE, null, initialValues );

    }
    
    public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
    	
    	if( oldVersion != FlashcardDBHelper.V_ALFA )
    		db.execSQL( "drop table if exists " + TABLE );
    	else Log.d( "Box", "Upgrade not needed" );
    	create( db );
    	
    }
    
	public Uri insert( SQLiteDatabase db, Uri uri, ContentValues initialValues )
		throws IllegalArgumentException, SQLException {
		
		throw new IllegalArgumentException( "Box: insert not allowed" );
		
	}

	public int delete( SQLiteDatabase db, Uri uri, String where, String[] whereArgs ) {

		throw new IllegalArgumentException( "Box: delete not allowed" );
		
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
		
		throw new IllegalArgumentException( "Box: update not allowed" );
		
	}
}
