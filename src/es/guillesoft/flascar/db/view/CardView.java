package es.guillesoft.flascar.db.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import es.guillesoft.flascar.db.DBUtil;
import es.guillesoft.flascar.db.FlashcardProvider;

public class CardView extends View {
	
	/* TABLE card */
	
	protected static final String T_CARD    = "card";
	public static final String ID           = "_id";
	public static final String CARDFILE     = "cardfile";
	
	private static final String [] columnsCard = {
		DBUtil.primaryColumn( ID ),
		DBUtil.numberColumn( CARDFILE )
	};
	
	/* TABLE card_sense */
	
	private static final String T_CARD_SENSE = "card_sense";
	private static final String CARD         = "card";
	private static final String SENSE        = "sense";
	private static final String BOX          = "box";
	private static final String SIDE         = "side";
	private static final String LAST_CHECKED = "last_checked";
	
	private static final String [] columnsCardSense = {
		DBUtil.numberColumn( CARD ),
		DBUtil.numberColumn( SENSE ),
		DBUtil.numberColumn( BOX ),
		DBUtil.textColumn( SIDE ),
		DBUtil.tsColumn( LAST_CHECKED )
	};
	
	/* VIEW v_card */
	
	protected static final String V_CARD         = "v_card";
	public static final String BOX_A          = "box_a";
	public static final String BOX_B          = "box_b";
	public static final String SIDE_A         = "side_a";
	public static final String SIDE_B         = "side_b";
	public static final String LAST_CHECKED_A = "last_checked_a";
	public static final String LAST_CHECKED_B = "last_checked_b";

	@Override
	public void create( SQLiteDatabase db ) {
		Log.d( this.getClass().getSimpleName(), "create" );
		
		String sql = DBUtil.createTable( T_CARD, columnsCard );
		DBUtil.log( this.getClass().getSimpleName(), sql );
		db.execSQL( sql );
		
		sql = DBUtil.createTable( T_CARD_SENSE, columnsCardSense );
		DBUtil.log( this.getClass().getName(), sql );
		db.execSQL( sql );

		String whereAB =
			DBUtil.and(
					DBUtil.equals( DBUtil.field( T_CARD, ID ), DBUtil.field( T_CARD_SENSE, CARD ) ),
					DBUtil.equals( DBUtil.field( T_CARD_SENSE, SENSE ), Integer.toString( FlashcardProvider.SENSE_AB ) ) );
		
		String whereBA = 
			DBUtil.and(
					DBUtil.equals( DBUtil.field( T_CARD_SENSE, SENSE ), Integer.toString( FlashcardProvider.SENSE_BA ) ),
					DBUtil.equals( DBUtil.field( T_CARD, ID ), DBUtil.field( T_CARD_SENSE, CARD ) )
					 );
		
		String selectAB = DBUtil.select( 
				new String[] { T_CARD, T_CARD_SENSE }, 
				new String[] {
						DBUtil.fieldAs( T_CARD, ID, ID ),
						DBUtil.fieldAs( T_CARD, CARDFILE, CARDFILE ),
						DBUtil.fieldAs( T_CARD_SENSE, BOX, BOX_A ),
						DBUtil.fieldAs( T_CARD_SENSE, SIDE, SIDE_A ),
						DBUtil.fieldAs( T_CARD_SENSE, LAST_CHECKED, LAST_CHECKED_A )
				},
				whereAB );
		
		String selectBA = DBUtil.select( 
				new String[] { T_CARD, T_CARD_SENSE }, 
				new String[] {
						DBUtil.fieldAs( T_CARD, ID, ID ),
						DBUtil.fieldAs( T_CARD_SENSE, BOX, BOX_B ),
						DBUtil.fieldAs( T_CARD_SENSE, SIDE, SIDE_B ),
						DBUtil.fieldAs( T_CARD_SENSE, LAST_CHECKED, LAST_CHECKED_B )
				},
				whereBA );

		String join = DBUtil.join( 
				selectAB, "ab", 
				selectBA, "ba", 
				DBUtil.equals( DBUtil.field( "ab", ID ), DBUtil.field( "ba", ID ) ) );

		String select = DBUtil.select(
				new String[] { join }, 
				new String[] {
						DBUtil.fieldAs( "ab", ID, ID ),
						CARDFILE,
						BOX_A,
						BOX_B,
						SIDE_A,
						SIDE_B,
						LAST_CHECKED_A,
						LAST_CHECKED_B
				});
				
		sql = DBUtil.createView( V_CARD,	select );

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
		
		qb.setTables( V_CARD );
		
		Log.d( "query", qb.buildQuery( projection, selection, selectionArgs, null, null, sortOrder, null));
		return qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
		
	}
	
	@Override
	public long insert( SQLiteDatabase db, ContentValues initialValues ) {
		Log.d( this.getClass().getSimpleName(), "insert" );
		
		FlashcardProvider provider = getProvider();
		
		if( initialValues == null ) initialValues = new ContentValues();
	
		if( initialValues.containsKey( ID ) )
			throw new IllegalArgumentException( ID + " is a private field" );

		if( !initialValues.containsKey( CARDFILE ) )
			throw new IllegalArgumentException( CARDFILE + " can not be NULL" );
		
		Cursor cursor = provider.query(
				FlashcardProvider.getUri( FlashcardProvider.CARDFILE ),
				null, 
				CardfileView.ID + " = " + initialValues.getAsLong( CARDFILE ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + CARDFILE );
		
		cursor.close();
		
		if( !initialValues.containsKey( BOX_A ) )
			throw new IllegalArgumentException( BOX_A + " can not be NULL" );
		
		cursor = provider.query(
				FlashcardProvider.getUri( FlashcardProvider.BOX ),
				null, 
				BoxView.ID + " = " + initialValues.getAsLong( BOX_A ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + BOX_A );
		
		if( !initialValues.containsKey( BOX_B ) )
			throw new IllegalArgumentException( BOX_B + " can not be NULL" );
		
		cursor = provider.query(
				FlashcardProvider.getUri( FlashcardProvider.BOX ),
				null, 
				BoxView.ID + " = " + initialValues.getAsLong( BOX_B ),
				null,
				null );
		
		if( cursor.getCount() == 0 )
			throw new IllegalArgumentException( "foreign key violation: " + BOX_B );

		cursor.close();
		
		db.beginTransaction();
		
		ContentValues cardValues = new ContentValues();
		cardValues.put( CARDFILE, initialValues.getAsLong( CARDFILE ) );
		long cardID = db.insert( T_CARD, null, cardValues );
		if( cardID == -1 ) { db.endTransaction(); return -1; };
		
		ContentValues senseABValues = new ContentValues();
		senseABValues.put( CARD, cardID );
		senseABValues.put( SENSE, FlashcardProvider.SENSE_AB );
		senseABValues.put( BOX, initialValues.getAsLong( BOX_A ) );
		senseABValues.put( SIDE, initialValues.getAsString( SIDE_A ) );
		senseABValues.put( LAST_CHECKED, initialValues.getAsString( LAST_CHECKED_A ) );
		if( db.insert( T_CARD_SENSE, null, senseABValues ) == -1 ) { db.endTransaction(); return -1; };
		
		ContentValues senseBAValues = new ContentValues();
		senseBAValues.put( CARD, cardID );
		senseBAValues.put( SENSE, FlashcardProvider.SENSE_BA );
		senseBAValues.put( BOX, initialValues.getAsLong( BOX_B ) );
		senseBAValues.put( SIDE, initialValues.getAsString( SIDE_B ) );
		senseBAValues.put( LAST_CHECKED, initialValues.getAsString( LAST_CHECKED_B ) );
		if( db.insert( T_CARD_SENSE, null, senseBAValues ) == -1 ) { db.endTransaction(); return -1; };
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return cardID;

	}
	
	@Override
	public int delete( SQLiteDatabase db, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "delete" );
		
		// works only with a single card! where ignored!
		
		String cardWhere = DBUtil.equals( ID, "?" );
		String cardSenseWhere = DBUtil.equals( CARD, "?" );
		
		db.beginTransaction();
		if( db.delete( T_CARD, cardWhere, whereArgs ) != 1 ) { db.endTransaction(); return -1; };
		if( db.delete( T_CARD_SENSE, cardSenseWhere, whereArgs ) != 2 ) { db.endTransaction(); return -1; };
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return 1;
	
	}
	
	@Override
	public int update( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs ) {
		Log.d( this.getClass().getSimpleName(), "update" );
		
		// works only with a single card! where ignored!
		
		String cardSenseABWhere = DBUtil.and( 
				DBUtil.equals( CARD, "?" ), 
				DBUtil.equals( SENSE, Integer.toString( FlashcardProvider.SENSE_AB ) ) );
		String cardSenseBAWhere = DBUtil.and( 
				DBUtil.equals( CARD, "?" ), 
				DBUtil.equals( SENSE, Integer.toString( FlashcardProvider.SENSE_BA ) ) );
		
		db.beginTransaction();
		
		ContentValues senseABValues = new ContentValues();
		if( values.containsKey( BOX_A ) ) senseABValues.put( BOX, values.getAsLong( BOX_A ) );
		if( values.containsKey( SIDE_A ) ) senseABValues.put( SIDE, values.getAsString( SIDE_A ) );
		if( values.containsKey( LAST_CHECKED_A ) ) senseABValues.put( LAST_CHECKED, values.getAsString( LAST_CHECKED_A ) );
		if( senseABValues.size() > 0 ) 
			if( db.update( T_CARD_SENSE, senseABValues, cardSenseABWhere, whereArgs ) != 1 ) { 
				db.endTransaction(); 
				return -1; 
			}
		
		ContentValues senseBAValues = new ContentValues();
		if( values.containsKey( BOX_B ) ) senseABValues.put( BOX, values.getAsLong( BOX_B ) );
		if( values.containsKey( SIDE_B ) ) senseABValues.put( SIDE, values.getAsString( SIDE_B ) );
		if( values.containsKey( LAST_CHECKED_B ) ) senseABValues.put( LAST_CHECKED, values.getAsString( LAST_CHECKED_B ) );
		if( senseBAValues.size() > 0 ) 
			if( db.update( T_CARD_SENSE, senseBAValues, cardSenseBAWhere, whereArgs ) != 1 ) {
				db.endTransaction();
				return -1;
			}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return 1;
		
	}

}
