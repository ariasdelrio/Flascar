package es.guillesoft.flascar.dm;

import java.util.Observable;

import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.CardView;
import android.content.ContentResolver;
import android.util.Log;

public class Card {

    private CardCollection parent;
    private Cardfile cardfile;

    private long id;
    private long box_a;
    private long box_b;
    private String sideA;
    private String sideB;
    private String lastCheckedA;    
    private String lastCheckedB;   
        
    public Cardfile getCardfile() {
    	return cardfile;
    }
    
    public long getID() {
    	return id;
    }
    
    public String getSideA() {
    	return sideA;
    }
    
    public String getSideB() {
    	return sideB;
    }
    
    public void setSideA( String sideA ) {
    	this.sideA = sideA;
    }
    
    public void setSideB( String sideB ) {
    	this.sideB = sideB;
    }
    
    public String getLastCheckedA() {
    	return lastCheckedA;
    }
    
    public String getLastCheckedB() {
    	return lastCheckedB;
    }
    
    public long getBoxA() {
    	return box_a;
    }
    
    public long getBoxB() {
    	return box_b;
    }
    
    /* protected */

    protected Card( CardCollection parent, Cardfile cardfile, long id, long box_a, long box_b, String sideA, String sideB, String lastCheckedA, String lastCheckedB ) {
    	
    	this.parent = parent;
    	this.cardfile = cardfile;
    	this.id = id;
    	this.box_a = box_a;
    	this.box_b = box_b;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.lastCheckedA = lastCheckedA;
    	this.lastCheckedB = lastCheckedB;

    }


    /* PA Q COMPILE
    public boolean update( ContentResolver contentResolver, FlashcardProvider.Sense sense ) {
     	 
    	Log.d( "Card", "update " + id + " - " + sense );
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
		Date date = new Date();
		String box_str = sense == FlashcardProvider.Sense.AB ? BOX_A : BOX_B;
		long box_val = sense == FlashcardProvider.Sense.AB ? box_a : box_b;
		String lch_str = sense == FlashcardProvider.Sense.AB ? LAST_CHECKED_A : LAST_CHECKED_B;
		String lch_val = dateFormat.format(date);
		
		ContentValues values = new ContentValues();
		values.put( SIDE_A, sideA );
		values.put( SIDE_B, sideB );
		values.put( box_str, box_val );
		values.put( lch_str,  lch_val );
		
		Log.w( "Card", "SIDE A         = " + sideA );
		Log.w( "Card", "SIDE B         = " + sideB );
		Log.w( "Card", box_str + "          = " + box_val );
		Log.w( "Card", lch_str + " = " + lch_val );
		
		return contentResolver.update( 
     		FlashcardProvider.card.getUri(), 
     		values, 
     		ID + " = ?",
     		new String[] { new Long( id ).toString() } ) == 1;
		
	}
	*/

    /* PA Q COMPILE
    public boolean climbUp( ContentResolver cr, FlashcardProvider.Sense sense ) {
		 
    	long box_id = sense == FlashcardProvider.Sense.AB ? box_a : box_b;
    	
    	Log.d( "Card", "climb up " + id + " - " + sense );

    	if( box_id < 1 || box_id >= Box.BOX_COUNT ) Log.d( "Card", "already on top" );
    	else {
    		Log.d( "Card", "climb " + box_id + " -> " + ( box_id + 1 ) );
    		if( sense == FlashcardProvider.Sense.AB ) box_a++; else box_b++;
    	}
		
		return update( cr, sense );
				
	}
    	
	public boolean climbDown( ContentResolver cr, FlashcardProvider.Sense sense ) {
		 
    	long box_id = sense == FlashcardProvider.Sense.AB ? box_a : box_b;
    	
    	Log.d( "Card", "climb down " + id + " - " + sense );
		
		if( box_id <= 1 || box_id > Box.BOX_COUNT ) Log.d( "Card", "already at bottom" );
		else {
			Log.d( "Card", "climb " + box_id + " -> " + ( box_id - 1 ) );
    		if( sense == FlashcardProvider.Sense.AB ) box_a--; else box_b--;
		}
		
		return update( cr, sense );
				
	}
	*/
	/* Parcelable */
	
//	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card> () {
//	    public Card createFromParcel( Parcel source ) {
//	          return new Card( source );
//	    }
//	    public Card[] newArray( int size ) {
//	          return new Card[size];
//	    }
//	};
//	
//	public int describeContents() {
//		return hashCode();
//	}
	
//	public void writeToParcel( Parcel parcel, int flags ) {
//
//		parcel.writeLong( id );
//		parcel.writeLong( box_a );
//		parcel.writeLong( box_b );
//		parcel.writeString( sideA );
//		parcel.writeString( sideB );
//		parcel.writeString( lastCheckedA );
//		parcel.writeString( lastCheckedB );
//		parcel.writeParcelable( cardfile, 0 );
//
//	}
//	
//	public Card( Parcel parcel ) {
//		
//		id = parcel.readLong();
//		box_a = parcel.readLong();
//		box_b = parcel.readLong();
//		sideA = parcel.readString();
//		sideB = parcel.readString();
//		lastCheckedA = parcel.readString();
//		lastCheckedB = parcel.readString();
//		cardfile = parcel.readParcelable( Cardfile.class.getClassLoader() );
//		
//	}
	
}
