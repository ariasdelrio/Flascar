package es.guillesoft.flascar.dm.op;

import es.guillesoft.flascar.dm.Card;

public class CardFilter implements Predicate<Card> {

	private String prompt;
	
	public CardFilter( String prompt ) {
	
		this.prompt = prompt;
		
	}
	
	@Override
	public boolean apply( Card card ) {
		
		return card.getSideA().contains( prompt );
		
	}
	
}