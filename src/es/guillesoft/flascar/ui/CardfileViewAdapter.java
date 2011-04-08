package es.guillesoft.flascar.ui;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardfileViewAdapter extends BaseAdapter {

	private Context context;
	private Main dm;
	
	public CardfileViewAdapter( Context context ) {
		this.context = context;
		dm = Core.getInstance().getDataModel( context.getContentResolver() );
	}
	
	@Override
	public int getCount() {
		return dm.getCardfileCount();
	}

	@Override
	public Object getItem( int position ) {
		return dm.getCardfile( position );
	}

	@Override
	public long getItemId( int position ) {
		return position;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		
		Cardfile cardfile = dm.getCardfile( position );
		
		View view = convertView;
		if( view == null ) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			view = inflater.inflate( R.layout.select_cardfile_row, null );
		}
		
		TextView txtName = ( TextView ) view.findViewById( R.id.select_cardfile_row_name );
		txtName.setText( cardfile.getName() );
		
		TextView txtInfo = (TextView) view.findViewById( R.id.select_cardfile_row_info );
		txtInfo.setText( cardfile.getSideA() + " - " + cardfile.getSideB() 
				+ " (" + cardfile.getCardCount() + " tarjetas )" );
			
		return view;

	}

}
