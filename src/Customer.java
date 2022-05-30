//Customer is a specialised class of User
public class Customer extends User {

	public Customer(int userID, String userName, String surname, Address address) {
		super(userID, userName, surname, address);
		
	}
	public void addToBasket() {
		
	}
	
	//These methods are used for the searching methods
	//these are placeholder so it returns null at the moment
	public String searchBooks(Genre genre, double audio) {
		
		return null;
	}
	public String searchBooks(Genre genre) {
		
		return null;
	}
	public String searchBooks(double audio) {
		
		return null;
	}
}
