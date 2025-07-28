package library;
import java.sql.*;
import java.util.*;

public class Functionalities {
	List<User> users = new ArrayList<>();
	List<Book> books = new ArrayList<>();
	List<Transaction> transactions = new ArrayList<>();
	
		// Adding a User
		public void addUser(String name,String email,String password, String role){ 
			String query = "INSERT INTO users (name, email, password, role) VALUES(?,?,?,?)";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(query)){
					stmt.setString(1, name);
					stmt.setString(2, email);
					stmt.setString(3, password);
					stmt.setString(4, role);
					
					stmt.executeUpdate();
					System.out.println(role.toUpperCase() + " registered succesfully");
					
				} 
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
		
		// Checking if user already registered or not
		public boolean isUserExists(String email) {
		    String query = "SELECT COUNT(*) FROM users WHERE email = ?";

		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(query)) {
		        
		        stmt.setString(1, email);
		        ResultSet rs = stmt.executeQuery();

		        if (rs.next() && rs.getInt(1) > 0) {
		            return true; // User exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return false; // User does not exist
		}
		
	// Validating login for clerks & librarians
		public boolean login(String email, String password, String role) {
			String query = "SELECT role FROM users where email = ? AND password = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(query);){
				stmt.setString(1, email);
				stmt.setString(2, password);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					return rs.getString("Role").equals(role);
				}
						
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		
	// Validating borrowers login
		public int borrowerLogin(String email, String password) {
			String query = "SELECT user_id,role FROM users where email = ? AND password = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(query);){
				stmt.setString(1, email);
				stmt.setString(2, password);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					if(rs.getString("Role").equals("borrower")) {
						return rs.getInt("user_id");
					}
				}
						
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		
		// Getting borrower name
		public String getBorrowerName(int userId) {
			String query = "SELECT name,role FROM users where user_id = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(query);){
				stmt.setInt(1, userId);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					if(rs.getString("Role").equals("borrower")) {
						return rs.getString("Name");
					}
				}
						
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return "";
		}
		
		// View all books
		public void displayAllBooks(){ 
			books.clear();
			String query = "SELECT book_id, title, author, price, status FROM books";
			try(Connection conn = DatabaseConnection.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query)){
					
				while(rs.next()) {
					books.add(new Book(rs.getInt("Book_Id"), rs.getString("Title"), rs.getString("Author"),
							rs.getDouble("Price"), rs.getString("status")));
				}
				
				for(Book b : books) {
					System.out.println("|-------------------------------------------------------------------------------------------------------");
					System.out.println("| Book_id: " + b.getBookId()  + " | Title: " + b.getTitle() + " | Author: "
							+ b.getAuthor() + " | Price: " + b.getPrice() + " | Status: " + b.getStatus());
				}
				System.out.println("|-------------------------------------------------------------------------------------------------------");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// View only available books
		public boolean availableBooks() {
			books.clear();
			String query = "SELECT book_id, title, author, price, status FROM books WHERE status = 'Available'";
			try(Connection conn = DatabaseConnection.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query)){
					
				while(rs.next()) {
					books.add(new Book(rs.getInt("Book_Id"), rs.getString("Title"), rs.getString("Author"),
							rs.getDouble("Price"), rs.getString("Status")));
				}
				if(books.isEmpty()) {
					return false;
				}
				System.out.println("\nThe following books are available");
				for(Book b : books) {
					System.out.println("|-------------------------------------------------------------------------------------------------------");
					System.out.println("Book_id = " + b.getBookId()  + ", Title = " + b.getTitle() + ", Author = "
							+ b.getAuthor() + ", Price = " + b.getPrice() + ", Status = " + b.getStatus());
				}
				System.out.println("|-------------------------------------------------------------------------------------------------------");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return true;
		}
		
		// Check availability and if book is valid or not
		public boolean isBookAvailable(int bookId) {
		    String query = "SELECT status FROM books WHERE book_id = ?";

		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(query)) {
		        
		        stmt.setInt(1, bookId);
		        ResultSet rs = stmt.executeQuery();

		        if (rs.next()) {
		            return rs.getString("status").equalsIgnoreCase("Available");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return false; // Book does not exist or is not available
		}
		
		// Reserve books as borrower
		public void reserveBook(int borrower_id, int book_id) {
			Connection conn = null;
			PreparedStatement insertStmt = null;
			PreparedStatement updateStmt = null;
			try {
			    conn = DatabaseConnection.getConnection();

			    // Insert transaction
			    String insertQuery = "INSERT INTO transactions (borrower_id, book_id) VALUES (?, ?)";
			    insertStmt = conn.prepareStatement(insertQuery);
			    insertStmt.setInt(1, borrower_id);
			    insertStmt.setInt(2, book_id);
			    insertStmt.executeUpdate();

			    // Update book status
			    String updateQuery = "UPDATE books SET status = 'Not Available' WHERE book_id = ?";
			    updateStmt = conn.prepareStatement(updateQuery);
			    updateStmt.setInt(1, book_id);
			    updateStmt.executeUpdate();

			    System.out.println("Transaction recorded and book status updated.");
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		}
		
		// View All transactions as borrower
		public void transactionsOfBorrower(int userId) {
			transactions.clear();
	        String query = "SELECT * FROM transactions WHERE borrower_id = ?";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)){
	        	
	        	 stmt.setInt(1, userId);
	        	 ResultSet rs = stmt.executeQuery();
	        	 
	            while (rs.next()) {
	                transactions.add(new Transaction(rs.getInt("transaction_id"),rs.getInt("borrower_id"),rs.getInt("book_id"),
	                		rs.getTimestamp("borrow_date"),rs.getTimestamp("return_date"),rs.getInt("fine")));
	            }
	            
	            for (Transaction t : transactions) {
	            	System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------");
	                System.out.println("| Transaction ID: " + t.getTransactionId() +
	                                   " | Borrower ID: " + t.getBorrowerId() +
	                                   " | Book ID: " + t.getBookId() +
	                                   " | Borrow Date: " + t.getBorrowDate() +
	                                   " | Return Date: " + (t.getReturnDate() != null ? t.getReturnDate() : "Not returned") +
	                                   " | Fine: ₹" + t.getFine());
	            }
	            System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
		// Delete borrower using user-id
				public void deleteBorrower(int userId) { 
					Iterator<User> iterator = users.iterator();
					while(iterator.hasNext()) {
						User users = iterator.next();
						if(users.getUserId() == userId) {
							iterator.remove();
							break;
						}
					}
					
					String deleteQuery = "DELETE FROM users WHERE User_id = ? AND role = 'borrower'";
					try(Connection conn = DatabaseConnection.getConnection();
							PreparedStatement stmt = conn.prepareStatement(deleteQuery);) {
						stmt.setInt(1, userId);
						int affectedRows = stmt.executeUpdate();
						
						if(affectedRows > 0) {
							System.out.println("Borrower deleted sucessfully..!");
						} else {
							System.out.println("Borrower not found..!");
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}	
		
	// To get list of all borrowers
	public void fetchBorrowers() { 
		users.clear();
		
		String query = "SELECT User_id, name, email, password FROM users WHERE role = 'borrower'";
		try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query)){
				
			while(rs.next()) {
				users.add(new User(rs.getInt("User_id"),rs.getString("Name"),rs.getString("Email"),
						rs.getString("Password"), rs.getString("role")));
			}
			
			for(User u : users) {
				System.out.println("ID: " + u.getUserId() + ", Name: " + u.getName() + ", Email: " + u.getEmail());
			}				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Change status of book as Librarian
	public void changeStatusOfBook(int bookId, String status) {
		String updateQuery = "UPDATE transactions SET return_date = CURRENT_TIMESTAMP, fine = ? WHERE book_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            int fine = calculateFine(bookId);
            pstmt.setInt(1, fine);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();

            System.out.println("Book returned successfully with fine: ₹" + fine);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
		String query = "UPDATE books SET status = ? WHERE book_id = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)){
				
			stmt.setString(1, status);
			stmt.setInt(2, bookId);
			stmt.executeUpdate();
			System.out.println("Book status changed successfully !");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//calculate fine for borrower
	public int calculateFine(int bookId) {
		String query = "SELECT borrow_date FROM transactions WHERE book_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Timestamp borrowDate = rs.getTimestamp("borrow_date");
                long daysLate = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
                return (daysLate > 14) ? (int) (daysLate - 14) * 20 : 0; // ₹20 per day after 14 days
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
	}
	
	// Adding books in database
		public void addBooks(String title, String author, double price) {
			String query = "INSERT INTO books(title, author, price, status) VALUES (?,?,?,'Available')";
			try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);){
				stmt.setString(1, title);
				stmt.setString(2, author);
				stmt.setDouble(3, price);
				
				stmt.executeUpdate();
				
				System.out.println("Book added Successfully !");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Deleting book from database
		public void deleteBooks(int bookId) {
			books.removeIf(book -> book.getBookId() == bookId);
			String query = "DELETE FROM books WHERE book_id = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(query)){
				stmt.setInt(1, bookId);
				int rowsAffected = stmt.executeUpdate();
				
				if(rowsAffected > 0) {
					System.out.println("Book deleted Succesfully");
				} else {
					System.out.println("Book not found !");
				}
								
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	
	// Viewing all user as Admin
	public void viewAllUsers(){
		users.clear();
		
		String query = "SELECT User_id, name, email, password, role FROM users";
		try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query)){
				
			while(rs.next()) {
				users.add(new User(rs.getInt("User_id"),rs.getString("Name"),
										   rs.getString("Email"),rs.getString("Password"), rs.getString("Role")));
			}
			
			for(User u : users) {
				System.out.println("|------------------------------------------------------------------------");
				System.out.println("| ID: " + u.getUserId() + " | Name: " + u.getName() 
									+ " | Email: " + u.getEmail() + " | Role: " + u.getRole());
			}				
			System.out.println("|------------------------------------------------------------------------");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Deleting clerks as Admin
	public void deleteClerk(int userId) {
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			User users = iterator.next();
			if(users.getUserId() == userId) {
				iterator.remove();
				break;
			}
		}
		
		String deleteQuery = "DELETE FROM users WHERE User_id = ? AND role = 'clerk'";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(deleteQuery);) {
			stmt.setInt(1, userId);
			int affectedRows = stmt.executeUpdate();
			
			if(affectedRows > 0) {
				System.out.println("Clerk deleted sucessfully..!");
			} else {
				System.out.println("Clerk not found..!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Deleting librarians as Admin
	public void deleteLibrarian(int userId) {
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			User users = iterator.next();
			if(users.getUserId() == userId) {
				iterator.remove();
				break;
			}
		}
		
		String deleteQuery = "DELETE FROM users WHERE User_id = ? AND role = 'librarian'";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(deleteQuery);) {
			stmt.setInt(1, userId);
			int affectedRows = stmt.executeUpdate();
			
			if(affectedRows > 0) {
				System.out.println("Librarian deleted sucessfully..!");
			} else {
				System.out.println("Librarian not found..!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Borrowed books
			public void borrowedBooks() {
				transactions.clear();
		        String query = "SELECT * FROM transactions";

		        try (Connection conn = DatabaseConnection.getConnection();
		             Statement stmt = conn.createStatement();
		             ResultSet rs = stmt.executeQuery(query)) {

		            while (rs.next()) {
		                transactions.add(new Transaction(rs.getInt("transaction_id"),rs.getInt("borrower_id"),rs.getInt("book_id"),
		                		rs.getTimestamp("borrow_date"),rs.getTimestamp("return_date"),rs.getInt("fine")));
		            }
		            
		            for (Transaction t : transactions) {
		                System.out.println("Transaction ID: " + t.getTransactionId() +
		                                   ", Borrower ID: " + t.getBorrowerId() +
		                                   ", Book ID: " + t.getBookId() +
		                                   ", Borrow Date: " + t.getBorrowDate() +
		                                   ", Return Date: " + (t.getReturnDate() != null ? t.getReturnDate() : "Not returned") +
		                                   ", Fine: ₹" + t.getFine());
		            }
		            
		            System.out.println("Transactions loaded successfully.");
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
}
