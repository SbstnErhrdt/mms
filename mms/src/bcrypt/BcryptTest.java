package bcrypt;

public class BcryptTest {
	
	private static String pepper = "hanskuckindieLuft";
	
	private static String password = "tim";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		String pass = password;
		password += pepper;
		
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

		System.out.println("Password1: "+ password);
		//System.out.println("Hashed: "+hashed);
		
		
		String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt());

		System.out.println("Password2: "+ password);
		
		System.out.println("Hashed: "+ hashed2);
		

		if (BCrypt.checkpw(pass+pepper, hashed))
			System.out.println("It matches");
		else
			System.out.println("It does not match");
		
		if (BCrypt.checkpw(password, hashed2))
			System.out.println("It matches");
		else
			System.out.println("It does not match");

		
	}

}
