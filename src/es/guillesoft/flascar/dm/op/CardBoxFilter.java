package es.guillesoft.flascar.dm.op;

import es.guillesoft.flascar.dm.Card;
import es.guillesoft.flascar.dm.Cardfile.Sense;

public class CardBoxFilter implements Predicate<Card> {

	private long box;
	private Sense sense;
	
	public CardBoxFilter( long box, Sense sense ) {
	
		this.box = box;
		this.sense = sense;
		
	}
	
	@Override
	public boolean apply( Card card ) {
		
		long cardBox = sense == Sense.AB ? card.getBoxA() : card.getBoxB();
		return cardBox == box; 
		
	}
	
}