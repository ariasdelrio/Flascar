package es.guillesoft.flascar.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.R.id;
import es.guillesoft.flascar.R.layout;
import es.guillesoft.flascar.R.menu;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.ui.CardfileViewAdapter;

public class SelectCardfile extends FlascarActivity implements IFlascarActivity, OnItemClickListener {
	
//	public static final String EXTRA_CARDFILE = "cardfile";
	
	private Main dm;
	private ListView lview;
	
	public SelectCardfile() {
		super( "SelectCardfile", R.layout.select_cardfile );
	}
	
	@Override
	public void setUp() {
	
		ContentResolver cr = getContentResolver();
		if( cr == null ) Log.e( "CR", "ES NULL!" );
		else Log.e( "CR", "NO ES NULL!" );
		dm = Core.getInstance().getDataModel( getContentResolver() );
		
		lview = (ListView)findViewById( R.id.select_cardfile_lview );
		lview.setOnItemClickListener( this ); 
		refresh();
    
	}
	
	@Override
	public void tearDown() {
		
        // ?

	}
	
	@Override
	public void refresh() {
		
		lview.setAdapter( new CardfileViewAdapter( lview.getContext() ) );
		
	}
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent) {
		
		// ?
		
	}
	
	/* User events */
	
	@Override
	public void onItemClick( AdapterView<?> adapter, View view, int position, long id ) {

		Cardfile cardfile = dm.getCardfile( (int)id );
		Log.d( getClass().getSimpleName(), "cardfile selected: " + cardfile.getID() );
		dm.setCurrentCardfile( cardfile );
		/*
        Intent intent = new Intent();
        intent.putExtra( EXTRA_CARDFILE, Cardfile.toCardfile( c ) );
        */ 
        setResult( RESULT_OK, null );

        finish();
      
    }
	
	@Override
	public void onBackPressed()
    {
		
		Log.d( getClass().getSimpleName(), "BACK" );
        setResult( RESULT_CANCELED );
        finish();
        
    }
	
	
	
	
	
	public void addCardfile() {
		/* PA Q COMPILE
		Cardfile.create( getContentResolver(), getString( R.string.new_cardfile ), "A", "B" );
		reload();
		*/
		
	}
	

	
	   
	
	
	@Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    	
    	switch( item.getItemId() ) {
    	
    		case R.id.select_cardfile_add:
    			addCardfile();
    			return true;
            
    		case R.id.select_cardfile_add_test:
    			
    			ContentResolver cr = getContentResolver();
    			
    			File path = Environment.getExternalStorageDirectory();
        		File file = new File( path, "test.deck" );
        		try {
        			
        			BufferedReader in = new BufferedReader( new FileReader( file ) ); 
        			
        		    String line;
        			/* PA Q COMPILE
        		    Cardfile cardfile = Cardfile.create( cr, "test", "test-a", "test-b" );
        		    if( cardfile == null ) {
        		    	Log.e( "test", "Error creating cardfile!" );
        		    }
        		    else
        		    while( ( line = in.readLine() ) != null) {
        		    	Log.d( "READ", line );
        		    	String data[] = line.split("\\$");
        		    	Log.d( "READ", "DATA 0 = " + data[0] );
        		    	Log.d( "READ", "DATA 1 = " + data[1] );
        		    	cardfile.addCard( cr, data[0], data[1] );
        		    }
*/
        		    in.close();

        		} catch ( Exception e ) {
        			e.printStackTrace();
        			Log.e( "READ", e.getMessage() );
        		}
    	
        		
        		refresh();
    			return true;
            
    		default:
        	   Log.e( "SelectCardfile", "Menu option not recognized: " + item.getItemId());
        	   break;
            
    	}
    	    	
    	return super.onOptionsItemSelected(item);
        
    }
	
	/* Lifecycle */
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onPrepareOptionsMenu( menu );
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate( R.menu.select_cardfile, menu );
	    return true;
	    	
	}



	 
}