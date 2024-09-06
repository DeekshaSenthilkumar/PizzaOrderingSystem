package pizzaapp;

import java.sql.*;

public class DbConnector {
	static final String url = "jdbc:mysql://localhost:3306/pizza";
	static final String user = "root";
	static final String password = "Dee@0411";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

}