package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.ui.ConfirmDialog;
import es.guillesoft.flascar.ui.ConfirmDialogListener;

public class EditCard extends FlascarActivity implements ConfirmDialogListener {
		
	/// long cardID (nullable)
	public static final String IN_CARD = "card";
	
	private Card card;
	
	private TextView txtSideATitle;
	private TextView txtSideBTitle;
	private EditText edtSideA;
	private EditText edtSideB;
	
	private int dlgConfirm;
	
	public EditCard() {
		super( "EditCard", R.layout.edit_card );
	}

	/* FlascarActivity methods */
	
	@Override
	public void setUp() {
		
		dlgConfirm = registerDialog( new ConfirmDialog( getString( R.string.dlg_confirm_cancel_unsaved ), this ) );
		
		Bundle extras = getIntent().getExtras();
		if( extras != null ) {
			
			try {
				
				card = Core.getInstance().getDataModel( getContentResolver() ).getCurrentCardfile()
					.getCard( extras.getLong( IN_CARD ) );
				
			}
			catch( Exception e ) { 
			
				Log.e( getClass().getSimpleName(), "Exception retrieving card: " + e );
				
			}

		}
		
		txtSideATitle = (TextView) findViewById( R.id.txtSideATitle );
		txtSideBTitle = (TextView) findViewById( R.id.txtSideBTitle );
		edtSideA = (EditText) findViewById( R.id.edtSideA );
		edtSideB = (EditText) findViewById( R.id.edtSideB );
				
	}
	
	@Override
	public void tearDown() {

	}
		
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
		
	}

	@Override
	public void back() {
	
		showDialog( dlgConfirm );
		
	}
    	
	@Override
	public void refresh() {
		
		Cardfile cardfile = Core.getInstance().getDataModel( getContentResolver() ).getCurrentCardfile();
		txtSideATitle.setText( cardfile.getSideA() );
		txtSideBTitle.setText( cardfile.getSideB() );
		
		if( card == null ) {
			
			edtSideA.setText( "" );
			edtSideB.setText( "" );
			
		}
		else {
			
			
			edtSideA.setText( card.getSideA() );
			edtSideB.setText( card.getSideB() );
						
		}
		
	}
	
	public void save( View view ) {
		
		String sideA = edtSideA.getText().toString();
		String sideB = edtSideB.getText().toString();
		
		if( card == null ) {

			try {
			
				Core.getInstance().getDataModel( getContentResolver() ).getCurrentCardfile().addCard( sideA, sideB );
				returnOK();
				
			}
			catch( Exception e ) {
				
				Log.e( getClass().getSimpleName(), "Exception adding card: " + e );
				returnCancel();
				
			}
			
			
		}
		else {
			
			if( card.getCardfile().updateCard( card, sideA, sideB ) ) returnOK();
			else returnCancel();
		
		}
		
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