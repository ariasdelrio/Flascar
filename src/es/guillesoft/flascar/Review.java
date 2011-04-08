package es.guillesoft.flascar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile;

public class Review extends Activity implements OnClickListener {
	
	private static final int ACTIVITY_RESULT = 0;

	public static final String EXTRA_CARDFILE = "cardfile";
	public static final String EXTRA_BOX_ID = "boxID";
	public static final String EXTRA_SENSE_AB = "senseAB";
	
	private Cardfile cardfile;
	/* PA Q COMPILE
	private Sense sense;
	*/
	private Cursor cursor;
	private boolean hidden;
		
	private int right, wrong, pass;
	
    protected void setUp() {        
        
    	right = wrong = pass = 0;
    	hidden = true;
        setContentView( R.layout.review );
        
        TextView txtSideB = (TextView) findViewById( R.id.review_txtSideB );
        txtSideB.setOnClickListener( this );
		/* PA Q COMPILE
        Bundle extras = getIntent().getExtras();
        cardfile = extras.getParcelable( EXTRA_CARDFILE );
        sense = extras.getBoolean( EXTRA_SENSE_AB ) ? Sense.AB : Sense.BA; 
        Log.d( "REVIEW", "sense = " + sense );
        long boxID = extras.getLong( EXTRA_BOX_ID );
     
        cursor = cardfile.getCardsToReview( getContentResolver(), boxID, sense );
        cursor.moveToFirst();
     */   
		reload();
		        
    }
    
    public void markRight( View view ) {
		/* PA Q COMPILE
		Card.toCard( cursor, cardfile ).climbUp( getContentResolver(), sense );
		cursor.moveToNext();
		hidden = true;
		right++;
		reload();
		*/
		
    }
    
    public void markWrong( View view ) {
		/* PA Q COMPILE
		Card.toCard( cursor, cardfile ).climbDown( getContentResolver(), sense );
		cursor.moveToNext();
		hidden = true;
		wrong++;
		reload();
*/
    }

    public void pass( View view ) {
		
		cursor.moveToNext();
		hidden = true;
		pass++;
		reload();
		
    }
    
    private void reload() {
		/* PA Q COMPILE
    	String card_a_str = sense == Sense.AB ? Card.SIDE_A: Card.SIDE_B;
    	String card_b_str = sense == Sense.AB ? Card.SIDE_B: Card.SIDE_A;
    	
    	if( cursor.isAfterLast() ) {
    		Log.w( "Review", "DONE" );
    		Intent i = new Intent( this, Result.class );
            i.putExtra( Result.EXTRA_RIGHT, right );
            i.putExtra( Result.EXTRA_WRONG, wrong );
            i.putExtra( Result.EXTRA_PASS, pass );
            i.putExtra( Result.EXTRA_ABORT, false );
            Log.d( "Review", "-> ACTIVITY RESULT" );
            startActivityForResult(i, ACTIVITY_RESULT );
            return;
    	}
    	
    	TextView txtHeader = (TextView) findViewById( R.id.review_txtHeader );
    	txtHeader.setText( "Progreso: " + ( right + wrong + pass ) + " / " + cursor.getCount() ); 
    	
    	TextView txtSideA = (TextView) findViewById(R.id.review_txtSideA);
    	String sideA = cursor.getString( cursor.getColumnIndex( card_a_str ) );
    	txtSideA.setText( sideA );
    	
    	TextView txtSideB = (TextView) findViewById( R.id.review_txtSideB );
    	
    	ImageButton btnPass = (ImageButton) findViewById( R.id.review_btnPass );    	
    	ImageButton btnRight = (ImageButton) findViewById( R.id.review_btnRight );
    	ImageButton btnWrong = (ImageButton) findViewById( R.id.review_btnWrong );
    	
    	LinearLayout rwButtons = (LinearLayout) findViewById( R.id.rw_buttons );
    	LinearLayout pButton = (LinearLayout) findViewById( R.id.p_button );
    	
    	if( hidden ) {
    		
    		txtSideB.setText( "?" );
    		btnPass.setEnabled( true );
    		btnRight.setEnabled( false );
    		btnWrong.setEnabled( false );
    		rwButtons.setVisibility( View.GONE );
    		pButton.setVisibility( View.VISIBLE );
    		
    	}
    	else {
    		
    		String sideB = cursor.getString( cursor.getColumnIndex( card_b_str ) );
    		txtSideB.setText( sideB );
    		btnPass.setEnabled( false );
    		btnRight.setEnabled( true );
    		btnWrong.setEnabled( true );
    		rwButtons.setVisibility( View.VISIBLE );
    		pButton.setVisibility( View.GONE );
    		
    	}
    	*/
    }
        
    @Override
	public void onBackPressed()
    {
    	
    	Log.w( "Review", "BACK" );
    	
    	Intent i = new Intent( this, Result.class );
        i.putExtra( Result.EXTRA_RIGHT, right );
        i.putExtra( Result.EXTRA_WRONG, wrong );
        i.putExtra( Result.EXTRA_PASS, pass );
        i.putExtra( Result.EXTRA_ABORT, true );
        Log.d( "Review", "-> ACTIVITY RESULT" );
        startActivityForResult(i, ACTIVITY_RESULT );
        
    }
   
    public void onClick( View v ) {
		
    	switch( v.getId() ) {
    		
    		case R.id.review_txtSideB:
    			
    			hidden = false;
    			break;
    			
    	}
    	    	
    	reload();
    	
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
        
            case ACTIVITY_RESULT:
            	
            	Log.d("CardEditor", "<- ACTIVITY RESULT");
            	setResult( RESULT_OK, new Intent() );
            	finish();
            	break;
            	
        }
    }
    
    /* Lifecycle */
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	Log.d( "Learn", "CREATE" );
    	setUp();
    }
	
    @Override
    protected void onStart()
    {
    	super.onStart();
    	Log.d( "Review", "START" );
    	setUp();
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
    	Log.d( "Review", "STOP" );
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	Log.d( "Review", "DESTROY" );
    }
	
}