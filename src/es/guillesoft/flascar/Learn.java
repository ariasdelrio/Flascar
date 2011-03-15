package es.guillesoft.flascar;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.guillesoft.flascar.db.FlashcardProvider.Sense;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.ui.BoxViewBinder;

public class Learn extends ListActivity {
	
	private static final int ACTIVITY_REVIEW = 0;
	
	public static final String EXTRA_CARDFILE = "cardfile";
	public static final String EXTRA_SENSE_AB = "senseAB";
	
	private Cardfile cardfile;
	private Sense sense;
	private Cursor cursor;

	public void setUp() {

		setContentView( R.layout.learn );
		Bundle extras = getIntent().getExtras();
        cardfile = extras.getParcelable( EXTRA_CARDFILE );
        sense = extras.getBoolean( EXTRA_SENSE_AB )? Sense.AB : Sense.BA; 
		reload();
    
	}
	
	/* Reload view */

	private void reload() {

		if( cardfile == null ) return;
		
		cursor = cardfile.getBoxesExt( getContentResolver(), sense );
        startManagingCursor( cursor );
                
        String[] from = new String[] { Cardfile.BOX_NAME, Cardfile.BOX_TOTAL, Cardfile.BOX_DIRTY };
    	int[] to = new int[] { R.id.learn_row_name, R.id.learn_row_count, R.id.learn_row_count };
    	
    	SimpleCursorAdapter adapter =
            new SimpleCursorAdapter( this, R.layout.learn_row, cursor, from, to );
        
    	adapter.setViewBinder( new BoxViewBinder() );
    	
        setListAdapter(adapter);

    }
	
	/* Events */

	@Override
    protected void onListItemClick( ListView l, View v, int position, long id ) {
        super.onListItemClick(l, v, position, id);

        Cursor c = cursor;
        c.moveToPosition( position );
        long boxID = cursor.getLong( cursor.getColumnIndexOrThrow( Cardfile.BOX_ID ) );
        
        Intent intent = new Intent( this, Review.class );
        intent.putExtra( Review.EXTRA_CARDFILE, cardfile );
        intent.putExtra( Review.EXTRA_BOX_ID, boxID );
        intent.putExtra( Review.EXTRA_SENSE_AB, sense == Sense.AB );
                
        Log.d( "Learn", "-> ACTIVITY REVIEW" );
        startActivityForResult( intent, ACTIVITY_REVIEW );
        
    }
  
	@Override
	public void onBackPressed()
    {
		
		Log.d( "ShowCards", "BACK" );
        setResult( RESULT_OK );
        finish();
        
    }

	
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_REVIEW:
            	
            	Log.d( "Learn", "<- ACTIVITY_REVIEW" );
            	break;
            	
        }

        reload();
        
    }
	
	/* Lifecycle */
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	Log.d( "Learn", "CREATE" );
    	setUp();
    }

	@Override
    protected void onStart() {
    	super.onStart();
    	Log.d( "Learn", "START" );
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d( "Learn", "STOP" );
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d( "Learn", "DESTROY" );
    }
    
}