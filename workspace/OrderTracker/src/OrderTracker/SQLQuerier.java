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
	
	public boolean searchFor(String value, String column, String table) {
		
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

}
