package es.guillesoft.flascar.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class Cardfile {
    /*
	
	//public static final String URI_REVIEW = "review";
	public static final String URI_REVIEW_A = "review-a";
	public static final String URI_REVIEW_B = "review-b";
	public static final String URI_VIEW = "myview";
	    
    private static HashMap<String, String> COUNT_PMAP = new HashMap<String, String>();
    static {
    	COUNT_PMAP.put( ID, TABLE + "." + ID + " as " + ID );
    	COUNT_PMAP.put( NAME, TABLE + "." + NAME + " as " + NAME );
    	COUNT_PMAP.put( SIDE_A, TABLE + "." + SIDE_A + " as " + SIDE_A);
    	COUNT_PMAP.put( SIDE_B, TABLE + "." + SIDE_B + " as " + SIDE_B );
    	COUNT_PMAP.put( COUNT_CARDS, "count (" + Card.TABLE + "." + Card.ID + ") as " + COUNT_CARDS );
    }
    
    public static final String BOX_ID = "_id";
    public static final String BOX_NAME = "name";
    public static final String BOX_TOTAL = "total";
    public static final String BOX_DIRTY = "dirty";
    
    private static HashMap<String, String> BOX_PMAP = new HashMap<String, String>();
	static {
		BOX_PMAP.put( BOX_ID, "t1." + BOX_ID + " as " + BOX_ID );
		BOX_PMAP.put( BOX_NAME, "t1." + BOX_NAME + " as " + BOX_NAME );
		BOX_PMAP.put( BOX_TOTAL, "t1.total as " + BOX_TOTAL );
		BOX_PMAP.put( BOX_DIRTY, "t2.total as " + BOX_DIRTY );
	}		
	
	public static final String REVIEW_ID = "_id";
	public static final String REVIEW_SIDE_A = "side_a";
	public static final String REVIEW_SIDE_B = "side_b";
	public static final String REVIEW_BOX_A = "box_a";
	public static final String REVIEW_BOX_B = "box_b";
	public static final String REVIEW_LAST_CHECKED_A = "last_checked_a";
	public static final String REVIEW_LAST_CHECKED_B = "last_checked_b";
	
	private static HashMap<String, String> REVIEW_A_PMAP = new HashMap<String, String>();
	static {
		REVIEW_A_PMAP.put( REVIEW_ID, Card.TABLE + "." + Card.ID + " as " + REVIEW_ID );
		REVIEW_A_PMAP.put( REVIEW_SIDE_A, Card.TABLE + "." + Card.SIDE_A + " as " + REVIEW_SIDE_A );
		REVIEW_A_PMAP.put( REVIEW_SIDE_B, Card.TABLE + "." + Card.SIDE_B + " as " + REVIEW_SIDE_B );
		REVIEW_A_PMAP.put( REVIEW_BOX_A, Card.TABLE + "." + Card.BOX_A + " as " + REVIEW_BOX_A );
		REVIEW_A_PMAP.put( REVIEW_LAST_CHECKED_A, Card.TABLE + "." + Card.LAST_CHECKED_A + " as " + REVIEW_LAST_CHECKED_A );
	}
	
	private static HashMap<String, String> REVIEW_B_PMAP = new HashMap<String, String>();
	static {
		REVIEW_B_PMAP.put( REVIEW_ID, Card.TABLE + "." + Card.ID + " as " + REVIEW_ID );
		REVIEW_B_PMAP.put( REVIEW_SIDE_A, Card.TABLE + "." + Card.SIDE_A + " as " + REVIEW_SIDE_A );
		REVIEW_B_PMAP.put( REVIEW_SIDE_B, Card.TABLE + "." + Card.SIDE_B + " as " + REVIEW_SIDE_B );
		REVIEW_B_PMAP.put( REVIEW_BOX_B, Card.TABLE + "." + Card.BOX_B + " as " + REVIEW_BOX_B );
		REVIEW_B_PMAP.put( REVIEW_LAST_CHECKED_B, Card.TABLE + "." + Card.LAST_CHECKED_B + " as " + REVIEW_LAST_CHECKED_B );
	}
	
	private static HashMap<String, String> TEST_PMAP = new HashMap<String, String>();
	static {
		TEST_PMAP.put( "expiration", "box.expiration as expiration" );
		TEST_PMAP.put( "lch1", "card.last_checked_b as lch1" );
		TEST_PMAP.put( "lch2", "datetime (card.last_checked_b, '-10 minutes') as lch2" );
		TEST_PMAP.put( "lch3", "datetime ('now', box.expiration) as lch3" );
	}
	
    public Cardfile( String authority, String baseCT ) {
		super( authority, baseCT );
	}
    
    public Uri getUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + URI_PLAIN );
    }
    
    public Uri getCountUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + URI_COUNT );
    }
    
    public Uri getBoxUri( FlashcardProvider.Sense sense ) {
    	String str = sense == FlashcardProvider.Sense.AB ?  URI_BOX_A : URI_BOX_B; 
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + str );
    }
    
    public Uri getReviewUri( FlashcardProvider.Sense sense ) {
    	String str = sense == FlashcardProvider.Sense.AB ?  URI_REVIEW_A : URI_REVIEW_B; 
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + str );
    }
    
    public Uri getTestUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + URI_TEST );
    }
    
    public Uri getViewUri() {
    	return Uri.parse( "content://" + getAuthority() + "/" + URI_ROOT + "/" + URI_VIEW );
    }
    
	public String getContentType( Uri uri ) {
		if( uri.getLastPathSegment().equals( URI_COUNT ) ) return getBaseCT() + URI_ROOT + "." + URI_COUNT;
		else return getBaseCT() + URI_ROOT;
	}

	public void create( SQLiteDatabase db ) {
    	db.execSQL(
    			"create table " + TABLE + " (" +	
    			ID + " integer primary key autoincrement, " +
    			NAME + " text not null, " +
    			SIDE_A + " text not null, " +
    			SIDE_B + " text not null);" );
    	new CardfileView().create( db );
    }
    
    public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
    	if( oldVersion != FlashcardDBHelper.V_ALFA ) {
    		db.execSQL( "drop table if exists " + TABLE );
    		db.execSQL( "drop view if exists " + CardfileView.VIEW_NAME );
    	}
    	else Log.d( "Cardfile", "Upgrade not needed" );
    	create( db );
    }
 
	public Uri insert( SQLiteDatabase db, Uri uri, ContentValues initialValues )
		throws IllegalArgumentException, SQLException {

		if( ! uri.getLastPathSegment().equals( URI_PLAIN ) ) 
			throw new IllegalArgumentException( "insert not allowed: " + uri );
				
		if( initialValues == null ) initialValues = new ContentValues();
		
		if( initialValues.containsKey( ID ) )
			throw new IllegalArgumentException( ID + " is a private field" );

		// @todo ver si es necesario el nullHack
		long rowId = db.insert( TABLE, NAME, initialValues );
	
        if (rowId > 0) return ContentUris.withAppendedId( getUri(), rowId );
        else throw new SQLException( "Failed to insert row into " + uri );
		
	}
	
	public int delete( SQLiteDatabase db, Uri uri, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException {
		
		if( ! uri.getLastPathSegment().equals( URI_PLAIN ) ) 
			throw new IllegalArgumentException( "delete not allowed: " + uri );
		
		// @todo delete en cascada de las cards
		return db.delete( TABLE, where, whereArgs );
		
	}
	
	public Cursor query( SQLiteDatabase db, Uri uri,  
			String[] projection, String selection, String[] selectionArgs, String sortOrder )
		throws IllegalArgumentException, SQLException {
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		String now = dateFormat.format( date );
		
		if( uri.getLastPathSegment().equals( URI_COUNT ) ) { 
				
			qb.setTables( 
					TABLE + " left outer join " + Card.TABLE + 
					" on (" + TABLE + "." + ID + " = " + 
					Card.TABLE + "." + Card.CARDFILE_ID + ")");
			
			groupBy = TABLE + "." + ID;
			qb.setProjectionMap( COUNT_PMAP );
			
		}
		else if( uri.getLastPathSegment().equals( URI_BOX_A ) ) {

			qb.setTables( 
					"(select " + Box.TABLE + "." + Box.ID + " as " + BOX_ID + ", " +
					Box.TABLE + "." + Box.NAME + " as " + BOX_NAME + 
					", count(" + Card.TABLE + "." + Card.ID + ") total from " +
					Box.TABLE + " left outer join (select * from " + Card.TABLE +
					" where " + Card.CARDFILE_ID + " = ? ) " + Card.TABLE +
					" on (" + Box.TABLE + "." + Box.ID + " = " + 
					Card.TABLE + "." + Card.BOX_A + " ) group by " + Box.TABLE +  "." + Box.ID + ") t1 " +
					"join (select " + Box.TABLE + "." + Box.ID + " as " + BOX_ID + ", " +
					" count(" + Card.TABLE + "." + Card.ID + ") total from " +
					Box.TABLE + " left outer join (select * from " + Card.TABLE +
					" where " + Card.CARDFILE_ID + " = ? ) " + Card.TABLE +
					" on (" + Box.TABLE + "." + Box.ID + " = " + 
					Card.TABLE + "." + Card.BOX_A + " and " + Card.TABLE + "." + Card.LAST_CHECKED_A + " < datetime ('" + now + "', " +
					Box.TABLE + "." + Box.EXPIRATION + ")) group by " + Box.TABLE +  "." + Box.ID + ") t2 " +
					"on (t1." + BOX_ID + " = t2." + BOX_ID + ")" ); 
			
			qb.setProjectionMap( BOX_PMAP );
			
		}
		else if( uri.getLastPathSegment().equals( URI_BOX_B ) ) {

			qb.setTables( 
					"(select " + Box.TABLE + "." + Box.ID + " as " + BOX_ID + ", " +
					Box.TABLE + "." + Box.NAME + " as " + BOX_NAME + 
					", count(" + Card.TABLE + "." + Card.ID + ") total from " +
					Box.TABLE + " left outer join (select * from " + Card.TABLE +
					" where " + Card.CARDFILE_ID + " = ? ) " + Card.TABLE +
					" on (" + Box.TABLE + "." + Box.ID + " = " + 
					Card.TABLE + "." + Card.BOX_B + " ) group by " + Box.TABLE +  "." + Box.ID + ") t1 " +
					"join (select " + Box.TABLE + "." + Box.ID + " as " + BOX_ID + ", " +
					" count(" + Card.TABLE + "." + Card.ID + ") total from " +
					Box.TABLE + " left outer join (select * from " + Card.TABLE +
					" where " + Card.CARDFILE_ID + " = ? ) " + Card.TABLE +
					" on (" + Box.TABLE + "." + Box.ID + " = " + 
					Card.TABLE + "." + Card.BOX_B + " and " + Card.TABLE + "." + Card.LAST_CHECKED_B + " < datetime ('" + now + "', " +
					Box.TABLE + "." + Box.EXPIRATION + ")) group by " + Box.TABLE +  "." + Box.ID + ") t2 " +
					"on (t1." + BOX_ID + " = t2." + BOX_ID + ")" ); 
			
			qb.setProjectionMap( BOX_PMAP );

		}
		else if( uri.getLastPathSegment().equals( URI_REVIEW_A ) ) {

			qb.setTables( Box.TABLE + ", " + Card.TABLE );
			
			
			selection = 
				Card.TABLE + "." + Card.CARDFILE_ID + " = ? and " + 
				Card.TABLE + "." + Card.BOX_A + " = ? and " + 
				Card.TABLE + "." + Card.BOX_A + " = " + Box.TABLE + "." + Box.ID + " and " +
				Card.TABLE + "." + Card.LAST_CHECKED_A + " < datetime ('" + now + "', " +
				Box.TABLE + "." + Box.EXPIRATION + ")";

			qb.setProjectionMap( REVIEW_A_PMAP );
			
		}
		else if( uri.getLastPathSegment().equals( URI_REVIEW_B ) ) {

			qb.setTables( Box.TABLE + ", " + Card.TABLE );
			
			selection = 
				Card.TABLE + "." + Card.CARDFILE_ID + " = ? and " + 
				Card.TABLE + "." + Card.BOX_B + " = ? and " + 
				Card.TABLE + "." + Card.BOX_B + " = " + Box.TABLE + "." + Box.ID + " and " +
				Card.TABLE + "." + Card.LAST_CHECKED_B + " < datetime ('" + now + "', " +
				Box.TABLE + "." + Box.EXPIRATION + ")";

			qb.setProjectionMap( REVIEW_B_PMAP );
			
		}
		else if( uri.getLastPathSegment().equals( URI_TEST ) ) {

			qb.setTables( Box.TABLE + ", " + Card.TABLE );
			
			selection = 
				Card.TABLE + "." + Card.CARDFILE_ID + " = ? and " + 
				Card.TABLE + "." + Card.BOX_B + " = ? and " + 
				Card.TABLE + "." + Card.BOX_B + " = " + Box.TABLE + "." + Box.ID;
						
			qb.setProjectionMap( TEST_PMAP );
			

		}
		else if( uri.getLastPathSegment().equals( URI_VIEW ) ) {

			// !!!
			return new CardfileView().query( db, projection, selection, selectionArgs, sortOrder );
			

		}
		else {
			
			qb.setTables( TABLE );
			qb.setProjectionMap( PMAP );
			
		}
		
		Log.d( "query", qb.buildQuery( projection, selection, selectionArgs, groupBy, null, sortOrder, null));
		Cursor c = qb.query( db, projection, selection, selectionArgs, groupBy, null, sortOrder );
		
		return c;
		    
	}
	
	public int update( SQLiteDatabase db, Uri uri, ContentValues values, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException {
		
		if( ! uri.getLastPathSegment().equals( URI_PLAIN ) ) 
			throw new IllegalArgumentException( "update not allowed: " + uri );
		
		return db.update( TABLE, values, where, whereArgs);
		
	}
	*/
	
}
