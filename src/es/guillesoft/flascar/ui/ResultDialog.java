package es.guillesoft.flascar.ui;

import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultDialog implements FlascarDialog {
    
	private String title;
	private boolean cancellable;
	private int right;
	private int wrong;
	private int pass;
	private ResultDialogListener listener;

	public ResultDialog( String title, boolean cancellable, int right, int wrong, int pass, ResultDialogListener listener ) {
		
		this.title = title;
		this.right = right;
		this.wrong = wrong;
		this.pass = pass;
		this.listener = listener;
		this.cancellable = cancellable;
		
	}
	
	public Dialog build( FlascarActivity activity ) {
		
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		
		View layout = inflater.inflate( R.layout.dlg_result,
		                               (ViewGroup)activity.findViewById( R.id.layout_root ));
		
		TextView txtTitle = (TextView) layout.findViewById( R.id.txtHeader );
		txtTitle.setText( title );
		
		TextView txtRight = (TextView) layout.findViewById( R.id.txtRight );
		txtRight.setText( "Correctas: " + right );
		
		TextView txtWrong = (TextView) layout.findViewById( R.id.txtWrong );
		txtWrong.setText( "Incorrectas: " + wrong );
		
		TextView txtPass = (TextView) layout.findViewById( R.id.txtPass );
		txtPass.setText( "Sin responder: " + pass );
		
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setView( layout );
		
		builder.setPositiveButton( R.string.dlg_positive, new DialogInterface.OnClickListener() {
	    
			public void onClick( DialogInterface dialog, int id ) {

				listener.dlgResult( false );
				
			}
	    
		});
		
		if( cancellable )
		
			builder.setNegativeButton( R.string.dlg_negative, new DialogInterface.OnClickListener() {
			
				public void onClick( DialogInterface dialog, int id ) {
				
					listener.dlgResult( true );
				
				}
			
			});
		
		return builder.create();

	}
	    	
}