import java.util.Comparator;


public class DateComparator implements Comparator<Logs>{
	
	public int compare(Logs log1, Logs log2)
	{
		
		return (log1.getDate()).compareTo(log2.getDate());
		
	}

}
