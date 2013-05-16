import model.User;
import model.userRights.UserRights;
import controller.UserDbController;

public class Test {
	public static void main(String[] args) {
		UserDbController udbc = new UserDbController();
		
		User user = new User("Sebastian", "Erhardt", "Herr Dr", "email@email.de", "Bachelor", 
				"1234", 12345678, 4, new UserRights(false), false);
		//if(udbc.createUser(user)) System.out.println("user "+user+" created successfully");
		
		// if(udbc.deleteUser(user)) System.out.println("user "+user+" deleted succesfully");
		
		User user1 = new User("email@email.de", "1234");
		
		user1 = udbc.getUser(user1);
		
		System.out.println(user1);
		System.out.println(user1.getUserRights());
	}
}
