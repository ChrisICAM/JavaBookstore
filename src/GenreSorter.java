import java.util.Comparator;
public class GenreSorter implements Comparator<Book> {
	
	public int compare(Book bk1, Book bk2)
	// takes in a list an compares the books in it by their genre
	{
		return (bk1.getGenre()).compareTo(bk2.getGenre());
		// outputs in alphabetical order
		
		
	}
}



