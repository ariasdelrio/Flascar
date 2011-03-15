package es.guillesoft.flascar.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

abstract class Entity {
	
	private String authority;
	private String baseCT;
	
	public Entity( String authority, String baseCT ) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}
	
	public String getBaseCT() {
		return baseCT;
	}
		
	public abstract String getContentType( Uri uri );
	public abstract void create( SQLiteDatabase db );
	public abstract void upgrade( SQLiteDatabase db, int oldVersion, int newVersion );
	public abstract Uri insert( SQLiteDatabase db, Uri uri, ContentValues initialValues ) 
		throws IllegalArgumentException, SQLException;
	public abstract int delete( SQLiteDatabase db, Uri uri, String where, String[] whereArgs ) 
		throws IllegalArgumentException, SQLException;
	public abstract Cursor query( SQLiteDatabase db, Uri uri,
		String[] projection, String selection, String[] selectionArgs, String sortOrder) 
		throws IllegalArgumentException, SQLException;
	public abstract int update( SQLiteDatabase db, Uri uri, ContentValues values, String where, String[] whereArgs )
		throws IllegalArgumentException, SQLException;
	
	
}