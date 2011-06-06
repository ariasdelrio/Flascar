package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.ui.CardfileViewAdapter;

public class SelectCardfile extends FlascarActivity implements OnItemClickListener {
	
	private Main dm;
	private ListView lview;
	private TextView txtEmpty;
	
	public SelectCardfile() {
		super( "SelectCardfile", R.layout.select_cardfile );
	}
	
	@Override
	public void setUp() {
	
		registerActivity( EditCardfile.class );
				
		dm = Core.getInstance().getDataModel( getContentResolver() );
		lview = (ListView)findViewById( R.id.list );
		lview.setOnItemClickListener( this ); 
		lview.setAdapter( new CardfileViewAdapter( lview.getContext(), dm.getCardfiles() ) );
		txtEmpty = (TextView)findViewById( R.id.empty );

	}
	
	@Override
	public void tearDown() {

	}
	
	@Override
	public void refresh() {
		
		lview.invalidateViews();
		
		if( dm.getCardfileCount() == 0 ) {
			
			lview.setVisibility( View.GONE );
			txtEmpty.setVisibility( View.VISIBLE );
			
		}
		else {
		
			lview.setVisibility( View.VISIBLE );
			txtEmpty.setVisibility( View.GONE );
			
		}
		
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
		if( clazz.equals( EditCardfile.class ) ) if ( resultCode == RESULT_OK ) { 
			
			Cardfile newCardfile = null;
			
			Bundle extras = intent.getExtras();
			if( extras != null ) {
				
				try {
					
					newCardfile = dm.getCardfile( extras.getLong( EditCardfile.OUT_CARDFILE ) );
					Log.d( getClass().getSimpleName(), "cardfile added and selected: " + newCardfile.getID() );
					dm.setCurrentCardfile( newCardfile );
					returnOK();
					
				}
				catch( Exception e ) { 
					
					Log.e( getClass().getSimpleName(), "Exception retrieving card: " + e );
					returnCancel();
					
				}
				
			}
			
		}
		
	}

	@Override
	public void back() {
		
		returnCancel();
        
	}
	
	/* List events */
	
	@Override
	public void onItemClick( AdapterView<?> adapter, View view, int position, long id ) {

		try {
		
			Cardfile cardfile = dm.getCardfile( id );
			Log.d( getClass().getSimpleName(), "cardfile selected: " + cardfile.getID() );
			dm.setCurrentCardfile( cardfile );
			returnOK();
			
		}
		catch( Exception e ) {
			
			Log.e( getClass().getSimpleName(), "Exception retrieving card: " + e );
			returnCancel();
			
		}
		
    }
	
	/* Layout events */
	
	public void add( View view ) {

		Log.d( getClass().getSimpleName(), "addCardfile" );
		startActivity( EditCardfile.class );
		
	}
	
}