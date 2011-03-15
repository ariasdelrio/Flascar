package es.guillesoft.flascar.dm;

import es.guillesoft.flascar.db.FlashcardProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

public class Main {

	public static final String CURRENT_CARDFILE = es.guillesoft.flascar.db.Main.CURRENT_CARDFILE;
    
    public static long getCurrentCardfile( ContentResolver cr ) {
		
		Cursor c = cr.query( 
				FlashcardProvider.main.getUri(), 
				new String[] { CURRENT_CARDFILE }, 
				es.guillesoft.flascar.db.Main.ID + " = ?", 
				new String[] { "1" }, 
				null );
		
		if( c == null || c.moveToFirst() == false ) return -1;
		int index = c.getColumnIndex( CURRENT_CARDFILE );
		
		return c.isNull( index ) ? -1 : c.getLong( index );

	}

    public static boolean setCurrentCardfile( ContentResolver cr, long id ) {
     	 
    	ContentValues values = new ContentValues();
		values.put( CURRENT_CARDFILE, id );
		
		return cr.update( 
     		FlashcardProvider.main.getUri(), 
     		values, 
     		es.guillesoft.flascar.db.Main.ID + " = ?",
     		new String[] { "1" } ) == 1;
		
    }


	
}
