package es.guillesoft.flascar.dm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.MainView;
import es.guillesoft.flascar.dm.op.CardfileComparator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Main {
	
	private ContentResolver cr;
	private CardfileCollection cardfileCollection;
	private long currentCardfile;
	
	public Main( ContentResolver cr ) {
		
		this.cr = cr;
		cardfileCollection = new CardfileCollection( cr );
		currentCardfile = -1;

	}
	
	public int getCardfileCount() {
		
		try {
		
			return cardfiles().size();
			
		}
		catch( Exception e ) {
			
			return 0;
			
		}
		
	}
	
	public Cardfile getCardfile( long id ) throws Exception {
		
		return cardfiles().get( id );
		
	}
	
	public List<Cardfile> getCardfiles() {
    
		try {
			
			List<Cardfile> list = new ArrayList<Cardfile>( cardfiles().values() );
			
			Collections.sort( list, new CardfileComparator() );
			
			return list;
			
		}
		catch( Exception e ) {
			
			return null;
			
		}
		
    }

	public Cardfile addCardfile( String name, String sideA, String sideB ) throws Exception {
    	
    	return cardfileCollection.addCardfile( name, sideA, sideB );
    	
    }
	
	//TODO: deleteCardfile
	
    public Cardfile getCurrentCardfile() {
		
    	try {
    		
    		if( currentCardfile != -1 ) return cardfiles().get( currentCardfile );
    	 
    		Cursor c = cr.query( 
    				FlashcardProvider.getUri( FlashcardProvider.MAIN ), 
    				new String[] { MainView.CURRENT_CARDFILE }, 
    				DBUtil.equals( MainView.ID, "1" ),
    				null, 
    				null );
		
    		if( c == null || c.moveToFirst() == false ) throw new Exception( "cursor is empty or query failed" );
    	
    		int index = c.getColumnIndex( MainView.CURRENT_CARDFILE );
    		if( c.isNull( index ) ) return null;
    			
    		long id = c.getLong( index );
    		Cardfile cardfile = cardfiles().get( id );
    		if( cardfile == null ) throw new Exception( "current cardfile points to invalid ID" );
    		
    		currentCardfile = id;
    		return cardfile;
    		
    	}
    	catch( Exception e ) {
    		
    		Log.d( this.getClass().getSimpleName(), "error retrieving current cardfile: " + e );
    		return null;
    		
    	}
    	
	}
    
    public void setCurrentCardfile( Cardfile cardfile ) {
    
    	try {
    		
    		long id = cardfile.getID();
    		if( !cardfiles().containsKey( id ) ) 
    			throw new IllegalArgumentException( "cardfile " + id + " does not exist" );
    		
    		ContentValues values = new ContentValues();
    		values.put( MainView.CURRENT_CARDFILE, id );
    	
    		if( cr.update(
    			FlashcardProvider.getUri( FlashcardProvider.MAIN ),
    			values,
    			DBUtil.equals( MainView.ID, "1" ),
    			null) == 1 ) {
    		
    			currentCardfile = id;
    		
    		}
    	
    	}
    	catch( Exception e ) {
			
			Log.d( this.getClass().getSimpleName(), "error setting current cardfile: " + e );
			
		}
    	
    }
    
    /* private */
    
    private HashMap<Long, Cardfile> cardfiles() throws Exception {
    	
    	try {
		
    		return cardfileCollection.getCardfiles();
    		
    	}
    	catch( Exception e ) {
    		
    		Log.e( this.getClass().getSimpleName(), "cardfiles(): " + e );
    		throw e;
    	}
		
	}
    
}



/* SIN LLEVAR A MEMORIA */

//package es.guillesoft.flascar.dm;
//
//import java.util.List;
//
//import es.guillesoft.flascar.db.DBUtil;
//import es.guillesoft.flascar.db.FlashcardProvider;
//import es.guillesoft.flascar.db.view.CardfileView;
//import es.guillesoft.flascar.db.view.MainView;
//
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.net.Uri;
//import android.util.Log;
//
//public class Main {
//	
//	private ContentResolver cr;
//	
//	private Cursor cardfiles;
//	private Cardfile currentCardfile;
//	private boolean read;
//	
//	public Main( ContentResolver cr ) {
//		
//		this.cr = cr;
//		cardfiles = null;
//		currentCardfile = null;
//		read = false;
//
//	}
//
//    public Cardfile getCurrentCardfile() {
//		
//    	if( read ) return currentCardfile;
//    	 
//    	Cursor c = cr.query( 
//    		FlashcardProvider.getUri( FlashcardProvider.MAIN ), 
//    		new String[] { MainView.CURRENT_CARDFILE }, 
//			MainView.ID + " = 1",
//			null, 
//			null );
//		
//    	if( c == null || c.moveToFirst() == false ) { 
//    		Log.e( this.getClass().getSimpleName(), "getCurrentCardfile failed" );
//    		return null;
//    	}
//    	
//    	int index = c.getColumnIndex( MainView.CURRENT_CARDFILE );
//    	read = true;
//    	
//    	if( !c.isNull( index ) ) currentCardfile = readCardfile( c.getLong( index ) );
//
//    	return currentCardfile;
//    	
//	}
//    
//    public void setCurrentCardfile( Cardfile cardfile ) {
//    
//    	ContentValues values = new ContentValues();
//    	values.put( MainView.CURRENT_CARDFILE, cardfile.getID() );
//    	
//    	if( cr.update(
//    			FlashcardProvider.getUri( FlashcardProvider.MAIN ),
//    			values,
//    			DBUtil.equals( MainView.ID, "1" ),
//    			null) == 1 ) {
//    		
//    		currentCardfile = cardfile;
//    		
//    	}
//    	
//    }
//
//    public Cardfile getCardfileByID( long id ) {
//     	 
//    	return readCardfile( id );
//		
//    }
//	
//    public int getCardfileCount() {
//    	
//    	if( cardfiles == null ) readCardfiles();
//    	return cardfiles.getCount();
//    	
//    }
//    
////    public Cardfile getCardfile( int index ) {
////    	
////    	if( index < 0 || index >= getCardfileCount() ) return null;
////    	cardfiles.moveToPosition( index );
////    	return cursorToCardfile( cardfiles );
////    	
////    }
//    
//    public Cardfile addCardfile( String name, String sideA, String sideB ) {
//    	
//    	Cardfile newCardfile = createCardfile( name, sideA, sideB );
//    	readCardfiles();
//    	return newCardfile;
//    	
//    }
//    
//    /* protected */
//    
//    protected ContentResolver getContentResolver() {
//    	return cr;
//    }
//    
//    protected void notifyUpdate( long id ) {
//
//    	readCardfiles();
//    	if( read && currentCardfile.getID() == id ) read = false;
//    	
//    }
//    
//    /* private */
//    
//    private void readCardfiles() {
//    	
//    	cardfiles  = cr.query( FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), null,	null, null,	null );
//    	
//    }
//	
//	private Cardfile readCardfile( long id ) {
//		
//		Cursor c = cr.query( 
//			FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), 
//			new String[] { CardfileView.ID, CardfileView.NAME, CardfileView.SIDE_A, CardfileView.SIDE_B, CardfileView.CARDS }, 
//			CardfileView.ID + " = ?", 
//			new String[] { new Long( id ).toString() }, 
//			null );
//			
//		if( c == null || c.moveToFirst() == false ) return null;
//		return cursorToCardfile( c );
//		
//	}
//	
//	private Cardfile cursorToCardfile( Cursor cursor ) throws IllegalArgumentException {
//		
//		try {
//		
//			int index = cursor.getColumnIndexOrThrow( CardfileView.ID );
//			if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Cardfile must have ID" );
//			long id =  cursor.getLong( index ); 
//		
//			index = cursor.getColumnIndexOrThrow( CardfileView.NAME );
//			String name = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//		
//			index = cursor.getColumnIndexOrThrow( CardfileView.SIDE_A );
//			String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//		
//			index = cursor.getColumnIndexOrThrow( CardfileView.SIDE_B );
//			String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//		
//			long cardCount = 0;
//			index = cursor.getColumnIndex( CardfileView.CARDS );
//			if( index != -1 && !cursor.isNull( index ) ) cardCount = cursor.getLong( index ); 
//		
//			return new Cardfile( this, id, name, sideA, sideB, cardCount );
//		
//		}
//		catch( IllegalArgumentException e ) {
//		
//			Log.e( "Cardfile", "Illegal argument" );
//			throw e;
//		
//		}
//	
//	}
//	
//	private Cardfile createCardfile( String name, String sideA, String sideB ) {
//    	
//    	Log.d( "Cardfile", "create" );
//		
//		ContentValues values = new ContentValues();
//		values.put( CardfileView.NAME, name );
//		values.put( CardfileView.SIDE_A, sideA );
//		values.put( CardfileView.SIDE_B, sideB );
//
//		Uri newUri = cr.insert( FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), values );
//	
//		List<String> s = newUri.getPathSegments();
//		long id = Long.parseLong( s.get( 1 ) );
//		Log.d( "Cardfile", "created (" + id + ")" );
//		return new Cardfile( this, id, name, sideA, sideB, 0 );
//		
//    }
//
//}
