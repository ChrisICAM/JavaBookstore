import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidation implements DateValidator {
    private String dateFormat;
    
    public DateValidation(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        // puts the date into a date format, this checks if the entered date is valid
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false; //if the date entered has an error it will return false
        }
        return true; // if valid return true
    }

}