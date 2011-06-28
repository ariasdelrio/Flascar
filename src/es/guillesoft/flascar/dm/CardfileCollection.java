package es.guillesoft.flascar.dm;

import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.CardfileView;

class CardfileCollection {
	
	private HashMap<Long, Cardfile> cardfiles;
	private ContentResolver cr;
	
	CardfileCollection( ContentResolver cr ) {
		
		this.cr = cr;
		cardfiles = null;

	}
	
	HashMap<Long, Cardfile> getCardfiles() throws Exception {
	
    	if( cardfiles == null ) readCardfiles();
    	return cardfiles;
    		
    }
	
	Cardfile addCardfile( String name, String sideA, String sideB,
			String languageA, String languageB ) throws Exception {
		
		if( cardfiles == null ) readCardfiles();
		Cardfile cardfile = createCardfile( name, sideA, sideB, languageA, languageB );
		cardfiles.put( cardfile.getID(), cardfile );
		return cardfile;
		
	}
			
	ContentResolver getContentResolver() {
		return cr;
	}
	
	/* private */
    
    private void readCardfiles() throws Exception {
    	
    	Log.d( this.getClass().getSimpleName(), "readCardfiles" );
    	
    	Cursor c = cr.query( FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), null,	null, null,	null );
    	if( c == null ) throw new Exception( "cursor is null" );
    		
    	cardfiles = new HashMap<Long, Cardfile>();
    	if( c.moveToFirst() == true ) while( !c.isAfterLast() ) {
    			
    		Cardfile cardfile = cursorToCardfile( c );
    		cardfiles.put( new Long( cardfile.getID() ), cardfile );
    		c.moveToNext();
    			
    	}
    	
    }

    private Cardfile createCardfile( String name, 
    		String sideA, String sideB, String languageA, String languageB ) throws Exception {
    	
    	Log.d( this.getClass().getSimpleName(), "createCardfile" );
		
		ContentValues values = new ContentValues();
		values.put( CardfileView.NAME, name );
		values.put( CardfileView.SIDE_A, sideA );
		values.put( CardfileView.SIDE_B, sideB );
		values.put( CardfileView.LANGUAGE_A, languageA );
		values.put( CardfileView.LANGUAGE_B, languageB );

		Uri newUri = cr.insert( FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), values );
	
		List<String> s = newUri.getPathSegments();
		long id = Long.parseLong( s.get( 1 ) );
		Log.d( this.getClass().getSimpleName(), "cardfile created (" + id + ")" );
		
		Cardfile cardfile = new Cardfile( this, id, name, sideA, sideB, languageA, languageB, 0 );
		
		return cardfile;
		
    }

	private Cardfile cursorToCardfile( Cursor cursor ) throws IllegalArgumentException {
		
		int index = cursor.getColumnIndexOrThrow( CardfileView.ID );
		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Cardfile must have ID" );
		long id =  cursor.getLong( index ); 
		
		index = cursor.getColumnIndexOrThrow( CardfileView.NAME );
		String name = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
			
		index = cursor.getColumnIndexOrThrow( CardfileView.SIDE_A );
		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
		
		index = cursor.getColumnIndexOrThrow( CardfileView.SIDE_B );
		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
		
		index = cursor.getColumnIndexOrThrow( CardfileView.LANGUAGE_A );
		String languageA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
		
		index = cursor.getColumnIndexOrThrow( CardfileView.LANGUAGE_B );
		String languageB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
		
		long cardCount = 0;
		index = cursor.getColumnIndex( CardfileView.CARDS );
		if( index != -1 && !cursor.isNull( index ) ) cardCount = cursor.getLong( index ); 
		
		return new Cardfile( this, id, name, sideA, sideB, languageA, languageB, cardCount );
			
	}
	
}