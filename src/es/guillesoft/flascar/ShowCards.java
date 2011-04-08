package es.guillesoft.flascar;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Card;

public class ShowCards extends ListActivity {
	
	private static final int ACTIVITY_SHOW_CARD = 0;
	private static final int ACTIVITY_EDIT_CARD = 1;
	
	public static final String EXTRA_CARDFILE = "cardfile";
	
	private Cardfile cardfile;
	private Cursor cursor;

	public void setUp() {

		setContentView( R.layout.show_cards );
		Bundle extras = getIntent().getExtras();
        cardfile = extras.getParcelable( EXTRA_CARDFILE );
		reload();
    
	}

	public void addCard() {
		
		if( cardfile == null ) return;
		/* PA Q COMPILE
		Card card = cardfile.addCard( getContentResolver(), "", "");
		
		Log.d( "ShowCards", "-> ACTIVITY_EDIT_CARD" );
		Intent intent = new Intent( this, EditCard.class );
		intent.putExtra( EditCard.EXTRA_CARD, card );
		startActivityForResult( intent, ACTIVITY_EDIT_CARD );
		*/
	}
	
	public void showCard( Card card ) {
		
		Log.d( "ShowCards", "-> ACTIVITY_SHOW_CARD" );
		Intent intent = new Intent( this, ShowCard.class );
		intent.putExtra( ShowCard.EXTRA_CARD, card );
		startActivityForResult( intent, ACTIVITY_SHOW_CARD );
		
	}

	
	/* Reload view */

	private void reload() {

		if( cardfile == null ) return;
		/* PA Q COMPILE
		cursor = cardfile.getCards( getContentResolver() );
        startManagingCursor( cursor );
                
        String[] from = new String[] { Card.SIDE_A, Card.SIDE_B };
    	int[] to = new int[] { R.id.show_card_row_side_a, R.id.show_card_row_side_b };
    	
    	SimpleCursorAdapter adapter =
            new SimpleCursorAdapter( this, R.layout.show_cards_row, cursor, from, to );
   	
        setListAdapter( adapter );
*/
    }
	
	/* Events */

	@Override
    protected void onListItemClick( ListView l, View v, int position, long id ) {
        super.onListItemClick(l, v, position, id);

        Cursor c = cursor;
        c.moveToPosition( position );
        Card card = Card.toCard( c, cardfile );
        Log.d( "ShowCards", "click " + card.getID() );
        showCard( card );
        
    }
  
	@Override
	public void onBackPressed()
    {
		
		Log.d( "ShowCards", "BACK" );
        setResult( RESULT_OK );
        finish();
        
    }

	@Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    	
		switch( item.getItemId() ) {
    	
    		case R.id.show_cards_add:
    			addCard();
    			return true;
            
           default:
        	   Log.e( "ShowCards", "Menu option not recognized: " + item.getItemId());
        	   break;
            
    	}
    	    	
    	return super.onOptionsItemSelected(item);
        
    }

	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_SHOW_CARD:
            	
            	Log.d( "ShowCards", "<- ACTIVITY_SHOW_CARD" );
            	break;
            	
            case ACTIVITY_EDIT_CARD:
            	
            	Log.d( "ShowCards", "<- ACTIVITY_EDIT_CARD" );
            	break;
            	
        }

        reload();
        
    }
	
	/* Lifecycle */
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	Log.d( "SelectCardfile", "CREATE" );
    	setUp();
    }

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onPrepareOptionsMenu( menu );
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate( R.menu.show_cards, menu );
	    return true;
	    	
	}

	@Override
    protected void onStart() {
    	super.onStart();
    	Log.d( "ShowCards", "START" );
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d( "ShowCards", "STOP" );
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d( "ShowCards", "DESTROY" );
    }
    
}