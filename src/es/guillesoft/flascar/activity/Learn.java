package es.guillesoft.flascar.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Cardfile.ReviewInfo;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.dm.Cardfile.Sense;
import es.guillesoft.flascar.ui.ReviewInfoViewAdapter;

public class Learn extends FlascarActivity implements OnItemClickListener {
	
	private Main dm;
	private Sense sense;
	
	private ListView lview;
	private TextView txtSense;
	private ArrayList<ReviewInfo> reviewInfo;
	
	public Learn() {
		
		super( "Learn", R.layout.learn );
		reviewInfo = null;
		
	}

	@Override
	public void setUp() {
		
		registerActivity( Review.class );
		
        dm = Core.getInstance().getDataModel( getContentResolver() );
        sense = Sense.AB;

        lview = (ListView)findViewById( R.id.list );
		lview.setOnItemClickListener( this ); 
		
		txtSense = (TextView) findViewById( R.id.txtSense );
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Override
	public void refresh() {
		
		Cardfile currentCardfile = dm.getCurrentCardfile();
		
		reviewInfo = currentCardfile.getReviewInfo( sense );
		lview.setAdapter( new ReviewInfoViewAdapter( lview.getContext(), reviewInfo ) );
		
		if( sense == Sense.AB ) txtSense.setText( currentCardfile.getSideA() + " > " + currentCardfile.getSideB() );
		else txtSense.setText( currentCardfile.getSideB() + " > " + currentCardfile.getSideA() );
		
    }
	
	/* Layout events */

	public void changeSense( View view ) {
		
		Log.d( getClass().getSimpleName(), "changeSense" );
		
		sense = sense == Sense.AB ? Sense.BA : Sense.AB;
		refresh();
		
	}
	
	/* List events */

	@Override
	public void onItemClick( AdapterView<?> adapter, View view, int position, long id ) {

		Log.d( getClass().getSimpleName(), "review" );

		if( reviewInfo != null && reviewInfo.get( position ).pending == 0 ) {
			
			Toast.makeText( getApplicationContext(), "No hay tarjetas para revisar", Toast.LENGTH_SHORT ).show();
			return;
			
		}
		
		Bundle extras = new Bundle();
		extras.putInt( Review.IN_BOX, (int)id );
		extras.putBoolean( Review.IN_SENSE, sense == Sense.AB );
		startActivity( Review.class, extras );
		        
    }
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
		refresh();
		
	}

	@Override
	public void back() {
		
		returnOK();
		
	}
    
}