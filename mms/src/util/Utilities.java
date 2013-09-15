package util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	
	private final static String newLine = System.getProperty("line.separator");
	
	/**
	 * @param array
	 * @return the array as string, separated by commas
	 */
	public static String arrayToString(String[] array) {
		String string = "";
		for(int i=0; i<array.length-1; i++) {
			string += array[i] + ", ";
		}
		string += array[array.length-1];
		return string;
	}
	
	/**
	 * pattern of a valid email string
	 */
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	/**
	 * @param email
	 * @return true, if the email is valid
	 */
	public static boolean validateEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		return matcher.find();
	}
	
	/**
	 * @return a random hash as string
	 */
	public static String createRandomHash() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		return output;
	}
	
	/**
	 * @param e
	 * @return the stack trace of the passed Throwable instance as String
	 */
	public static String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append(newLine);
	    }
	    return sb.toString();
	}
}
