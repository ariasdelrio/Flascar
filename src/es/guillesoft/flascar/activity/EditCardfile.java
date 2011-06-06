package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.ui.ConfirmDialog;
import es.guillesoft.flascar.ui.ConfirmDialogListener;

public class EditCardfile extends FlascarActivity implements ConfirmDialogListener {
	
	/// long cardfileID (nullable)
	public static final String IN_CARDFILE = "cardfile";
	/// long cardfileID
	public static final String OUT_CARDFILE = "cardfile";
	
	private Cardfile cardfile;
	private EditText txtName;
	private EditText txtSideA;
	private EditText txtSideB;
	
	private int dlgConfirm;
	
	public EditCardfile() {
		super( "EditCardfile", R.layout.edit_cardfile );
	}
	
	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
		
		dlgConfirm = registerDialog( new ConfirmDialog( getString( R.string.dlg_confirm_cancel_unsaved ), this ) );
		
		Bundle extras = getIntent().getExtras();
		if( extras != null ) {
			
			try {
				
				cardfile = Core.getInstance().getDataModel( getContentResolver() ).getCardfile( extras.getLong( IN_CARDFILE ) );
				
			}
			catch( Exception e ) { 
			
				Log.e( getClass().getSimpleName(), "Exception retrieving card: " + e );
				
			}

		}
		
		txtName = (EditText) findViewById( R.id.edtName );
		txtSideA = (EditText) findViewById( R.id.edtSideA );
		txtSideB = (EditText) findViewById( R.id.edtSideB );
		
	}
	
	@Override
	public void tearDown() {

	}
		
	@Override
	public void refresh() {
		
		if( cardfile == null ) {
			
			txtName.setText( "" );
			txtSideA.setText( "" );
			txtSideB.setText( "" );
			
		}
		else {
		
			txtName.setText( cardfile.getName() );
			txtSideA.setText( cardfile.getSideA() );
			txtSideB.setText( cardfile.getSideB() );
							
		}
		
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent) {
		
	}
	
	@Override
	public void back() {
		
		showDialog( dlgConfirm );
		
	}

	/* Layout events */

	public void save( View view ) {
	
		String name = txtName.getText().toString();
		String sideA = txtSideA.getText().toString();
		String sideB = txtSideB.getText().toString();
		
		if( cardfile == null ) {

			try {
			
				cardfile = Core.getInstance().getDataModel( getContentResolver() ).addCardfile( name, sideA, sideB );
				
			}
			catch( Exception e ) { 
				
				Log.e( getClass().getSimpleName(), "Exception adding card: " + e );
				
			}
			
		}
		else {
			
			cardfile.setName( name );
			cardfile.setSideA( sideA );
			cardfile.setSideB( sideB );
			cardfile.update();
			
		}
        
		Bundle extras = new Bundle();
		extras.putLong( OUT_CARDFILE, cardfile.getID() );
        returnOK( extras );
		
	}
	
	public void cancel( View view ) {
		
		showDialog( dlgConfirm );
		
	}
	
	/* Dialog events */
	
	@Override
	public void dlgConfirm( boolean confirm ) {
		
		if( confirm ) returnCancel();
		
	}
	
}