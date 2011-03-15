package es.guillesoft.flascar.dm;

public class Box {
    
	public static int BOX_COUNT = 5;
	
	private long id;
    private String name;
    private String expiration;

    public long getID() {
    	return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getExpiration() {
    	return expiration;
    }
    
    private Box( long id, String name, String expiration ) {
    	
    	this.id = id;
    	this.name = name;
    	this.expiration = expiration;
    	
    }
    
   
}