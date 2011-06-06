package es.guillesoft.flascar.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Deck {

	private ArrayList<DeckCard> cards;
	
	public Deck() {
		
		cards = new ArrayList<DeckCard>();
		
	}
	
	public void add( DeckCard card ) {
		
		cards.add( card );
		
	}
	
	public List<DeckCard> getCards() {
		
		return cards;
		
	}
	
	public static Deck load( File file ) throws Exception {

		try {
			
			String line;
			BufferedReader in = new BufferedReader( new FileReader( file ) ); 
			Deck deck = new Deck();
			
			while( ( line = in.readLine() ) != null) {
		    
				String data[] = line.split( "\\$" );
		    	deck.add( new DeckCard( data[0], data[1] ) );
		    	
		    }

		    in.close();
		    
		    return deck;

		} catch ( Exception e ) {
			
			Log.e( "Deck", "load: " + e.getMessage() );
			throw e;
			
		}
		
	}
		
	public void save( File file ) throws Exception {
				
		try {
			
			BufferedWriter out = new BufferedWriter( new FileWriter( file ) ); 
			
			for( DeckCard card : cards ) {
				
				out.write( card.sideA + "$" + card.sideB );
				out.newLine();
				
			}
			
		    out.close();

		} catch ( Exception e ) {

			Log.e( "Deck", "save: " + e.getMessage() );
			throw e;
			
		}
		
	}
	
}
