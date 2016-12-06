package OrderTracker;

import java.sql.*;

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
	
	public boolean searchFor(String table, String column, String value) {
		
		boolean exists = false;
		
		try {
	        stmt = con.createStatement();
			ResultSet rs;
			String query = "select * from " + table;
			
			rs = stmt.executeQuery(query);
			
			while( rs.next() ) {
				if( rs.getString(column).equals(value)) {
					exists = true;
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
	
	public boolean searchFor(String table, String cols[], String vals[]) {
		boolean exists = false;
		
		try {
	        stmt = con.createStatement();
			ResultSet rs;
			
			// create the query
			String query = "select " + cols[0];
			for(int i = 1; i < cols.length; i++){
				query = query + "," + cols[i];
			}
			query = query + " from " + table;
			
			// execute the query in the table
			rs = stmt.executeQuery(query);
			
			while(rs.next() && !exists) {
				exists = searchForRecurse(rs, cols, vals, 0);
			}
			
			rs = stmt.executeQuery(query);
			
			rs.close();
		}
		catch (SQLException e) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			exists = false;
		}
		
		return exists;
	}
	
	// check each column of a row for an sql table and sees if it matches a set of values.
	public boolean searchForRecurse(ResultSet rs, String cols[], String vals[], int size) {
		boolean correct = false;
		
		// check if this level's value exists in this level's column
		try {
			if(size < cols.length) {
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
	/**/
}
