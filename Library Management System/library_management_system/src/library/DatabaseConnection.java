package library;
import java.sql.*;

public class DatabaseConnection {
	private static final String url = "jdbc:mysql://localhost:3306/librarydb";
	private static final String username = "root";
	private static final String password = "Tonychris454@";
	
	public static Connection getConnection()throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
		
}
