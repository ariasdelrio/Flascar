package es.guillesoft.flascar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import es.guillesoft.flascar.db.FlashcardProvider.Sense;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;

public class Welcome extends Activity {
	
	private static final int ACTIVITY_SELECT_CARDFILE = 0;
	private static final int ACTIVITY_SHOW_CARDFILE   = 1;
	private static final int ACTIVITY_LEARN           = 2;
	
	private Cardfile cardfile;
	private Sense sense;
	
	public void setUp() {
		
        setContentView( R.layout.welcome );
        cardfile = Cardfile.getCurrent( getContentResolver() );
        if( cardfile == null ) Log.d("ERROR", "NULL");
        sense = Sense.AB;
        reload();

	}
	
	public void changeCardfile( View view ) {
		Log.d( "Welcome", "changeCardfile" );

        Log.d( "Welcome", "-> ACTIVITY_SELECT_CARDFILE" );
		Intent intent = new Intent( this, SelectCardfile.class );
        startActivityForResult( intent, ACTIVITY_SELECT_CARDFILE );
		
	}
	
	public void changeSense( View view ) {
		Log.d( "Welcome", "changeSense" );

		sense = sense == Sense.AB ? Sense.BA : Sense.AB;
		reload();
		
	}

	public void showCardfile( View view ) {
		Log.d( "Welcome", "showCardfile" );
		
		if( cardfile == null ) return; // button shouldn't be enabled
		
		Log.d( "Welcome", "-> ACTIVITY_SHOW_CARDFILE" );
		Intent intent = new Intent( this, ShowCardfile.class );
		intent.putExtra( ShowCardfile.EXTRA_CARDFILE, cardfile );
		startActivityForResult( intent, ACTIVITY_SHOW_CARDFILE );
		
	}

	public void learn( View view ) {
		Log.d( "Welcome", "learn" );
		
		if( cardfile == null ) return; // button shouldn't be enabled
		
		Log.d( "Welcome", "-> ACTIVITY_LEARN" );
		Intent intent = new Intent( this, Learn.class );
		intent.putExtra( Learn.EXTRA_CARDFILE, cardfile );
		intent.putExtra( Learn.EXTRA_SENSE_AB, sense == Sense.AB );
		startActivityForResult( intent, ACTIVITY_LEARN );
		
	}
	
	public void importCardfile( View view ) {
		Log.d( "Welcome", "importCardfile" );

		Cardfile newCardfile = null;
		
		File path = Environment.getExternalStorageDirectory();
		File file = new File( path, "import.deck" );
		try {
			
			BufferedReader in = new BufferedReader( new FileReader( file ) ); 
			
			ContentResolver cr = getContentResolver();
			
		    String line;
		    
		    if( ( line = in.readLine() ) != null) {
		    	Log.d( "READ", line );
		    	String data[] = line.split("\\$");
		    	Log.d( "READ", "DATA 0 = " + data[0] );
		    	Log.d( "READ", "DATA 1 = " + data[1] );
		    	Log.d( "READ", "DATA 2 = " + data[2] );
		    	newCardfile = Cardfile.create( cr, data[0], data[1], data[2] );
		    }
		    
		    if( newCardfile == null ) {
		    	Log.e( "test", "Error creating cardfile!" );
		    }
		    else
		    while( ( line = in.readLine() ) != null) {
		    	Log.d( "READ", line );
		    	String data[] = line.split("\\$");
		    	Log.d( "READ", "DATA 0 = " + data[0] );
		    	Log.d( "READ", "DATA 1 = " + data[1] );
		    	newCardfile.addCard( cr, data[0], data[1] );
		    }

		    in.close();

		} catch ( Exception e ) {
			e.printStackTrace();
			Log.e( "import", e.getMessage() );
			return;
		}
		
		Main.setCurrentCardfile( getContentResolver(), newCardfile.getID() );
		cardfile = newCardfile;
		reload();
		
	}

	public void exportCardfile( View view ) {
		Log.d( "Welcome", "exportCardfile" );

		if( cardfile == null ) return;
		
		File path = Environment.getExternalStorageDirectory();
		File file = new File( path, "export.deck" );
		try {
			
			BufferedWriter out = new BufferedWriter( new FileWriter( file ) ); 
			ContentResolver cr = getContentResolver();
			out.write( cardfile.getName() + "$" + cardfile.getSideA() + "$" + cardfile.getSideB() );
			out.newLine();
			
			Cursor cards = cardfile.getCards( cr );
			
			for( cards.moveToFirst(); ! cards.isAfterLast(); cards.moveToNext() ) {
				
				Card card = Card.toCard( cards, cardfile );
				out.write( card.getSideA() + "$" + card.getSideB() );
				out.newLine();
				
			}
			
		    out.close();

		} catch ( Exception e ) {
			e.printStackTrace();
			Log.e( "export", e.getMessage() );
			return;
		}
		
	}
	
	/* Reload view */
	
	private void reload() {
		
		TextView txtCardfileName = (TextView) findViewById( R.id.welcome_txtCardfileName );
		TextView txtSense = (TextView) findViewById( R.id.welcome_txtSense );
		
		String senseInfo;
		
		if( cardfile == null ) {
			
			txtCardfileName.setText( R.string.welcome_none );
			// DESACTIVAR BOTONES
			
		}
		else {
			
			txtCardfileName.setText( "Fichero: " + cardfile.getName() );
			if( sense == Sense.AB ) senseInfo = cardfile.getSideA() + " > " +  cardfile.getSideB();
			else senseInfo = cardfile.getSideB() + " > " +  cardfile.getSideA();
			txtSense.setText( senseInfo );
			
		}
		
	}
	
	/* Events */
	
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        switch( requestCode ) {
        
            case ACTIVITY_SELECT_CARDFILE:
            	
            	Log.d( "Welcome", "<- ACTIVITY_SELECT_CARDFILE" );
            	
            	if ( resultCode == RESULT_OK )
            	{
            		
            		Bundle extras = intent.getExtras();
            		cardfile = extras.getParcelable( SelectCardfile.EXTRA_CARDFILE );
            		Main.setCurrentCardfile( getContentResolver(), cardfile.getID() );
            		reload();
            		
            	}
            	else Log.d( "Welcome", "ACTIVITY_SELECT_CARDFILE cancelled" );
            	break;
            	
            case ACTIVITY_SHOW_CARDFILE:
            	
            	Log.d( "Welcome", "<- ACTIVITY_SHOW_CARDFILE" );
            	if ( resultCode == RESULT_OK )
            	{
            		
            		Bundle extras = intent.getExtras();
            		cardfile = extras.getParcelable( SelectCardfile.EXTRA_CARDFILE );
            		reload();
            		
            	}
            	else Log.e( "Welcome", "ACTIVITY_SHOW_CARDFILE returns with error" );
            	break;
            	
            case ACTIVITY_LEARN:
            	
            	Log.d( "Welcome", "<- ACTIVITY_LEARN" );
            	/*
            	if ( resultCode == RESULT_OK ) reload();
            	else Log.e( "Welcome", "ACTIVITY_LEARN returns with error" );
            	*/
            	break;
            	
        }
    	
    }
		
	/* Lifecycle */
	
	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Log.d( "Welcome", "CREATE" );
        setUp();
    }
	
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.d( "Welcome", "START" );
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d( "Welcome", "STOP" );
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d( "Welcome", "DESTROY" );
    }
    
}