package es.guillesoft.flascar.ui;

import java.util.List;

import es.guillesoft.flascar.dm.Cardfile.ReviewInfo;
import es.guillesoft.flascar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewInfoViewAdapter extends BaseAdapter {

	private Context context;
	private List<ReviewInfo> infos;
	
	public ReviewInfoViewAdapter( Context context, List<ReviewInfo> infos ) {

		this.context = context;
		this.infos = infos;
		
	}
	
	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem( int position ) {
		return infos.get( position );
	}

	@Override
	public long getItemId( int position ) {
		return position + 1;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		
		ReviewInfo info = infos.get( position );
		
		View view = convertView;
		if( view == null ) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			view = inflater.inflate( R.layout.learn_row, null );
		}
		
		ImageView imgBox = ( ImageView ) view.findViewById( R.id.imgBox );
		imgBox.setImageResource( info.pending > 0  ? R.drawable.box_nok : R.drawable.box_ok );
		
		TextView txtName = ( TextView ) view.findViewById( R.id.txtName );
		txtName.setText( info.name );
		
		TextView txtStats = ( TextView ) view.findViewById( R.id.txtStats );
		txtStats.setText( info.pending + " / " + info.total );
		
		return view;

	}

}
