package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	public static String arrayToString(String[] array) {
		String string = "";
		for(int i=0; i<array.length-1; i++) {
			string += array[i] + ", ";
		}
		string += array[array.length-1];
		return string;
	}
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static boolean validateEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		return matcher.find();
	}
}
