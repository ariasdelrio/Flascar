package es.guillesoft.flascar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Result extends Activity implements OnClickListener {
    
	public static final String EXTRA_ABORT = "abort";
	public static final String EXTRA_RIGHT = "right";
	public static final String EXTRA_WRONG = "wrong";
	public static final String EXTRA_PASS = "pass";
		
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        
    	super.onCreate( savedInstanceState );
        Log.d( "Result", "CREATE" );
        
        setContentView(R.layout.result);
        
        TextView txtHeader = (TextView) findViewById(R.id.result_txtHeader);
        TextView txtRight = (TextView) findViewById(R.id.result_txtRight);
        TextView txtWrong = (TextView) findViewById(R.id.result_txtWrong);
        TextView txtPass = (TextView) findViewById(R.id.result_txtPass);
                
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	
        	int right = extras.getInt( EXTRA_RIGHT );
        	int wrong = extras.getInt( EXTRA_WRONG );
        	int pass = extras.getInt( EXTRA_PASS );
        	
        	txtHeader.setText( extras.getBoolean( EXTRA_ABORT ) ? "Abortado" : "Finalizado" );
        	txtRight.setText( "Aciertos: " + right );
        	txtWrong.setText( "Fallos: " + wrong );
        	txtPass.setText( "Sin responder: " + pass );
        	
        	
        }
        
    }
    
    @Override
	public void onBackPressed()
    {
    	
    	Log.w( "Result", "BACK" );
    	
        Intent intent = new Intent();
        setResult( RESULT_OK, intent );
        
        finish();
        
    }
   
    public void onClick( View v ) {
		
    	
	}
	
    /* DEBUG */
    @Override
    protected void onStart()
    {
    	super.onStart();
    	Log.d( "Result", "START" );
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
    	Log.d( "Result", "STOP" );
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	Log.d( "Result", "DESTROY" );
    }
	
}