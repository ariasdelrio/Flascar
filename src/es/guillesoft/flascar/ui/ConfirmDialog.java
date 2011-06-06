package es.guillesoft.flascar.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;

public class ConfirmDialog implements FlascarDialog {
	
	private String prompt;
	private ConfirmDialogListener listener;
	
	public ConfirmDialog( String prompt, ConfirmDialogListener listener ) {
	
		this.prompt = prompt;
		this.listener = listener;
		
	}
	
	public Dialog build( FlascarActivity activity ) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setMessage( prompt );
		
		builder.setPositiveButton( R.string.dlg_positive, new DialogInterface.OnClickListener() {
	    
			public void onClick( DialogInterface dialog, int id ) {

				listener.dlgConfirm( true );
				
			}
	    
		});
		
		builder.setNegativeButton( R.string.dlg_negative, new DialogInterface.OnClickListener() {
			
			public void onClick( DialogInterface dialog, int id ) {
				
				listener.dlgConfirm( false );
				
			}
			
		});
		
		return builder.create();

	}
	
}
	