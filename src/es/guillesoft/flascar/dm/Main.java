package es.guillesoft.flascar.dm;

import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.MainView;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Main {
	
	private ContentResolver cr;
	
	private Cursor cardfiles;
	private Cardfile currentCardfile;
	private boolean read;

	
	public Main( ContentResolver cr ) {
		
		this.cr = cr;
		cardfiles = null;
		currentCardfile = null;
		read = false;

	}

    public Cardfile getCurrentCardfile() {
		
    	if( read ) return currentCardfile;
    	 
    	Cursor c = cr.query( 
    		FlashcardProvider.getUri( FlashcardProvider.MAIN ), 
    		new String[] { MainView.CURRENT_CARDFILE }, 
			MainView.ID + " = 1",
			null, 
			null );
		
    	if( c == null || c.moveToFirst() == false ) { 
    		Log.e( this.getClass().getSimpleName(), "getCurrentCardfile failed" );
    		return null;
    	}
    	
    	int index = c.getColumnIndex( MainView.CURRENT_CARDFILE );
    	read = true;
    	
    	if( !c.isNull( index ) ) currentCardfile = Cardfile.get( cr, c.getLong( index ) );

    	return currentCardfile;
    	
	}

    public void setCurrentCardfile( Cardfile cardfile ) {
     	 
    	ContentValues values = new ContentValues();
		values.put( MainView.CURRENT_CARDFILE, cardfile.getID() );
		
		if( cr.update( 
				FlashcardProvider.getUri( FlashcardProvider.MAIN ), 
				values, 
				MainView.ID + " = 1",
				null ) != 1 ) {
			Log.e( this.getClass().getSimpleName(), "setCurrentCardfile failed" );
		}
		else currentCardfile = cardfile;
		
    }
	
    public int getCardfileCount() {
    	
    	if( cardfiles == null ) readCardfiles();
    	return cardfiles.getCount();
    	
    }
    
    public Cardfile getCardfile( int index ) {
    	
    	if( index < 0 || index >= getCardfileCount() ) return null;
    	cardfiles.moveToPosition( index );
    	return Cardfile.toCardfile( cardfiles );
    	
    }
    
    public Cardfile addCardfile( String name, String sideA, String sideB ) {
    	
    	Cardfile newCardfile = Cardfile.create( cr, name, sideA, sideB );
    	readCardfiles();
    	return newCardfile;
    	
    }
    
    /* AUX */
    
    private void readCardfiles() {
    	
    	cardfiles = Cardfile.getAll( cr );
    	
    }
	
}
