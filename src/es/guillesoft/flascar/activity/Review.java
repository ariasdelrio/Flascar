package es.guillesoft.flascar.activity;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Cardfile.Sense;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.dm.op.CardPendingFilter;
import es.guillesoft.flascar.dm.op.CollectionFilter;
import es.guillesoft.flascar.ui.ResultDialog;
import es.guillesoft.flascar.ui.ResultDialogListener;

public class Review extends FlascarActivity implements ResultDialogListener  {
	
	/// int boxID
	public static final String IN_BOX   = "box";
	/// boolean sense == Sense.AB
	public static final String IN_SENSE = "sense";
	
	private List<Card> cards;
	private Sense sense;
	private int current;
	private boolean hidden;
	private int right, wrong, pass;
	
	private TextView txtHeader;
	private TextView txtSideA;
	private TextView txtSideB;
	private ProgressBar prbProgress;
	private LinearLayout llRightWrong;
	private LinearLayout llPass;
	private ImageButton btnPass;    	
	private ImageButton btnRight;
	private ImageButton btnWrong;
	
	public Review() {
		
		super( "Review", R.layout.review );
		
	}

	@Override
	public void setUp() {
        
    	right = wrong = pass = 0;
    	hidden = true;
        
    	txtHeader = (TextView) findViewById( R.id.txtHeader );
    	txtSideA = (TextView) findViewById( R.id.txtSideA );
        txtSideB = (TextView) findViewById( R.id.txtSideB );
        prbProgress = (ProgressBar) findViewById( R.id.prbProgress );
    	llRightWrong = (LinearLayout) findViewById( R.id.llRightWrong );
    	llPass = (LinearLayout) findViewById( R.id.llPass );
    	btnPass = (ImageButton) findViewById( R.id.btnPass );    	
    	btnRight = (ImageButton) findViewById( R.id.btnRight );
    	btnWrong = (ImageButton) findViewById( R.id.btnWrong );
    	
		// TODO: hacerlo bien
		try {
			Bundle extras = getIntent().getExtras();
			Main dm = Core.getInstance().getDataModel( getContentResolver() );
			Cardfile currentCardfile = dm.getCurrentCardfile();
			sense = extras.getBoolean( IN_SENSE ) ? Sense.AB : Sense.BA; 
			int box = extras.getInt( IN_BOX );
			cards = CollectionFilter.filter( currentCardfile.getCards(), 
					new CardPendingFilter( dm.getBoxes().get( box - 1 ), sense ));
			Collections.shuffle( cards, new Random() );
			prbProgress.setMax( cards.size() );
		}
		catch( Exception e ) { Log.e( "EX", e.toString() ); }
     
		current = 0;
		
    }

	@Override
	public void tearDown() {
		
	}
	
	@Override
	public void refresh() {
		
		prbProgress.setProgress( current );
		
		if( current < 0 || current >= cards.size() ) {
			showResults( false );
			return;
		}
		
		Card card = cards.get( current );
		
		txtSideA.setText( sense == Sense.AB ? card.getSideA() : card.getSideB() );
    	txtHeader.setText( "Progreso: " + ( right + wrong + pass ) + " / " + cards.size() ); 
		
    	if( hidden ) {
    		
    		txtSideB.setText( "?" );
    		btnPass.setEnabled( true );
    		btnRight.setEnabled( false );
    		btnWrong.setEnabled( false );
    		llRightWrong.setVisibility( View.GONE );
    		llPass.setVisibility( View.VISIBLE );
    		
    	}
    	else {
    		
    		txtSideB.setText( sense == Sense.AB ? card.getSideB() : card.getSideA() );
    		btnPass.setEnabled( false );
    		btnRight.setEnabled( true );
    		btnWrong.setEnabled( true );
    		llRightWrong.setVisibility( View.VISIBLE );
    		llPass.setVisibility( View.GONE );
    		
    	}
    	
    }
	
	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
	}

	@Override
	public void back() {
		
		showResults( true );
	    
	}
	
	/* Layout events */
	
	public void tap( View view ) {
	    			
		if( ! hidden ) return;
		
		hidden = false;
		refresh();
	    	
	}
	
    public void markRight( View view ) {

    	Card card = cards.get( current );
    	
    	card.climbUp( sense );
    	current++;
    	hidden = true;
		right++;
		refresh();
		
    }
    
    public void markWrong( View view ) {
		
    	Card card = cards.get( current );
    	
    	card.climbDown( sense );
    	current++;
    	hidden = true;
		wrong++;
		refresh();
		
    }

    public void pass( View view ) {
		
    	current++;
    	hidden = true;
		pass++;
		refresh();
		
    }
    
    /* private */
    
    private void showResults( boolean cancelled ) {

    	showDialog( registerDialog( new ResultDialog( 
    			getString( cancelled ? R.string.dlg_result_cancelled : R.string.dlg_result_ended ),
    			cancelled,
    			right,
    			wrong,
    			pass, 
    			this ) ) );
    	
    }

	@Override
	public void dlgResult( boolean cancelled ) {
		
		if( !cancelled ) {
		
			returnOK();
			
		}
		
	}
   	
}