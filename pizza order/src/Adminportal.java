package pizzaapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Adminportal {
	static Scanner sc = new Scanner(System.in);

	public void input() throws Exception {
		System.out.println("=================================");
		System.out.println("1 - Employee Details");
		System.out.println("2 - Menu Details");
		System.out.println("3 - Customer Details");
		System.out.println("4 - Exit");
		System.out.println("=================================");
		System.out.print("Enter your choice:");
		int c = sc.nextInt();
		switch (c) {
		case 1:
			employeeDetails();
			System.out.println();
			break;

		case 2:
			menuDetails();
			System.out.println();
			break;

		case 3:
			customerDetails();
			System.out.println();
			break;

		case 4:
			System.out.println("******Thank you!!!******");
			break;
		default:
			System.out.println("Invalid Choice!");
		}
	}

	private static void employeeDetails() throws Exception {
		try (Connection con = DbConnector.getConnection()) {
			Scanner sc = new Scanner(System.in);
			boolean exit = false;
			while (!exit) {
				System.out.println("================================");
				System.out.println("\nEmployee Management");
				System.out.println("================================");
				System.out.println("1 - Add Employee");
				System.out.println("2 - View Employee");
				System.out.println("3 - Update Employee");
				System.out.println("4 - Delete Employee");
				System.out.println("5 - Exit");
				System.out.println("================================");
				System.out.print("Enter your choice:");
				int c = sc.nextInt();
				switch (c) {
				case 1:
					addEmployee(con, sc);
					break;
				case 2:
					viewEmployee(con);
					break;
				case 3:
					updateEmployee(con, sc);
					break;
				case 4:
					deleteEmployee(con, sc);
					break;
				case 5:
					exit = true;
					break;
				default:
					System.out.println("Invalid Choice!!!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// add emp
	private static void addEmployee(Connection con, Scanner sc) throws SQLException {
		System.out.print("Enter employee name: ");
		String name = sc.next();
		System.out.print("Enter employee age: ");
		int age = sc.nextInt();
		System.out.print("Enter employee username: ");
		String username = sc.next();
		System.out.print("Enter employee pswd: ");
		String pswd = sc.next();

		String sql = "INSERT INTO employee (emp_name, age,emp_uname,emp_pswd) VALUES (?, ?,?,?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setInt(2, age);
			pstmt.setString(3, username);
			pstmt.setString(4, pswd);
			pstmt.executeUpdate();
			System.out.println("Employee added successfully!!!");
		}
	}

	// viewemp
	private static void viewEmployee(Connection con) throws SQLException {
		String sql = "SELECT * FROM employee";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			if (!rs.isBeforeFirst()) {
				System.out.println("No employees found.");
			} else {
				System.out.println("--------------------------------------------------------------------");
				System.out.printf("%-5s %-15s %-5s %-15s %-10s%n", "Id", "Name", "Age", "Username", "Password");
				System.out.println("--------------------------------------------------------------------");
				while (rs.next()) {
					int id = rs.getInt("emp_id");
					String name = rs.getString("emp_name");
					int age = rs.getInt("age");
					String username = rs.getString("emp_uname");
					String pswd = rs.getString("emp_pswd");
					System.out.printf("%-5s %-15s %-5s %-15s %-10s%n", id, name, age, username, pswd);
				}
				System.out.println("--------------------------------------------------------------------");
			}
		}
	}

	// updateemp
	private static void updateEmployee(Connection con, Scanner sc) throws SQLException {
		System.out.print("Enter employee ID to update: ");
		int id = sc.nextInt();
		sc.nextLine(); // consume newline

		System.out.println("Enter new name: ");
		String newName = sc.nextLine();
		System.out.println("Enter new age: ");
		int newAge = sc.nextInt();

		String sql = "UPDATE employee SET emp_name = ?, age = ? WHERE emp_id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, newName);
			pstmt.setInt(2, newAge);
			pstmt.setInt(3, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Employee details updated successfully!!!");
			} else {
				System.out.println("No employee found with ID " + id);
			}
		}
	}

	// deleteemp
	private static void deleteEmployee(Connection con, Scanner sc) throws SQLException {
		System.out.print("Enter employee ID to delete: ");
		int id = sc.nextInt();

		String sql = "DELETE FROM employee WHERE emp_id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Employee deleted successfully!!!");
			} else {
				System.out.println("No employee found with ID " + id);
			}
		}
	}

	// MENU DETAILS
	private static void menuDetails() throws Exception {
		try (Connection con = DbConnector.getConnection()) {
			Scanner sc = new Scanner(System.in);
			boolean exit = false;
			while (!exit) {
				System.out.println("================================");
				System.out.println("******Menu Item Management******");
				System.out.println("================================");
				System.out.println("1 - Add Item");
				System.out.println("2 - View Item");
				System.out.println("3 - Update Item");
				System.out.println("4 - Delete Item");
				System.out.println("5 - Exit");
				System.out.println("================================");
				System.out.print("Enter your choice:");
				int c = sc.nextInt();
				switch (c) {
				case 1:
					addItem(con, sc);
					break;
				case 2:
					viewItem(con);
					break;
				case 3:
					updateItem(con, sc);
					break;
				case 4:
					deleteItem(con, sc);
					break;
				case 5:
					exit = true;
					break;
				default:
					System.out.println("Invalid Choice!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// additem
	private static void addItem(Connection con, Scanner sc) throws SQLException {
		System.out.println("Adding New Menu Item:");
		System.out.print("Enter item name: ");
		String name = sc.next();
		System.out.print("Enter item price: ");
		double price = sc.nextDouble();

		String sql = "INSERT INTO menu (name, price) VALUES (?, ?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setDouble(2, price);
			pstmt.executeUpdate();
			System.out.println("Menu item added successfully!!!");
		}
	}

	// viewitem
	private static void viewItem(Connection con) throws SQLException {
		System.out.println("\nMenu:");
		String sql = "SELECT * FROM menu";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			if (!rs.isBeforeFirst()) {
				System.out.println("No menu items found.");
			} else {
				System.out.println("-------------------------------------");
				System.out.printf("%-5s %-25s %-7s%n", "Id", "Name", "Price");
				System.out.println("-------------------------------------");

				while (rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					double price = rs.getDouble("price");
					System.out.printf("%-5s %-25s %-7s%n", id, name, price);
				}
				System.out.println("-------------------------------------");

			}
		}
	}

//updateitem
	private static void updateItem(Connection con, Scanner sc) throws SQLException {
		System.out.println("\n*******Updating Menu Item******");
		System.out.println("Enter item ID to update: ");
		int id = sc.nextInt();
		sc.nextLine(); // consume newline

		System.out.println("Enter new name: ");
		String newName = sc.nextLine();
		System.out.println("Enter new price: ");
		double newPrice = sc.nextDouble();

		String sql = "UPDATE menu SET name = ?, price = ? WHERE id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, newName);
			pstmt.setDouble(2, newPrice);
			pstmt.setInt(3, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Menu item updated successfully!!!");
			} else {
				System.out.println("No menu item found with ID " + id);
			}
		}
	}

	// deleteitem
	private static void deleteItem(Connection con, Scanner sc) throws SQLException {
		System.out.println("\nDeleting Menu Item:");
		System.out.print("Enter item ID to delete: ");
		int id = sc.nextInt();

		String sql = "DELETE FROM menu WHERE id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Menu item deleted successfully!!!");
			} else {
				System.out.println("No menu item found with ID " + id);
			}
		}
	}

	// CUSTOMER
	private static void customerDetails() throws Exception {
		try (Connection con = DbConnector.getConnection()) {

			Scanner sc = new Scanner(System.in);
			boolean exit = false;
			while (!exit) {
				System.out.println("========================================");
				System.out.println("\n******Customer Management System******");
				System.out.println("========================================");
				System.out.println("1. Add Customer");
				System.out.println("2. View Customers");
				System.out.println("3. Update Customer");
				System.out.println("4. Delete Customer");
				System.out.println("5. Exit");
				System.out.println("========================================");
				System.out.println("Enter your choice: ");
				int choice = sc.nextInt();
				sc.nextLine(); // consume newline

				switch (choice) {
				case 1:
					addCustomer(con, sc);
					break;
				case 2:
					viewCustomers(con);
					break;
				case 3:
					updateCustomer(con, sc);
					break;
				case 4:
					deleteCustomer(con, sc);
					break;
				case 5:
					exit = true;
					break;
				default:
					System.out.println("Invalid choice!(Please enter a number between 1 and 5)");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// addcust
	private static void addCustomer(Connection con, Scanner sc) throws SQLException {
		System.out.println("\n******Adding New Customer*******");
		System.out.print("Enter customer name: ");
		String name = sc.nextLine();
		System.out.println("Enter customer phone number: ");
		String phone = sc.nextLine();
		System.out.println("Enter customer uname: ");
		String uname = sc.nextLine();
		System.out.println("Enter customer pswd: ");
		String pswd = sc.nextLine();

		String sql = "INSERT INTO customer (cust_name,cust_phno,cust_uname,cust_pswd) VALUES (?, ?, ?,?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, uname);
			pstmt.setString(4, pswd);
			pstmt.executeUpdate();
			System.out.println("Customer added successfully!!!");
		}
	}

	// viewcust
	private static void viewCustomers(Connection con) throws SQLException {
		System.out.println("\n******List of Customers******");
		String sql = "SELECT * FROM customer";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			if (!rs.isBeforeFirst()) {
				System.out.println("No customers found.");
			} else {
				System.out.println("------------------------------------------------------------------------");
				System.out.printf("%-5s %-15s %-12s %-15s %-7s%n", "Id", "Name", "Phoneno", "Username", "Password");
				System.out.println("------------------------------------------------------------------------");
				while (rs.next()) {
					int id = rs.getInt("cust_id");
					String name = rs.getString("cust_name");
					String phone = rs.getString("cust_phno");
					String uname = rs.getString("cust_uname");
					String pswd = rs.getString("cust_pswd");
					System.out.printf("%-5s %-15s %-12s %-15s %-7s%n", id, name, phone, uname, pswd);
				}
				System.out.println("------------------------------------------------------------------------");

			}
		}
	}

	// update
	private static void updateCustomer(Connection con, Scanner sc) throws SQLException {
		System.out.println("\n*******Updating Customer Details******");
		System.out.println("Enter customer ID to update: ");
		int id = sc.nextInt();
		sc.nextLine(); // consume newline

		System.out.println("Enter new name: ");
		String newName = sc.nextLine();
		System.out.println("Enter new phone number: ");
		String newPhone = sc.nextLine();
		System.out.println("Enter new user name: ");
		String newUname = sc.nextLine();
		System.out.println("Enter new password: ");
		String newPswd = sc.nextLine();

		String sql = "UPDATE customer SET cust_name = ?, cust_phno = ?,cust_uname=?,cust_pswd=? WHERE cust_id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, newName);
			pstmt.setString(2, newPhone);
			pstmt.setString(3, newUname);
			pstmt.setString(4, newPswd);
			pstmt.setInt(4, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Customer details updated successfully!!!");
			} else {
				System.out.println("No customer found with ID " + id);
			}
		}
	}

	private static void deleteCustomer(Connection con, Scanner sc) throws SQLException {
		System.out.println("******Deleting Customer******");
		System.out.println("Enter customer ID to delete: ");
		int id = sc.nextInt();

		String sql = "DELETE FROM customer WHERE cust_id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Customer deleted successfully!!!");
			} else {
				System.out.println("No customer found with ID " + id);
			}
		}
	}

}
