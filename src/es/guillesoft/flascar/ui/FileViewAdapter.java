package es.guillesoft.flascar.ui;

import java.io.File;

import es.guillesoft.flascar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileViewAdapter extends BaseAdapter {

	private Context context;
	private File [] files;
	
	public FileViewAdapter( Context context, File [] files ) {
		this.context = context;
		this.files = files;
	}
	
	@Override
	public int getCount() {
		return files.length;
	}

	@Override
	public Object getItem( int position ) {
		return files[position];
	}

	@Override
	public long getItemId( int position ) {
		return position;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		
		File file = files[position];
		
		View view = convertView;
		if( view == null ) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			view = inflater.inflate( R.layout.select_file_row, null );
		}
		
		ImageView imgIcon = (ImageView) view.findViewById( R.id.icon );
		imgIcon.setImageResource( file.isDirectory() ? R.drawable.folder : R.drawable.deck );
		
		TextView txtName = ( TextView ) view.findViewById( R.id.text );
		txtName.setText( file.getName() );
		
		return view;

	}

}
