package es.guillesoft.flascar.dm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.guillesoft.flascar.db.FlashcardProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Card implements Parcelable {

	public static final String ID = es.guillesoft.flascar.db.Card.ID;
    public static final String CARDFILE_ID = es.guillesoft.flascar.db.Card.CARDFILE_ID;
    public static final String BOX_A = es.guillesoft.flascar.db.Card.BOX_A;
    public static final String BOX_B = es.guillesoft.flascar.db.Card.BOX_B;
    public static final String SIDE_A = es.guillesoft.flascar.db.Card.SIDE_A;
    public static final String SIDE_B = es.guillesoft.flascar.db.Card.SIDE_B;
    public static final String LAST_CHECKED_A = es.guillesoft.flascar.db.Card.LAST_CHECKED_A;
    public static final String LAST_CHECKED_B = es.guillesoft.flascar.db.Card.LAST_CHECKED_B;
    
    private long id;
//    private long cardfile_id;
    private long box_a;
    private long box_b;
    private String sideA;
    private String sideB;
    private String lastCheckedA;    
    private String lastCheckedB;   
    private Cardfile cardfile;
        
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
    
    public Cardfile getCardfile() {
    	return cardfile;
    }
    
//    private Card( long id, long cardfile_id, long box_a, long box_b, String sideA, String sideB, String lastCheckedA, String lastCheckedB ) {
//    	
//    	this.id = id;
//    	this.cardfile_id = cardfile_id;
//    	this.box_a = box_a;
//    	this.box_b = box_b;
//    	this.sideA = sideA;
//    	this.sideB = sideB;
//    	this.lastCheckedA = lastCheckedA;
//    	this.lastCheckedB = lastCheckedB;
//    	
//    }

    private Card( long id, Cardfile cardfile, long box_a, long box_b, String sideA, String sideB, String lastCheckedA, String lastCheckedB ) {
    	
    	this.id = id;
    	this.cardfile = cardfile;
    	this.box_a = box_a;
    	this.box_b = box_b;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.lastCheckedA = lastCheckedA;
    	this.lastCheckedB = lastCheckedB;
    	
    }

//    protected static Card create( ContentResolver cr, long cardfile_id, long box_a, long box_b, String sideA, String sideB, 
//    		String lastCheckedA, String lastCheckedB ) {
//    	
//    	Log.d( "Card", "create" );
//		
//		ContentValues values = new ContentValues();
//		values.put( Card.CARDFILE_ID, cardfile_id );
//		values.put( Card.SIDE_A, sideA );
//		values.put( Card.SIDE_B, sideB );
//		values.put( Card.LAST_CHECKED_A, lastCheckedA );
//		values.put( Card.LAST_CHECKED_B, lastCheckedB );
//		values.put( Card.BOX_A, box_a );
//		values.put( Card.BOX_B, box_b );
//		
//		Uri newUri = cr.insert( FlashcardProvider.card.getUri(), values );
//		
//		List<String> s = newUri.getPathSegments();
//		if( s.get( 0 ).equals( es.guillesoft.flascar.db.Card.URI_ROOT ) ) {
//			
//			long id = Long.parseLong( s.get( 1 ) );
//			Log.d( "Card", "created (" + id + ")" );
//			return new Card( id, cardfile_id, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
//			
//		}
//		else return null;
//    	
//    }
    
    protected static Card create( ContentResolver cr, Cardfile cardfile, long box_a, long box_b, String sideA, String sideB, 
    		String lastCheckedA, String lastCheckedB ) {
    	
    	Log.d( "Card", "create" );
		
		ContentValues values = new ContentValues();
		values.put( Card.CARDFILE_ID, cardfile.getID() );
		values.put( Card.SIDE_A, sideA );
		values.put( Card.SIDE_B, sideB );
		values.put( Card.LAST_CHECKED_A, lastCheckedA );
		values.put( Card.LAST_CHECKED_B, lastCheckedB );
		values.put( Card.BOX_A, box_a );
		values.put( Card.BOX_B, box_b );
		
		Uri newUri = cr.insert( FlashcardProvider.card.getUri(), values );
		
		List<String> s = newUri.getPathSegments();
		if( s.get( 0 ).equals( es.guillesoft.flascar.db.Card.URI_ROOT ) ) {
			
			long id = Long.parseLong( s.get( 1 ) );
			Log.d( "Card", "created (" + id + ")" );
			return new Card( id, cardfile, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
			
		}
		else return null;
    	
    }
    
//    public static Card toCard( Cursor cursor ) {
//    	
//    	try {
//    		
//    		int index = cursor.getColumnIndexOrThrow( ID );
//    		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Card must have ID" );
//    		long id =  cursor.getLong( index ); 
//
//    		index = cursor.getColumnIndex( BOX_A );
//    		long box_a = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
//    		
//    		index = cursor.getColumnIndex( BOX_B );
//    		long box_b = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
//    		
//    		index = cursor.getColumnIndexOrThrow( SIDE_A );
//    		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndexOrThrow( SIDE_B );
//    		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndex( LAST_CHECKED_A );
//    		String lastCheckedA = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		index = cursor.getColumnIndex( LAST_CHECKED_B );
//    		String lastCheckedB = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
//    		
//    		return new Card( id, -1, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
//    		
//    	}
//    	catch( IllegalArgumentException e ) {
//    		
//    		Log.e( "Card", "Illegal argument" );
//    		throw e;
//    		
//    	}
//
//    }
    
    public static Card toCard( Cursor cursor, Cardfile cardfile ) {
    	
    	try {
    		
    		int index = cursor.getColumnIndexOrThrow( ID );
    		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Card must have ID" );
    		long id =  cursor.getLong( index ); 

    		index = cursor.getColumnIndex( BOX_A );
    		long box_a = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
    		
    		index = cursor.getColumnIndex( BOX_B );
    		long box_b = index == -1 || cursor.isNull( index ) ? -1 : cursor.getLong( index ); 
    		
    		index = cursor.getColumnIndexOrThrow( SIDE_A );
    		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		index = cursor.getColumnIndexOrThrow( SIDE_B );
    		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		index = cursor.getColumnIndex( LAST_CHECKED_A );
    		String lastCheckedA = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		index = cursor.getColumnIndex( LAST_CHECKED_B );
    		String lastCheckedB = index == -1 || cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		return new Card( id, cardfile, box_a, box_b, sideA, sideB, lastCheckedA, lastCheckedB );
    		
    	}
    	catch( IllegalArgumentException e ) {
    		
    		Log.e( "Card", "Illegal argument" );
    		throw e;
    		
    	}

    }
   
    public boolean delete( ContentResolver cr ) {
		
		Log.d( "Card", "delete " + id );
		
		return cr.delete( 
				FlashcardProvider.card.getUri(), 
				ID + " = ?",
				new String[] { new Long( id ).toString() } ) == 1;
		
	}
    
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
    
    public boolean update( ContentResolver contentResolver ) {
    	 
    	Log.d( "Card", "update " + id );
    	
		ContentValues values = new ContentValues();
		values.put( SIDE_A, sideA );
		values.put( SIDE_B, sideB );
		
		Log.w( "Card", "SIDE A         = " + sideA );
		Log.w( "Card", "SIDE B         = " + sideB );
		
		return contentResolver.update( 
     		FlashcardProvider.card.getUri(), 
     		values, 
     		ID + " = ?",
     		new String[] { new Long( id ).toString() } ) == 1;
		
	}

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
	
	/* Parcelable */
	
	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card> () {
	    public Card createFromParcel( Parcel source ) {
	          return new Card( source );
	    }
	    public Card[] newArray( int size ) {
	          return new Card[size];
	    }
	};
	
	public int describeContents() {
		return hashCode();
	}

//	public void writeToParcel( Parcel parcel, int flags ) {
//
//		parcel.writeLong( id );
//		parcel.writeLong( cardfile_id );
//		parcel.writeLong( box_a );
//		parcel.writeLong( box_b );
//		parcel.writeString( sideA );
//		parcel.writeString( sideB );
//		parcel.writeString( lastCheckedA );
//		parcel.writeString( lastCheckedB );
//
//	}
//	
//	public Card( Parcel parcel ) {
//		
//		id = parcel.readLong();
//		cardfile_id = parcel.readLong();
//		box_a = parcel.readLong();
//		box_b = parcel.readLong();
//		sideA = parcel.readString();
//		sideB = parcel.readString();
//		lastCheckedA = parcel.readString();
//		lastCheckedB = parcel.readString();
//		
//	}
	
	public void writeToParcel( Parcel parcel, int flags ) {

		parcel.writeLong( id );
		parcel.writeLong( box_a );
		parcel.writeLong( box_b );
		parcel.writeString( sideA );
		parcel.writeString( sideB );
		parcel.writeString( lastCheckedA );
		parcel.writeString( lastCheckedB );
		parcel.writeParcelable( cardfile, 0 );

	}
	
	public Card( Parcel parcel ) {
		
		id = parcel.readLong();
		box_a = parcel.readLong();
		box_b = parcel.readLong();
		sideA = parcel.readString();
		sideB = parcel.readString();
		lastCheckedA = parcel.readString();
		lastCheckedB = parcel.readString();
		cardfile = parcel.readParcelable( Cardfile.class.getClassLoader() );
		
	}
	
}
