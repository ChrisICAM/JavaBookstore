import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WriteLogsToFile {
	// a separate file for writing logs to file
	private String fileName;
	private ArrayList<Logs> logList;
	//A list that holds all of the logs
	public WriteLogsToFile(String fileName, ArrayList<Logs> logList) {
		//initialising the object
		this.fileName = fileName;
		this.logList = logList;
	}
	public void write() {
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName,false));
			//connects the program to the file, false overwrites the entire file
			for(Logs log: logList) {
				SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
				String stringDate= DateFor.format(log.getDate());
				bw.write(log.getUserID() +", " + log.getPostcode() +", " + log.getISBN() +", " + log.getPrice() +", " + log.getQuantity() +", " + log.getChoice() +", " + log.getMethod() +", " + stringDate +"\n");
				// This goes for each log changes the date from date data type to a string so that it can be written
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(bw != null) {
					bw.close();
					
				}
			} catch(IOException e) {
				
				e.printStackTrace();
			}
		
	}
		
	}
}
