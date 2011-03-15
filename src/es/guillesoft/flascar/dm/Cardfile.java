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

public class Cardfile implements Parcelable {
  
    public static final String ID = es.guillesoft.flascar.db.Cardfile.ID;
    public static final String NAME = es.guillesoft.flascar.db.Cardfile.NAME;
    public static final String SIDE_A = es.guillesoft.flascar.db.Cardfile.SIDE_A;
    public static final String SIDE_B = es.guillesoft.flascar.db.Cardfile.SIDE_B;
    
    public static final String COUNT_CARDS = es.guillesoft.flascar.db.Cardfile.COUNT_CARDS;
    
    public static final String BOX_ID = es.guillesoft.flascar.db.Cardfile.BOX_ID;
    public static final String BOX_NAME = es.guillesoft.flascar.db.Cardfile.BOX_NAME;
    public static final String BOX_TOTAL = es.guillesoft.flascar.db.Cardfile.BOX_TOTAL;
    public static final String BOX_DIRTY = es.guillesoft.flascar.db.Cardfile.BOX_DIRTY;
    
	public static final String REVIEW_ID = es.guillesoft.flascar.db.Cardfile.REVIEW_ID;
	public static final String REVIEW_SIDE_A = es.guillesoft.flascar.db.Cardfile.REVIEW_SIDE_A;
	public static final String REVIEW_SIDE_B = es.guillesoft.flascar.db.Cardfile.REVIEW_SIDE_B;
	public static final String REVIEW_BOX_A = es.guillesoft.flascar.db.Cardfile.REVIEW_BOX_A;
	public static final String REVIEW_BOX_B = es.guillesoft.flascar.db.Cardfile.REVIEW_BOX_B;
	public static final String REVIEW_LAST_CHECKED_A = es.guillesoft.flascar.db.Cardfile.REVIEW_LAST_CHECKED_A;
	public static final String REVIEW_LAST_CHECKED_B = es.guillesoft.flascar.db.Cardfile.REVIEW_LAST_CHECKED_B;
	
    private long id;
    private String name;
    private String sideA;
    private String sideB;
    
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
    
    private Cardfile( long id, String name, String sideA, String sideB ) {
    	
    	this.name = name;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.id = id;
    	
    }
    
    public static Cardfile create( ContentResolver cr, String name, String sideA, String sideB ) {
    	
    	Log.d( "Cardfile", "create" );
		
		ContentValues values = new ContentValues();
		values.put( NAME, name );
		values.put( SIDE_A, sideA );
		values.put( SIDE_B, sideB );

		Uri newUri = cr.insert( FlashcardProvider.cardfile.getUri(), values );
	
		List<String> s = newUri.getPathSegments();
		if( s.get( 0 ).equals( es.guillesoft.flascar.db.Cardfile.URI_ROOT  ) &&
		    s.get( 1 ).equals( es.guillesoft.flascar.db.Cardfile.URI_PLAIN ) ) {
			long id = Long.parseLong( s.get( 2 ) );
			Log.d( "Cardfile", "created (" + id + ")" );
			return new Cardfile( id, name, sideA, sideB );
		}
		else return null;
		
    }
    
    public static Cardfile get( ContentResolver cr, long id ) {
		
		Cursor c = cr.query( 
				FlashcardProvider.cardfile.getUri(), 
				new String[] { ID, NAME, SIDE_A, SIDE_B }, 
				ID + " = ?", 
				new String[] { new Long( id ).toString() }, 
				null );
		
		if( c == null || c.moveToFirst() == false ) return null;
		return Cardfile.toCardfile( c );

	}
    
    public static Cardfile getCurrent( ContentResolver cr ) {
		
    	long current = Main.getCurrentCardfile( cr );
    	if( current == -1 ) return null;
    	else return get( cr, current );

	}

    public static Cursor getAll( ContentResolver contentResolver ) {

		return contentResolver.query( 
				FlashcardProvider.cardfile.getCountUri(), 
				new String[] { ID, NAME, SIDE_A, SIDE_B, COUNT_CARDS }, 
				null, 
				null, 
				null );
		
	}
    
    public static Cardfile toCardfile( Cursor cursor ) throws IllegalArgumentException {
    	
    	try {
    		
    		int index = cursor.getColumnIndexOrThrow( ID );
    		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Cardfile must have ID" );
    		long id =  cursor.getLong( index ); 
    		
    		index = cursor.getColumnIndexOrThrow( NAME );
    		String name = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		index = cursor.getColumnIndexOrThrow( SIDE_A );
    		String sideA = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		index = cursor.getColumnIndexOrThrow( SIDE_B );
    		String sideB = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
    		
    		return new Cardfile( id, name, sideA, sideB );
    		
    	}
    	catch( IllegalArgumentException e ) {
    		
    		Log.e( "Cardfile", "Illegal argument" );
    		throw e;
    		
    	}
    	
    }
    
    public boolean delete( ContentResolver cr ) {
		
		Log.d( "Cardfile", "delete " + id );
		
		// @todo remove all cards
		return cr.delete( 
				FlashcardProvider.cardfile.getUri(), 
				ID + " = ?",
				new String[] { new Long( id ).toString() } ) == 1;
		
	}

    public boolean update( ContentResolver contentResolver ) {
      	 
    	Log.d( "Cardfile", "update " + id );
		
		ContentValues values = new ContentValues();
		values.put( NAME, name );
		values.put( SIDE_A, sideA );
		values.put( SIDE_B, sideB );
		
		return contentResolver.update( 
			FlashcardProvider.cardfile.getUri(), 
     		values, 
     		ID + " = ?",
     		new String[] { new Long( id ).toString() } ) == 1;
		
	}
    
	public Cursor getCards( ContentResolver contentResolver ) {
		
		Log.d( "Cardfile", "getCards " + id );
    	
    	return contentResolver.query( 
    			FlashcardProvider.card.getUri(), 
    			null,
				Card.CARDFILE_ID + " = ?",
				new String[] { new Long( id ).toString() },
				null );
		
	}
    
    public Card addCard( ContentResolver cr, String sideA, String sideB ) {
		
		Log.d( "Cardfile", "addCard" );
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		String lch = dateFormat.format( date );
		
		return Card.create( cr, this, 1, 1, sideA, sideB, lch, lch );

	}
    
    public Cursor getBoxesExt( ContentResolver cr, FlashcardProvider.Sense sense ) {
		
		Log.d( "Cardfile", "getBoxesExt " + id + " - " + sense );
    	
    	return cr.query( 
    			FlashcardProvider.cardfile.getBoxUri( sense ),
				new String[] { BOX_ID, BOX_NAME, BOX_TOTAL, BOX_DIRTY }, 
				null,
				new String[] { new Long( id ).toString(),  new Long( id ).toString() },
				null );

	}

	public Cursor getCardsToReview( ContentResolver cr, long boxID, FlashcardProvider.Sense sense ) {

		Log.d( "Cardfile", "getCardsToReview " + id + " (box " + boxID + ") - " + sense );
		
		String box_str = REVIEW_BOX_A;
		String lch_str = REVIEW_LAST_CHECKED_A;
		if( sense == FlashcardProvider.Sense.BA ) {
			box_str = REVIEW_BOX_B;
			lch_str = REVIEW_LAST_CHECKED_B;
		}
    	
    	return cr.query( 
				FlashcardProvider.cardfile.getReviewUri( sense ), 
				new String[] { REVIEW_ID, REVIEW_SIDE_A, REVIEW_SIDE_B, box_str, lch_str },
				null,
				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
				null );
		
		
	}
	
	public Cursor test( ContentResolver cr, long boxID ) {

		Log.d( "Cardfile", "test " + id + " (box " + boxID + ")" );
		
    	return cr.query( 
				FlashcardProvider.cardfile.getTestUri( ), 
				null,
				null,
				new String[] { new Long( id ).toString(), new Long( boxID ).toString() },
				null );
    	
    	
	}
	
	/* Parcelable */
	
	public static final Parcelable.Creator<Cardfile> CREATOR = new Parcelable.Creator<Cardfile> () {
	    public Cardfile createFromParcel( Parcel source ) {
	          return new Cardfile( source );
	    }
	    public Cardfile[] newArray( int size ) {
	          return new Cardfile[size];
	    }
	};
	
	public int describeContents() {
		return hashCode();
	}

	public void writeToParcel( Parcel parcel, int flags ) {

		parcel.writeLong( id );
		parcel.writeString( name );
		parcel.writeString( sideA );
		parcel.writeString( sideB );

	}
	
	public Cardfile( Parcel parcel ) {
		
		id = parcel.readLong();
		name = parcel.readString();
		sideA = parcel.readString();
		sideB = parcel.readString();
		
	}
	
}

