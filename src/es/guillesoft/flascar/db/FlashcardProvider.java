package es.guillesoft.flascar.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import es.guillesoft.flascar.db.view.View;

public class FlashcardProvider extends ContentProvider {
	
	public static final int V_ALFA = 1;
	public static final int V_BETA = 2;
	public static final int V_1    = 3;
	
	public static final int SENSE_AB = 1;
	public static final int SENSE_BA = 2;
	
	public static final int BOX      = 0;
	public static final int CARD     = 1;
	public static final int CARDFILE = 2;
	public static final int MAIN     = 3;
		
	private static final String DB_NAME = "flascar";
	private static final String AUTHORITY = "es.guillesoft.flascar.db.FlashcardProvider";
	private static final String BASE_CT = "vnd.android.cursor.item/vnd.guillesoft.";
	private static final int DB_VERSION = 28;
	
	private static final ViewInfo [] vinfo = new ViewInfo[4];
	static {
		vinfo[BOX]      = new ViewInfo( "box",      "es.guillesoft.flascar.db.view.BoxView"      );
		vinfo[CARD]     = new ViewInfo( "card",     "es.guillesoft.flascar.db.view.CardView"     );
		vinfo[CARDFILE] = new ViewInfo( "cardfile", "es.guillesoft.flascar.db.view.CardfileView" );
		vinfo[MAIN]     = new ViewInfo( "main",     "es.guillesoft.flascar.db.view.MainView"     );
	};
	
	private List<View> views = new ArrayList<View>();
	private UriMatcher uriMatcher;
	
	private FlashcardDBHelper dbHelper;
	private SQLiteDatabase dbR;
    private SQLiteDatabase dbRW;
    
    public static Uri getUri( int viewID ) {
    	return Uri.parse( "content://" + AUTHORITY + "/" + vinfo[viewID].uriID );
    }
    
	@Override
	public boolean onCreate() {
		Log.d( "FlashcardProvider", "CREATE" );
		
		uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    	
		try {
			
			for( int i = 0; i < vinfo.length; i++ ) {
				Log.d( "FlashcardProvider", "view " + vinfo[i].className );
				View view = (View)(Class.forName( vinfo[i].className )).newInstance();
				view.setProvider( this );
				views.add( view );
				uriMatcher.addURI( AUTHORITY, vinfo[i].uriID, i );
			}
			
		}
		catch( InstantiationException e ) {
			Log.e( "DB", "Instatiation failed: " + e );
			return false;
		}
		catch( IllegalAccessException e ) {
			Log.e( "DB", "Illegal access: " + e );
			return false;
		}
		catch( ClassNotFoundException e ) {
			Log.e( "DB", "Class not found: " + e );
			return false;
		}
		
		dbHelper = new FlashcardDBHelper( getContext() );
		dbR = dbHelper.getReadableDatabase();
		dbRW = dbHelper.getWritableDatabase();

		return dbR != null && dbRW != null ? true : false;
      
	}
	
	@Override
	public String getType( Uri uri ) {

		try {
			
			return BASE_CT + vinfo[uriMatcher.match( uri )].uriID;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}
	
	@Override
	public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
			
		try {
			
			Cursor cursor = views.get( uriMatcher.match( uri ) ).query( dbR, projection, selection, selectionArgs, sortOrder );
	        cursor.setNotificationUri( getContext().getContentResolver(), uri );
	        return cursor;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
				
	}                  

	@Override
	public Uri insert( Uri uri, ContentValues initialValues ) {

		try {
			
			int viewID = uriMatcher.match( uri );
			Uri newUri = getUri( viewID );
			long rowID = views.get( viewID ).insert( dbRW, initialValues );
			if( rowID > 0 ) {
				getContext().getContentResolver().notifyChange( newUri, null );
				return ContentUris.withAppendedId( newUri, rowID );
			}
		    else throw new SQLException( "Failed to insert row into " + uri );
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}
	
	@Override
    public int update( Uri uri, ContentValues values, String where, String[] whereArgs ) {

		try {
			
			int count = views.get( uriMatcher.match( uri ) ).update( dbRW, values, where, whereArgs );
			getContext().getContentResolver().notifyChange( uri, null );
	        return count;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
				
	}

	@Override
	public int delete( Uri uri, String where, String[] whereArgs ) {
		
		try {
			
			int count = views.get( uriMatcher.match( uri ) ).delete( dbRW, where, whereArgs );
			getContext().getContentResolver().notifyChange( uri, null );
			return count;
			
		}
		catch( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException( "Unknown URI " + uri );
		}
		
	}
	
	
	
	class FlashcardDBHelper extends SQLiteOpenHelper {
		
		FlashcardDBHelper( Context context ) {
			super( context, DB_NAME, null, DB_VERSION );
		}

	    @Override
	    public void onCreate( SQLiteDatabase db ) {

	    	Log.d( "DB", "create - begin" );
	    	for( View view : views ) view.create( db );
	        Log.d( "DB", "create - end" );
	        	
	    }

	    @Override
	    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
	        	
	    	Log.d( "DB", "upgrade - begin" );
	      	for( View view : views ) view.upgrade( db, oldVersion, newVersion );
	      	Log.d( "DB", "upgrade - end" );
	       
	    }
	    
	}
	    
}

class ViewInfo {
	
	public String uriID;
	public String className;
	
	public ViewInfo( String uriID, String className ) {
		this.uriID = uriID;
		this.className = className;
	}
	
}