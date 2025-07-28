package library;

public class Book {
	private int bookId;
	private String title;
	private String author;
	private double price;
	private String status;
	
	
	public Book(int bookId, String title, String author, double price, String status) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.price = price;
		this.status = status; 
	}
	
	public int getBookId() { 
		return bookId; 
	}
	
	public String getTitle() { 
		return title; 
	}
	
	public String getAuthor() { 
		return author; 
	}
	
	public double getPrice() { 
		return price; 
	}
	
	public String getStatus() { 
		return status; 
	}
}
