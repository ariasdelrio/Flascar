package es.guillesoft.flascar.dm.op;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import android.util.Log;

import es.guillesoft.flascar.dm.Card;

public class CardComparator implements Comparator<Card> {
	
	public enum SortType {
		ID,
		SIDE_A,
		SIDE_B,
		BOX_A,
		BOX_B,
		LAST_CHECKED_A,
		LAST_CHECKED_B
	};
	
	public enum SortOrder {
		ASCENDING,
		DESCENDING
	};
	
	private SortType sortType;
	private SortOrder sortOrder;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
	
	public CardComparator( SortType sortType, SortOrder sortOrder ) {
		
		this.sortType = sortType;
		this.sortOrder = sortOrder;
		
	}
	
	public int compare( Card a, Card b ) {

		try {
			
			int result = 0;
			
			switch( sortType ) {
			
			case ID:
				result = (int)(a.getID() - b.getID());
				break;
				
			case SIDE_A:
				result = a.getSideA().compareTo( b.getSideA() );
				break;
				
			case SIDE_B:
				result = a.getSideB().compareTo( b.getSideB() );
				break;
				
			case BOX_A:
				result = (int)(a.getBoxA() - b.getBoxA());
				break;
			
			case BOX_B:
				result = (int)(a.getBoxB() - b.getBoxB());
				break;
				
			case LAST_CHECKED_A:
				result = dateFormat.parse( a.getLastCheckedA() ).compareTo( dateFormat.parse( b.getLastCheckedA() ) );
				break;
	
			case LAST_CHECKED_B:
				result = dateFormat.parse( a.getLastCheckedB() ).compareTo( dateFormat.parse( b.getLastCheckedB() ) );
				break;
	
			}
			
			return sortOrder == SortOrder.ASCENDING ? result : -result; 
	
		}
		catch( ParseException e ) {
			
			Log.e( this.getClass().getSimpleName(), "Error de parseo: " + e );
			return 0;
			
		}
	}
	
}
