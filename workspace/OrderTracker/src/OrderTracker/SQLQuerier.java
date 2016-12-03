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
	
	public void searchFor(String value, String column, String table) {
		
	}

}
