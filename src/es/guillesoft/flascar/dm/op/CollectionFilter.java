package es.guillesoft.flascar.dm.op;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionFilter {

	public static <T> ArrayList<T> filter( Collection<T> target, Predicate<T> predicate ) {
		
		ArrayList<T> result = new ArrayList<T>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
		
	}
	
}