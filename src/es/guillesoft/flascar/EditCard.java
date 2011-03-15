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

public class EditCard extends Activity {
		
	public static final String EXTRA_CARD = "card";
	
	private Card card;
	private TextView txtSideA;
	private TextView txtSideB;

	public void setUp() {

		setContentView( R.layout.edit_card );
		
		Bundle extras = getIntent().getExtras();
        card = extras.getParcelable( EXTRA_CARD );
        
        TextView txtSideA = (TextView) findViewById( R.id.edit_card_txtSideA_name );
		TextView txtSideB = (TextView) findViewById( R.id.edit_card_txtSideB_name );
		
		txtSideA.setText( card.getCardfile().getSideA() );
		txtSideB.setText( card.getCardfile().getSideB() );
		
        reload();
    
	}
		
	/* Reload view */

	private void reload() {
		
		txtSideA = (TextView) findViewById( R.id.edit_card_txtSideA );
		txtSideB = (TextView) findViewById( R.id.edit_card_txtSideB );
		
		if( card == null ) {
			
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			
		}
		else {
			
			txtSideA.setText( card.getSideA() );
			txtSideB.setText( card.getSideB() );
						
		}
		
	}
	
	public void save( View view ) {
		
		card.setSideA( txtSideA.getText().toString() );
		card.setSideB( txtSideB.getText().toString() );
		
		card.update( getContentResolver() );
		
		Intent intent = new Intent();
		intent.putExtra( EXTRA_CARD, card ); 
		setResult( RESULT_OK, intent );
		
        finish();
        		
		
	}
	
	public void cancel( View view ) {
		
		Log.d( "EditCard", "BACK" );
		setResult( RESULT_CANCELED );
		finish();
		
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