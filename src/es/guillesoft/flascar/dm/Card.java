package es.guillesoft.flascar.dm;

import es.guillesoft.flascar.dm.Cardfile.Sense;
import android.util.Log;

public class Card {

    private CardCollection parent;
    private Cardfile cardfile;

    private long id;
    private long box_a;
    private long box_b;
    private String sideA;
    private String sideB;
    private String lastCheckedA;    
    private String lastCheckedB;   
        
    public Cardfile getCardfile() {
    	return cardfile;
    }
    
    public long getID() {
    	return id;
    }
    
    public String getSideA() {
    	return sideA;
    }
    
    public String getSideB() {
    	return sideB;
    }
    
    public void setSideA( String sideA ) {
    	this.sideA = sideA;
    }
    
    public void setSideB( String sideB ) {
    	this.sideB = sideB;
    }
    
    public String getLastCheckedA() {
    	return lastCheckedA;
    }
    
    public String getLastCheckedB() {
    	return lastCheckedB;
    }
    
    public long getBoxA() {
    	return box_a;
    }
    
    public long getBoxB() {
    	return box_b;
    }
    
    public void setBoxA( long boxA ) {
    	this.box_a = boxA;
    }
    
    public void setBoxB( long boxB ) {
    	this.box_b = boxB;
    }
        
    public void setLastCheckedA( String lastCheckedA ) {
    	this.lastCheckedA = lastCheckedA;
    }
    
    public void setLastCheckedB( String lastCheckedB ) {
    	this.lastCheckedB = lastCheckedB;
    }
    
    /* protected */

    protected Card( CardCollection parent, Cardfile cardfile, long id, long box_a, long box_b, String sideA, String sideB, String lastCheckedA, String lastCheckedB ) {
    	
    	this.parent = parent;
    	this.cardfile = cardfile;
    	this.id = id;
    	this.box_a = box_a;
    	this.box_b = box_b;
    	this.sideA = sideA;
    	this.sideB = sideB;
    	this.lastCheckedA = lastCheckedA;
    	this.lastCheckedB = lastCheckedB;

    }

    public boolean climbUp( Sense sense ) {
		 
    	Log.d( getClass().getSimpleName(), "climb up " + id + " - " + sense );

    	long boxID = sense == Sense.AB ? box_a : box_b;
    	long newBoxID;
    	
    	if( boxID < 1 || boxID >= Box.BOX_COUNT ) {
    		
    		Log.d( getClass().getSimpleName(), "already on top" );
    		newBoxID = Box.BOX_COUNT;
    		return true;
    		
    	}
    	else newBoxID = boxID + 1;
    	
    	Log.d( getClass().getSimpleName(), "climb " + boxID + " -> " + newBoxID );
    	return parent.updateCard( this, newBoxID, sense );
    			
	}
    	
	public boolean climbDown( Sense sense ) {
		 
		Log.d( getClass().getSimpleName(), "climb down " + id + " - " + sense );
		
		long boxID = sense == Sense.AB ? box_a : box_b;
    	
    	Log.d( getClass().getSimpleName(), "climb " + boxID + " -> " + 1 );
    	return parent.updateCard( this, 1, sense );
				
	}
	
}
