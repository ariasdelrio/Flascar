package es.guillesoft.flascar.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;

public class SearchDialog implements FlascarDialog {
	
	private SearchDialogListener listener;
	private EditText etPrompt; 

	public SearchDialog( SearchDialogListener listener ) {
		
		this.listener = listener;
		
	}
	
	public Dialog build( FlascarActivity activity ) {
		
		AlertDialog.Builder builder;

		LayoutInflater inflater = (LayoutInflater)activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		
		View layout = inflater.inflate( R.layout.dlg_search,
		                               (ViewGroup)activity.findViewById( R.id.layout_root ));

		etPrompt = (EditText) layout.findViewById( R.id.prompt );
	    
	    builder = new AlertDialog.Builder( activity );
		builder.setView( layout );
		
		builder.setPositiveButton( R.string.dlg_search_positive, new DialogInterface.OnClickListener() {
			
			public void onClick( DialogInterface dialog, int id ) {
				
				listener.dlgSearch( etPrompt.getText().toString() );
		
	        }
			
	    });
		
		builder.setNegativeButton( R.string.dlg_sort_negative, new DialogInterface.OnClickListener() {
		
			public void onClick( DialogInterface dialog, int id ) {
				
				dialog.cancel();
				
			}
			
		});
		
		return builder.create();
	}
	
}
	