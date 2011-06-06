package es.guillesoft.flascar.ui;

import java.util.List;

import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardfileViewAdapter extends BaseAdapter {

	private Context context;
	private List<Cardfile> cardfiles;
	
	public CardfileViewAdapter( Context context, List<Cardfile> cardfiles ) {
		this.context = context;
		this.cardfiles = cardfiles;
	}
	
	@Override
	public int getCount() {
		return cardfiles.size();
	}

	@Override
	public Object getItem( int position ) {
		return cardfiles.get( position );
	}

	@Override
	public long getItemId( int position ) {
		return cardfiles.get( position ).getID();
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		
		Cardfile cardfile = cardfiles.get( position );
		
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
