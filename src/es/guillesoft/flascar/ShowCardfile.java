package es.guillesoft.flascar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import es.guillesoft.flascar.activity.SelectCardfile;
import es.guillesoft.flascar.dm.Cardfile;

public class ShowCardfile extends Activity {
		
	private static final int ACTIVITY_EDIT_CARDFILE = 0;
	private static final int ACTIVITY_SHOW_CARDS    = 1;
	
	public static final String EXTRA_CARDFILE = "cardfile";
	
	private Cardfile cardfile;

	public void setUp() {

		setContentView( R.layout.show_cardfile );
		
		Bundle extras = getIntent().getExtras();
        cardfile = extras.getParcelable( EXTRA_CARDFILE );
		
        reload();
    
	}
		
	/* Reload view */

	private void reload() {
		
		TextView txtName = (TextView) findViewById( R.id.show_cardfile_txtName );
		TextView txtSideA = (TextView) findViewById( R.id.show_cardfile_txtSideA );
		TextView txtSideB = (TextView) findViewById( R.id.show_cardfile_txtSideB );
		
		if( cardfile == null ) {
			
			txtName.setText( "Error" );
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			
		}
		else {
			/* PA Q COMPILE
			txtName.setText( cardfile.getName() );
			txtSideA.setText( cardfile.getSideA() );
			txtSideB.setText( cardfile.getSideB() );
						*/
		}
		
	}
	
	public void editCardfile( View view ) {
		Log.d( "ShowCardfile", "editCardfile" );
		if( cardfile == null ) return;
		
		Log.d( "ShowCardfile", "-> ACTIVITY_EDIT_CARDFILE" );
		Intent intent = new Intent( this, EditCardfile.class );
		intent.putExtra( EditCardfile.EXTRA_CARDFILE, cardfile );
		startActivityForResult( intent, ACTIVITY_EDIT_CARDFILE );
		
	}
	
	public void showCards( View view ) {
		Log.d( "ShowCardfile", "showCards" );
		if( cardfile == null ) return;

		Log.d( "ShowCards", "-> ACTIVITY_SHOW_CARDS" );
		Intent intent = new Intent( this, ShowCards.class );
		intent.putExtra( ShowCards.EXTRA_CARDFILE, cardfile );
		startActivityForResult( intent, ACTIVITY_SHOW_CARDS );

	}
	
	/* Events */
	   
	@Override
	public void onBackPressed()
    {
		
		Log.d( "ShowCardfile", "BACK" );
		
		if( cardfile == null ) setResult( RESULT_CANCELED );
		else {
	        Intent intent = new Intent();
	        intent.putExtra( EXTRA_CARDFILE, cardfile ); 
	        setResult( RESULT_OK, intent );
		}
		
        finish();
        
    }
		
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_EDIT_CARDFILE:
            	
            	Log.d( "Welcome", "<- ACTIVITY_EDIT_CARDFILE" );
            	
            	if ( resultCode == RESULT_OK )
            	{
            		
            		Bundle extras = intent.getExtras();
            		cardfile = extras.getParcelable( /*SelectCardfile.EXTRA_CARDFILE*/"lala" );
            		reload();
            		
            	}
            	else Log.d( "Welcome", "ACTIVITY_EDIT_CARDFILE cancelled" );
            	break;
            	
            case ACTIVITY_SHOW_CARDS:
            	
            	Log.d( "Welcome", "<- ACTIVITY_SHOW_CARDS" );
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