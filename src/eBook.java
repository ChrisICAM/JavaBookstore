//Specialised version of Book that handles only eBook so it gets more attributes
public class eBook extends Book{
	
	private int noOfPages;
	private eBookFormat format;
	//attributes of no of pages and the enum format
	
	public eBook(int bookISBN, String bookTitle, Language language, Genre genre, String date, double price,
			int quantity, int noOfPages, eBookFormat format) {
		super(bookISBN, bookTitle, language, genre, date, price, quantity);
		this.noOfPages = noOfPages;
		this.format = format;  
	}		
	
	//getter methods
	public int getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}

	public eBookFormat getFormat() {
		return format;
	}

	public void setFormat(eBookFormat format) {
		this.format = format;
	}
	
	//overrides this method so that it has a specific function within eBook
	// all of these methods will output the data as a string since it would not work if they were all different data types
	public String getAdditionalInfo1S() {
		String a = Integer.toString(noOfPages);
		return a;
	}
	
	public String getAdditionalInfo2S() {
		String a = format.toString();
		return a;
	}

}
