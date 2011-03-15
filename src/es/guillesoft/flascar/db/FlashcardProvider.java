package es.guillesoft.flascar.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class FlashcardProvider extends ContentProvider {
	
	public enum Sense { AB, BA };
	
	private FlashcardDBHelper dbHelper;
    public SQLiteDatabase dbR;
    private SQLiteDatabase dbRW;
    
    private static final String AUTHORITY = "es.guillesoft.flascar.db.FlashcardProvider";
    private static final String BASE_CT = "vnd.android.cursor.item/vnd.guillesoft.";
	
    private static final UriMatcher uriMatcher;
    private static List<Entity> entities = new ArrayList<Entity>();
    
    // base entities
    private static final int CARDFILE = 0;
    public static final Cardfile cardfile = new Cardfile( AUTHORITY, BASE_CT );
    private static final int CARD = 1;
    public static final Card card = new Card( AUTHORITY, BASE_CT );
    private static final int BOX = 2;
    public static final Box box = new Box( AUTHORITY, BASE_CT );
    private static final int MAIN = 3;
    public static final Main main = new Main( AUTHORITY, BASE_CT );
    
    static {
    	
    	uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    	
    	entities.add( cardfile );
    	uriMatcher.addURI( AUTHORITY, Cardfile.URI_PATTERN, CARDFILE );
    	
    	entities.add( card );
    	uriMatcher.addURI( AUTHORITY, Card.URI_PATTERN, CARD );
    	
    	entities.add( box ); 
    	uriMatcher.addURI( AUTHORITY, Box.URI_PATTERN, BOX );
    	
    	entities.add( main ); 
    	uriMatcher.addURI( AUTHORITY, Main.URI_PATTERN, MAIN );
    	
    }
        
	@Override
	public boolean onCreate() {

		Log.d( "FlashcardProvider", "CREATE" );
		
		Context context = getContext();
		dbHelper = new FlashcardDBHelper( context, entities );
		dbR = dbHelper.getReadableDatabase();
		dbRW = dbHelper.getWritableDatabase();

		return dbR != null && dbRW != null ? true : false;
      
	}
	
	@Override
	public int delete( Uri uri, String where, String[] whereArgs ) {
		
		try {
			
			int count = entities.get( uriMatcher.match( uri ) ).delete( dbRW, uri, where, whereArgs );
			getContext().getContentResolver().notifyChange( uri, null );
			return count;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}

	@Override
	public String getType( Uri uri ) {

		try {
			
			return entities.get( uriMatcher.match( uri ) ).getContentType( uri );
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}
		
	@Override
	public Uri insert( Uri uri, ContentValues initialValues ) {

		try {
			
			Uri newUri = entities.get( uriMatcher.match( uri ) ).insert( dbRW, uri, initialValues );
			getContext().getContentResolver().notifyChange( newUri, null );
			return newUri;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}
			
	@Override
	public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
			
		try {
			
			Cursor cursor = entities.get( uriMatcher.match( uri ) ).query( dbR, uri, projection, selection, selectionArgs, sortOrder );
	        cursor.setNotificationUri( getContext().getContentResolver(), uri );
	        return cursor;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
				
	}
	
	@Override
    public int update( Uri uri, ContentValues values, String where, String[] whereArgs ) {

		try {
			
			int count = entities.get( uriMatcher.match( uri ) ).update( dbRW, uri, values, where, whereArgs );
			getContext().getContentResolver().notifyChange( uri, null );
	        return count;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
				
	}
		
}
