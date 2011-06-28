package es.guillesoft.flascar.dm;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import es.guillesoft.flascar.db.FlashcardProvider;
import es.guillesoft.flascar.db.view.BoxView;

class BoxCollection {
	
	private ArrayList<Box> boxes;
	private ContentResolver cr;
	
	BoxCollection( ContentResolver cr ) {
		
		this.cr = cr;
		boxes = null;

	}
	
	ArrayList<Box> getBoxes() throws Exception {
	
    	if( boxes == null ) readBoxes();
    	return boxes;
    		
    }
	
	ContentResolver getContentResolver() {
		return cr;
	}
	
	/* private */
    
    private void readBoxes() throws Exception {
    	
    	Log.d( this.getClass().getSimpleName(), "readBoxes" );
    	
    	Cursor c = cr.query( FlashcardProvider.getUri( FlashcardProvider.BOX ), null,	null, null,	null );
    	if( c == null ) throw new Exception( "cursor is null" );
    		
    	Box [] boxesArray = new Box[Box.BOX_COUNT]; 
    	
    	if( c.moveToFirst() == true ) while( !c.isAfterLast() ) {
    			
    		Box box = cursorToBox( c );
    		
    		long id = box.getID();
    		if( id < 1 || id > Box.BOX_COUNT ) continue;
    		
    		boxesArray[(int)id - 1] = box;
    		c.moveToNext();
    			
    	}
    	
    	boxes = new ArrayList<Box>();
    	for( int i = 0; i < Box.BOX_COUNT; i++) boxes.add( boxesArray[i] ); 
    	
    }

	private Box cursorToBox( Cursor cursor ) throws IllegalArgumentException {
		
		int index = cursor.getColumnIndexOrThrow( BoxView.ID );
		if( cursor.isNull( index ) ) throw new IllegalArgumentException( "Box must have ID" );
		long id =  cursor.getLong( index ); 
		
		index = cursor.getColumnIndexOrThrow( BoxView.NAME );
		String name = cursor.isNull( index ) ? "ND" : cursor.getString( index ); 
			
		index = cursor.getColumnIndexOrThrow( BoxView.EXPIRATION );
		long expiration = cursor.isNull( index ) ? 0 : cursor.getLong( index ); 
		
		return new Box( id, name, expiration );
			
	}
	
}