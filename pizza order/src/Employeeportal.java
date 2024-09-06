package pizzaapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employeeportal {
	public static void input() throws Exception {

		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		while (!exit) {
			System.out.println("===========================");
			System.out.println("\nEmployee Portal Menu:");
			System.out.println("===========================");
			System.out.println("1 -  View orders");
			System.out.println("2 -  Update the delivery status");
			System.out.println("3 -  Exit");
			System.out.println("===========================");
			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				vieworders();
				break;
			case 2:
				updatedeliverystatus();
				break;
			case 3:
				exit = true;
				break;
			default:
				System.out.println("Invalid choice!");
			}
		}
	}

	private static void vieworders() throws Exception {
		System.out.println("-----------------------");
		System.out.println("*****viewing tasks*****");
		System.out.println("-----------------------");
		try (Connection con = DbConnector.getConnection()) {

			String query = "SELECT * FROM orders";
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			System.out.println("-----------------------------------------------------------");
			System.out.printf("%-8s %-7s %-7s %-7s%n", "CustId", "OrderId","Emp_ID" ,"OrderStatus");
			System.out.println("-----------------------------------------------------------");
			while (rs.next()) {
				int custId = rs.getInt("cust_id");
				int OrderId = rs.getInt("order_id");
				int employeeId = rs.getInt("emp_id");
				int status = rs.getInt("order_status");
				System.out.printf("%-8s %-7s %-7s %-7s%n", custId, OrderId,employeeId, status);
			}
			System.out.println("----------------------------------------------------------");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updatedeliverystatus()throws Exception{
		try(Connection con=DbConnector.getConnection()){
			Scanner sc=new Scanner(System.in);
			System.out.println("Enter your employee_username:");
			String username=sc.next();
			int emp_id=getEmployeeId(username,con);
			String query = "Update employee set status=0 where emp_uname=?";
			String query1="Update orders set order_status=1 where emp_id=? ";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1,username);
			pstmt.executeUpdate();
			PreparedStatement pstmt1=con.prepareStatement(query1);
			pstmt1.setInt(1, emp_id);
			pstmt1.executeUpdate();
			
	}}
	private static int getEmployeeId(String username, Connection con) throws SQLException {
        String query = "SELECT emp_id FROM employee WHERE emp_uname = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("emp_id");
                }
            }
        }
        throw new SQLException("Employee not found with username: " + username);
    }
}
