//Specialised version of Book that handles only paperback so it gets more attributes
public class Paperback extends Book {
	private int noOfPages;
	private PaperCondition bookCondition;
	
	public Paperback(int bookISBN, String bookTitle, Language language, Genre genre, String date, double price, int quantity, int noOfPages, PaperCondition bookCondition) {
		super(bookISBN, bookTitle, language, genre, date, price, quantity);
		// it is a super class of the book class which initialises its other attributes
			this.noOfPages = noOfPages;
			this.bookCondition = bookCondition;
		
	}
	// getter methods
	public int getNoOfPages() {
		return noOfPages;
	}
	
	public String getAdditionalInfo1S() {
		String a = Integer.toString(noOfPages);
		return a;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}

	public PaperCondition getBookCondition() {
		return bookCondition;
	}
	// Overriding the previous functions to have the specific function within AudioBook
	public String getAdditionalInfo2S() {
		String a = bookCondition.toString();
		return a;
	}
	// setter method
	public void setBookCondition(PaperCondition bookCondition) {
		this.bookCondition = bookCondition;
	}

	
}
