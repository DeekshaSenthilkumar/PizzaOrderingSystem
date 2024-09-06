package pizzaapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Customerportal {
	private static String username;
	void userName(String user) {
		this.username=user;
	}
	
	String user() {
		return username;
	}
	
	static Scanner sc = new Scanner(System.in);

	public void input() throws Exception {
		try (Connection con = DbConnector.getConnection()) {
			Scanner sc = new Scanner(System.in);
			boolean exit = false;
			while (!exit) {
				System.out.println("\n*******Welcome to Restaurant Menu*******");
				System.out.println("1. Display Menu");
				System.out.println("2. Place Order");
				System.out.println("3. Generate bill");
				System.out.println("4. Order Status");
				System.out.println("5. Exit");
				System.out.print("Enter your choice: ");
				int choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case 1:
					displayMenu(con);
					break;
				case 2:
					placeOrder();
					break;
				case 3:
					generateBill(con);
					break;
				case 4:
					orderStatus(con);
				case 5:
					exit = true;
					break;
				default:
					System.out.println("Invalid choice!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void displayMenu(Connection con) throws SQLException {
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
				System.out.println("------------------------------------");

			}
		}
	}
	
	private static void placeOrder()throws SQLException{
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Enter your item_id(separated by commas if multiple orders): ");
		String order_id=sc.nextLine();
		String[] orderInfo=order_id.split(",");
		
		System.out.println("Enter the quantity for each item:");
		String[] quantities = sc.nextLine().split(",");
		if (quantities.length != orderInfo.length) {
            System.out.println("Number of quantities must match the number of items. Please try again.");
            return;
        }
		
		int cust_id = getUsername(username);
		
		int emp_id=getEmployee(cust_id);
		
		try (Connection con = DbConnector.getConnection()) {
			String sql="Insert into orders (cust_id,order_id,quantity,emp_id,order_status) values (?,?,?,?,0)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int i=0;i<orderInfo.length;i++) {
			pstmt.setInt(1, cust_id);
			pstmt.setString(2,orderInfo[i]);
			pstmt.setInt(3, Integer.parseInt(quantities[i]));
			pstmt.setInt(4,emp_id);
			pstmt.executeUpdate();  
			}
			System.out.println("Order placed successfully!");
		
		//employee status 1;
		
        }
    catch(NumberFormatException e) {
    	System.out.println("Invalid quantity format. Please enter integers for quantity.");
    }}
    
		
	private static int getEmployee(int cust_id) throws SQLException {
	    try (Connection con = DbConnector.getConnection()) {
	        String assignEmployeeQuery = "SELECT emp_id FROM employee WHERE status = 0 LIMIT 1";
	        String fixEmployeeQuery = "UPDATE employee SET status = 1 WHERE emp_id = ?";
	        //String updateOrderQuery = "UPDATE orders SET emp_id = ?, order_status = 1 WHERE order_id = ?";

	        
	        PreparedStatement pstmt = con.prepareStatement(assignEmployeeQuery);
	        ResultSet rs = pstmt.executeQuery();
	        
	        int employeeId = 0;
	        if (rs.next()) {
	            employeeId = rs.getInt("emp_id");
	            try (PreparedStatement fixStmt = con.prepareStatement(fixEmployeeQuery)) {
	                fixStmt.setInt(1, employeeId);
	                fixStmt.executeUpdate();
	            }
	        }
	        
	        return employeeId;
	    }
	}

	private static int getUsername(String username) throws SQLException {
	    try (Connection con = DbConnector.getConnection()) {
	        String sql = "SELECT cust_id FROM customer WHERE cust_uname = ?";
	        PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, username);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("cust_id");
	            }
	        }
	    }
	    throw new SQLException("Username not found: " + username);
	}
private static void generateBill(Connection con) throws SQLException {
    int total = 0;
    int cust_id = getUsername(username);
    
    String sql = "SELECT order_id,quantity from orders WHERE cust_id = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setInt(1, cust_id);
        try (ResultSet rs = pstmt.executeQuery()) {
        	System.out.println("----------------------------------------------------------------");
        	System.out.printf("%-8s %-20s %-10s %-5s%n","Item_id","Item_name","Quantity","Price");
        	System.out.println("----------------------------------------------------------------");
            while (rs.next()) {
                int item_id = rs.getInt("order_id");
               int quantity=rs.getInt("quantity");
               
                int price = getPrice(con, item_id)*quantity;
                String name=getName(con,item_id);
                //total += price;
                System.out.println("");
                total += price;
             System.out.printf("%-8s %-20s %-10s %-5s%n", item_id, name,quantity,price);
            }
            System.out.println("---------------------------------------------------------------");
            
        }
    }
    
    System.out.println("Total bill amount: " + total);
 
    	}
    private static String getName(Connection con, int item_id)throws SQLException {
	String sql="Select name from menu where id=?";
	String itemName="";
	try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setInt(1, item_id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
               itemName = rs.getString("name");
            }
        }
    }
    return itemName;
}
	


	private static int getPrice(Connection con, int item_id) throws SQLException {
    String sql = "SELECT price FROM menu WHERE id = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setInt(1, item_id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("price");
            }
        }
    }
    return 0; // If item not found or price is not retrievable
}
    private static void orderStatus(Connection con) throws SQLException {
    	Scanner sc=new Scanner(System.in);
    	System.out.println("Enter your username: ");
    	String un=sc.next();
    	int custId=getCustuname(con,un);
    	if(custId!=-1) {
    		String orderQuery = "SELECT order_status FROM orders WHERE cust_id = ?";
    		try (PreparedStatement pstmt = con.prepareStatement(orderQuery)) {
                pstmt.setInt(1, custId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int orderStatus = rs.getInt("order_status");
                    if (orderStatus == 1) {
                        System.out.println("Order delivered");
                    } else {
                        System.out.println("Order not yet delivered");
                    }
                } else {
                    System.out.println("No orders found for the customer");
                }
            }
        } else {
            System.out.println("Username not found");
        }
    }
    private static int getCustuname(Connection con, String username) throws SQLException {
        String query = "SELECT cust_id FROM customer WHERE cust_uname = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cust_id");
            }
        }
        return -1; // Return -1 if username not found
    }
    //sc.close();
}
