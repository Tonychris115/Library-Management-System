package library;

import java.util.*;
import java.sql.*;

public class Main {
	
	//For taking inputs
	@SuppressWarnings("resource")
	public static int getInput(int min, int max){    
        String choice;
        Scanner input = new Scanner(System.in);        
        
        while(true) {
            System.out.println("\nEnter Choice : ");
            choice = input.next();

            if((choice.matches("\\d+")) && (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max)) {
                return Integer.parseInt(choice);
            }
            
            else {
            	System.out.println("\nInvalid Choice.... Choose a valid Option !");
            }           
        }  
    }
	
	// Main method
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Functionalities fun = new Functionalities();
		
		try {
			// Getting connection with database
			Scanner sc = new Scanner(System.in); 
			Connection conn = DatabaseConnection.getConnection();;
			if(conn == null) {
				return; 
			}
			
			while(true) { // Home page
				System.out.println("\n-------------------------------------------------------");
	            System.out.println("\tWelcome to Library Management System");
	            System.out.println("-------------------------------------------------------");
	            
	            System.out.println("Choose any of the following options\n");
	            System.out.println("1. Login/register as Borrower");
	            System.out.println("2. Login as Others");
	            System.out.println("3. Exit"); // Exit loop 
	            
	            System.out.println("-------------------------------------------------------\n");
	            
				int value = getInput(0,4);

				if(value == 1) { // Login / register as borrower
					while(true) {
						System.out.println("\n-------------------------------------------------------");
			            System.out.println("\tWelcome to Borrower's page");
			            System.out.println("-------------------------------------------------------");
			            
			            System.out.println("The following options are available\n");
			            System.out.println("1. Login as Borrower");
			            System.out.println("2. Register as Borrower");
			            System.out.println("3. Back to main page");
			            
			            System.out.println("-------------------------------------------------------\n");
			            
			            int choice = getInput(0,4);			      
			            if(choice == 1){ // Login as borrower
			            	System.out.println("\nEnter Email-Id : ");
			            	String username = sc.nextLine();
			            	System.out.println("\nEnter Password : ");
			            	String password = sc.nextLine();		            
			            	int userId = fun.borrowerLogin(username, password);
			            	if(userId > 0) {
			            		while(true) {
			            			String name = fun.getBorrowerName(userId);
			            			System.out.println("\n-------------------------------------------------------");
						            System.out.println("\tWelcome " + name);
						            System.out.println("-------------------------------------------------------");
						            
						            System.out.println("The following options are available\n");
						            System.out.println("1. View Books");
						            System.out.println("2. Reserve Books");
						            System.out.println("3. View Transaction History");
						            System.out.println("4. Logout");
						            
						            System.out.println("-------------------------------------------------------\n");
						            int val = getInput(0, 5);
						            if(val == 1) { // Displaying all books
						            	fun.displayAllBooks();
						            }
						            else if(val == 2) {		// Displaying available books
						            	if(!fun.availableBooks()) {
						            		System.out.println("Sorry....... no books are available right now !");
						            		break;
						            	} 
						            	System.out.println("\nEnter Book_Id to reserve");
						            	int c = sc.nextInt();
						            	if(fun.isBookAvailable(c)) {
						            		fun.reserveBook(userId, c);
						            	} else {
						            		System.out.println("\nBook is not available or doesn't exist !");
						            	}						            						          						            	
						            }
						            else if(val == 3) {		// Transactions list of borrower		   
						            	fun.transactionsOfBorrower(userId);
						            }					             
						            else { // Logout of borrower
						            	System.out.println("\nLogged out successfully............!");
						            	break;
						            }	
						       }	     
			            	} else {
			            		System.out.println("\nInvalid Credentials !");
			            		break;
			            	}
			            }
			            else if(choice == 2){ // Register as borrower
			            	System.out.println("Enter Name");
			            	String n = sc.nextLine();
			            	System.out.println("Enter Email");
			            	String em = sc.nextLine();
			            	if(fun.isUserExists(em)){
			            		System.out.println("Email is registered with another user! Please use another email..");
			            	} else {
			            		System.out.println("Create password");
				            	String p = sc.nextLine();
				            	fun.addUser(n,em,p,"borrower");	
			            	}	            	
			            }
			            else if(choice == 3){
			            	break;
			            }		       			     
					}
				}
				
				else if(value == 2) { // Login as admin / librarian / clerk
					while(true) {
						System.out.println("\n-------------------------------------------------------");
			            System.out.println("\tWelcome to Other user's login page");
			            System.out.println("-------------------------------------------------------");
			            
			            System.out.println("The following options are available\n");
			            System.out.println("1. Login as Clerk");
			            System.out.println("2. Login as Librarian");
			            System.out.println("3. Login as Admin");
			            System.out.println("4. Back to main page");
			            
			            System.out.println("-------------------------------------------------------\n");
			            
			            int choice = getInput(0,5);			      
			            if(choice == 1){ // Login as clerk
			            	System.out.println("\nEnter username : ");
			            	String username = sc.nextLine();
			            	System.out.println("\nEnter password : ");
			            	String password = sc.nextLine();
			            	if(fun.login(username, password, "clerk")) {
			            		while(true) {
			            			System.out.println("\n-------------------------------------------------------");
						            System.out.println("\tWelcome Clerk " + " ");
						            System.out.println("-------------------------------------------------------");
						            
						            System.out.println("The following options are available\n");
						            System.out.println("1. Add Borrower");
						            System.out.println("2. Delete Borrower");
						            System.out.println("3. View Borrowers");
						            System.out.println("4. View Books");
						            System.out.println("5. Change status of Book");
						            System.out.println("6. Logout");
						            
						            System.out.println("-------------------------------------------------------\n");
						            int val = getInput(0, 7);
						            if(val == 1) {					          
						            	System.out.println("Enter Borrower Name");
						            	String n = sc.next();
						            	System.out.println("Enter Borrower Email");
						            	String em = sc.next();
						            	System.out.println("Create password for Borrower");
						            	String p = sc.next();
						            	fun.addUser(n,em,p,"borrower");					          
						            }
						            else if(val == 2) {
						            	System.out.println("Enter Borrower Id delete");
						            	int c = sc.nextInt();
						            	fun.deleteBorrower(c);						            	
						            }
						            else if(val == 3) {
						            	fun.fetchBorrowers();					          
						            }
						            else if(val == 4) {
						            	fun.displayAllBooks();
						            }
						            else if(val == 5) {
						            	System.out.println("Enter Book Id to change its status");
						            	int c = sc.nextInt();
						            	System.out.println("Enter status of Book");
						            	String status = sc.next();					      
						            	fun.changeStatusOfBook(c, status);
						            } 
						            else {
						            	break;
						            }
			            		}		        
			            	} else {
			            		System.out.println("Invalid Credentials"); // If username or password error
			            	}			         
			            }
			            else if(choice == 2){ // Login as librarian
			            	System.out.println("\nEnter username : ");
			            	String username = sc.next();
			            	System.out.println("\nEnter password : ");
			            	String password = sc.next();
			            	if(fun.login(username, password, "librarian")) {
			            		while(true) {
			            			System.out.println("\n-------------------------------------------------------");
						            System.out.println("\tWelcome Librarian " + " ");
						            System.out.println("-------------------------------------------------------");
						            
						            System.out.println("The following options are available\n");
						            System.out.println("1. Add Borrower");
						            System.out.println("2. Delete Borrower");
						            System.out.println("3. View Borrowers");
						            System.out.println("4. View Books");
						            System.out.println("5. Add Books");
						            System.out.println("6. Delete Books");					            
						            System.out.println("7. Logout");
						            
						            System.out.println("-------------------------------------------------------\n");
						            int val = getInput(0, 8);
						            if(val == 1) {
						            	System.out.println("Enter Borrower Name");
						            	String n = sc.next();
						            	System.out.println("Enter Borrower Email");
						            	String em = sc.next();
						            	System.out.println("Create password for Borrower");
						            	String p = sc.next();
						            	fun.addUser(n,em,p,"borrower");
						            }
						            else if(val == 2) {
						            	System.out.println("Enter Borrower Id delete");
						            	int c = sc.nextInt();
						            	fun.deleteBorrower(c);	
						            }
						            else if(val == 3) {
						            	fun.fetchBorrowers();
						            }
						            else if(val == 4) {
						            	fun.displayAllBooks();
						            }
						            else if(val == 5) {	
						            	sc.nextLine();
						            	System.out.println("Enter Book Title :");
						            	String title = sc.nextLine();
						            	System.out.println("Enter Author Name :");
						            	String author = sc.nextLine();
						            	System.out.println("Enter Book Price :");
						            	while (!sc.hasNextDouble()) { //Prevents InputMismatchException for price
						                    System.out.println("Invalid input. Please enter a valid price:");
						                    sc.next(); // Consume invalid input
						                }
						                double price = sc.nextDouble();
						                sc.nextLine();
						            	fun.addBooks(title, author, price);
						            }
						            else if(val == 6) {
						            	System.out.println("Enter Book Id to delete :");
						            	int c = sc.nextInt();
						            	fun.deleteBooks(c);	
						            }
						            else {
						            	break;
						            }
			            		}
			            		
			            	} else {
			            		System.out.println("Invalid Credentials"); // If username or password error
			            	}			       
			            }
			            else if(choice == 3){ // Login as admin
			            	System.out.println("\nEnter username : ");
			            	String username = sc.next();
			            	System.out.println("\nEnter password : ");
			            	String password = sc.next();
			            	if(username.equals("admin") && password.equals("admin")) {
			            		while(true) {
			            			System.out.println("\n-------------------------------------------------------");
						            System.out.println("\tWelcome Admin " + " ");
						            System.out.println("-------------------------------------------------------");
						            
						            System.out.println("The following options are available\n");
						            System.out.println("1. View All Users");
						            System.out.println("2. View Books");
						            System.out.println("3. Add Book");
						            System.out.println("4. Delete Book");
						            System.out.println("5. Add Borrower");
						            System.out.println("6. Delete Borrower");
						            System.out.println("7. Add Clerk");
						            System.out.println("8. Delete Clerk");
						            System.out.println("9. Add Librarian");
						            System.out.println("10. Delete Librarian");						            				            
						            System.out.println("11. Logout");
						            System.out.println("12. View Transactions");
						            
						            System.out.println("-------------------------------------------------------\n");
						            int val = getInput(0, 13);
						            if(val == 1) { // Display all users
						            	fun.viewAllUsers();
						            }
						            else if(val == 2) { // Display all books
						            	fun.displayAllBooks();
						            }
						            else if(val == 3) { // Add book						            	
						            	System.out.println("Enter Book Title :");
						            	String title = sc.next();
						            	System.out.println("Enter Author Name :");
						            	String author = sc.next();
						            	System.out.println("Enter Book Price :");
						            	double price = sc.nextDouble();
						            	fun.addBooks(title, author, price);
						            }
						            else if(val == 4) { // Delete book
						            	System.out.println("Enter Book Id to delete :");
						            	int c = sc.nextInt();
						            	fun.deleteBooks(c);					         
						            }
						            else if(val == 5) { // Add borrower
						            	System.out.println("Enter Borrower Name");
						            	String n = sc.next();
						            	System.out.println("Enter Borrower Email");
						            	String em = sc.next();
						            	System.out.println("Create password for Borrower");
						            	String p = sc.next();
						            	fun.addUser(n,em,p,"borrower");
						            }
						            else if(val == 6) {// Delete borrower
						            	System.out.println("Enter Borrower Id delete");
						            	int c = sc.nextInt();
						            	fun.deleteBorrower(c);
						            }
						            else if(val == 7) { // Creating clerk account
						            	System.out.println("Enter Clerk Name");
						            	String n = sc.next();
						            	System.out.println("Enter Clerk Email");
						            	String em = sc.next();
						            	System.out.println("Create password for Clerk");
						            	String p = sc.next();
						            	fun.addUser(n,em,p,"clerk");
						            }
						            else if(val == 8) { // Deleting clerk account
						            	System.out.println("Enter Clerk Id to delete");
						            	int c = sc.nextInt();
						            	fun.deleteClerk(c);
						            }
						            else if(val == 9) { // Creating Librarian account
						            	System.out.println("Enter Librarian Name");
						            	String n = sc.next();
						            	System.out.println("Enter Librarian Email");
						            	String em = sc.next();
						            	System.out.println("Create password for Librarian");
						            	String p = sc.next();
						            	fun.addUser(n,em,p,"librarian");
						            }
						            else if(val == 10) { // Deleting Librarian account
						            	System.out.println("Enter Librarian Id to delete");
						            	int c = sc.nextInt();
						            	fun.deleteLibrarian(c);
						            }
						            else if(val == 12) {
						            	fun.borrowedBooks();
						            }
						            else {
						            	break;
						            }
			            		}			            		
			            	} else {
			            		System.out.println("Invalid Credentials"); // If username or password error
			            	}			       	
			            }
			            else if(choice == 4){
			            	break;
			            }		 	
					}
				}
				
				else if(value == 3) { // Exit program
					System.out.println("\nExiting............!");
					break;
				} 
			}
									
			sc.close();
		}
		catch(Exception e) {
			System.out.println("Unable to connect to Database " + e.toString());
		}
	}
}
