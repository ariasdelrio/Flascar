package es.guillesoft.flascar.dm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.CardfileView;
import es.guillesoft.flascar.file.Deck;
import es.guillesoft.flascar.file.DeckCard;

import android.content.ContentValues;
import android.util.Log;

public class Cardfile extends Observable {
  
	public enum Sense { AB, BA };
	public final class ReviewInfo { 
		public String name; 
		public long total, pending;
		ReviewInfo( String name ) { this.name = name; total = pending = 0; }
	};
	
	private CardfileCollection parent;
	private CardCollection cardCollection;
    
	private long id;
    private String name;
    private String sideA;
    private String sideB;
    private String languageA;
    private String languageB;
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
	
	public String getLanguageA() {
		return languageA;
	}

	public String getLanguageB() {
		return languageB;
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
	
	public void setLanguageA( String languageA ) {
		this.languageA = languageA;
	}
	
	public void setLanguageB( String languageB ) {
		this.languageB = languageB;
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
	
	public ArrayList<ReviewInfo> getReviewInfo( Sense sense ) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
		Date now = new Date();
		
		ArrayList<Box> boxes = Core.getInstance().getDataModel( cardCollection.getContentResolver() ).getBoxes();
		
		ArrayList<ReviewInfo> info = new ArrayList<ReviewInfo>();
		for( int i = 0; i < boxes.size(); i++ )
			info.add( new ReviewInfo( boxes.get( i ).getName() ) );
		
		for( Card card : getCards() ) {
			
			int boxPosition = (int) ( sense == Sense.AB ? card.getBoxA() : card.getBoxB() ) - 1;
			
			info.get( boxPosition ).total++;
			
			try {
			
				Date lastChecked = dateFormat.parse( sense == Sense.AB ? card.getLastCheckedA() : card.getLastCheckedB() );
				
				if( now.getTime() - lastChecked.getTime() >= boxes.get( boxPosition ).getExpiration() * 1000 * 60 ) {
					info.get( boxPosition ).pending++;
				}
				
			}
			catch( Exception e ) {
			}
			
		}
			
		return info;			
			
	}
	
	/* Modifications */
	
	public void update() {
   	 
    	Log.d( this.getClass().getSimpleName(), "update " + id );
		
		ContentValues values = new ContentValues();
		values.put( CardfileView.NAME, name );
		values.put( CardfileView.SIDE_A, sideA );
		values.put( CardfileView.SIDE_B, sideB );
		values.put( CardfileView.LANGUAGE_A, languageA );
		values.put( CardfileView.LANGUAGE_B, languageB );
		
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
	
	protected Cardfile( CardfileCollection parent, long id, String name, String sideA, String sideB, 
			String languageA, String languageB, long cardCount ) {
    	
    	this.parent = parent;
		this.name = name;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.languageA = languageA;
    	this.languageB = languageB;
    	this.id = id;
    	this.cardCount = cardCount;
    	cardCollection = new CardCollection( parent.getContentResolver(), this );
    	
    }

}