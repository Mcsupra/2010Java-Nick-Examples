package ConnectionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	public Connection conn;
	
	public Connection createConnection() throws SQLException {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/?", "postgres", "password");
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Caught SQL exception on jdbc connection");
			return null;
		}
		
		
		
	}
}
