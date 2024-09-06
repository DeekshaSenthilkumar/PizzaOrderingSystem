package pizzaapp;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

	public static void login() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n-----Login-----");
		System.out.print("Enter username: ");
		String username = sc.nextLine();
		
		Customerportal c = new Customerportal();
		c.userName(username);
		System.out.print("Enter password: ");
		String password = sc.nextLine();

		String role = checkCredentials(username, password);
		switch (role) {
		case "admin":
			adminPortal();
			break;
		case "employee":
			employeePortal();
			break;
		case "user":
			userPortal();
			break;
		default:
			System.out.println("Invalid role!");
		}

	}

	public static void signup() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n-----Signup-----");
		System.out.print("Enter name: ");
		String name = sc.nextLine();
		System.out.print("Enter phoneno: ");
		String phno = sc.nextLine();
		System.out.print("Enter username: ");
		String username = sc.nextLine();
		System.out.print("Enter password: ");
		String password = sc.nextLine();

		System.out.println("Signup successful for user: " + username);

		try (Connection con = DbConnector.getConnection()) {
			String sql = "INSERT INTO customer (cust_name,cust_phno,cust_uname,cust_pswd) VALUES (?, ?, ?,?)";
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				pstmt.setString(1, name);
				pstmt.setString(2, phno);
				pstmt.setString(3, username);
				pstmt.setString(4, password);
				pstmt.executeUpdate();
				System.out.println("Customer added successfully!!!");
			}
		}
		// sc.close();
	}

	private static String checkCredentials(String username, String password) {
		try (Connection con = DbConnector.getConnection()) {

			String query = "SELECT * FROM admin WHERE admin_username = ? AND admin_pswd = ?";
			try (PreparedStatement statement = con.prepareStatement(query)) {
				statement.setString(1, username);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return "admin";
					}
				}
			}

			query = "SELECT * FROM employee WHERE emp_uname = ? AND emp_pswd = ?";
			try (PreparedStatement statement = con.prepareStatement(query)) {
				statement.setString(1, username);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return "employee";
					}
				}
			}

			query = "SELECT * FROM customer WHERE cust_uname = ? AND cust_pswd = ?";
			try (PreparedStatement statement = con.prepareStatement(query)) {
				statement.setString(1, username);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return "user";
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "invalid";
	}

	private static void adminPortal() throws Exception {
		System.out.println("\n-----------------------------");
		System.out.println("***Welcome to Admin Portal***");
		System.out.println("-----------------------------");
		Adminportal ap = new Adminportal();
		ap.input();
	}

	private static void employeePortal() throws Exception {
		System.out.println("\n--------------------------------");
		System.out.println("***Welcome to Employee Portal***");
		System.out.println("--------------------------------");
		Employeeportal ep = new Employeeportal();
		ep.input();
	}

	private static void userPortal() throws Exception {
		System.out.println("\n----------------------------");
		System.out.println("***Welcome to User Portal***");
		System.out.println("----------------------------");
		Customerportal up = new Customerportal();
		up.input();
	}
}
