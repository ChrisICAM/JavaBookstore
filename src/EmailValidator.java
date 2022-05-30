import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	
	public static boolean validEmail(String email) {
		
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		//uses a string regex to get the possible patterns for an email
		Pattern pat = Pattern.compile(emailRegex);
		//makes the string into a pattern type
        if (email == null)
            return false; //if doesnt follow the pattern it is false
        return pat.matcher(email).matches(); //if the email and the pattern match it is a valid email
	}
}
