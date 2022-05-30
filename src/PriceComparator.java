import java.util.Comparator;

public class PriceComparator implements Comparator<Book>{
	
	public int compare(Book bk1, Book bk2)
	//compares the prices of the books to each other and puts them in order of choice
	{
		if (bk1.getPrice() < bk2.getPrice()) 
			return -1;
		if (bk1.getPrice() > bk2.getPrice()) 
			return 1;
		else return 0;
		
		
	}

}
