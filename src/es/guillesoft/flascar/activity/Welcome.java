package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;

public class Welcome extends FlascarActivity {
	
	private Main dm;
	
	private TextView txtName;
	private TextView txtInfo;
	
	public Welcome() {
	
		super( "Welcome", R.layout.welcome );
		
	}
	
	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
		        
		registerActivity( SelectCardfile.class );
		registerActivity( ShowCardfile.class );
		registerActivity( Learn.class );
		
		dm = Core.getInstance().getDataModel( getContentResolver() );
		
		txtName = (TextView) findViewById( R.id.txtName );
		txtInfo = (TextView) findViewById( R.id.txtInfo );
		
	}
	
	@Override
	public void tearDown() {

	}
	
	@Override
	public void refresh() {
		
		Cardfile currentCardfile = dm.getCurrentCardfile();
		
		if( currentCardfile == null ) {
			
			txtName.setText( R.string.welcome_none );
			txtInfo.setText( "" );
			
		}
		else {
		
			txtName.setText( currentCardfile.getName() );
			txtInfo.setText( currentCardfile.getCardCount() + " " + getString( R.string.cards ) );
			
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
	
	public void show( View view ) {
		
		Cardfile cardfile = dm.getCurrentCardfile();
		
		// TODO: toast
		if( cardfile == null ) return; // button shouldn't be enabled
		
		Bundle extras = new Bundle();
		extras.putLong( ShowCardfile.IN_CARDFILE, cardfile.getID() );
		startActivity( ShowCardfile.class, extras );
		
	}

	public void learn( View view ) {
		
		Cardfile cardfile = dm.getCurrentCardfile();
		
		Log.d( getClass().getSimpleName(), "learn" );
		
		// TODO: toast
		if( cardfile == null ) return; // button shouldn't be enabled
		
		startActivity( Learn.class );
		
	}
		
}