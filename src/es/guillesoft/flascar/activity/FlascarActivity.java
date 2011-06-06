package es.guillesoft.flascar.activity;

import java.util.ArrayList;

import es.guillesoft.flascar.ui.FlascarDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public abstract class FlascarActivity extends Activity {

	private String name;
	private int layout;
	private ArrayList<Class<? extends Activity>> registeredActivities;
	private ArrayList<FlascarDialog> registeredDialogs;
	private int menuLayout;
	
	public FlascarActivity( String name, int layout, int menuLayout ) {
		
		this.name = name;
		this.layout = layout;
		this.menuLayout = menuLayout;
		registeredActivities = new ArrayList<Class<? extends Activity>>();
		registeredDialogs =  new ArrayList<FlascarDialog>();
	}

	public FlascarActivity( String name, int layout ) {
		
		this( name, layout, -1 );
		
	}
		
	public void registerActivity( Class<? extends Activity> clazz ) {
		
		registeredActivities.add( clazz );
		
	}
	
	public int registerDialog( FlascarDialog dialog ) {
		
		registeredDialogs.add( dialog );
		return registeredDialogs.size() - 1;
		
	}
	
	public void startActivity( Class<? extends Activity> activity, Bundle extras ) {
		
		int actID = registeredActivities.indexOf( activity );
    	
    	if( actID == -1 ) {
    		
    		Log.e( name, "attempted to start unregistered activity " + activity.getSimpleName() );
    		
    	}
    	else {
    	
    		Log.d( name, "-> " + activity.getSimpleName() + " (" + actID + ")" );
    		Intent intent = new Intent( this, activity );
    		if( extras != null ) intent.putExtras( extras );
    		startActivityForResult( intent, actID );
    		
    	}
    	
    }
	
	public void startActivity( Class<? extends Activity> activity ) {
		
		startActivity( activity, null );
		
	}
	
	public void returnCancel() {
		Log.d( name, "finish CANCEL" );
		
		setResult( RESULT_CANCELED );
		finish();
		
	}
	
	public void returnOK( Bundle extras ) {
		Log.d( name, "finish OK" );
		
		Intent intent = new Intent();
		intent.putExtras( extras );
		setResult( RESULT_OK, intent );
		
        finish();
        
	}
	
	public void returnOK() {
		
		setResult( RESULT_OK, null );
		finish();
		
	}
	
	@Override
    public void onCreate( Bundle savedInstanceState ) {
		
        super.onCreate( savedInstanceState );
        Log.d( name, "CREATE" );
        this.setContentView( layout );
        setUp();
        
    }
	
    @Override
    protected void onStart() {
    	
    	super.onStart();
    	Log.d( name, "START" );
        refresh();
        
    }
    
    @Override
    protected void onStop() {
    	
    	super.onStop();
    	Log.d( name, "STOP" );
    	
    }
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    	Log.d( name, "DESTROY" );
    	tearDown();
    	
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        super.onActivityResult( requestCode, resultCode, intent );
        if( requestCode < 0 || requestCode >= registeredActivities.size() ) {
            
        	Log.e( name, "unregistered activity " + requestCode + " returns" );
        	
        }
        else {
        	
        	Class<? extends Activity> clazz = registeredActivities.get( requestCode );
        	String resultString = resultCode == Activity.RESULT_OK ? "OK" : "CANCELLED";
        	
        	Log.d( name, "<- " + clazz.getSimpleName() + " (" + requestCode + ") " + resultString );
        	onResult( registeredActivities.get( requestCode ), resultCode, intent );
        	
        }
        
    }
    
    @Override
	public void onBackPressed()
    {
		
		Log.d( name, "BACK" );
		back();
        
    }
    
    @Override
	public boolean onCreateOptionsMenu( Menu menu ) {

    	boolean ret = super.onPrepareOptionsMenu( menu );
    	if( menuLayout != -1 ) {
    		MenuInflater inflater = getMenuInflater();
    		inflater.inflate( menuLayout, menu );
    		return true;
    	}
    	return ret;
    		
	}
    
    @Override 
    protected Dialog onCreateDialog( int id ) {
    	
    	
    	if( id < 0 || id > registeredDialogs.size() ) {
    		
    		Log.e( name, "attempted to start unregistered dialog " + id );
    		return null;
    		
    	}
    	else return registeredDialogs.get( id ).build( this );
    	
    }

    /* Abstract methods */
    
    public abstract void setUp();
	public abstract void tearDown();
	public abstract void refresh();
	public abstract void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent );
	public abstract void back();
	
}

