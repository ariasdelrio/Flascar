package es.guillesoft.flascar.dm.op;

import java.util.Comparator;

import es.guillesoft.flascar.dm.Cardfile;

public class CardfileComparator implements Comparator<Cardfile> {
	
	public int compare( Cardfile a, Cardfile b ) {

		return a.getName().compareTo( b.getName() );
			
	}
	
}
