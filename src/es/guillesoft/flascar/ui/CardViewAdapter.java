package es.guillesoft.flascar.ui;

import java.util.List;

import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.R;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardViewAdapter extends BaseAdapter {

	private Context context;
	private List<Card> cards;
	
	public CardViewAdapter( Context context, List<Card> cards ) {

		this.context = context;
		this.cards = cards;
		
	}
	
	@Override
	public int getCount() {
		return cards.size();
	}

	@Override
	public Object getItem( int position ) {
		return cards.get( position );
	}

	@Override
	public long getItemId( int position ) {
		return cards.get( position ).getID();
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		
		Card card = cards.get( position );
		
		View view = convertView;
		if( view == null ) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			view = inflater.inflate( R.layout.show_cards_row, null );
		}
		
		TextView txtSideA = ( TextView ) view.findViewById( R.id.top_left );
		txtSideA.setText( card.getSideA() );
		
		TextView txtSideB = ( TextView ) view.findViewById( R.id.bottom_left );
		txtSideB.setText( card.getSideB() );
		
		TextView txtBoxA = ( TextView ) view.findViewById( R.id.top_right );
		txtBoxA.setText( Long.toString( card.getBoxA() ) );
		
		TextView txtBoxB = ( TextView ) view.findViewById( R.id.bottom_right );
		txtBoxB.setText( Long.toString( card.getBoxB() ) );
			
		return view;

	}

}
