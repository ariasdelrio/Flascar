package es.guillesoft.flascar.db;

import android.util.Log;

public class DBUtil {
	
	public static String createTable( String tableName, String [] columns ) {
		
		return "create table " + tableName + "(" + commaList( columns ) + ")";
		
	}
	
	public static String primaryColumn( String name ) {
		
		return name + " integer primary key autoincrement";
		
	}
	
	public static String textColumn( String name ) {
		
		return name + " text not null";
		
	}
	
	public static String numberColumn( String name ) {
		
		return name + " integer";
		
	}

	public static String tsColumn( String name ) {
		
		return name + " timestamp";
		
	}

	public static String createView( String viewName, String select ) {
		
		return "create view " + viewName + " as " + select;
		
	}
	
	
	public static String field( String table, String column ) {
		return table + "." + column;
	}
	
	public static String fieldAs( String table, String column, String alias ) {
		return field( table, column ) + " as " + alias;
	}
	
	public static String countAs( String alias ) {
		return countAs( null, alias );
	}
	
	public static String countAs( String field, String alias ) {
		
		return "count(" + ( field == null ? "*" : field ) + ") as " + alias;
	}
	
	public static String dropView( String viewName ) {
		return drop( "view", viewName );
	}
	
	public static String dropTable( String tableName ) {
		return drop( "table", tableName );
	}
	
	
	/* SELECT */
	
	public static String select( String [] tables, String [] columns, String where, String groupBy ) {
	
		String groupByStr = groupBy == null ? "" : " group by " + groupBy;
		String columnsStr = columns == null ? "*" : commaList( columns ); 
		String whereStr = where == null ? "" : " where " + where; 
		
		return "select " + columnsStr + " from " + commaList( tables ) + whereStr + groupByStr;
		
	}
	
	public static String select( String [] tables, String [] columns, String where ) {
		
		return select( tables, columns, where, null );
		
	}
	
	public static String select( String [] tables, String where ) {
		
		return select( tables, null, where );
		
	}
	
	public static String select( String [] tables, String [] columns ) {
		
		return select( tables, columns, null, null );
		
	}

	/* JOIN */
	
	private static String join( String type, String query1, String alias1, String query2, String alias2, String on ) {
		
		return "(" + query1 + ") " + alias1 + type + " join (" + query2 + ") " + alias2 + " on (" + on + ")"; 
		
	}
	
	public static String join( String query1, String alias1, String query2, String alias2, String on ) {
		
		return join( "", query1, alias1, query2, alias2, on );
		
	}

	public static String loJoin( String query1, String alias1, String query2, String alias2, String on ) {
		
		return join( " left outer", query1, alias1, query2, alias2, on );
		
	}
	/*
	public static String loJoin( String query1, String query2, String on ) {
		
		return join( " left outer", query1, "", query2, "", on );
		
	}
*/
	/* OPERATIONS */
	
	public static String equals( String first, String second ) {
		return first + " = " + second;
	}
	
	public static String and( String first, String second ) {
		return "(" + first + ") and (" + second + ")";
	}
	
	/* PRIVATE */
	
	private static String drop( String what, String name ) {
		return "drop " + what + " if exists " + name;
	}
	
	private static String commaList( String [] elements ) {
		
		String commaList = null;
		for( String element : elements ) commaList = ( commaList == null ? "" : commaList + ", " ) + element;
		return commaList;
		
	}
	
	/* STUFF */
	
	public static void log( String title, String str ) {
		
		Log.d( title, "--- sql begin ---" );
		
		int strlen = str.length();
		int finalpos = 70;
		
		for( int initpos = 0; finalpos < strlen ; initpos += 70 ) {
			
			finalpos = initpos + 70;
			if( finalpos > strlen ) finalpos = strlen;
			Log.d( title, str.substring( initpos, finalpos ) );
			
		}
		
		Log.d( title, "--- sql end ---" );
		
	}
	
}
