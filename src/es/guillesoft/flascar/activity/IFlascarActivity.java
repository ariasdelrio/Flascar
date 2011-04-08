package es.guillesoft.flascar.activity;

import android.app.Activity;
import android.content.Intent;

public interface IFlascarActivity {

	public Activity thiz();
	public void setUp();
	public void tearDown();
	public void refresh();
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent );
//	public void onBackPressed();
	
}
