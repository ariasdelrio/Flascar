package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public abstract class FlascarListActivity extends ListActivity implements IFlascarActivity {

	private CFlascarActivity common;
	
	public FlascarListActivity( String name, int layout ) {
		common = new CFlascarActivity( this, name, layout );
	}
		
	public void registerActivity( Class<? extends FlascarActivity> clazz ) {
		common.registerActivity( clazz );
	}
	
	public void startActivity( Class<? extends FlascarActivity> activity ) {
    	common.startActivity( activity );
    }
	
	@Override
	public Activity thiz() {
		return this;
	}
	
	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        common.onCreate( savedInstanceState );
    }
	
    @Override
    protected void onStart() {
    	super.onStart();
    	common.onStart();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	common.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	common.onDestroy();
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        super.onActivityResult( requestCode, resultCode, intent );
        common.onActivityResult( requestCode, resultCode, intent );
    }
    
}
