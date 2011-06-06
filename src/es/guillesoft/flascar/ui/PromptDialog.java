package es.guillesoft.flascar.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;

public class PromptDialog implements FlascarDialog {
	
	private String title;
	private PromptDialogListener listener;
	
	private EditText etPrompt; 
	
	public PromptDialog( String title, PromptDialogListener listener ) {
	
		this.title = title;
		this.listener = listener;
		
	}
	
	public Dialog build( FlascarActivity activity ) {
		
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		
		View layout = inflater.inflate( R.layout.dlg_prompt,
		                               (ViewGroup)activity.findViewById( R.id.layout_root ));
		
		TextView txtTitle = (TextView) layout.findViewById( R.id.title );
		txtTitle.setText( title );
		
		etPrompt = (EditText) layout.findViewById( R.id.prompt );
		
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setView( layout );
		
		builder.setPositiveButton( R.string.dlg_positive, new DialogInterface.OnClickListener() {
	    
			public void onClick( DialogInterface dialog, int id ) {

				listener.dlgPrompt( etPrompt.getText().toString() );
				
			}
	    
		});
		
		builder.setNegativeButton( R.string.dlg_negative, new DialogInterface.OnClickListener() {
			
			public void onClick( DialogInterface dialog, int id ) {
				
				listener.dlgPrompt( null );
				
			}
			
		});
		
		return builder.create();

	}
	
}
	