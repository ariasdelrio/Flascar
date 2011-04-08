package es.guillesoft.flascar.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

class CFlascarActivity {

	private IFlascarActivity fcActivity;
	private String name;
	private int layout;
	private ArrayList<Class<? extends Activity>> registeredActivities;
	
	public CFlascarActivity( IFlascarActivity fcActivity, String name, int layout ) {
		this.name = name;
		this.layout = layout;
		this.fcActivity = fcActivity;
		registeredActivities = new ArrayList<Class<? extends Activity>>();
	}
	
	public void registerActivity( Class<? extends Activity> clazz ) {
		registeredActivities.add( clazz );
	}
	
	public void startActivity( Class<? extends Activity> activity ) {
    	
    	int actID = registeredActivities.indexOf( activity );
    	
    	if( actID == -1 ) {
    		
    		Log.e( name, "attempted to start unregistered activity " + activity.getSimpleName() );
    		
    	}
    	else {
    	
    		Log.d( name, "-> " + activity.getSimpleName() + " (" + actID + ")" );
    		Intent intent = new Intent( fcActivity.thiz(), activity );
    		fcActivity.thiz().startActivityForResult( intent, actID );
    		
    	}
    	
    }
	
    public void onCreate( Bundle savedInstanceState ) {
        Log.d( name, "CREATE" );
        fcActivity.thiz().setContentView( layout );
        fcActivity.setUp();
        fcActivity.refresh();
    }
	
    public void onStart() {
    	Log.d( name, "START" );
    }
    
    public void onStop() {
    	Log.d( name, "STOP" );
    }
    
    public void onDestroy() {
    	Log.d( name, "DESTROY" );
    	fcActivity.tearDown();
    }
    
    public void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    	
        if( requestCode < 0 || requestCode >= registeredActivities.size() ) {
        
        	Log.e( name, "unregistered activity " + requestCode + " returns" );
        	
        }
        else {
        	
        	Class<? extends Activity> clazz = registeredActivities.get( requestCode );
        	String resultString = resultCode == Activity.RESULT_OK ? "OK" : "CANCELLED";
        	
        	Log.d( name, "<- " + clazz.getSimpleName() + " (" + requestCode + ") " + resultString );
        	fcActivity.onResult( registeredActivities.get( requestCode ), resultCode, intent );
        	
        }
        
    }
    /*
	public void onBackPressed()
    {
		
		Log.d( name, "BACK" );
        fcActivity.setResult( Activity.RESULT_CANCELED );
        fcActivity.finish();
        
    }
	*/
	
	
}
