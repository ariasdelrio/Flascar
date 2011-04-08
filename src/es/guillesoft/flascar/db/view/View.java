package es.guillesoft.flascar.db.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.guillesoft.flascar.db.FlashcardProvider;

public abstract class View {
	
	private FlashcardProvider provider;
	
	public View() {
		provider = null;
	}
	
	public void setProvider( FlashcardProvider provider ) {
		this.provider = provider;
	}
	
	protected FlashcardProvider getProvider() {
		return provider;
	}
	
	public abstract void   create ( SQLiteDatabase db );
	public abstract void   upgrade( SQLiteDatabase db, int oldVersion, int newVersion );
	public abstract Cursor query  ( SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder);
	public abstract long   insert ( SQLiteDatabase db, ContentValues initialValues );
	public abstract int    delete ( SQLiteDatabase db, String where, String[] whereArgs );
	public abstract int    update ( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs );
	
}
