import java.util.Date;
//I made this class as an easy way to store the logs, an easier way to put it into a list
public class Logs {
	
	private int userID;
	private String postcode;
	private int ISBN;
	private double price;
	private int quantity;
	private String choice;
	private String method;
	private Date date;
	
	public Logs(int userID, String postcode, int ISBN, double price, int quantity, String choice, String method, Date date) {
		this.userID = userID;
		this.postcode = postcode;
		this.ISBN = ISBN;
		this.price = price;
		this.quantity = quantity;
		this.choice = choice;
		this.method = method;
		this.date = date;
	}
	// getter methods
	
	public int getUserID() {
		return userID;
	}

	public String getPostcode() {
		return postcode;
	}

	public int getISBN() {
		return ISBN;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getChoice() {
		return choice;
	}

	public String getMethod() {
		return method;
	}

	public Date getDate() {
		return date;
	}
}
