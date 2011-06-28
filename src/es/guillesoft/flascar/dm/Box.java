package es.guillesoft.flascar.dm;

public class Box {
    
	public static int BOX_COUNT = 5;
	
	private long id;
    private String name;
    private long expiration; // minutes

    public long getID() {
    	return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public long getExpiration() {
    	return expiration;
    }
    
    /* protected */
    
    protected Box( long id, String name, long expiration ) {
    	
    	this.id = id;
    	this.name = name;
    	this.expiration = expiration;
    	
    }
    
   
}