package es.guillesoft.flascar.activity;

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

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.ShowCardfile;
import es.guillesoft.flascar.R.layout;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Main;

public class Welcome extends FlascarActivity implements IFlascarActivity {
	/*
	private static final int ACTIVITY_SELECT_CARDFILE = 0;
	private static final int ACTIVITY_SHOW_CARDFILE   = 1;
	private static final int ACTIVITY_LEARN           = 2;
	*/
	private Main dm;
	
	private Cardfile cardfile;
	/* PA Q COMPILE
	private Sense sense;
	*/
	
	public Welcome() {
		super( "Welcome", R.layout.welcome );
	}
	
	@Override
	public void setUp() {
		        
		registerActivity( SelectCardfile.class );
		dm = Core.getInstance().getDataModel( getContentResolver() );
		
	}
	
	@Override
	public void tearDown() {
		
        // ?

	}

	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
		if( clazz.equals( SelectCardfile.class ) ) onSelectCardfileResult( resultCode, intent );
			
			/*
		switch( requestCode ) {
        
        
        	
        case ACTIVITY_SHOW_CARDFILE:
        	
        	Log.d( "Welcome", "<- ACTIVITY_SHOW_CARDFILE" );
        	if ( resultCode == RESULT_OK )
        	{
        		
        		Bundle extras = intent.getExtras();
        		cardfile = extras.getParcelable( SelectCardfile.EXTRA_CARDFILE );
        		refresh();
        		
        	}
        	else Log.e( "Welcome", "ACTIVITY_SHOW_CARDFILE returns with error" );
        	break;
        	
        case ACTIVITY_LEARN:
        	
        	Log.d( "Welcome", "<- ACTIVITY_LEARN" );
        	
        	if ( resultCode == RESULT_OK ) reload();
        	else Log.e( "Welcome", "ACTIVITY_LEARN returns with error" );
        	
        	break;
        	
    }
	
}
		*/
	}

	@Override
	public void refresh() {
		
		TextView txtCardfileName = (TextView) findViewById( R.id.welcome_txtCardfileName );
		
		Cardfile currentCardfile = dm.getCurrentCardfile();
		
		if( currentCardfile == null ) {
			
			txtCardfileName.setText( R.string.welcome_none );
			
		}
		else {
		
			txtCardfileName.setText( getString ( R.string.cardfile ) + ": " + currentCardfile.getName() );
			
		}
		
		/* PA Q COMPILE
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
		*/
	}
	
	
	
	
	
	
	
	
	/* User events */
	
	public void changeCardfile( View view ) {

		startActivity( SelectCardfile.class );
		
	}
	
	public void changeSense( View view ) {
		Log.d( "Welcome", "changeSense" );
		/* PA Q COMPILE
		sense = sense == Sense.AB ? Sense.BA : Sense.AB;
		reload();
		*/
		
	}

	public void showCardfile( View view ) {
		Log.d( "Welcome", "showCardfile" );
		/*
		if( cardfile == null ) return; // button shouldn't be enabled
		
		Log.d( "Welcome", "-> ACTIVITY_SHOW_CARDFILE" );
		Intent intent = new Intent( this, ShowCardfile.class );
		intent.putExtra( ShowCardfile.EXTRA_CARDFILE, cardfile );
		startActivityForResult( intent, ACTIVITY_SHOW_CARDFILE );
		*/
	}

	public void learn( View view ) {
		Log.d( "Welcome", "learn" );
		
		if( cardfile == null ) return; // button shouldn't be enabled
		/* PA Q COMPILE
		Log.d( "Welcome", "-> ACTIVITY_LEARN" );
		Intent intent = new Intent( this, Learn.class );
		intent.putExtra( Learn.EXTRA_CARDFILE, cardfile );
		intent.putExtra( Learn.EXTRA_SENSE_AB, sense == Sense.AB );
		startActivityForResult( intent, ACTIVITY_LEARN );
		*/
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
			/* PA Q COMPILE
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
*/
		    in.close();

		} catch ( Exception e ) {
			e.printStackTrace();
			Log.e( "import", e.getMessage() );
			return;
		}
		/* PA Q COMPILE
		Main.setCurrentCardfile( getContentResolver(), newCardfile.getID() );
		cardfile = newCardfile;
		*/
		refresh();
		
	}

	public void exportCardfile( View view ) {
		Log.d( "Welcome", "exportCardfile" );
		/* PA Q COMPILE
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
		*/
	}
	
	/* Activity Result */
	
	public void onSelectCardfileResult( int resultCode, Intent intent ) {
		
		if ( resultCode == RESULT_OK ) {
    	    		/*
    		Bundle extras = intent.getExtras();
    		cardfile = extras.getParcelable( SelectCardfile.EXTRA_CARDFILE );
    		Main.setCurrentCardfile( getContentResolver(), cardfile.getID() );
    		*/
    		refresh();
    		
    	}
    	
	}



	
	
}