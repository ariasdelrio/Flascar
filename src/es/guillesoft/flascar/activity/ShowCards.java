package es.guillesoft.flascar.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import es.guillesoft.flascar.Core;
import es.guillesoft.flascar.R;
import es.guillesoft.flascar.dm.Cardfile;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Main;
import es.guillesoft.flascar.dm.op.CardComparator;
import es.guillesoft.flascar.dm.op.CardFilter;
import es.guillesoft.flascar.dm.op.CollectionFilter;
import es.guillesoft.flascar.dm.op.CardComparator.SortOrder;
import es.guillesoft.flascar.dm.op.CardComparator.SortType;
import es.guillesoft.flascar.ui.CardViewAdapter;
import es.guillesoft.flascar.ui.SearchDialog;
import es.guillesoft.flascar.ui.SearchDialogListener;
import es.guillesoft.flascar.ui.SortDialog;
import es.guillesoft.flascar.ui.SortDialogListener;

public class ShowCards extends FlascarActivity implements OnItemClickListener, SortDialogListener, SearchDialogListener, Observer {
	
	/// long cardfileID
	public static final String IN_CARDFILE = "cardfile";
	
	private ListView lview;
	private TextView txtEmpty;
	private Main dm;
	
	private Cardfile cardfile;
	private List<Card> cards;
	private int sortDialogID;
	private int searchDialogID;
	private ArrayList<String> sortTypes;
	private final SortType STYPES[] = { 
			SortType.SIDE_A,
			SortType.SIDE_B,
			SortType.LAST_CHECKED_A,
			SortType.LAST_CHECKED_B,
			SortType.BOX_A,
			SortType.BOX_B
	};
	
	public ShowCards() {
		
		super( "ShowCards", R.layout.show_cards );
		sortDialogID = -1;
		
	}
	
	@Override
	public void setUp() {

		registerActivity( ShowCard.class );
		registerActivity( EditCard.class );
		
        dm = Core.getInstance().getDataModel( getContentResolver() );

		Bundle extras = getIntent().getExtras();
		// TODO: hacerlo bien
		try {
			cardfile = dm.getCardfile( extras.getLong( IN_CARDFILE ) );
			cardfile.addObserver( this );
		}
		catch( Exception e ) { Log.e( "EX", e.toString() ); }
        
		cards = cardfile.getCards();
		
		lview = (ListView)findViewById( R.id.list );
		lview.setOnItemClickListener( this ); 
		lview.setAdapter( new CardViewAdapter( lview.getContext(), cards ) );
		txtEmpty = (TextView)findViewById( R.id.empty );
		
		sortTypes = new ArrayList<String>();
		sortTypes.add( getString( R.string.side ) + " " + cardfile.getSideA() );
		sortTypes.add( getString( R.string.side ) + " " + cardfile.getSideB() );
		sortTypes.add( getString( R.string.lastReview ) + " " + cardfile.getSideA() + " > " + cardfile.getSideB() );
		sortTypes.add( getString( R.string.lastReview ) + " " + cardfile.getSideB() + " > " + cardfile.getSideA() );
		sortTypes.add( getString( R.string.box ) + " " + cardfile.getSideA() );
		sortTypes.add( getString( R.string.box ) + " " + cardfile.getSideB() );
		
		sortDialogID = registerDialog( new SortDialog( sortTypes, this ) );
		
		searchDialogID = registerDialog( new SearchDialog( this ) );
    
	}

	@Override
	public void tearDown() {

		if( cardfile != null ) cardfile.deleteObserver( this );
		
	}
	
	@Override
	public void refresh() {
	
		lview.setAdapter( new CardViewAdapter( lview.getContext(), cards ) );
		
		if( cards.size() == 0 ) {
			
			lview.setVisibility( View.GONE );
			txtEmpty.setVisibility( View.VISIBLE );
			
		}
		else {
		
			lview.setVisibility( View.VISIBLE );
			txtEmpty.setVisibility( View.GONE );
			
		}
	}
	
	/* Layout events */

	public void changeOrder( View view ) {
		
		Log.d( getClass().getSimpleName(), "changeOrder" );
		showDialog( sortDialogID );
		
	}
	
	public void search( View view ) {
		
		Log.d( getClass().getSimpleName(), "search" );
		showDialog( searchDialogID );
		
	}
	
	public void reload( View view ) {
		
		Log.d( getClass().getSimpleName(), "reload" );
		cards = cardfile.getCards();
		refresh();
		
	}
	
	public void add( View view ) {
		
		Log.d( getClass().getSimpleName(), "add" );
		startActivity( EditCard.class );
		
	}
	
	/* List events */

	@Override
	public void onItemClick( AdapterView<?> adapter, View view, int position, long id ) {

		Log.d( getClass().getSimpleName(), "show" );

		Bundle extras = new Bundle();
		extras.putLong( ShowCard.IN_CARD, id );
		startActivity( ShowCard.class, extras );
		        
    }

	@Override
	public void onResult( Class<? extends Activity> clazz, int resultCode, Intent intent ) {
		
	}

	@Override
	public void back() {
		
		returnOK();
		
	}

	@Override
	public void dlgSort( int item, boolean invertedOrder ) {

		SortType sortType = item > 0 && item < STYPES.length ? STYPES[item] : SortType.SIDE_A;
		SortOrder sortOrder = invertedOrder ? SortOrder.DESCENDING : SortOrder.ASCENDING;
		
    	Collections.sort( cards, new CardComparator( sortType, sortOrder ) );
    		
    	refresh();
		
	}
	
	@Override
	public void dlgSearch( String prompt ) {

		cards = CollectionFilter.filter( cards, new CardFilter( prompt ) );
		refresh();
		
	}

	@Override
	public void update( Observable observable, Object data ) {

		if( data instanceof Card ) {
			
			cards = cardfile.getCards();
			
		}
		
	}
    
}