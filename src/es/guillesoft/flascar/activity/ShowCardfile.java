package es.guillesoft.flascar.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;

public class ShowCardfile extends FlascarActivity {
		
	/// long cardfileID
	public static final String IN_CARDFILE = "cardfile";
	
	private Cardfile cardfile;
	
	private TextView txtName;
	private TextView txtSideA;
	private TextView txtSideB;
	private TextView txtCards;
	
	private boolean importMode;

	public ShowCardfile() {
		super( "ShowCardfile", R.layout.show_cardfile );
	}
	
	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
		
		registerActivity( EditCardfile.class );
		registerActivity( ShowCards.class );
		registerActivity( SelectFile.class );
		
		Bundle extras = getIntent().getExtras();
		if( extras != null ) {
			// TODO: hacerlo bien
			try {
			cardfile = Core.getInstance().getDataModel( getContentResolver() )
				.getCardfile( extras.getLong( IN_CARDFILE ) );
			}
			catch( Exception e ) { Log.e( "EX", e.toString() ); }
		}
        
        txtName = (TextView) findViewById( R.id.txtName );
		txtSideA = (TextView) findViewById( R.id.txtSideA );
		txtSideB = (TextView) findViewById( R.id.txtSideB );
		txtCards = (TextView) findViewById( R.id.txtCards );
		
	}
	
	@Override
	public void tearDown() {

	}
	
	@Override
	public void refresh() {
		
		if( cardfile == null ) {
				
			txtName.setText( "Error" );
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			txtCards.setText( "Error" );
				
		}
		else {

			txtName.setText( cardfile.getName() );
			txtSideA.setText( cardfile.getSideA() + " (" + cardfile.getLanguageA() + ")" );
			txtSideB.setText( cardfile.getSideB() + " (" + cardfile.getLanguageB() + ")" );
			txtCards.setText( Long.toString( cardfile.getCardCount() ) );
			
		}
			
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
		if( clazz.equals( SelectFile.class ) ) if ( resultCode == RESULT_OK ) {
			
			Bundle extras = intent.getExtras();
			if( extras != null ) {
				
				String filename = extras.getString( SelectFile.OUT_FILE );
				Log.d( getClass().getSimpleName(), ( importMode ? "import from" : "export to" ) + filename );
				
				try {
					
					File file = new File( extras.getString( SelectFile.OUT_FILE ) );
					if( importMode ) cardfile.addCardsFromDeck( file );
					else cardfile.saveCardsToDeck( file );
					Toast.makeText( getApplicationContext(), "success!", Toast.LENGTH_SHORT ).show();
					
				}
				catch( Exception e ) {
					
					Log.e( getClass().getSimpleName(), "error loading deck: " + e.getMessage() );
					Toast.makeText( getApplicationContext(), "error!", Toast.LENGTH_SHORT ).show();
					
				}
				
			}
			
		}
		
	}

	@Override
	public void back() {
		
		setResult( cardfile == null ? RESULT_CANCELED : RESULT_OK );
		finish();
	        
	}
	
	/* Layout events */
	
	public void edit( View view ) {
		Log.d( getClass().getSimpleName(), "edit" );
		
		if( cardfile == null ) return;
		
		Bundle extras = new Bundle();
		extras.putLong( EditCardfile.IN_CARDFILE, cardfile.getID() );
		startActivity( EditCardfile.class, extras );
		
	}
		
	public void show( View view ) {
		Log.d( getClass().getSimpleName(), "show" );
		
		if( cardfile == null ) return;

		Bundle extras = new Bundle();
		extras.putLong( ShowCards.IN_CARDFILE, cardfile.getID() );
		startActivity( ShowCards.class, extras );
	
	}
	
	public void importDeck( View view ) {
		Log.d( getClass().getSimpleName(), "importDeck" );
		
		importMode = true;
		
		Bundle extras = new Bundle();
		extras.putString( SelectFile.IN_EXTENSION, "deck" );
		extras.putBoolean( SelectFile.IN_READONLY, true );
		startActivity( SelectFile.class, extras );
	
	}
	
	public void exportDeck( View view ) {
		Log.d( getClass().getSimpleName(), "exportDeck" );
		
		importMode = false;
		
		Bundle extras = new Bundle();
		extras.putString( SelectFile.IN_EXTENSION, "deck" );
		extras.putBoolean( SelectFile.IN_READONLY, false );
		startActivity( SelectFile.class, extras );
	
	}
		
}