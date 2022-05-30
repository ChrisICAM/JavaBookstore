// this extends User so that it gains the methods and attributes of user
// making it more specialised version of User
public class Admin extends User {

	public Admin(int userID, String userName, String surname, Address address) {
		super(userID, userName, surname, address);
		
	}
	
	public void addBook() {
		
	}
}
