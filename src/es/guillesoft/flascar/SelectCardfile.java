package es.guillesoft.flascar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.ui.SelectCardfileViewBinder;

public class SelectCardfile extends ListActivity {
	
	public static final String EXTRA_CARDFILE = "cardfile";
	
	private Cursor cursor;

	public void setUp() {

		setContentView( R.layout.select_cardfile );
		reload();
    
	}
	
	public void addCardfile() {
		
		Cardfile.create( getContentResolver(), getString( R.string.new_cardfile ), "A", "B" );
		reload();
		
	}
	
	/* Reload view */

	private void reload() {

		cursor = Cardfile.getAll( getContentResolver() );
        startManagingCursor( cursor );
                
        String[] from = new String[] { Cardfile.NAME, Cardfile.SIDE_A, Cardfile.SIDE_B };
    	int[] to = new int[] { R.id.select_cardfile_row_name, R.id.select_cardfile_row_info, R.id.select_cardfile_row_info };
    	
    	SimpleCursorAdapter adapter =
            new SimpleCursorAdapter( this, R.layout.select_cardfile_row, cursor, from, to );
        
    	adapter.setViewBinder( new SelectCardfileViewBinder() );
    	
        setListAdapter( adapter );

    }
	
	/* Events */
	
	@Override
    protected void onListItemClick( ListView l, View v, int position, long id ) {
        super.onListItemClick(l, v, position, id);

        Cursor c = cursor;
        c.moveToPosition( position );
        long cardfileID = cursor.getLong( cursor.getColumnIndexOrThrow( Cardfile.ID ) );
        
        Log.d( "SelectCardfile", "<- selected = " + cardfileID );
        Intent intent = new Intent();
        intent.putExtra( EXTRA_CARDFILE, Cardfile.toCardfile( c ) ); 
        setResult( RESULT_OK, intent );
        
        finish();
      
    }
	   
	@Override
	public void onBackPressed()
    {
		Log.d( "SelectCardfile", "BACK" );
        setResult( RESULT_CANCELED );
        finish();
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

        		    in.close();

        		} catch ( Exception e ) {
        			e.printStackTrace();
        			Log.e( "READ", e.getMessage() );
        		}
    	
        		reload();
    			return true;
            
    		default:
        	   Log.e( "SelectCardfile", "Menu option not recognized: " + item.getItemId());
        	   break;
            
    	}
    	    	
    	return super.onOptionsItemSelected(item);
        
    }
	
	/* Lifecycle */
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	Log.d( "SelectCardfile", "CREATE" );
    	setUp();
    }
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onPrepareOptionsMenu( menu );
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate( R.menu.select_cardfile, menu );
	    return true;
	    	
	}
	 
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.d( "SelectCardfile", "START" );
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d( "SelectCardfile", "STOP" );
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d( "SelectCardfile", "DESTROY" );
    }
    
}