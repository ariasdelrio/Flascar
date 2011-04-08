package es.guillesoft.flascar.ui;

import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;

public class BoxViewBinder implements ViewBinder {

    public boolean setViewValue( View view, Cursor cursor, int columnIndex ) { 
		/* PA Q COMPILE
		int iID = cursor.getColumnIndex( Cardfile.BOX_ID );
		int iAll = cursor.getColumnIndex( Cardfile.BOX_TOTAL );
		int iDirty = cursor.getColumnIndex( Cardfile.BOX_DIRTY );
		
		if ( columnIndex == iDirty ) return true;
			
		if ( columnIndex ==  iID ) {
		
			int box = cursor.getInt( iID );
			
			TextView txtName = ( TextView ) view.findViewById( R.id.learn_row_name );
			txtName.setText( "Caja " + box );
		
			return true;
			
		}
		else if ( columnIndex ==  iAll ) {
		
			int allcards = cursor.getInt( iAll );
			int dirtycards = cursor.isNull( iDirty ) ? 0 : cursor.getInt( iDirty );
			
			TextView tvCount = (TextView)view.findViewById( R.id.learn_row_count );
			tvCount.setText( "Tarjetas: " + dirtycards + " / " + allcards );
			
			return true;
			
		}
		
		return false;
		*/
    	return false;
	}
  
}
