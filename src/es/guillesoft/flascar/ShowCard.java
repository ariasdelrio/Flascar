package es.guillesoft.flascar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import es.guillesoft.flascar.dm.Card;

public class ShowCard extends Activity {
		
	private static final int ACTIVITY_EDIT_CARD = 0;
	
	public static final String EXTRA_CARD = "card";
	
	private Card card;

	public void setUp() {

		setContentView( R.layout.show_card );
		
		Bundle extras = getIntent().getExtras();
        card = extras.getParcelable( EXTRA_CARD );
        
        TextView txtSideA = (TextView) findViewById( R.id.show_card_txtSideA_name );
		TextView txtSideB = (TextView) findViewById( R.id.show_card_txtSideB_name );
		/* PA Q COMPILE
		txtSideA.setText( card.getCardfile().getSideA() );
		txtSideB.setText( card.getCardfile().getSideB() );
		*/
        reload();
    
	}
		
	/* Reload view */

	private void reload() {
				
//		Cursor c = card.getCardfile().test( getContentResolver(), 1 );
//		c.moveToFirst();
//		Log.d("cursor", "N = " + c.getColumnCount() );
//		
//	TextView txtSideA = (TextView) findViewById( R.id.show_card_txtSideA );
//	TextView txtSideB = (TextView) findViewById( R.id.show_card_txtSideB );
//	TextView txtLastCheckedA = (TextView) findViewById( R.id.show_card_txtLastCheckedA );
//	TextView txtLastCheckedB = (TextView) findViewById( R.id.show_card_txtLastCheckedB );
//	
//	txtSideA.setText( c.getString( c.getColumnIndex( "expiration" ) ) );
//	txtSideB.setText( c.getString( c.getColumnIndex( "lch1" ) ) );
//	txtLastCheckedA.setText( c.getString( c.getColumnIndex( "lch2" ) ) );
//	txtLastCheckedB.setText( c.getString( c.getColumnIndex( "lch3" ) ) );
		
		TextView txtSideA = (TextView) findViewById( R.id.show_card_txtSideA );
		TextView txtSideB = (TextView) findViewById( R.id.show_card_txtSideB );
		TextView txtLastCheckedA = (TextView) findViewById( R.id.show_card_txtLastCheckedA );
		TextView txtLastCheckedB = (TextView) findViewById( R.id.show_card_txtLastCheckedB );
		TextView txtBoxA = (TextView) findViewById( R.id.show_card_txtBoxA );
		TextView txtBoxB = (TextView) findViewById( R.id.show_card_txtBoxB );
		
		if( card == null ) {
			
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			txtLastCheckedA.setText( "Error" );
			txtLastCheckedB.setText( "Error" );
			txtBoxA.setText( "Error" );
			txtBoxB.setText( "Error" );
			
		}
		else {
			
			txtSideA.setText( card.getSideA() );
			txtSideB.setText( card.getSideB() );
			txtLastCheckedA.setText( card.getLastCheckedA() );
			txtLastCheckedB.setText( card.getLastCheckedB() );
			txtBoxA.setText( "" + card.getBoxA() );
			txtBoxB.setText( "" + card.getBoxB() );
						
		}
		
	}
	
	public void editCard( View view ) {
		Log.d( "ShowCard", "editCard" );
		if( card == null ) return;
		
		Log.d( "ShowCard", "-> ACTIVITY_EDIT_CARD" );
		Intent intent = new Intent( this, EditCard.class );
		intent.putExtra( EditCard.EXTRA_CARD, card );
		startActivityForResult( intent, ACTIVITY_EDIT_CARD );
		
	}
	
	public void deleteCard( View view ) {
		Log.d( "ShowCard", "deleteCard" );
		if( card == null ) return;
		
		card.delete( getContentResolver() );
		setResult( RESULT_OK );
		finish();

	}
	
	/* Events */
	   
	@Override
	public void onBackPressed()
    {
		
		Log.d( "ShowCardfile", "BACK" );
		setResult( card == null ? RESULT_OK : RESULT_CANCELED );
        finish();
        
    }
	
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_EDIT_CARD:
            	
            	Log.d( "ShowCard", "<- ACTIVITY_EDIT_CARD" );
            	
            	if ( resultCode == RESULT_OK )
            	{
            		
            		Bundle extras = intent.getExtras();
            		card = extras.getParcelable( EditCard.EXTRA_CARD );
            		reload();
            		
            	}
            	else Log.d( "ShowCard", "ACTIVITY_EDIT_CARD cancelled" );
            	break;
            	
        }
    	
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
	    inflater.inflate( R.menu.select_cardfile, menu );
	    return true;
	    	
	}
	 
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.d( "SelectCardfile", "START" );
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d( "SelectCardfile", "STOP" );
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d( "SelectCardfile", "DESTROY" );
    }
    
}