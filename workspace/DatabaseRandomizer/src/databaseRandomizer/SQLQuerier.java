package databaseRandomizer;

import java.sql.*;

public class SQLQuerier {
	Connection con = null;
	Statement stmt = null;
	
	public SQLQuerier(Connection c) {
		con = c;
	}
	
	public void createTable(String query) throws SQLException {
	    Statement stmt = null;
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

}
