package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Cardfile.Sense;
import es.guillesoft.flascar.dm.Main;

public class Welcome extends FlascarActivity {
	
	private Main dm;
	
	private TextView txtName;
	private TextView txtSense;
	private ImageButton btnSense;
	
	private Sense sense;
	
	public Welcome() {
	
		super( "Welcome", R.layout.welcome );
		
	}
	
	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
		        
		registerActivity( SelectCardfile.class );
		registerActivity( ShowCardfile.class );
		dm = Core.getInstance().getDataModel( getContentResolver() );
		
		sense = Sense.AB;
		
		txtName = (TextView) findViewById( R.id.txtName );
		txtSense = (TextView) findViewById( R.id.txtSense );
		btnSense = (ImageButton) findViewById( R.id.btnSense );
		
	}
	
	@Override
	public void tearDown() {

	}
	
	@Override
	public void refresh() {
		
		Cardfile currentCardfile = dm.getCurrentCardfile();
		
		if( currentCardfile == null ) {
			
			txtName.setText( R.string.welcome_none );
			txtSense.setText( "" );
			btnSense.setVisibility( View.INVISIBLE );
			
		}
		else {
		
			txtName.setText( currentCardfile.getName() );
			if( sense == Sense.AB ) txtSense.setText( currentCardfile.getSideA() + " > " + currentCardfile.getSideB() );
			else txtSense.setText( currentCardfile.getSideB() + " > " + currentCardfile.getSideA() );
			btnSense.setVisibility( View.VISIBLE );
			
		}
		
		
				
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {

	}

	@Override
	public void back() {
		
	}
	
	/* Layout events */
	
	public void changeCardfile( View view ) {

		startActivity( SelectCardfile.class );
		
	}
	
	public void changeSense( View view ) {
		
		Log.d( getClass().getSimpleName(), "changeSense" );
		
		sense = sense == Sense.AB ? Sense.BA : Sense.AB;
		refresh();
		
	}

	public void show( View view ) {
		
		Cardfile cardfile = dm.getCurrentCardfile();
		
		if( cardfile == null ) return; // button shouldn't be enabled
		
		Bundle extras = new Bundle();
		extras.putLong( ShowCardfile.IN_CARDFILE, cardfile.getID() );
		startActivity( ShowCardfile.class, extras );
		
	}

	public void learn( View view ) {
		
		Log.d( getClass().getSimpleName(), "learn" );
		
		Toast.makeText( getApplicationContext(), "sin implementar", Toast.LENGTH_SHORT ).show();
		
//		if( cardfile == null ) return; // button shouldn't be enabled
		/* PA Q COMPILE
		Log.d( "Welcome", "-> ACTIVITY_LEARN" );
		Intent intent = new Intent( this, Learn.class );
		intent.putExtra( Learn.EXTRA_CARDFILE, cardfile );
		intent.putExtra( Learn.EXTRA_SENSE_AB, sense == Sense.AB );
		startActivityForResult( intent, ACTIVITY_LEARN );
		*/
	}
		
}