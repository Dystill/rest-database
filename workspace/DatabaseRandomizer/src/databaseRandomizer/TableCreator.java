package databaseRandomizer;

import java.sql.*;

public class TableCreator {
	
	public TableCreator(){
		
	}

	public static void main(String[] args) throws SQLException {
		
		String dbName = "Restaurant";
		Connection con = null;

		// create or connect to the sqlite file
		try {
			String url = "jdbc:sqlite:" + dbName + ".db";
			con = DriverManager.getConnection(url);
		}
		catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.out.println("Need SQLite JDBC Drivers.");
		}
	    System.out.println("Opened database successfully.");
		
	    // create object of custom class with sql functions
		SQLQuerier sql = new SQLQuerier(con);
		
		// strings to add tables
		String Residences;
		Residences = "create table Residences(" +
				"zipcode int primary key, " + 
				"city varchar(30), " +
				"state varchar(2)" +
				")";
		sql.createTable(Residences);
		

	}

}
