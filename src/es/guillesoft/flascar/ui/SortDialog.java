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
import android.widget.Spinner;
import android.widget.ToggleButton;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.activity.FlascarActivity;

public class SortDialog implements FlascarDialog {
	
	private List<String> sortTypes;
	private SortDialogListener listener;
	
	private Spinner spSortType;
	private ToggleButton tbSortOrder;
	
	public SortDialog( List<String> sortTypes, SortDialogListener listener ) {
	
		this.sortTypes = sortTypes;
		this.listener = listener;
		
	}
	
	public Dialog build( FlascarActivity activity ) {

		// Root view
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.dlg_sort,
		                               (ViewGroup)activity.findViewById( R.id.layout_root ));

		// Spinner
		spSortType = (Spinner) layout.findViewById( R.id.type );
	    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>( activity, android.R.layout.simple_spinner_item ); 
	    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	    for( String sortType : sortTypes ) adapter.add( sortType );
	    spSortType.setAdapter(adapter);

	    // ToggleButton
	    tbSortOrder = (ToggleButton) layout.findViewById( R.id.order );
	    
	    // Dialog
	    AlertDialog.Builder builder;
	    builder = new AlertDialog.Builder( activity );
		builder.setView( layout );
		
		// Positive button
		builder.setPositiveButton( R.string.dlg_sort_positive, new DialogInterface.OnClickListener() {
			
			public void onClick( DialogInterface dialog, int id ) {
				
				listener.dlgSort( spSortType.getSelectedItemPosition(), tbSortOrder.isChecked() );
		
	        }
			
	    });
		
		// Negative button
		builder.setNegativeButton( R.string.dlg_sort_negative, new DialogInterface.OnClickListener() {
		
			public void onClick( DialogInterface dialog, int id ) {
				
				dialog.cancel();
				
			}
			
		});
		
		return builder.create();
	}
	
}
	