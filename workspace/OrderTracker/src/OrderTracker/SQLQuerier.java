package OrderTracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLQuerier {
	Connection con = null;
	Statement stmt = null;
	
	public SQLQuerier(String dbName) {
		// create or connect to the sqlite file
		try {
			String url = "jdbc:sqlite:" + dbName + ".db";
			con = DriverManager.getConnection(url);
		}
		catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.out.println("Need SQLite JDBC Drivers.");
		}
	}
	
	public void createTable(String query) throws SQLException {
		
	    try {
	        stmt = con.createStatement();
	        stmt.executeUpdate(query);
	    }
	    catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	        if (stmt != null) { stmt.close(); }
	    }
		System.out.println("Created Table.");
	}
	
	// searches for a single value in a single column of the specified table
	// returns true if the valeu exists
	public boolean searchFor(String table, String column, String value) {
		
		// flag for if the value was found
		boolean exists = false;
		
		try {
			// create statement
	        stmt = con.createStatement();
			ResultSet rs;
			
			// make a query for the entire table
			String query = "select * from " + table;
			
			rs = stmt.executeQuery(query);
			
			// searches for the value in the column until it is found
			while( rs.next() && !exists) {
				if( rs.getString(column).equals(value)) {
					exists = true;	// set flag to true if the value was found.
				}
			}
			rs.close();
		}
		catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			exists = false;
		}
		
		return exists;
	}
	
	// searches the specified sql table for a row that matches the array of values
	// in correspondence to the array of columns
	// returns true if the row exists.
	public boolean searchFor(String table, String cols[], String vals[]) {
		boolean exists = false;
		
		try {
	        stmt = con.createStatement();
			ResultSet rs;
			
			// create a query for all of the columns specified from the table specified
			String query = "select " + cols[0];
			for(int i = 1; i < cols.length; i++){
				query = query + "," + cols[i];
			}
			query = query + " from " + table;
			
			// execute the query in the table
			rs = stmt.executeQuery(query);

			// searches for the row until there's a match
			while(rs.next() && !exists) {
				// call recursive search function
				exists = searchForRecurse(rs, cols, vals, 0);
			}
			
			rs.close();
		}
		catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			exists = false;
		}
		
		return exists;
	}
	
	// check each column of a row for an sql table and sees if it matches a set of values.
	private boolean searchForRecurse(ResultSet rs, String cols[], String vals[], int size) {
		boolean correct = false;
		
		// check if this level's value exists in this level's column
		try {
			// keep recursing for each column
			if(size < cols.length) {
				// sees if the value matches
				if(rs.getString(cols[size]).equals(vals[size])) {
					if(size == (cols.length - 1)) {
						// if it does exist and it's the last column, return true.
						correct = true;
					}
					else {
						// if it does exists, go to the next recursion level
						correct = searchForRecurse(rs, cols, vals, size + 1);
					}
				}
				else {
					// if it does not exists, return false
					correct = false;
				}
			}
		}
		catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			correct = false;
		}
		
		return correct;
	}
	
	// return a string array containing a list of the specified column
	public String[] getListOf(String table, String column) {
		
		// initialize an arraylist to hold the string values
		List<String> list = new ArrayList<String>();
		
		try {
			// create statement
	        stmt = con.createStatement();
			ResultSet rs;
			
			// create a query to get the desired column from the table
			String query = "select " + column + " from " + table;
			
			// execute the query
			rs = stmt.executeQuery(query);
			
			// go through the column to add each string to the arraylist
			while(rs.next()) {
				list.add(rs.getString(column));
			}
			
			// close the result set
			rs.close();
		}
		catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		// convert the arraylist to a string array
		return list.toArray(new String[list.size()]);
	}
	
}
