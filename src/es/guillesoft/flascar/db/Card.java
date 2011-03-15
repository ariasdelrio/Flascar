package es.guillesoft.flascar.db;

import java.util.HashMap;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class Card extends Entity {
	
	public static final String URI_ROOT = "card";
	public static final String URI_PATTERN = URI_ROOT;
	
	public static final String TABLE = "card";
    
    public static final String ID = "_id";
    public static final String CARDFILE_ID = "cardfile_id";
    public static final String BOX_A = "box_a";
    public static final String BOX_B = "box_b";
    public static final String SIDE_A = "side_a";
    public static final String SIDE_B = "side_b";
    public static final String LAST_CHECKED_A = "last_checked_a";
    public static final String LAST_CHECKED_B = "last_checked_b";
    
    private static HashMap<String, String> PMAP = new HashMap<String, String>();
    static {
    	PMAP.put( ID, ID );
    	PMAP.put( CARDFILE_ID, CARDFILE_ID );
    	PMAP.put( BOX_A, BOX_A );
    	PMAP.put( BOX_B, BOX_B );
    	PMAP.put( SIDE_A, SIDE_A );
    	PMAP.put( SIDE_B, SIDE_B );
    	PMAP.put( LAST_CHECKED_A, LAST_CHECKED_A );
    	PMAP.put( LAST_CHECKED_B, LAST_CHECKED_B );
    }
	
    public Card( String authority, String baseCT ) {
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
	              CARDFILE_ID + " integer, " +
	              BOX_A + " integer, " +
	              BOX_B + " integer, " +
	              SIDE_A + " text not null, " +
	              SIDE_B + " text not null, " +
	              LAST_CHECKED_A + " timestamp, " +
	              LAST_CHECKED_B + " timestamp);" );	
    }
    
    public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
    	if( oldVersion != FlashcardDBHelper.V_ALFA )
    		db.execSQL( "drop table if exists " + TABLE );
    	else Log.d( "Card", "Upgrade not needed" );
    	create( db );
    }
 
	public Uri insert( SQLiteDatabase db, Uri uri, ContentValues initialValues )
		throws IllegalArgumentException, SQLException {

		if( initialValues.containsKey( ID ) )
			throw new IllegalArgumentException( ID + " is a private field" );
		
		if( !initialValues.containsKey( CARDFILE_ID ) )
			throw new IllegalArgumentException( CARDFILE_ID + " can not be NULL" );
		
		Cursor cursor = FlashcardProvider.cardfile.query(
				db,
				FlashcardProvider.cardfile.getUri(),
				null, 
				Cardfile.ID + " = " + initialValues.getAsLong( CARDFILE_ID ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + CARDFILE_ID );
		
		cursor.close();
		
		if( !initialValues.containsKey( BOX_A ) )
			throw new IllegalArgumentException( BOX_A + " can not be NULL" );
		
		cursor = FlashcardProvider.box.query(
				db,
				FlashcardProvider.box.getUri(),
				null, 
				Box.ID + " = " + initialValues.getAsLong( BOX_A ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + BOX_A );
		
		if( !initialValues.containsKey( BOX_B ) )
			throw new IllegalArgumentException( BOX_B + " can not be NULL" );
		
		cursor = FlashcardProvider.box.query(
				db,
				FlashcardProvider.box.getUri(),
				null, 
				Box.ID + " = " + initialValues.getAsLong( BOX_B ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + BOX_B );

		cursor.close();
		
		// @todo ver si es necesario el nullHack
		long rowId = db.insert( TABLE, CARDFILE_ID, initialValues );
		
        if (rowId > 0) return ContentUris.withAppendedId( getUri(), rowId );
        else throw new SQLException( "Failed to insert row into " + getUri() );
		
	}
	
	public int delete( SQLiteDatabase db, Uri uri, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException {
			
		return db.delete( TABLE, where, whereArgs );
		
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