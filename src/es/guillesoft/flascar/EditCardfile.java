package es.guillesoft.flascar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import es.guillesoft.flascar.dm.Cardfile;

public class EditCardfile extends Activity {
		
	public static final String EXTRA_CARDFILE = "cardfile";
	
	private Cardfile cardfile;
	private TextView txtName;
	private TextView txtSideA;
	private TextView txtSideB;

	public void setUp() {

		setContentView( R.layout.edit_cardfile );
		
		Bundle extras = getIntent().getExtras();
        cardfile = extras.getParcelable( EXTRA_CARDFILE );
		
        reload();
    
	}
		
	/* Reload view */

	private void reload() {
		
		txtName = (TextView) findViewById( R.id.edit_cardfile_txtName );
		txtSideA = (TextView) findViewById( R.id.edit_cardfile_txtSideA );
		txtSideB = (TextView) findViewById( R.id.edit_cardfile_txtSideB );
		
		if( cardfile == null ) {
			
			txtName.setText( "Error" );
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			
		}
		else {
			
			txtName.setText( cardfile.getName() );
			txtSideA.setText( cardfile.getSideA() );
			txtSideB.setText( cardfile.getSideB() );
						
		}
		
	}
	
	public void save( View view ) {
		
		cardfile.setName( txtName.getText().toString() );
		cardfile.setSideA( txtSideA.getText().toString() );
		cardfile.setSideB( txtSideB.getText().toString() );
		
		cardfile.update( getContentResolver() );
		
		Intent intent = new Intent();
		intent.putExtra( EXTRA_CARDFILE, cardfile ); 
		setResult( RESULT_OK, intent );
		
        finish();
        		
		
	}
	
	public void cancel( View view ) {
		
		Log.d( "EditCardfile", "BACK" );
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