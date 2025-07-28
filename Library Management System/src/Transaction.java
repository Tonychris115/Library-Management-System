package library;
import java.sql.*;

public class Transaction {
	private int transaction_id;
	private int borrower_id;
	private int book_id;
	Timestamp borrow_date;
	Timestamp return_date;
	int fine;
	
	public Transaction(int transaction_id, int borrower_id, int book_id, Timestamp borrow_date, Timestamp return_date, int fine) {
		this.transaction_id = transaction_id;
		this.borrower_id = borrower_id;
		this.book_id = book_id;
		this.borrow_date = borrow_date;
		this.return_date = return_date;
		this.fine = fine;		
	}
	
	public int getTransactionId() {
		return transaction_id;
	}
	
	public int getBorrowerId() {
		return borrower_id;
	}
	
	public int getBookId() {
		return book_id;
	}
	
	public Timestamp getBorrowDate() {
		return borrow_date;
	}
	
	public Timestamp getReturnDate() {
		return borrow_date;
	}
	
	public int getFine() {
		return fine;
	}
}
