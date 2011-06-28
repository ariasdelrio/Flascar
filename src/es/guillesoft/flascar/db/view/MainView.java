package es.guillesoft.flascar.db.view;

import es.guillesoft.flascar.db.DBUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class MainView extends View {
	
	/* TABLE main */
	
	protected static final String T_MAIN           = "main";
	public static final String ID               = "_id";
	public static final String CURRENT_CARDFILE = "current_cardfile";

	
	private static final String [] columns = {
		DBUtil.primaryColumn( ID ),
		DBUtil.numberColumn( CURRENT_CARDFILE )
	};

	@Override
	public void create( SQLiteDatabase db ) {
		Log.d( this.getClass().getSimpleName(), "create" );
		
		String sql = DBUtil.createTable( T_MAIN, columns );
		DBUtil.log( this.getClass().getSimpleName(), sql );
		db.execSQL( sql );
		
		ContentValues initialValues = new ContentValues();
    	initialValues.put( ID,  1 );
    	db.insert( T_MAIN, null, initialValues );
				
	}
	
	@Override
	public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( this.getClass().getSimpleName(), "upgrade " + oldVersion + " -> " + newVersion );

	}
     
	@Override
	public long insert( SQLiteDatabase db, ContentValues initialValues ) {
		Log.d( this.getClass().getSimpleName(), "insert" );
		
		throw new IllegalArgumentException( "Main: insert not allowed" );
		
	}
	
	@Override
	public int delete( SQLiteDatabase db, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "delete" );
			
		throw new IllegalArgumentException( "Main: delete not allowed" );
		
	}
	
	@Override
	public Cursor query( SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
		Log.d( this.getClass().getSimpleName(), "query" );
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables( T_MAIN );
		
		Log.d( "query", qb.buildQuery( projection, selection, selectionArgs, null, null, sortOrder, null));
		return qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
		
	}
	
	@Override
	public int update( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "update" );
		
		return db.update( T_MAIN, values, where, whereArgs);
		
	}
	
}