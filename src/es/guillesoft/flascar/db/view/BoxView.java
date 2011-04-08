package es.guillesoft.flascar.db.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import es.guillesoft.flascar.db.DBUtil;

public class BoxView extends View {
	
	/* TABLE box */
	
	protected static final String T_BOX      = "box";
	protected static final String ID         = "_id";
	protected static final String NAME       = "name";
	protected static final String EXPIRATION = "expiration";
	
	private static final String [] columnsBox = {
		DBUtil.primaryColumn( ID ),
		DBUtil.textColumn( NAME ),
		DBUtil.textColumn( EXPIRATION )
	};
		
	@Override
	public void create( SQLiteDatabase db ) {
		Log.d( this.getClass().getSimpleName(), "create" );
		
		String sql = DBUtil.createTable( T_BOX, columnsBox );
		DBUtil.log( this.getClass().getName(), sql );
		db.execSQL( sql );
		
		ContentValues initialValues = new ContentValues();
    	initialValues.put( ID,  1 );
    	initialValues.put( NAME,  "a diario" );
    	initialValues.put( EXPIRATION,  "-1 days" );
    	db.insert( T_BOX, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  2 );
    	initialValues.put( NAME,  "cada tres días" );
    	initialValues.put( EXPIRATION,  "-3 days" );
    	db.insert( T_BOX, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  3 );
    	initialValues.put( NAME,  "semanal" );
    	initialValues.put( EXPIRATION,  "-7 days" );
    	db.insert( T_BOX, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  4 );
    	initialValues.put( NAME,  "mensual" );
    	initialValues.put( EXPIRATION,  "-1 months" );
    	db.insert( T_BOX, null, initialValues );
    	
    	initialValues = new ContentValues();
    	initialValues.put( ID,  5 );
    	initialValues.put( NAME,  "cada tres meses" );
    	initialValues.put( EXPIRATION,  "-3 months" );
    	db.insert( T_BOX, null, initialValues );
		
	}

	@Override
	public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( this.getClass().getSimpleName(), "upgrade " + oldVersion + " -> " + newVersion );
		
//		if( oldVersion <= FlashcardProvider.V_1 ) {
			
			db.execSQL( DBUtil.dropTable( T_BOX ) );
			create( db );
			
//		}


	}
	
	@Override
	public Cursor query( SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
		Log.d( this.getClass().getSimpleName(), "query" );
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables( T_BOX );
		return qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
		
	}
	
	@Override
	public long insert( SQLiteDatabase db, ContentValues initialValues ) {
		Log.d( this.getClass().getSimpleName(), "insert" );
		
		throw new IllegalArgumentException( "Box: insert not allowed" );

	}
	
	@Override
	public int delete( SQLiteDatabase db, String where, String[] whereArgs ) {
		Log.d( this.getClass().getName(), "delete" );
		
		throw new IllegalArgumentException( "Box: delete not allowed" );
	
	}
	
	@Override
	public int update( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs ) {
		Log.d( this.getClass().getName(), "update" );
		
		throw new IllegalArgumentException( "Box: update not allowed" );
		
	}

}