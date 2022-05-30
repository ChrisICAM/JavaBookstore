import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WritingToFile {
	private String fileName;
	private ArrayList<Book> booksList;
	public WritingToFile(String fileName, ArrayList<Book> booksList) {
		this.fileName = fileName;
		this.booksList = booksList;
	}
	
	public void write() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName,false));
			for(Book book: booksList) {
				//since this is a list of books the only way to see the differences between them is to check the class of the book
				if (book.getClass() == AudioBook.class) {
					bw.write(book.getISBN()+", "+ BookType.AudioBook +", "+ book.getTitle()+ ", "+ book.getLanguage()+ ", "+ book.getGenre()+ ", "+ book.getDate()+ ", "+ Double.toString(book.getPrice())+ ", "+ Integer.toString(book.getQuantity())+ ", "+ book.getAdditionalInfo1S()+ ", "+ book.getAdditionalInfo2S() + "\n");
					//this writes the line to the txt file
				} else if (book.getClass() == eBook.class){
					bw.write(book.getISBN()+", "+ BookType.eBook +", "+ book.getTitle()+ ", "+ book.getLanguage()+ ", "+ book.getGenre()+ ", "+ book.getDate()+ ", "+ Double.toString(book.getPrice())+ ", "+ Integer.toString(book.getQuantity())+ ", "+ book.getAdditionalInfo1S()+ ", "+ book.getAdditionalInfo2S() + "\n");
				} else {
					bw.write(book.getISBN()+", "+ BookType.Paperback +", "+ book.getTitle()+ ", "+ book.getLanguage()+ ", "+ book.getGenre()+ ", "+ book.getDate()+ ", "+ Double.toString(book.getPrice())+ ", "+ Integer.toString(book.getQuantity())+ ", "+ book.getAdditionalInfo1S()+ ", "+ book.getAdditionalInfo2S() + "\n");
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(bw != null) {
					//this closes the file after it finishes doing its process
					bw.close();
					
				}
			} catch(IOException e) {
				
				e.printStackTrace();
			}
		
	}
		
	}
}
