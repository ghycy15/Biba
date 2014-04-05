package BibaMain;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class DBconnection {
	public static Connection DBconnection() throws SQLException{
	    System.out.print("Connecting to the database...");
	    System.out.flush();
	    System.out.println("Connecting...");
	    // Open an OracleDataSource and get a connection
	    OracleDataSource ods = new OracleDataSource();
	    ods.setURL("jdbc:oracle:thin:@claros.cs.purdue.edu:1524:strep");
	    ods.setUser("li1428");
	    ods.setPassword("vTiWwi9j");
	    Connection conn = ods.getConnection();
	    System.out.println("connected.");
	    
	    return conn;
	}
}
