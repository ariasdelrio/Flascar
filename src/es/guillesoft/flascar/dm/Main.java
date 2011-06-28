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
	private BoxCollection boxCollection;
	private long currentCardfile;
	
	public Main( ContentResolver cr ) {
		
		this.cr = cr;
		cardfileCollection = new CardfileCollection( cr );
		boxCollection = new BoxCollection( cr );
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

	public Cardfile addCardfile( String name, String sideA, String sideB, 
			String languageA, String languageB ) throws Exception {
    	
    	return cardfileCollection.addCardfile( name, sideA, sideB, languageA, languageB );
    	
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
    
    public ArrayList<Box> getBoxes() {
        
		try {
			
			return boxCollection.getBoxes();
			
		}
		catch( Exception e ) {
			
			return null;
			
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
