package es.guillesoft.flascar.dm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.CardView;
import es.guillesoft.flascar.dm.Cardfile.Sense;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

class CardCollection {

	private HashMap<Long, Card> cards;
	private Cardfile cardfile;
	private ContentResolver cr;
	
	CardCollection( ContentResolver cr, Cardfile cardfile ) {
		
		this.cr = cr;
		this.cardfile = cardfile;
		cards = null;

	}
	
	ContentResolver getContentResolver() {
		return cr;
	}
	
	HashMap<Long, Card> getCards() throws Exception {
		
    	if( cards == null ) readCards();
    	return cards;
    		
    }
	
	ArrayList<Card> getCardList() throws Exception {
	    
		return new ArrayList<Card>( getCards().values() );
		
	}
	
	Card getCard( long id ) throws Exception {
		
		return getCards().get( new Long( id ) );
		
	}

	Card addCard( String sideA, String sideB ) throws Exception {
		
		Log.d( this.getClass().getSimpleName(), "addCard" );
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		String str = dateFormat.format( date );
		
		Card card = createCard( 1, 1, sideA, sideB, str, str );
		cards.put( card.getID(), card );
		
		return card;
		
	}
	
	boolean deleteCard( Card card ) {
		
		long id = card.getID();
		
		Log.d( this.getClass().getSimpleName(), "deleteCard " + id );

		int ret = cr.delete( 
			FlashcardProvider.getUri( FlashcardProvider.CARD ), 
			null,
			new String[] { new Long( id ).toString() } );
	
		if( ret != 1 ) return false;
	
		cards.remove( new Long( id ) );

		return true;
		
	}
	
	boolean updateCard( Card card, String sideA, String sideB ) {
		
		long id = card.getID();
		
		Log.d( this.getClass().getSimpleName(), "updateCard " + id );
	    	
		ContentValues values = new ContentValues();
		values.put( CardView.SIDE_A, sideA );
		values.put( CardView.SIDE_B, sideB );
			
		int ret = cr.update( 
	     		FlashcardProvider.getUri( FlashcardProvider.CARD ), 
	     		values, 
	     		null,
	     		new String[] { new Long( id ).toString() } 
	    );
		
		if( ret != 1 ) return false;
		
		card.setSideA( sideA );
		card.setSideB( sideB );
		
		return true;
				
	}
	
	boolean updateCard( Card card, long boxID, Sense sense ) {
		
		long id = card.getID();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String now = dateFormat.format( new Date() );
		
		Log.d( this.getClass().getSimpleName(), "updateCard " + id );
	    	
		ContentValues values = new ContentValues();
		values.put( sense == Sense.AB ? CardView.BOX_A : CardView.BOX_B, boxID );
		values.put( sense == Sense.AB ? CardView.LAST_CHECKED_A : CardView.LAST_CHECKED_B, now );
		
		int ret = cr.update( 
	     		FlashcardProvider.getUri( FlashcardProvider.CARD ), 
	     		values, 
	     		null,
	     		new String[] { new Long( id ).toString() } 
	    );
		
		if( ret != 1 ) return false;
		
		if( sense == Sense.AB ) {
			
			card.setBoxA( boxID );
			card.setLastCheckedA( now );
			
		}
		else {
			
			card.setBoxB( boxID );
			card.setLastCheckedB( now );
			
		}
		
		return true;
				
	}
	
	/* private */
    
    private void readCards() throws Exception {
    	
    	Log.d( this.getClass().getSimpleName(), "readCards" );
    	
    	Cursor c = cr.query( 
    			FlashcardProvider.getUri( FlashcardProvider.CARD ), 
    			null, 
    			DBUtil.equals( CardView.CARDFILE, "?" ),
				new String[] { new Long( cardfile.getID() ).toString() },
				null );
    	
    	if( c == null ) throw new Exception( "cursor is null" );
    		
    	cards = new HashMap<Long, Card>();
    	if( c.moveToFirst() == true ) while( !c.isAfterLast() ) {
    			
    		Card card = cursorToCard( c );
    		cards.put( new Long( card.getID() ), card );
    		c.moveToNext();
    		Log.d( this.getClass().getSimpleName(), "read card: " + card.getID() );
    			
    	}
    	
    }
    
    private Card createCard( long box_a, long box_b, String sideA, String sideB, String lastCheckedA, String lastCheckedB ) {
		Log.d( this.getClass().getSimpleName(), "createCard" );

		try {
			if( cards == null ) readCards();
		}
		catch(Exception e) {return null;}
		
		ContentValues values = new ContentValues();
		values.put( CardView.CARDFILE, cardfile.getID() );
		values.put( CardView.SIDE_A, sideA );
		values.put( CardView.SIDE_B, sideB );
		values.put( CardView.LAST_CHECKED_A, lastCheckedA );
		values.put( CardView.LAST_CHECKED_B, lastCheckedB );
		values.put( CardView.BOX_A, box_a );
		values.put( CardView.BOX_B, box_b );

		Uri newUri = cr.insert( FlashcardProvider.getUri( FlashcardProvider.CARD ), values );
		
		List<String> s = newUri.getPathSegments();
		long cardID = Long.parseLong( s.get( 1 ) );
		Log.d( this.getClass().getSimpleName(), "card created (" + cardID + ")" );

		return new Card( this, cardfile, cardID, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
	
	}
    
    private Card cursorToCard( Cursor cursor ) throws IllegalArgumentException {
		
		int index = cursor.getColumnIndexOrThrow( CardView.ID );
		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Card must have ID" );
		long id =  cursor.getLong( index ); 

		index = cursor.getColumnIndex( CardView.BOX_A );
		long box_a = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
    		
		index = cursor.getColumnIndex( CardView.BOX_B );
		long box_b = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
    		
		index = cursor.getColumnIndexOrThrow( CardView.SIDE_A );
		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
		index = cursor.getColumnIndexOrThrow( CardView.SIDE_B );
		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
		index = cursor.getColumnIndex( CardView.LAST_CHECKED_A );
		String lastCheckedA = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
		index = cursor.getColumnIndex( CardView.LAST_CHECKED_B );
		String lastCheckedB = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
		return new Card( this, cardfile, id, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
    		
	}

}
	
	
	