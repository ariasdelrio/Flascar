package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.ui.ConfirmDialog;
import es.guillesoft.flascar.ui.ConfirmDialogListener;

public class ShowCard extends FlascarActivity implements ConfirmDialogListener {
		
	/// long cardID
	public static final String IN_CARD = "card";
	
	private Card card;

	private TextView txtSideATitle;
	private TextView txtSideBTitle;
	private TextView txtSideA;
	private TextView txtSideB;
	private TextView txtLastCheckedA;
	private TextView txtLastCheckedB;
	private TextView txtBoxA;
	private TextView txtBoxB;
	
	private int dlgConfirm; 
	
	public ShowCard() {
		super( "ShowCard", R.layout.show_card );
	}
	
	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
	
		registerActivity( EditCard.class );
		
		dlgConfirm = registerDialog( new ConfirmDialog( getString( R.string.dlg_confirm_delete_card ), this ) );
		
		Bundle extras = getIntent().getExtras();
		if( extras != null ) {
			// TODO: hacerlo bien
			try {
				card = Core.getInstance().getDataModel( getContentResolver() )
					.getCurrentCardfile().getCard( extras.getLong( IN_CARD ) );
			}
			catch( Exception e ) { Log.e( "EX", e.toString() ); }
		}
        
		txtSideATitle = (TextView) findViewById( R.id.txtSideATitle );
		txtSideBTitle = (TextView) findViewById( R.id.txtSideBTitle );
		txtSideA = (TextView) findViewById( R.id.txtSideA );
		txtSideB = (TextView) findViewById( R.id.txtSideB );
		txtLastCheckedA = (TextView) findViewById( R.id.txtLastCheckedA );
		txtLastCheckedB = (TextView) findViewById( R.id.txtLastCheckedB );
		txtBoxA = (TextView) findViewById( R.id.txtBoxA );
		txtBoxB = (TextView) findViewById( R.id.txtBoxB );
    
	}
		
	public void tearDown() {

	}
	
	@Override
	public void refresh() {
			
		if( card == null ) {
			
			txtSideATitle.setText( "Error" );
			txtSideBTitle.setText( "Error" );
			txtSideA.setText( "Error" );
			txtSideB.setText( "Error" );
			txtLastCheckedA.setText( "Error" );
			txtLastCheckedB.setText( "Error" );
			txtBoxA.setText( "Error" );
			txtBoxB.setText( "Error" );
			
		}
		else {
			
			txtSideATitle.setText( card.getCardfile().getSideA() + " (" + card.getCardfile().getLanguageA() + ")" );
			txtSideBTitle.setText( card.getCardfile().getSideB() + " (" + card.getCardfile().getLanguageB() + ")" );
			txtSideA.setText( card.getSideA() );
			txtSideB.setText( card.getSideB() );
			txtLastCheckedA.setText( getString( R.string.lastReview ) + ": " + card.getLastCheckedA() );
			txtLastCheckedB.setText( getString( R.string.lastReview ) + ": " + card.getLastCheckedB() );
			txtBoxA.setText( getString( R.string.box ) + ": " + card.getBoxA() );
			txtBoxB.setText( getString( R.string.box ) + ": " + card.getBoxB() );
						
		}
		
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
	
		/*
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_EDIT_CARD:
            	
            	Log.d( "ShowCard", "<- ACTIVITY_EDIT_CARD" );
            	
            	if ( resultCode == RESULT_OK )
            	{
            		
            		reload();
            		
            	}
            	else Log.d( "ShowCard", "ACTIVITY_EDIT_CARD cancelled" );
            	break;
            	
        }
*/
    

	}
	
	@Override
	public void back() {
		
		if( card == null ) returnOK();
		else returnCancel();
	        
	}
	
	/* Layout events */
	
	public void editCard( View view ) {
		Log.d( getClass().getSimpleName(), "editCard" );
		
		if( card == null ) return;
		
		Bundle extras = new Bundle();
		extras.putLong( EditCard.IN_CARD, card.getID() );
		startActivity( EditCard.class, extras );
		
	}
	
	public void deleteCard( View view ) {
		Log.d( getClass().getSimpleName(), "deleteCard" );
		
		if( card == null ) return;
		
		showDialog( dlgConfirm );
		
	}
	
	public void openWikiA( View view ) {
		Log.d( getClass().getSimpleName(), "openWikiA" );
		
		openWiki( card.getCardfile().getLanguageA(), card.getSideA() );
		
	}
	
	public void openWikiB( View view ) {
		Log.d( getClass().getSimpleName(), "openWikiB" );
		
		openWiki( card.getCardfile().getLanguageB(), card.getSideB() );		
		
	}

	public void openDictionaryA( View view ) {
		Log.d( getClass().getSimpleName(), "openDictionaryA" );
		
		openDictionary( card.getCardfile().getLanguageA(), card.getSideA() );
		
	}

	public void openDictionaryB( View view ) {
		Log.d( getClass().getSimpleName(), "openDictionaryB" );
		
		openDictionary( card.getCardfile().getLanguageB(), card.getSideB() );
		
	}
	
	/* Dialog events */
	
	@Override
	public void dlgConfirm( boolean confirm ) {
		
		if( !confirm ) return;
		
		card.getCardfile().deleteCard( card );
		returnOK();
		
	}
	
	/* private */

	private void openWiki( String languageCode, String text ) {
		
		String url = "http://" + languageCode + ".wikipedia.org/wiki/" + text.replace( ' ', '_' );
		Intent intent = new Intent( Intent.ACTION_VIEW ).setData( Uri.parse( url ) );
		startActivity( intent );
		
	}
	
	private void openDictionary( String languageCode, String text ) {
		
		String dictionary = null;
		if( languageCode.equals( "es" ) ) dictionary = "rae.es/";
		else if( languageCode.equals( "en" ) ) dictionary = "dictionary.reference.com/browse/";
		else {
			Toast.makeText( getApplicationContext(), "Idioma no soportado: " + languageCode, Toast.LENGTH_SHORT ).show();
			return;
		}
			
		String url = "http://" + dictionary + text;
		Intent intent = new Intent( Intent.ACTION_VIEW ).setData( Uri.parse( url ) );
		startActivity( intent );
		
	}
	
}