package es.guillesoft.flascar.dm;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.CardfileView;
import es.guillesoft.flascar.file.Deck;
import es.guillesoft.flascar.file.DeckCard;

import android.content.ContentValues;
import android.util.Log;

public class Cardfile extends Observable {
  
	public enum Sense { AB, BA };
	
	private CardfileCollection parent;
	private CardCollection cardCollection;
    
	private long id;
    private String name;
    private String sideA;
    private String sideB;
    private long cardCount;
	
	public long getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSideA() {
		return sideA;
	}
	
	public String getSideB() {
		return sideB;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public void setSideA( String sideA ) {
		this.sideA = sideA;
	}
	
	public void setSideB( String sideB ) {
		this.sideB = sideB;
	}
	
	public long getCardCount() {
		return cardCount;
	}
	
	
	public ArrayList<Card> getCards() {
	    
		try {
			
			return cardCollection.getCardList();
			
		}
		catch( Exception e ) {
			
			Log.d( this.getClass().getSimpleName(), "error retrieving cards: " + e );
			return null;
			
		}
		
    }
	
	public Card getCard( long id ) {
		
		try {
			
			return cardCollection.getCard( id );
			
		}
		catch( Exception e ) {

			Log.d( this.getClass().getSimpleName(), "error retrieving card " + id + ": " + e );
			return null;
			
		}

	}
	
	public void saveCardsToDeck( File file ) throws Exception {
		
		Deck deck = new Deck();
		
		for( Card card : getCards() ) deck.add( new DeckCard( card.getSideA(), card.getSideB() ) );
		
		deck.save( file );
		
	}

	/* Modifications */
	
	public void update() {
   	 
    	Log.d( this.getClass().getSimpleName(), "update " + id );
		
		ContentValues values = new ContentValues();
		values.put( CardfileView.NAME, name );
		values.put( CardfileView.SIDE_A, sideA );
		values.put( CardfileView.SIDE_B, sideB );
		
		parent.getContentResolver().update( 
			FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), 
     		values, 
     		DBUtil.equals( CardfileView.ID, "?" ),
     		new String[] { new Long( id ).toString() } );
		
		setChanged();
		notifyObservers( this );
		
	}
	
	public Card addCard( String sideA, String sideB ) throws Exception {
		
		Card card = cardCollection.addCard( sideA, sideB );
		cardCount++;
		
		setChanged();
		notifyObservers( card );
		
		return card;
		
	}
	
	public boolean deleteCard( Card card ) {
		
		boolean ret = cardCollection.deleteCard( card );
		if( ret == false ) return false;
		cardCount--;
		
		setChanged();
		notifyObservers( card );
		
		return true;
		
	}
	
	/// only sideA and sideB
	public boolean updateCard( Card card, String sideA, String sideB ) {
		
		boolean ret = cardCollection.updateCard( card, sideA, sideB );
		if( ret == false ) return false;
		
		setChanged();
		notifyObservers( card );

		return true;
		
	}
	
	public void addCardsFromDeck( File file ) throws Exception {
		
		Deck deck = Deck.load( file );
		
		for( DeckCard dc : deck.getCards() ) {
			
			cardCollection.addCard( dc.sideA, dc.sideB );
			cardCount++;
		
		}
		
		setChanged();
		notifyObservers( this );
		
	}

	/* protected */
	
	protected Cardfile( CardfileCollection parent, long id, String name, String sideA, String sideB, long cardCount ) {
    	
    	this.parent = parent;
		this.name = name;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.id = id;
    	this.cardCount = cardCount;
    	cardCollection = new CardCollection( parent.getContentResolver(), this );
    	
    }

	
	   
    // TODO: todo lo que sigue
//    public boolean delete( ContentResolver cr ) {
//		
//		Log.d( "Cardfile", "delete " + id );
//		
//		// @todo remove all cards
//		return cr.delete( 
//				FlashcardProvider.cardfile.getUri(), 
//				ID + " = ?",
//				new String[] { new Long( id ).toString() } ) == 1;
//		
//	}
    
//    public Cursor getBoxesExt( ContentResolver cr, FlashcardProvider.Sense sense ) {
//		
//		Log.d( "Cardfile", "getBoxesExt " + id + " - " + sense );
//    	
//    	return cr.query( 
//    			FlashcardProvider.cardfile.getBoxUri( sense ),
//				new String[] { BOX_ID, BOX_NAME, BOX_TOTAL, BOX_DIRTY }, 
//				null,
//				new String[] { new Long( id ).toString(),  new Long( id ).toString() },
//				null );
//
//	}
//
//	public Cursor getCardsToReview( ContentResolver cr, long boxID, FlashcardProvider.Sense sense ) {
//
//		Log.d( "Cardfile", "getCardsToReview " + id + " (box " + boxID + ") - " + sense );
//		
//		String box_str = REVIEW_BOX_A;
//		String lch_str = REVIEW_LAST_CHECKED_A;
//		if( sense == FlashcardProvider.Sense.BA ) {
//			box_str = REVIEW_BOX_B;
//			lch_str = REVIEW_LAST_CHECKED_B;
//		}
//    	
//    	return cr.query( 
//				FlashcardProvider.cardfile.getReviewUri( sense ), 
//				new String[] { REVIEW_ID, REVIEW_SIDE_A, REVIEW_SIDE_B, box_str, lch_str },
//				null,
//				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
//				null );
//		
//		
//	}
//	
//	public Cursor test( ContentResolver cr, long boxID ) {
//
//		Log.d( "Cardfile", "test " + id + " (box " + boxID + ")" );
//		
//    	return cr.query( 
//				FlashcardProvider.cardfile.getTestUri( ), 
//				null,
//				null,
//				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
//				null );
//    	
//    	
//	}
		
}




/* SIN LLEVAR A MEMORIA */

//package es.guillesoft.flascar.dm;
//
//import es.guillesoft.flascar.db.FlashcardProvider;
//import es.guillesoft.flascar.db.view.CardView;
//import es.guillesoft.flascar.db.view.CardfileView;
//
//import android.content.ContentValues;
//import android.database.Cursor;
////import android.os.Parcel;
////import android.os.Parcelable;
//import android.util.Log;
//
//public class Cardfile {
//  
//	private Main main;
//	private Cursor cards;
//    
//	private long id;
//    private String name;
//    private String sideA;
//    private String sideB;
//    private long cardCount;
//	
//	public long getID() {
//		return id;
//	}
//	
//	public String getName() {
//		return name;
//	}
//	
//	public String getSideA() {
//		return sideA;
//	}
//	
//	public String getSideB() {
//		return sideB;
//	}
//	
//	public void setName( String name ) {
//		this.name = name;
//	}
//	
//	public void setSideA( String sideA ) {
//		this.sideA = sideA;
//	}
//	
//	public void setSideB( String sideB ) {
//		this.sideB = sideB;
//	}
//	
//	public long getCardCount() {
//		return cardCount;
//	}
//
//	public void update() {
//    	 
//    	Log.d( "Cardfile", "update " + id );
//		
//		ContentValues values = new ContentValues();
//		values.put( CardfileView.NAME, name );
//		values.put( CardfileView.SIDE_A, sideA );
//		values.put( CardfileView.SIDE_B, sideB );
//		
//		if( main.getContentResolver().update( 
//			FlashcardProvider.getUri( FlashcardProvider.CARDFILE ), 
//     		values, 
//     		CardfileView.ID + " = ?",
//     		new String[] { new Long( id ).toString() } ) == 1 )
//			
//			main.notifyUpdate( id );
//		
//	}
//	
//	public Card getCard( int index ) {
//    	
//    	if( index < 0 || index >= cardCount ) return null;
//    	if( cards == null ) readCards();
//    	cards.moveToPosition( index );
//    	return cursorToCard( cards );
//    	
//    }
//
//	/* protected */
//	
//	protected Cardfile( Main main, long id, String name, String sideA, String sideB, long cardCount ) {
//    	
//    	this.main = main;
//		this.name = name;
//    	this.sideA = sideA;
//    	this.sideB = sideB;
//    	this.id = id;
//    	this.cardCount = cardCount;
//    	cards = null;
//    	
//    }
//	
//	/* private */
//	
//	protected void readCards() {
//		
//		Log.d( "Cardfile", "readCards " + id );
//    	
//    	cards = main.getContentResolver().query( 
//    			FlashcardProvider.getUri( FlashcardProvider.CARD ), 
//    			null,
//				Card.CARDFILE_ID + " = ?",
//				new String[] { new Long( id ).toString() },
//				null );
//		
//	}
//	
//	private Card cursorToCard( Cursor cursor ) throws IllegalArgumentException {
//
//		try {
//    		
//    		int index = cursor.getColumnIndexOrThrow( CardView.ID );
//    		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Card must have ID" );
//    		long id =  cursor.getLong( index ); 
//
//    		index = cursor.getColumnIndex( CardView.BOX_A );
//    		long box_a = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
//    		
//    		index = cursor.getColumnIndex( CardView.BOX_B );
//    		long box_b = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
//    		
//    		index = cursor.getColumnIndexOrThrow( CardView.SIDE_A );
//    		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndexOrThrow( CardView.SIDE_B );
//    		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndex( CardView.LAST_CHECKED_A );
//    		String lastCheckedA = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndex( CardView.LAST_CHECKED_B );
//    		String lastCheckedB = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		return new Card( this, id, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
//    		
//    	}
//    	catch( IllegalArgumentException e ) {
//    		
//    		Log.e( "Card", "Illegal argument" );
//    		throw e;
//    		
//    	}
//    	
//	}
//    
//	/* RESTO */
//	
//	
//	/* PA Q COMPILE
//    public static final String ID = es.guillesoft.flascar.db.Cardfile.ID;
//    public static final String NAME = es.guillesoft.flascar.db.Cardfile.NAME;
//    public static final String SIDE_A = es.guillesoft.flascar.db.Cardfile.SIDE_A;
//    public static final String SIDE_B = es.guillesoft.flascar.db.Cardfile.SIDE_B;
//    
//    public static final String COUNT_CARDS = es.guillesoft.flascar.db.Cardfile.COUNT_CARDS;
//    
//    public static final String BOX_ID = es.guillesoft.flascar.db.Cardfile.BOX_ID;
//    public static final String BOX_NAME = es.guillesoft.flascar.db.Cardfile.BOX_NAME;
//    public static final String BOX_TOTAL = es.guillesoft.flascar.db.Cardfile.BOX_TOTAL;
//    public static final String BOX_DIRTY = es.guillesoft.flascar.db.Cardfile.BOX_DIRTY;
//    
//	public static final String REVIEW_ID = es.guillesoft.flascar.db.Cardfile.REVIEW_ID;
//	public static final String REVIEW_SIDE_A = es.guillesoft.flascar.db.Cardfile.REVIEW_SIDE_A;
//	public static final String REVIEW_SIDE_B = es.guillesoft.flascar.db.Cardfile.REVIEW_SIDE_B;
//	public static final String REVIEW_BOX_A = es.guillesoft.flascar.db.Cardfile.REVIEW_BOX_A;
//	public static final String REVIEW_BOX_B = es.guillesoft.flascar.db.Cardfile.REVIEW_BOX_B;
//	public static final String REVIEW_LAST_CHECKED_A = es.guillesoft.flascar.db.Cardfile.REVIEW_LAST_CHECKED_A;
//	public static final String REVIEW_LAST_CHECKED_B = es.guillesoft.flascar.db.Cardfile.REVIEW_LAST_CHECKED_B;
//	*/
//    
//    /* PA Q COMPILE
//   
//    
//    
//    public boolean delete( ContentResolver cr ) {
//		
//		Log.d( "Cardfile", "delete " + id );
//		
//		// @todo remove all cards
//		return cr.delete( 
//				FlashcardProvider.cardfile.getUri(), 
//				ID + " = ?",
//				new String[] { new Long( id ).toString() } ) == 1;
//		
//	}
//
//    
//    
//	
//    public Card addCard( ContentResolver cr, String sideA, String sideB ) {
//		
//		Log.d( "Cardfile", "addCard" );
//		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//		Date date = new Date();
//		String lch = dateFormat.format( date );
//		
//		return Card.create( cr, this, 1, 1, sideA, sideB, lch, lch );
//
//	}
//    
//    public Cursor getBoxesExt( ContentResolver cr, FlashcardProvider.Sense sense ) {
//		
//		Log.d( "Cardfile", "getBoxesExt " + id + " - " + sense );
//    	
//    	return cr.query( 
//    			FlashcardProvider.cardfile.getBoxUri( sense ),
//				new String[] { BOX_ID, BOX_NAME, BOX_TOTAL, BOX_DIRTY }, 
//				null,
//				new String[] { new Long( id ).toString(),  new Long( id ).toString() },
//				null );
//
//	}
//
//	public Cursor getCardsToReview( ContentResolver cr, long boxID, FlashcardProvider.Sense sense ) {
//
//		Log.d( "Cardfile", "getCardsToReview " + id + " (box " + boxID + ") - " + sense );
//		
//		String box_str = REVIEW_BOX_A;
//		String lch_str = REVIEW_LAST_CHECKED_A;
//		if( sense == FlashcardProvider.Sense.BA ) {
//			box_str = REVIEW_BOX_B;
//			lch_str = REVIEW_LAST_CHECKED_B;
//		}
//    	
//    	return cr.query( 
//				FlashcardProvider.cardfile.getReviewUri( sense ), 
//				new String[] { REVIEW_ID, REVIEW_SIDE_A, REVIEW_SIDE_B, box_str, lch_str },
//				null,
//				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
//				null );
//		
//		
//	}
//	
//	public Cursor test( ContentResolver cr, long boxID ) {
//
//		Log.d( "Cardfile", "test " + id + " (box " + boxID + ")" );
//		
//    	return cr.query( 
//				FlashcardProvider.cardfile.getTestUri( ), 
//				null,
//				null,
//				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
//				null );
//    	
//    	
//	}
//	*/
//
//	
//}