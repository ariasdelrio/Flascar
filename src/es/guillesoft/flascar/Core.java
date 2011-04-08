package es.guillesoft.flascar;

import android.content.ContentResolver;
import es.guillesoft.flascar.dm.Main;

public class Core {
	
	/// Data model
	private Main main;

	public Main getDataModel( ContentResolver cr ) {
		if( main == null ) main = new Main( cr );
		return main;
	}
		
	/* Singleton */
	static private Core instance = null;
	
	public static Core getInstance() {
		if( instance == null ) instance = new Core();
		return instance;
	}
	
	private Core() {
	}
	

}
