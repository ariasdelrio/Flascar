package es.guillesoft.flascar.ui;

import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;

public class SelectCardfileViewBinder implements ViewBinder {

    public boolean setViewValue( View view, Cursor cursor, int columnIndex ) { 
    
		int iName = cursor.getColumnIndex( Cardfile.NAME );
		int iSideA = cursor.getColumnIndex( Cardfile.SIDE_A );
		int iSideB = cursor.getColumnIndex( Cardfile.SIDE_B );
		
		if ( columnIndex == iSideB ) return true;
			
		if ( columnIndex ==  iName ) {
		
			TextView txtName = ( TextView ) view.findViewById( R.id.select_cardfile_row_name );
			txtName.setText( cursor.getString( iName ) );
		
			return true;
			
		}
		else if ( columnIndex ==  iSideA ) {
		
			TextView txtInfo = (TextView) view.findViewById( R.id.select_cardfile_row_info );
			txtInfo.setText( cursor.getString( iSideA ) + " - " + cursor.getString( iSideB ) );
			
			return true;
			
		}
		
		return false;
		
	}
  
}
