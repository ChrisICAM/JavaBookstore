import java.util.ArrayList;

// a generalised user object that allows the different types of users have this object as a base
public class User {
	private int userID;
	private String userName;
	private String surname;
	private Address address;
	private ArrayList<Object> booksList;
	
	public User (int userID, String userName, String surname, Address address) {
		this.userID = userID;
		this.userName = userName;
		this.surname = surname;
		this.address = address;
		this.booksList = new ArrayList<Object>();
	}
	// a number of setter and getter methods
	public int getUserID() {
		return userID;
	}

	public String getUserName() {
		return userName;
	}

	public Address getAddress() {
		return address;
	}
	

	public ArrayList<Object> getBooksList() {
		return booksList;
	}

	public void setBooksList(ArrayList<Object> newBook) {
		this.booksList.add(newBook);
	}

	public String getSurname() {
		return surname;
	}
}
