package es.guillesoft.flascar.db.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class CardfileBoxView extends View {
	
	/* VIEW v_cardfilebox */
	
	protected static final String V_CARDFILEBOX = "v_cardfilebox";
	protected static final String CARDFILE      = "cardfile";
	protected static final String SENSE         = "sense";
	protected static final String BOX           = "box";
	protected static final String NAME          = "name";
	protected static final String TOTAL         = "total";
	protected static final String EXPIRED       = "expired";
	    
	@Override
	public void create( SQLiteDatabase db ) {
		Log.d( this.getClass().getName(), "create" );
		/*
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		String now = dateFormat.format( date );
		
		String fromTotalAB =
			DBUtil.loJoin( 
				DBUtil.select( new String[] { BoxView.T_BOX }, new String[] { BoxView.ID, BoxView.NAME } ),
				"box",
				DBUtil.select( new String[] { CardView.V_CARD }, new String[] { CardView.ID, CardView.BOX_A, CardView.CARDFILE } ),
				"card",
				DBUtil.equals( DBUtil.field( "box", BoxView.ID ), DBUtil.field( "card", CardView.BOX_A ) ) );
		
		Log.d( "FROM T AB", fromTotalAB );
					
		String totalAB =
			DBUtil.select(
				new String [] { fromTotalAB },
				new String [] { 
					DBUtil.fieldAs( "box", BoxView.ID, "box_id" ),
					DBUtil.fieldAs( "box", BoxView.NAME, "box_name" ),
					DBUtil.countAs( DBUtil.field ( "card", CardView.ID ), "total" )
				},
				"",
				DBUtil.field( "box", BoxView.ID ) );
				
		Log.d( "TOTAL AB", totalAB );
				
		String fromDirtyAB =
			DBUtil.loJoin( 
				DBUtil.select( new String[] { BoxView.T_BOX }, new String[] { BoxView.ID, BoxView.EXPIRATION } ),
				"box",
				DBUtil.select( new String[] { CardView.V_CARD }, new String[] { CardView.ID, CardView.BOX_A, CardView.LAST_CHECKED_A } ),
				"card",
				DBUtil.and( 
					DBUtil.equals( DBUtil.field( "box", BoxView.ID ), DBUtil.field( "card", CardView.BOX_A ) ),
					DBUtil.field( "card", CardView.LAST_CHECKED_A ) + " < datetime ('" + now + "', " +
					DBUtil.field( "box", BoxView.EXPIRATION ) + ")" ) );
							
		Log.d( "FROM D AB", fromDirtyAB );
					
		String dirtyAB =
			DBUtil.select(
				new String [] { fromDirtyAB },
				new String [] { 
					DBUtil.fieldAs( "box", BoxView.ID, "box_id" ),
					DBUtil.countAs( DBUtil.field ( "card", CardView.ID ), "total" )
				},
				"",
				DBUtil.field( "box", BoxView.ID ) );
				
		Log.d( "DIRTY AB", dirtyAB );
					
		String viewQuery =
			DBUtil.select(
				new String [] { DBUtil.join( 
					totalAB, "totalcards",
					dirtyAB, "dirtycards",
					DBUtil.equals( DBUtil.field( "totalcards", "box_id" ), DBUtil.field( "dirtycards", "box_id" ) ) )  },
				new String [] {
					DBUtil.fieldAs( "totalcards", "box_id" )
				}
				
				protected static final String V_CARDFILEBOX = "v_cardfilebox";
				protected static final String CARDFILE      = "cardfile";
				protected static final String SENSE         = "sense";
				protected static final String BOX           = "box";
				protected static final String NAME          = "name";
				protected static final String TOTAL         = "total";
				protected static final String EXPIRED       = "expired";
				
					
		
		
		String sql = DBUtil.createView( V_CARD,	select );
				
		db.execSQL( sql );
		*/
	}

	@Override
	public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( this.getClass().getName(), "upgrade " + oldVersion + " -> " + newVersion );
		/*
		if( oldVersion <= FlashcardProvider.V_1 ) {
			
			db.execSQL( DBUtil.dropTable( V_CARDFILEBOX ) );
			create( db );
			
		}
*/

	}
	
	@Override
	public Cursor query( SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder ) {
		Log.d( this.getClass().getName(), "query" );
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables( V_CARDFILEBOX );
		
		Log.d( "query", qb.buildQuery( projection, selection, selectionArgs, null, null, sortOrder, null));
		return qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
		
	}
	
	@Override
	public long insert( SQLiteDatabase db, ContentValues initialValues ) {
		Log.d( this.getClass().getName(), "insert" );
		
		throw new IllegalArgumentException( "Box: insert not allowed" );

	}
	
	@Override
	public int delete( SQLiteDatabase db, String where, String[] whereArgs ) {
		Log.d( this.getClass().getName(), "delete" );
		
		throw new IllegalArgumentException( "Box: delete not allowed" );
	
	}
	
	@Override
	public int update( SQLiteDatabase db, ContentValues values, String where, String[] whereArgs ) {
		Log.d( this.getClass().getName(), "update" );
		
		throw new IllegalArgumentException( "Box: update not allowed" );
		
	}

}