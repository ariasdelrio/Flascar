package es.guillesoft.flascar.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;

public class SearchDialog implements FlascarDialog {
	
	private List<String> searchFields;
	private SearchDialogListener listener;
	private EditText etPrompt; 
	private Spinner spSearchField;

	public SearchDialog( List<String> searchFields, SearchDialogListener listener ) {
		
		this.searchFields = searchFields;
		this.listener = listener;
		
	}
	
	public Dialog build( FlascarActivity activity ) {
		
		AlertDialog.Builder builder;

		LayoutInflater inflater = (LayoutInflater)activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		
		View layout = inflater.inflate( R.layout.dlg_search,
		                               (ViewGroup)activity.findViewById( R.id.layout_root ));

		etPrompt = (EditText) layout.findViewById( R.id.prompt );
	    
		// Spinner
		spSearchField = (Spinner) layout.findViewById( R.id.field );
	    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>( activity, android.R.layout.simple_spinner_item ); 
	    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	    for( String searchField : searchFields ) adapter.add( searchField );
	    spSearchField.setAdapter(adapter);
	    
	    builder = new AlertDialog.Builder( activity );
		builder.setView( layout );
		
		builder.setPositiveButton( R.string.dlg_search_positive, new DialogInterface.OnClickListener() {
			
			public void onClick( DialogInterface dialog, int id ) {
				
				listener.dlgSearch( spSearchField.getSelectedItemPosition(), etPrompt.getText().toString() );
		
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
	