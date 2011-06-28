package es.guillesoft.flascar.dm.op;

import es.guillesoft.flascar.dm.Card;

public class CardFilter implements Predicate<Card> {

	public enum Field {
		SIDE_A,
		SIDE_B
	};

	private Field field;
	private String prompt;
	
	public CardFilter( Field field, String prompt ) {
	
		this.prompt = prompt;
		this.field = field;
		
	}
	
	@Override
	public boolean apply( Card card ) {
		
		String cmpstr = "";
		
		switch( field ) {
		
		case SIDE_A:
			cmpstr = card.getSideA();
			break;
			
		case SIDE_B:
			cmpstr = card.getSideB();
			break;

		}
		
		return cmpstr.contains( prompt );
		
	}
	
}