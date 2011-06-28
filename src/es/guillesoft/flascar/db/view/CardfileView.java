package es.guillesoft.flascar.db.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import es.guillesoft.flascar.db.DBUtil;

public class CardfileView extends View {

	/* TABLE cardfile */
	
	protected static final String T_CARDFILE = "cardfile";
	public static final String ID            = "_id";
	public static final String NAME          = "name";
	public static final String SIDE_A        = "side_a";
	public static final String SIDE_B        = "side_b";
	public static final String LANGUAGE_A    = "language_a";
	public static final String LANGUAGE_B    = "language_b";
	
	private static final String [] columnsT = {
		DBUtil.primaryColumn( ID ),
		DBUtil.textColumn( NAME ),
		DBUtil.textColumn( SIDE_A ),
		DBUtil.textColumn( SIDE_B ),
		DBUtil.textColumn( LANGUAGE_A ),
		DBUtil.textColumn( LANGUAGE_B )
	};
	
	/* VIEW v_cardfile */
	
	protected static final String V_CARDFILE = "v_cardfile";
	public static final String CARDS        = "cards";
	
	private static final String [] tables = {
		DBUtil.loJoin( 
			T_CARDFILE, 
			"cf",
			CardView.T_CARD,
			"c",
			DBUtil.equals(
				DBUtil.field( "cf", ID ), 
				DBUtil.field( "c", CardView.CARDFILE )
			)
		)
	};

	private static final String [] columnsV = {
		DBUtil.fieldAs( "cf", ID, ID ),
		DBUtil.fieldAs(	"cf", NAME, NAME ),
		DBUtil.fieldAs( "cf", SIDE_A, SIDE_A ),
		DBUtil.fieldAs( "cf", SIDE_B, SIDE_B ),
		DBUtil.fieldAs( "cf", LANGUAGE_A, LANGUAGE_A ),
		DBUtil.fieldAs( "cf", LANGUAGE_B, LANGUAGE_B ),
		DBUtil.countAs( DBUtil.field( "c", CardView.ID ), CARDS )
	};
	
	@Override
	public void create( SQLiteDatabase db ) {
		Log.d( this.getClass().getSimpleName(), "create" );
		
		db.execSQL( DBUtil.createTable( T_CARDFILE, columnsT ) );
		
		String sql = DBUtil.createView(	
				V_CARDFILE, 
				DBUtil.select( tables, columnsV, null, DBUtil.field( "cf", ID ) ) );
		
		DBUtil.log( this.getClass().getSimpleName(), sql );
		
    	db.execSQL( sql );
    	
	}

	@Override
	public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( this.getClass().getSimpleName(), "upgrade " + oldVersion + " -> " + newVersion );

	}

	@Override
	public Cursor query( SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
		Log.d( this.getClass().getSimpleName(), "query" );
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables( V_CARDFILE ); 
		return qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
		
	}
	
	@Override
	public long insert( SQLiteDatabase db, ContentValues initialValues ) {
		Log.d( this.getClass().getSimpleName(), "insert" );
		
		if( initialValues == null ) initialValues = new ContentValues();
	
		if( initialValues.containsKey( ID ) )
			throw new IllegalArgumentException( ID + " is a private field" );

		return db.insert( T_CARDFILE, null, initialValues );

	}
	
	@Override
	public int delete( SQLiteDatabase db, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "delete" );
		
		// @todo delete en cascada de las cards
		return db.delete( T_CARDFILE, where, whereArgs );
	
	}
	
	@Override
	public int update( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "update" );
		
		return db.update( T_CARDFILE, values, where, whereArgs );

	}
		
}
