package es.guillesoft.flascar.activity;

import java.io.File;
import java.io.FileFilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import es.guillesoft.flascar.R;
import es.guillesoft.flascar.ui.ConfirmDialog;
import es.guillesoft.flascar.ui.ConfirmDialogListener;
import es.guillesoft.flascar.ui.FileViewAdapter;
import es.guillesoft.flascar.ui.PromptDialog;
import es.guillesoft.flascar.ui.PromptDialogListener;

public class SelectFile extends FlascarActivity implements OnItemClickListener, ConfirmDialogListener, PromptDialogListener {
	
	/// String file extension (nullable) - default: none
	public static final String IN_EXTENSION = "extension";
	/// Boolean read only (nullable) - default: true
	public static final String IN_READONLY = "readonly";
	/// String file
	public static final String OUT_FILE = "file";
	
	private String extension;
	private boolean readonly;
	private File currentDirectory;
	File [] files;
	
	private ListView lview;
	private TextView txtEmpty;
	private int dlgConfirm;
	private int dlgPrompt;
	private String chosenFile;
	
	public SelectFile() {
		super( "SelectFile", R.layout.select_file );
	}
	
	@Override
	public void setUp() {

		extension = null;
		readonly = true;
		currentDirectory = new File("/");

		dlgConfirm = registerDialog( new ConfirmDialog( getString( R.string.dlg_confirm_overwrite ), this ) );
		dlgPrompt = registerDialog( new PromptDialog( getString( R.string.dlg_prompt_addfile ), this ) );
		
		Intent intent = getIntent();
		if( intent != null ) {
			Bundle extras = getIntent().getExtras();
			if( extras != null && extras.containsKey( IN_EXTENSION ) ) extension = extras.getString( IN_EXTENSION );
			if( extras != null && extras.containsKey( IN_READONLY  ) ) readonly = extras.getBoolean( IN_READONLY  );
		}
		
		txtEmpty = (TextView)findViewById( R.id.empty );
		
		lview = (ListView)findViewById( R.id.list );
		lview.setOnItemClickListener( this ); 
		
		if( readonly ) findViewById( R.id.btnAdd ).setVisibility( View.GONE );
		
	}

	@Override
	public void tearDown() {
		
	}
	
	@Override
	public void refresh() {
	
		files = currentDirectory.listFiles( new FileFilter() {
			
			@Override
			public boolean accept( File pathname ) {

				if( pathname.isHidden() ) return false;
				if( !pathname.canRead() ) return false;

				if( pathname.isDirectory() ) return true;

				String fileName = pathname.getName();
				int dotIndex = fileName.lastIndexOf(".");
				if( dotIndex == -1 ) return false;
				String fileExtension = fileName.substring( dotIndex + 1, fileName.length() );
				return fileExtension.equals( extension );
			
			}
			
		});
		
		lview.setAdapter( new FileViewAdapter( lview.getContext(), files ) );
		
		if( files.length == 0 ) {
			
			lview.setVisibility( View.GONE );
			txtEmpty.setVisibility( View.VISIBLE );
			
		}
		else {
		
			lview.setVisibility( View.VISIBLE );
			txtEmpty.setVisibility( View.GONE );
			
		}
		
	}
	
	private void returnFile() {
		
		Bundle extras = new Bundle();
		extras.putString( OUT_FILE, chosenFile );
		returnOK( extras );
		
	}
	
	/* Layout events */
	
	public void moveUp( View view ) {
		
		Log.d( getClass().getSimpleName(), "moveUp" );
		
		File parentFile = currentDirectory.getParentFile();
		
		if( parentFile == null ) 
			Toast.makeText( getApplicationContext(), "estás en /", Toast.LENGTH_SHORT ).show();
		
		else {
			
			currentDirectory = parentFile;
			refresh();
			
		}
		
	}
	
	public void moveToRoot( View view ) {
		
		Log.d( getClass().getSimpleName(), "moveToRoot" );
		
		currentDirectory = new File("/");
		
		refresh();
		
	}
	
	public void moveToSD( View view ) {
		
		Log.d( getClass().getSimpleName(), "moveToSD" );
		
		currentDirectory = new File("/sdcard");
		
		refresh();
		
	}
	
	public void add( View view ) {
		
		Log.d( getClass().getSimpleName(), "add" );
		
		if( readonly ) return;
		
		showDialog( dlgPrompt );
		
	}	
	
	/* List events */

	@Override
	public void onItemClick( AdapterView<?> adapter, View view, int position, long id ) {

		File file = files[position];
		
		if( file.isDirectory() ) {
		
			currentDirectory = file;
			refresh();
			
		}
		else {
			
			chosenFile = files[position].getAbsolutePath();
			
			if( ! readonly ) showDialog( dlgConfirm );
			else returnFile();
			
		}
		        
    }

	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
	}

	@Override
	public void back() {
		
		returnCancel();
		
	}

	@Override
	public void dlgConfirm( boolean confirm ) {
		
		if( confirm ) returnFile();
		
	}

	@Override
	public void dlgPrompt( String value ) {
		
		if( value == null ) return;
		
		File file = new File( currentDirectory, value );
		chosenFile = file.getAbsolutePath();
		returnFile();
		
	}
	
}