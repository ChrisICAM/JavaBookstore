import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class BookshopGUI extends JFrame {
	// These initialise all the variables that will be used in the GUI, they are private so they cannot be accessed from any other class
	private JPanel contentPane;
	private JComboBox usernameBox;
	private JTable tblUsers;
	private ArrayList<User> usersL;
	private ArrayList<Book> booksL;
	private ArrayList<Book> booksLNone;
	private ArrayList<Book> booksLAudio;
	private ArrayList<Book> booksLGenre;
	private ArrayList<Book> booksLBoth;
	private ArrayList<Book> basketBookL;
	private ArrayList<User> activeCustomerL;
	private ArrayList<Logs> logsL;
	private DefaultTableModel dtmUser;
	private DefaultTableModel dtmViewBooks;
	private DefaultTableModel dtmBasket;
	private JTable tblViewBooks;
	private JTextField tfTitle;
	private MaskFormatter mfDate;
	private MaskFormatter mfISBN;
	private MaskFormatter mfCC;
	private MaskFormatter mfCVV;
	private NumberFormat priceFormat;
	private NumberFormat lengthFormat;
	private NumberFormat QuantityFormat;
	private DefaultComboBoxModel dcbmeFormat;
	private DefaultComboBoxModel dcbmCondition;
	private DefaultComboBoxModel dcbmaFormat;
	private JTextField textEmail;
	private JTable tblBasket;
	private HashMap<Book, Integer> bookQuantityHM;
	private HashMap<Integer, User> customerHM;
	private HashMap<Integer, Book> bookHM;
	private HashMap<HashMap<Integer, User>, HashMap<Integer, Book>> basketHashMap;
	private JTable tblFilter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookshopGUI frame = new BookshopGUI();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		 
		
		
		
		
	}

	/**
	 * Create the frame.
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public BookshopGUI() throws FileNotFoundException, ParseException {
		// A vast amount of ArrayLists are used, so the objects all need to be created before actually placing data in it
		ArrayList<User> usersL = new ArrayList<User>();
		ArrayList<User> activeCustomerL = new ArrayList<User>();
		ArrayList<Book> basketBookL = new ArrayList<Book>();
		ArrayList<Book> booksLNone = new ArrayList<Book>();
		ArrayList<Book> booksLAudio = new ArrayList<Book>();
		ArrayList<Book> booksLGenre = new ArrayList<Book>();
		ArrayList<Book> booksLBoth = new ArrayList<Book>();
		ArrayList<Logs> logsL = new ArrayList<Logs>();
		// HashMaps are the way that I made the baskets, as a way to hold the objects and their unique identifiers
		bookQuantityHM = new HashMap<Book, Integer>();
		// But to hold the quantity the HM holds the Book as the key and the quantity as the value
		customerHM = new HashMap<Integer, User>();
		bookHM = new HashMap<Integer, Book>();
		basketHashMap = new HashMap<HashMap<Integer, User>, HashMap<Integer, Book>>();
		// This HM is for holding the HashMaps of customers data and the book data so that they can be used in junction as a basket. It holds all the data at once 
		
		File usersFile = new File("UserAccounts.txt");
		// Opens the txt file where it can access accounts
		Scanner fileScanner = new Scanner(usersFile);
		User user;
		Address address;
		while (fileScanner.hasNextLine()) {
			String[] details = fileScanner.nextLine().split(",") ;
			if (details[6].trim().equals("admin")) {
				address = new Address(Integer.parseInt(details[3].trim()), details[4].trim(), details[5].trim());
				// a separate object for address
				user = new Admin(Integer.parseInt(details[0].trim()), details[1].trim(), details[2].trim(), address);
				//the user object is using the address object as a parameter
				usersL.add(user);
			} else{
				address = new Address(Integer.parseInt(details[3].trim()), details[4].trim(), details[5].trim());
				user = new Customer(Integer.parseInt(details[0].trim()), details[1].trim(), details[2].trim(), address);
				usersL.add(user);
			}	 
		}
			
		fileScanner.close();
		
		File logFile = new File("ActivityLog.txt");
		// Opens the txt file where it can access logs
		Scanner logScanner = new Scanner(logFile);
		Logs log;
		Date date;
		SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-yyyy");
		while (logScanner.hasNextLine()) {
			String[] details = logScanner.nextLine().split(",") ;
			date = DateFormat.parse(details[7].trim());
			// changes the str into date data type
			log = new Logs(Integer.parseInt(details[0].trim()), details[1].trim(), Integer.parseInt(details[2].trim()), Double.parseDouble(details[3].trim()), Integer.parseInt(details[4].trim()), details[5].trim(), details[6].trim(), date);
			logsL.add(log);
			// adds the log to a list of logs for everything in the file
		}
		logScanner.close();
		
		
		ArrayList<Book> booksL = new ArrayList<Book>();
		//This ArrayList hold all the book details
		File booksFile = new File("Stock.txt");
		// Opens the txt file for the stock
		Scanner stockScanner = new Scanner(booksFile);
		Book book;
		while (stockScanner.hasNextLine()) {
			//checks each line from the txt file
			String[] details = stockScanner.nextLine().split(",") ;
			// splits each line by the , and puts it in a array
			if (details[1].trim().equals("Paperback")) {
				// if the booktype is pb
				if (details[3].trim().equals("English")) {
					Language lang = Language.English;
					// sets the lang to enum lang
					switch (details[4].trim()) {
					// switch case for each type of genre
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						//sets the genre to the enum genre
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
							// creates the paperback book object from the details in the txt file and adds it to the list of books
							
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					default:
						Genre genre4 = Genre.Politics;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
						
					}
				} else {
					Language lang = Language.French;
					switch (details[4].trim()) {
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
					default:
						Genre genre4 = Genre.Politics;
						if (details[9].trim().equals("NEW")) {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.NEW);
							booksL.add(book);
						}else {
							book = new Paperback(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), PaperCondition.USED);
							booksL.add(book);
						}
						break;
						
					}
				}
			} else if (details[1].trim().equals("eBook")){
				// finds if the book type is ebook so that the right object will be made
				
				if (details[3].trim().equals("English")) {
					Language lang = Language.English;
					switch (details[4].trim()) {
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							// creates the eBook book object from the details in the txt file and adds it to the list of books
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					
					default:
						Genre genre4 = Genre.Politics;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
						
					}
				} else {
					Language lang = Language.French;
					switch (details[4].trim()) {
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
					
					default:
						Genre genre4 = Genre.Politics;
						switch (details[9].trim()) {
						case "EPUB":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.EPUB);
							booksL.add(book);
							break;
						case "MOBI":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.MOBI);
							booksL.add(book);
							break;
						case "AZW3":
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.AZW3);
							booksL.add(book);
							break;
						default:
							book = new eBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Integer.parseInt(details[8].trim()), eBookFormat.PDF);
							booksL.add(book);
							break;
						}
						break;
						
					
						
					}
				}
			
			} else {
				
				if (details[3].trim().equals("English")) {
					Language lang = Language.English;
					switch (details[4].trim()) {
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							// creates the Audiobook book object from the details in the txt file and adds it to the list of books
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					
					default:
						Genre genre4 = Genre.Politics;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
						
					}
				} else {
					Language lang = Language.French;
					switch (details[4].trim()) {
					case "ComputerScience":
						Genre genre = Genre.ComputerScience;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Biography":
						Genre genre1 = Genre.Biography;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre1, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Medicine":
						Genre genre2 = Genre.Medicine;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre2, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					case "Buiness":
						Genre genre3 = Genre.Business;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre3, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;
					
					default:
						Genre genre4 = Genre.Politics;
						switch (details[9].trim()) {
						case "AAC":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.AAC);
							booksL.add(book);
							break;
						case "WMA":
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.WMA);
							booksL.add(book);
							break;
						default:
							book = new AudioBook(Integer.parseInt(details[0].trim()), details[2].trim(), lang, genre4, details[5].trim(), Double.parseDouble(details[6].trim()), Integer.parseInt(details[7].trim()), Double.parseDouble(details[8].trim()), AudioFormat.MP3);
							booksL.add(book);
							break;
						}
						break;																				
					}
				}
			
			
				
			}
		}
			
		stockScanner.close();
		
		// These are all the GUI code thats automatically made
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 506, 319);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Login", null, panel, null);
		panel.setLayout(null);
		
		
		
		JLabel loginTitle = new JLabel("LOGIN");
		loginTitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		loginTitle.setBounds(207, 20, 93, 35);
		panel.add(loginTitle);
		
		JComboBox usernameBox = new JComboBox(UserIDs.values());
		usernameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		usernameBox.setToolTipText("");
		usernameBox.setBounds(186, 116, 134, 35);
		panel.add(usernameBox);
		
		
		
		JLabel usernameLbl = new JLabel("Usernames");
		usernameLbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameLbl.setBounds(186, 87, 145, 28);
		panel.add(usernameLbl);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (usernameBox.getSelectedItem() == UserIDs.user1 || usernameBox.getSelectedItem() == UserIDs.user4 ) {
					// this checks if the user that logged in is an admin
					bookHM.clear();
					// this clears all the books in the hashmap so that if you were previously logged in as a customer it won't work
					tabbedPane.setSelectedIndex(1);
					//These disables the tabs that arent being used as an admin
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(3, true);
					tabbedPane.setEnabledAt(4, true);
					tabbedPane.setEnabledAt(5, false);
					tabbedPane.setEnabledAt(6, true);
					tabbedPane.setEnabledAt(7, false);
					tabbedPane.setEnabledAt(8, false);
					tabbedPane.setEnabledAt(9, false);
				}else {
					for (User user: usersL) {
						if (user.getUserName().equals(usernameBox.getSelectedItem().toString())) {
							activeCustomerL.add(user);
							customerHM.put(user.getUserID(), user);
						}
						// searches for the active customer and puts it into a list and a hashmap
					}
					//clears the HM in case logging in as a new customer to create a new basket
					bookHM.clear();
					tabbedPane.setSelectedIndex(2);
					//These disables the tabs that arent being used as an customer
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, true);
					tabbedPane.setEnabledAt(3, false);
					tabbedPane.setEnabledAt(4, true);
					tabbedPane.setEnabledAt(5, true);
					tabbedPane.setEnabledAt(6, false);
					tabbedPane.setEnabledAt(7, true);
					tabbedPane.setEnabledAt(8, true);
					tabbedPane.setEnabledAt(9, false);
				}
				
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.ITALIC, 16));
		btnLogin.setBounds(186, 204, 134, 35);
		panel.add(btnLogin);
		
		
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Administration", null, panel_1, null);
		panel_1.setLayout(null);
		
		//buttons to change the page
		JButton btnViewBooksTab = new JButton("View Books");
		btnViewBooksTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(4);
			}
		});
		

		btnViewBooksTab.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnViewBooksTab.setBounds(27, 140, 205, 41);
		panel_1.add(btnViewBooksTab);
		
		JButton btnAddBooks = new JButton("Add Book");
		btnAddBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(6);
			}
		});
		btnAddBooks.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAddBooks.setBounds(262, 140, 205, 41);
		panel_1.add(btnAddBooks);
		
		JLabel lblNewLabel_11 = new JLabel("Administration");
		lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel_11.setBounds(152, 27, 254, 33);
		panel_1.add(lblNewLabel_11);
		
		JButton btnAdminLogout = new JButton("LOGOUT");
		btnAdminLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeCustomerL.clear();
				customerHM.clear();
				bookHM.clear();
				basketHashMap.clear();
				//when you logout it clears everything everything
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(4, false);
				tabbedPane.setEnabledAt(5, false);
				tabbedPane.setEnabledAt(6, false);
				tabbedPane.setEnabledAt(7, false);
				tabbedPane.setEnabledAt(8, false);
				tabbedPane.setEnabledAt(9, false);
			}
		});
		btnAdminLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdminLogout.setBounds(357, 230, 110, 21);
		panel_1.add(btnAdminLogout);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Customers", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_11_1 = new JLabel("Customer Hub");
		lblNewLabel_11_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel_11_1.setBounds(131, 25, 254, 33);
		panel_2.add(lblNewLabel_11_1);
		
		JButton btnViewBooksTab_1 = new JButton("View Books");
		btnViewBooksTab_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(4);
			}
		});
		btnViewBooksTab_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnViewBooksTab_1.setBounds(20, 122, 205, 41);
		panel_2.add(btnViewBooksTab_1);
		
		JButton btnSearchBooks = new JButton("Search Books");
		btnSearchBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(5);
			}
		});
		btnSearchBooks.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnSearchBooks.setBounds(265, 122, 205, 41);
		panel_2.add(btnSearchBooks);
		
		JButton btnBasketAdder = new JButton("Add Books to Basket");
		btnBasketAdder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(7);
			}
		});
		btnBasketAdder.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBasketAdder.setBounds(20, 203, 205, 41);
		panel_2.add(btnBasketAdder);
		
		JButton btnBasket = new JButton("Check Basket");
		btnBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(8);
			}
		});
		btnBasket.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnBasket.setBounds(265, 203, 205, 41);
		panel_2.add(btnBasket);
		
		JButton btnCustomerLogout = new JButton("LOGOUT");
		btnCustomerLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeCustomerL.clear();
				customerHM.clear();
				bookHM.clear();
				basketHashMap.clear();
				//when you logout it clears everything in the HM so that it does not cross over to another account
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(3, true);
				tabbedPane.setEnabledAt(4, false);
				tabbedPane.setEnabledAt(5, false);
				tabbedPane.setEnabledAt(6, false);
				tabbedPane.setEnabledAt(7, false);
				tabbedPane.setEnabledAt(8, false);
				tabbedPane.setEnabledAt(9, false);
			}
		});
		btnCustomerLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCustomerLogout.setBounds(381, 37, 110, 21);
		panel_2.add(btnCustomerLogout);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Users", null, panel_3, null);
		panel_3.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 396, 272);
		panel_3.add(scrollPane);
		
		tblUsers = new JTable();
		scrollPane.setViewportView(tblUsers);
		DefaultTableModel dtmUser = new DefaultTableModel();
		dtmUser.setColumnIdentifiers(new Object[] {"ID", "Username", "Surname", "Address"});
		// sets a default table that show the headers for the table before it is viewed
		tblUsers.setModel(dtmUser);
		
		JButton btnView = new JButton("View");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (User user : usersL) {
					Object[]  rows = new Object[] {user.getUserID(), user.getUserName(), user.getSurname(), (Integer.toString(user.getAddress().getHouseNo()) +" " + user.getAddress().getPostcode() +" " + user.getAddress().getCity() )};
					dtmUser.addRow(rows);
				}
				// goes through all the users and outputs all the data using methods to output them on a table
			}
		});
		btnView.setBounds(416, 96, 85, 21);
		panel_3.add(btnView);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("View Books", null, panel_4, null);
		panel_4.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 40, 481, 199);
		panel_4.add(scrollPane_1);
		
		tblViewBooks = new JTable();
		scrollPane_1.setViewportView(tblViewBooks);
		DefaultTableModel dtmViewBooks = new DefaultTableModel(null, new Object[] {"ISBN", "Book Title", "Language", "Genre","Release Date", "Price(£)", "Quantity", "Add1","Add2"});
		tblViewBooks.setModel(dtmViewBooks);
		
		JButton btnViewBooks = new JButton("View Books");
		btnViewBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//every time the button is pressed this action happens
				tblViewBooks.setModel(dtmViewBooks);
				dtmViewBooks.setRowCount(0);
				//This is for removing the table and resetting it once the button is pressed
				//otherwise it will add the new press to the end of the table that is already there making the table twice as long
				PriceComparator priceComparer = new PriceComparator();
				Collections.sort(booksL, priceComparer.reversed());
				// This takes the book list and compares the prices between each other and orders the list in descending order
				
				for (Book book : booksL) {
					Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), book.getLanguage(), book.getGenre(),book.getDate(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), book.getAdditionalInfo1S(), book.getAdditionalInfo2S()};
					dtmViewBooks.addRow(rows);
				}
				// goes through all the books and outputs all the data using methods to output them on a table
				
			}
		});
		btnViewBooks.setBounds(190, 249, 114, 21);
		panel_4.add(btnViewBooks);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Search Book", null, panel_8, null);
		panel_8.setLayout(null);
		
		JComboBox cbFilters = new JComboBox(Filters.values());
		//This makes the combobox have the values of the enumeration Filters
		cbFilters.setBounds(137, 48, 222, 21);
		panel_8.add(cbFilters);
		
		
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 79, 481, 152);
		panel_8.add(scrollPane_3);
		
		tblFilter = new JTable();
		scrollPane_3.setViewportView(tblFilter);
		DefaultTableModel dtmSearchBooks = new DefaultTableModel(null, new Object[] {"ISBN", "Book Title", "Language", "Genre","Release Date", "Price(£)", "Quantity", "Add1","Add2"});
		tblViewBooks.setModel(dtmSearchBooks);
		// creates the table for the Searching page
		
		JLabel lblNewLabel_22 = new JLabel("View Books");
		lblNewLabel_22.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel_22.setBounds(172, 0, 159, 37);
		panel_4.add(lblNewLabel_22);
		
		JButton btnFilters = new JButton("Apply Filters");
		btnFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tblFilter.setModel(dtmSearchBooks);
				//Changes the model for the table
				dtmSearchBooks.setRowCount(0);
				if (cbFilters.getSelectedItem() == Filters.None) {
					for (Book book:booksL) {
						booksLNone.add(book);
						// adds all the books in the book list to another list just for the filtering purposes
					}
					for (Book book : booksLNone) {
						Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), book.getLanguage(), book.getGenre(),book.getDate(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), book.getAdditionalInfo1S(), book.getAdditionalInfo2S()};
						dtmSearchBooks.addRow(rows);
						// creates the table using the new list
					}
				} else if (cbFilters.getSelectedItem() == Filters.Genre) {
					for (Book book:booksL) {
						booksLGenre.add(book);
					}
					GenreSorter genreSort = new GenreSorter();
					Collections.sort(booksLGenre, genreSort);
					// This sorts the new list in the alphabetical order by their genre
					for (Book book : booksLGenre) {
						Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), book.getLanguage(), book.getGenre(),book.getDate(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), book.getAdditionalInfo1S(), book.getAdditionalInfo2S()};
						dtmSearchBooks.addRow(rows);
						// Outputs the table in the order of the genre
					}
				} else if (cbFilters.getSelectedItem() == Filters.AudioBookFiveHrsPlus) {
					for (Book book:booksL) {
						if (book.getClass() == AudioBook.class) {
							
							if (Double.parseDouble(book.getAdditionalInfo1S()) > 5) {
								// this filters the books to only get the audiobooks that have a length greater than 5
								booksLAudio.add(book);
							}
						}
						
					}
					dtmSearchBooks.setRowCount(0);
					for (Book book : booksLAudio) {
						Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), book.getLanguage(), book.getGenre(),book.getDate(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), book.getAdditionalInfo1S(), book.getAdditionalInfo2S()};
						dtmSearchBooks.addRow(rows);
					}
				} else {
					for (Book book:booksL) {
						if (book.getClass() == AudioBook.class) {
							if (Double.parseDouble(book.getAdditionalInfo1S()) > 5) {
								// gets the audiobooks with length >5 
								booksLBoth.add(book);
							}
						}
						
					}
					GenreSorter genreSort = new GenreSorter();
					Collections.sort(booksLBoth, genreSort);
					//then also puts those books in the order of genre
					dtmSearchBooks.setRowCount(0);
					for (Book book : booksLBoth) {
						Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), book.getLanguage(), book.getGenre(),book.getDate(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), book.getAdditionalInfo1S(), book.getAdditionalInfo2S()};
						dtmSearchBooks.addRow(rows);
					}
				}
			}
		});
		btnFilters.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnFilters.setBounds(137, 239, 222, 28);
		panel_8.add(btnFilters);
		
		JLabel lblNewLabel_20 = new JLabel("Search Books");
		lblNewLabel_20.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel_20.setBounds(147, 10, 251, 28);
		panel_8.add(lblNewLabel_20);
		
		JLabel lblNewLabel_21 = new JLabel("Filters");
		lblNewLabel_21.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_21.setBounds(41, 50, 56, 17);
		panel_8.add(lblNewLabel_21);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Add Book", null, panel_5, null);
		panel_5.setLayout(null);
		
		
		
		JLabel lblNewLabel = new JLabel("Add Book");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel.setBounds(185, 0, 122, 35);
		panel_5.add(lblNewLabel);
		
		mfISBN = new MaskFormatter("########");
		mfISBN.setPlaceholderCharacter('~');
		//this mask allows that only numbers can be put in the text inputs as a pre-made form of validation witha pre made length
		// as the length of the ISBN is 8 digit
		JFormattedTextField ftfISBN = new JFormattedTextField(mfISBN);
		ftfISBN.setBounds(248, 42, 179, 19);
		panel_5.add(ftfISBN);
		
		mfDate = new MaskFormatter("##-##-####");
		mfDate.setPlaceholderCharacter('~');
		// this mask makes it so only numbers can be entered the dashes are added to the outputted string so it is in date format
		JFormattedTextField ftfDate = new JFormattedTextField(mfDate);
		ftfDate.setBounds(90, 118, 179, 19);
		panel_5.add(ftfDate);
		
		priceFormat = DecimalFormat.getInstance();
		priceFormat.setMinimumFractionDigits(2);
		priceFormat.setMaximumFractionDigits(2);
		priceFormat.setRoundingMode(RoundingMode.HALF_UP);
		priceFormat.setGroupingUsed(false);
		// forces the float entered to be dynamically changed into 2 decimal places
		JFormattedTextField ftfPrice = new JFormattedTextField(priceFormat);
		ftfPrice.setBounds(90, 165, 179, 19);
		panel_5.add(ftfPrice);
		
		
		QuantityFormat = NumberFormat.getInstance();
		QuantityFormat.setGroupingUsed(false);
		// forces the number entered to be an integer
		JFormattedTextField ftfQuantity = new JFormattedTextField(QuantityFormat);
		ftfQuantity.setBounds(349, 165, 109, 19);
		panel_5.add(ftfQuantity);
		
		
		JComboBox cbBookType = new JComboBox(BookType.values());
		cbBookType.setBounds(90, 41, 106, 21);
		panel_5.add(cbBookType);
		
		JComboBox cbLanguage = new JComboBox(Language.values());
		cbLanguage.setBounds(349, 77, 131, 21);
		panel_5.add(cbLanguage);
		
		JComboBox cbGenre = new JComboBox(Genre.values());
		cbGenre.setBounds(349, 117, 131, 21);
		panel_5.add(cbGenre);
		
		tfTitle = new JTextField();
		tfTitle.setBounds(90, 78, 180, 20);
		panel_5.add(tfTitle);
		tfTitle.setColumns(10);
		
		dcbmCondition = new DefaultComboBoxModel(PaperCondition.values()); 
		dcbmeFormat = new DefaultComboBoxModel(eBookFormat.values());
		dcbmaFormat = new DefaultComboBoxModel(AudioFormat.values());
		// I set default models for the combo box so that they could change dynamically
		JComboBox cbConditionOrFormat = new JComboBox(dcbmeFormat);
		cbConditionOrFormat.setBounds(349, 216, 131, 21);
		panel_5.add(cbConditionOrFormat);
		
		cbBookType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (BookType.eBook.equals(cbBookType.getSelectedItem())){
	                // if the cb for booktype is on ebook this changes the format condition cb to the model for EBOOK
	            	cbConditionOrFormat.setModel(dcbmeFormat);
	            } else if (BookType.AudioBook.equals(cbBookType.getSelectedItem())){
	            	cbConditionOrFormat.setModel(dcbmaFormat);
	            	// if the cb for booktype is on audiobook this changes the format condition cb to the model for AUDIOBOOK
	            } else {
	            	cbConditionOrFormat.setModel(dcbmCondition);
	            	// if the cb for booktype is on paperback this changes the format condition cb to the model for PAPERBACK
	            }
	        }
		});
		
		
		lengthFormat = DecimalFormat.getInstance();
		lengthFormat.setMinimumFractionDigits(1);
		lengthFormat.setMaximumFractionDigits(1);
		lengthFormat.setRoundingMode(RoundingMode.HALF_UP);
		lengthFormat.setGroupingUsed(false);
		// since this is a joined text box for both pages which is int and length which is a float
		// this forces it to 1dp
		JFormattedTextField ftfPagesOrLength = new JFormattedTextField(lengthFormat);
		ftfPagesOrLength.setBounds(90, 206, 179, 19);
		panel_5.add(ftfPagesOrLength);
		
		JLabel lblNewLabel_1 = new JLabel("Title");
		lblNewLabel_1.setBounds(10, 81, 45, 13);
		panel_5.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("ISBN");
		lblNewLabel_2.setBounds(206, 45, 45, 13);
		panel_5.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Book Type");
		lblNewLabel_3.setBounds(10, 45, 66, 13);
		panel_5.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Price");
		lblNewLabel_4.setBounds(10, 168, 45, 13);
		panel_5.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Language");
		lblNewLabel_5.setBounds(280, 81, 59, 13);
		panel_5.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Genre");
		lblNewLabel_6.setBounds(280, 121, 45, 13);
		panel_5.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Release Date");
		lblNewLabel_7.setBounds(10, 121, 80, 13);
		panel_5.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Quantity");
		lblNewLabel_8.setBounds(279, 168, 66, 13);
		panel_5.add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("Pages/Length");
		lblNewLabel_9.setBounds(10, 209, 80, 13);
		panel_5.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("Condition/Format:");
		lblNewLabel_10.setBounds(359, 194, 113, 13);
		panel_5.add(lblNewLabel_10);
		
		JButton btnNewButton = new JButton("Create Book");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean switcher = false;
				DateValidator validator = new DateValidation("dd-MM-yyyy");
				//sets the base format for the valid dates
				if (ftfDate.getValue() == null || ftfISBN.getValue() == null || ftfPrice.getValue() == null || ftfQuantity.getValue() == null || tfTitle.getText().isEmpty() == true || ftfPagesOrLength.getValue() == null) {
					JOptionPane.showMessageDialog(null, "Contains an EMPTY field", "ERROR", JOptionPane.WARNING_MESSAGE);
					// brings up a dialog box is brought up if there is a single empty entry box
				}else if (validator.isValid(ftfDate.getValue().toString())) {
					// this has checked if the date that has been entered is valid
					Book newBook;
					for (Book book:booksL) {
						if (Integer.parseInt(ftfISBN.getValue().toString()) == book.getISBN()) {
							JOptionPane.showMessageDialog(null, "Book ISBN Already Exists", "ERROR", JOptionPane.WARNING_MESSAGE);
							// checks if the bookISBN is already in the file
							switcher = true;
						}
					}
					if (BookType.eBook.equals(cbBookType.getSelectedItem()) && switcher == false) {
						// the boolean is to make sure that it will not add a book that has the same ISBN anyways
						newBook = new eBook(Integer.parseInt(ftfISBN.getValue().toString()), tfTitle.getText(),(Language) cbLanguage.getSelectedItem(), (Genre) cbGenre.getSelectedItem(), ftfDate.getValue().toString(), new BigDecimal(Double.valueOf(ftfPrice.getValue().toString())).setScale(2, RoundingMode.HALF_UP).doubleValue() , Integer.parseInt(ftfQuantity.getValue().toString()), Integer.parseInt(ftfPagesOrLength.getValue().toString()), (eBookFormat)cbConditionOrFormat.getSelectedItem());
						booksL.add(newBook);
						// adds the new book to the list of books so that it can be viewed
						WritingToFile writeFile = new WritingToFile("Stock.txt", booksL);    
						writeFile.write();
						// writes the new book to the file
						JOptionPane.showMessageDialog(null, "eBook has been created", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					}else if (BookType.AudioBook.equals(cbBookType.getSelectedItem()) && switcher == false){
						newBook = new AudioBook(Integer.parseInt(ftfISBN.getValue().toString()), tfTitle.getText(),(Language) cbLanguage.getSelectedItem(), (Genre) cbGenre.getSelectedItem(), ftfDate.getValue().toString(), new BigDecimal(Double.valueOf(ftfPrice.getValue().toString())).setScale(2, RoundingMode.HALF_UP).doubleValue(), Integer.parseInt(ftfQuantity.getValue().toString()), new BigDecimal(Double.valueOf(ftfPagesOrLength.getValue().toString())).setScale(1, RoundingMode.HALF_UP).doubleValue(), (AudioFormat)cbConditionOrFormat.getSelectedItem());
						booksL.add(newBook);
						WritingToFile writeFile = new WritingToFile("Stock.txt", booksL);
						writeFile.write();
						JOptionPane.showMessageDialog(null, "AudioBook has been created", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					}else if (BookType.Paperback.equals(cbBookType.getSelectedItem()) && switcher == false){
						newBook = new Paperback(Integer.parseInt(ftfISBN.getValue().toString()), tfTitle.getText(),(Language) cbLanguage.getSelectedItem(), (Genre) cbGenre.getSelectedItem(), ftfDate.getValue().toString(), new BigDecimal(Double.valueOf(ftfPrice.getValue().toString())).setScale(2, RoundingMode.HALF_UP).doubleValue(), Integer.parseInt(ftfQuantity.getValue().toString()), Integer.parseInt(ftfPagesOrLength.getValue().toString()), (PaperCondition)cbConditionOrFormat.getSelectedItem());
						booksL.add(newBook);
						WritingToFile writeFile = new WritingToFile("Stock.txt", booksL);
						writeFile.write();
						JOptionPane.showMessageDialog(null, "Paperback Book has been created", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					}else {
						;
					}
				}else {
					JOptionPane.showMessageDialog(null, "Date is not Valid", "ERROR", JOptionPane.WARNING_MESSAGE);
					// creates message box if it is not valid
					
				}
				ftfISBN.setText("");
				tfTitle.setText("");
				ftfDate.setText("");
				ftfPrice.setText("");
				ftfQuantity.setText("");
				ftfPagesOrLength.setText("");
				// Once the button is pressed it will not regain any memory, even if a mistake is made
			}
		});
		btnNewButton.setBounds(194, 247, 113, 21);
		panel_5.add(btnNewButton);
		
		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("Basket Adder", null, panel_9, null);
		panel_9.setLayout(null);
		
		JFormattedTextField ftfBasketISBN = new JFormattedTextField(mfISBN);
		ftfBasketISBN.setBounds(76, 77, 229, 19);
		panel_9.add(ftfBasketISBN);
		
		JFormattedTextField ftfBasketQuantity = new JFormattedTextField(QuantityFormat);
		ftfBasketQuantity.setBounds(76, 135, 136, 19);
		panel_9.add(ftfBasketQuantity);
		
		JButton btnAddBookToBasket = new JButton("Add To Basket");
		btnAddBookToBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean switcher = false;
				boolean found = false;
				if (ftfBasketISBN.getValue() == null || ftfBasketQuantity.getValue() == null) {
					JOptionPane.showMessageDialog(null, "Contains an EMPTY field", "ERROR", JOptionPane.WARNING_MESSAGE);
					// checks if the fields are empty
					switcher = true;
				}else {
					for (Book book:basketBookL) {
						if (Integer.parseInt(ftfBasketISBN.getValue().toString()) == book.getISBN()) {
							JOptionPane.showMessageDialog(null, "Book ISBN Already In Basket", "ERROR", JOptionPane.WARNING_MESSAGE);
							// checks if already in the basket
							switcher = true;
						}
					}
					for (Book book:booksL) {
						if (Integer.parseInt(ftfBasketISBN.getValue().toString()) == book.getISBN() && Integer.parseInt(ftfBasketQuantity.getValue().toString()) > book.getQuantity()) {
							JOptionPane.showMessageDialog(null, "The Book wanted does not have that many in stock", "ERROR", JOptionPane.WARNING_MESSAGE);
							//checks the list of books if the amount asked for is greater than the quantity of the books available 
							switcher = true;
						}
					}
					if (switcher == false) {
						for (Book book:booksL) {
							if (Integer.parseInt(ftfBasketISBN.getValue().toString()) == book.getISBN()) {
								basketBookL.add(book);
								bookQuantityHM.put(book, Integer.parseInt(ftfBasketQuantity.getValue().toString()));
								found = true;
								//adds the book the the basket
							}
						}
					
					}
					if (switcher == false && found == false) {
						JOptionPane.showMessageDialog(null, "That Book Does Not Exist", "ERROR", JOptionPane.WARNING_MESSAGE);
						// this goes through all of the checks and if none of them happen the book doesn't exist
					}
				}
				ftfBasketISBN.setText("");
				ftfBasketQuantity.setText("");
				
			}
		});
		btnAddBookToBasket.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAddBookToBasket.setBounds(138, 200, 167, 21);
		panel_9.add(btnAddBookToBasket);
		
		JLabel lblNewLabel_18 = new JLabel("Enter Book ISBN:");
		lblNewLabel_18.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_18.setBounds(27, 63, 124, 13);
		panel_9.add(lblNewLabel_18);
		
		JLabel lblNewLabel_19 = new JLabel("Enter Quantity:");
		lblNewLabel_19.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_19.setBounds(27, 111, 109, 13);
		panel_9.add(lblNewLabel_19);
		
		JLabel lblAddBookTo = new JLabel("Add Book To Basket");
		lblAddBookTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblAddBookTo.setBounds(107, 10, 264, 35);
		panel_9.add(lblAddBookTo);
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Shopping Basket", null, panel_6, null);
		panel_6.setLayout(null);
		
		JButton btnProcceed = new JButton("Procceed To Payment");
		btnProcceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (basketBookL.size() == 0) {
					JOptionPane.showMessageDialog(null, "Basket is EMPTY", "ERROR", JOptionPane.WARNING_MESSAGE);
					//cant go to payment if the basket is empty
				}else {
					for (Book book:basketBookL) {
						bookHM.put(book.getISBN(), book);
						// gets the books from the basket and puts in a HM for payment
					}
					tabbedPane.setSelectedIndex(9);
					tabbedPane.setEnabledAt(9, true);
					tabbedPane.setEnabledAt(8, false);
					tabbedPane.setEnabledAt(7, false);
					//makes sure that you cannot go back by pressing a tab as that could mess up the process
				}
				
			}
		});
		btnProcceed.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnProcceed.setBounds(61, 242, 391, 25);
		panel_6.add(btnProcceed);
		
		JLabel lblBasket = new JLabel("Basket");
		lblBasket.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblBasket.setBounds(195, 10, 122, 35);
		panel_6.add(lblBasket);
		
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 43, 481, 158);
		panel_6.add(scrollPane_2);
		
		tblBasket = new JTable();
		scrollPane_2.setViewportView(tblBasket);
		DefaultTableModel dtmBasket = new DefaultTableModel(null, new Object[] {"ISBN", "Book Title", "Price(£)", "Total Quantity", "Quantity Bought"});
		tblBasket.setModel(dtmBasket);
		
		JButton btnNewButton_1 = new JButton("View Basket");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tblBasket.setModel(dtmBasket);
				dtmBasket.setRowCount(0);
				for (Book book : basketBookL) {
					//searches the books in the basket
					for(HashMap.Entry<Book, Integer> entry : bookQuantityHM.entrySet()){
						// gets the second HM to get the quantity bought of the book
						if (book.getISBN() == entry.getKey().getISBN()) {
							//make sure that they are the same book before outputting to the table
							Object[]  rows = new Object[] {book.getISBN(), book.getTitle(), Double.toString(book.getPrice()), Integer.toString(book.getQuantity()), entry.getValue()};
							//entry gets the value of the quantity bought
							dtmBasket.addRow(rows);
						}
					}
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_1.setBounds(61, 209, 391, 25);
		panel_6.add(btnNewButton_1);
		
		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("Payment", null, panel_7, null);
		panel_7.setLayout(null);
		
		JButton btnCancelledPay = new JButton("Cancel");
		btnCancelledPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Logs log;
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					String cDate = formatter.format(date);
					Date currentDate = formatter.parse(cDate);
					//This gets the current date in the correct format
					basketHashMap.put(customerHM, bookHM);
					//puts HM of both hashMaps
					
					for(Entry<HashMap<Integer, User>, HashMap<Integer, Book>> entry : basketHashMap.entrySet()){
						
						for (int i : customerHM.keySet()) {
							for (int j : bookHM.keySet()) {
								for(HashMap.Entry<Book, Integer> atom : bookQuantityHM.entrySet()){
									//uses the HashMap of 2 HM to get data from both HM simultaneously
									if (entry.getValue().get(j).getISBN() == atom.getKey().getISBN()) {
										// this was needed as it would repeat data with the wrong quanityt if there were multiple books in the basket
										// this was to make sure that they were talking about the same ISBN
										log = new Logs(entry.getKey().get(i).getUserID(), entry.getKey().get(i).getAddress().getPostcode() , entry.getValue().get(j).getISBN(),entry.getValue().get(j).getPrice(), atom.getValue().intValue(), "cancelled", "", currentDate);
										logsL.add(log);
										// adds that log to the list even though it had been cancelled it had to be made into a log
									}
								}
										
							}
							
					    }	
					    
					}
					
					DateComparator dateComparer = new DateComparator();
					Collections.sort(logsL, dateComparer.reversed());
					//gets all the logs an puts the list in the order of dates most recent at the top
					WriteLogsToFile wLogsFile = new WriteLogsToFile("ActivityLog.txt", logsL);
					wLogsFile.write();
					//writes those logs to the ActivityLog txt file
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				basketBookL.clear();
				basketHashMap.clear();
				bookQuantityHM.clear();
				// once cancelled all the basket and anything attached to it is cleared
				tabbedPane.setSelectedIndex(2);
				tabbedPane.setEnabledAt(9, false);
				tabbedPane.setEnabledAt(8, true);
				tabbedPane.setEnabledAt(7, true);
			}
		});
		btnCancelledPay.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnCancelledPay.setBounds(60, 217, 160, 37);
		panel_7.add(btnCancelledPay);
		
		
		
		JComboBox cbPaymentMethod = new JComboBox(PaymentForm.values());
		cbPaymentMethod.setBounds(232, 67, 99, 21);
		panel_7.add(cbPaymentMethod);
		
		// masks force that only numbers be entered
		mfCC = new MaskFormatter("######");
		mfCC.setPlaceholderCharacter('#');
		JFormattedTextField ftfCreditCardNo = new JFormattedTextField(mfCC);
		ftfCreditCardNo.setBounds(135, 113, 239, 19);
		panel_7.add(ftfCreditCardNo);
		
		mfCVV = new MaskFormatter("###");
		mfCVV.setPlaceholderCharacter('#');
		JFormattedTextField ftfCVV = new JFormattedTextField(mfCVV);
		ftfCVV.setBounds(135, 142, 76, 19);
		panel_7.add(ftfCVV);
		
		textEmail = new JTextField();
		textEmail.setBounds(135, 167, 260, 21);
		panel_7.add(textEmail);
		textEmail.setColumns(10);
		
		JButton btnConfirmPay = new JButton("Confirm");
		btnConfirmPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (cbPaymentMethod.getSelectedItem() == PaymentForm.CreditCard) {
					//checks if credit card is selected so it looks at the right input boxes
					if (ftfCreditCardNo.getValue() == null || ftfCVV.getValue() == null) {
						//checks if the necessary fields are empty
						JOptionPane.showMessageDialog(null, "Credit Card Number OR CVV is EMPTY \n email is not used", "ERROR", JOptionPane.WARNING_MESSAGE);
					}else {

						try {
							Logs log;
							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
							String cDate = formatter.format(date);
							Date currentDate = formatter.parse(cDate);
							basketHashMap.put(customerHM, bookHM);
							double total = 0;
							for(Entry<HashMap<Integer, User>, HashMap<Integer, Book>> entry : basketHashMap.entrySet()){
								
								for (int i : customerHM.keySet()) {
									for (int j : bookHM.keySet()) {
										for(HashMap.Entry<Book, Integer> atom : bookQuantityHM.entrySet()){
											if (entry.getValue().get(j).getISBN() == atom.getKey().getISBN()) {
												log = new Logs(entry.getKey().get(i).getUserID(), entry.getKey().get(i).getAddress().getPostcode() , entry.getValue().get(j).getISBN(),entry.getValue().get(j).getPrice(), atom.getValue().intValue(), "purchased", "Credit Card", currentDate);
												logsL.add(log);
												//creates the log for the payment
												total = total + entry.getValue().get(j).getPrice() * atom.getValue().intValue();
												//used for outputting the total amount for the process
											}
										}
												
									}
									
							    }	
							    
							}
							
							String totalStr = String.format("%.2f", total);
							//makes the total to be in 2dp
							JOptionPane.showMessageDialog(null, "£" + totalStr + " paid using Credit Card", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
							DateComparator dateComparer = new DateComparator();
							Collections.sort(logsL, dateComparer.reversed());
							// logs in date order
							WriteLogsToFile wLogsFile = new WriteLogsToFile("ActivityLog.txt", logsL);
							wLogsFile.write();
							//writes the logs to file
							for (Book book:booksL) {
								for(HashMap.Entry<Book, Integer> atom : bookQuantityHM.entrySet()) {
									if (book.getISBN() == atom.getKey().getISBN()) {
										book.alterQuantity(atom.getValue().intValue());
										//changes the quantity in the files to reflect the amount taken out of them
									}
								}
							}
							WritingToFile writeFile = new WritingToFile("Stock.txt", booksL);    
							writeFile.write();
							//re-writes the stock with the new amount of stock
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ftfCreditCardNo.setText(null);
						ftfCVV.setText(null);
						textEmail.setText(null);
						basketBookL.clear();
						basketHashMap.clear();
						bookQuantityHM.clear();
						//clears the baskets and the input lines
						tabbedPane.setSelectedIndex(2);
						tabbedPane.setEnabledAt(9, false);
						tabbedPane.setEnabledAt(8, true);
						tabbedPane.setEnabledAt(7, true);					
					}
					
				}else {
					if (textEmail.getText().isEmpty() == true) {
						JOptionPane.showMessageDialog(null, "Email Address is EMPTY \n Credit Card OR CVV not used", "ERROR", JOptionPane.WARNING_MESSAGE);
						//checks if the email input is empty since this is for email
					}else if (EmailValidator.validEmail(textEmail.getText()) == false) {
						JOptionPane.showMessageDialog(null, "Email Address is not valid", "ERROR", JOptionPane.WARNING_MESSAGE);
						//checks if email is valid
					}else {
						try {
							// the exact same as the above
							Logs log;
							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
							String cDate = formatter.format(date);
							Date currentDate = formatter.parse(cDate);
							basketHashMap.put(customerHM, bookHM);
							double total = 0;
							for(Entry<HashMap<Integer, User>, HashMap<Integer, Book>> entry : basketHashMap.entrySet()){
								
								for (int i : customerHM.keySet()) {
									for (int j : bookHM.keySet()) {
										for(HashMap.Entry<Book, Integer> atom : bookQuantityHM.entrySet()){
											if (entry.getValue().get(j).getISBN() == atom.getKey().getISBN()) {
												log = new Logs(entry.getKey().get(i).getUserID(), entry.getKey().get(i).getAddress().getPostcode() , entry.getValue().get(j).getISBN(),entry.getValue().get(j).getPrice(), atom.getValue().intValue(), "purchased", "PayPal", currentDate);
												logsL.add(log);
												total = total + entry.getValue().get(j).getPrice() * atom.getValue().intValue();
											}
										}
												
									}
									
							    }	
							    
							}
							
							String totalStr = String.format("%.2f", total);
							JOptionPane.showMessageDialog(null, "£" + totalStr + " paid using PayPal", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
							DateComparator dateComparer = new DateComparator();
							Collections.sort(logsL, dateComparer.reversed());
							WriteLogsToFile wLogsFile = new WriteLogsToFile("ActivityLog.txt", logsL);
							wLogsFile.write();
							for (Book book:booksL) {
								for(HashMap.Entry<Book, Integer> atom : bookQuantityHM.entrySet()) {
									if (book.getISBN() == atom.getKey().getISBN()) {
										book.alterQuantity(atom.getValue().intValue());
									}
								}
							}
							WritingToFile writeFile = new WritingToFile("Stock.txt", booksL);    
							writeFile.write();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ftfCreditCardNo.setText(null);
						ftfCVV.setText(null);
						textEmail.setText(null);
						basketBookL.clear();
						basketHashMap.clear();
						bookQuantityHM.clear();
						tabbedPane.setSelectedIndex(2);
						tabbedPane.setEnabledAt(9, false);
						tabbedPane.setEnabledAt(8, true);
						tabbedPane.setEnabledAt(7, true);
						
					}
				}
			}
		});
		btnConfirmPay.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnConfirmPay.setBounds(276, 217, 160, 37);
		panel_7.add(btnConfirmPay);
		
		JLabel lblNewLabel_12 = new JLabel("Email Address");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_12.setBounds(44, 167, 92, 13);
		panel_7.add(lblNewLabel_12);
		
		JLabel lblNewLabel_13 = new JLabel("CVV:");
		lblNewLabel_13.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_13.setBounds(60, 142, 52, 13);
		panel_7.add(lblNewLabel_13);
		
		JLabel lblNewLabel_14 = new JLabel("Credit Card Number:");
		lblNewLabel_14.setBounds(10, 116, 144, 13);
		panel_7.add(lblNewLabel_14);
		
		JLabel lblNewLabel_15 = new JLabel("Payment Type");
		lblNewLabel_15.setBounds(133, 71, 136, 13);
		panel_7.add(lblNewLabel_15);
		
		JLabel lblNewLabel_16 = new JLabel("Choose either PayPal or Credit Card");
		lblNewLabel_16.setBounds(110, 40, 282, 13);
		panel_7.add(lblNewLabel_16);
		
		JLabel lblNewLabel_17 = new JLabel("Checkout");
		lblNewLabel_17.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel_17.setBounds(199, -5, 239, 37);
		panel_7.add(lblNewLabel_17);
		
		
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(4, false);
		tabbedPane.setEnabledAt(5, false);
		tabbedPane.setEnabledAt(6, false);
		tabbedPane.setEnabledAt(7, false);
		tabbedPane.setEnabledAt(8, false);
		tabbedPane.setEnabledAt(9, false);
	}
}
