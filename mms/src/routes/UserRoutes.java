package routes;

import javax.servlet.http.HttpServletRequest;


import controller.DbController;

import model.User;
import model.userRights.UserRights;

public class UserRoutes {
	public static void register(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String title = request.getParameter("title");
		String graduation = request.getParameter("graduation");
		int matricNum = 0;
		int semester = 0;
		if(!request.getParameter("matricNum").equals("")) {
			matricNum = Integer.parseInt(request.getParameter("matricNum"));
		} 
		if(!request.getParameter("semester").equals("")) {
			semester = Integer.parseInt(request.getParameter("semester"));
		}

		if(email != null && password != null) {
			User user = new User(firstName, lastName, title, email, graduation, password, matricNum, semester,
			getDefaultRights(), false);
			user.registerToDb();
		}
	}
	
	public static boolean login(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if(email != null && password != null) {
			User user = new User(email, password);
			boolean success = User.login();
			if(success) {
				System.out.println("successfully logged in as User " + user.getAttributes() + " " );
				return true;
			} else System.out.println("did not log in.");
		}
		return false;
	}
		
	public static UserRights getDefaultRights() {
		UserRights defaultRights = new UserRights();
		return defaultRights;
	}
}