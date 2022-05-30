
//this is the very basic version of all the different book types
//Since this is the generalised version of the types of books, they can all have the type book
//so it can be used for storing in a list of all type book
public class Book {
	private final int bookISBN;
	private String bookTitle;
	private Language language;
	private Genre genre;
	private String date;
	private double price;
	private int quantity;
	
	public Book(int bookISBN, String bookTitle, Language language, Genre genre, String date, double price, int quantity) {
		this.bookISBN = bookISBN;
		this.bookTitle = bookTitle;
		this.language = language;
		this.genre = genre;
		this.date = date;
		this.price = price;
		this.quantity = quantity;
	}
	
	// a lot of getter methods for the attributes
	public int getISBN() {
		return(bookISBN); }
	public String getTitle() {
		return(bookTitle); }
	public Language getLanguage() {
		return(language); }
	public Genre getGenre() {
		return(genre); }
	public String getDate() {
		 return(date); }
	public double getPrice() {
		return(price); }
	public int getQuantity() {
		return(quantity); }
	
	//This is the way that I can get the info for the additional info that the different specialised types of books have
	public String getAdditionalInfo1S() {
		return "";
	}
	public String getAdditionalInfo2S() {
		return "";
	}
	
	
	// this is a different type of setter method, but it just alters the quantity for the book
	public void alterQuantity(int quantityBought) {
		int originalQuantity = this.getQuantity();
		int newQuantity = originalQuantity - quantityBought;
		this.quantity = newQuantity;
		}
}
