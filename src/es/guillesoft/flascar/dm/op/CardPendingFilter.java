package es.guillesoft.flascar.dm.op;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.guillesoft.flascar.dm.Box;
import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile.Sense;

public class CardPendingFilter implements Predicate<Card> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
	
	private Box box;
	private Sense sense;
	private long now;
	
	public CardPendingFilter( Box box, Sense sense ) {
	
		this.box = box;
		this.sense = sense;
		now = new Date().getTime();
		
	}
	
	@Override
	public boolean apply( Card card ) {
		
		if( box.getID() != ( sense == Sense.AB ? card.getBoxA() : card.getBoxB() ) ) return false;
		
		try {
		
			long lastChecked = dateFormat.parse( sense == Sense.AB ? card.getLastCheckedA() : card.getLastCheckedB() ).getTime();
			return now - lastChecked >= box.getExpiration() * 1000 * 60;
			
		}
		catch( ParseException e ) {
			return false;
		}
		
	}
	
}