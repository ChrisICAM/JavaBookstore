//Audiobook is a more specialised version of book which has more attributes
public class AudioBook extends Book{
	private double bookLength;
	private AudioFormat format;
	//attributes of length and the enum format
	
	public AudioBook(int bookISBN, String bookTitle, Language language, Genre genre, String date, double price, int quantity, double bookLength, AudioFormat format) {
		super(bookISBN, bookTitle, language, genre, date, price, quantity);
			this.bookLength = bookLength;
			this.format = format;
		
	}
	//getter function for all the attributes
	public double getLength() {
		return bookLength;
	}

	public void setLength(double bookLength) {
		this.bookLength = bookLength;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}
	// Overriding the previous functions to have the specific function within AudioBook
	public String getAdditionalInfo1S() {
		String a = Double.toString(bookLength);
		return a;
	}
	
	public String getAdditionalInfo2S() {
		String a = format.toString();
		return a;
	}
}
