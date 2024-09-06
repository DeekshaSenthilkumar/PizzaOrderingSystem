package pizzaapp;

import java.util.*;

public class main {
	public static void main(String args[]) throws Exception {
		det();
	}

	public static void det() throws Exception {
		Scanner sc = new Scanner(System.in);
		boolean f = false;
		while (!f) {
			System.out.println("***WELCOME TO PIZZA UNIVERSE***");
			System.out.println("1.Login");
			System.out.println("2.Signup");
			System.out.println("3.Exit");
			System.out.print("Enter your choice:");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				Login.login();
				break;
			case 2:
				Login.signup();
				break;
			case 3:
				System.out.println("***Thankyou!!!***");
				f = true;
				break;
			default:
				System.out.println("Enter valid choice!");
			}
		}
	}
}