package routes;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


import controller.DbController;
import controller.UserDbController;

import model.User;
import model.userRights.UserRights;

public class UserRoutes {
	private UserDbController db;
	private Gson gson = new Gson();

	public UserRoutes() {
		db = new UserDbController();
	}
	
	// ####################################################
	// GET Methods
	// ####################################################
	
	public void readUser(HttpServletRequest request,
			HttpServletResponse response) {
		String email = request.getParameter("email");
		
		User user = new User("email@email.de", "123457");
		
		String json = gson.toJson(user);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void deleteUser(HttpServletRequest request,
			HttpServletResponse response) {
		
		String email = request.getParameter("email");
		
	}

	public void readUsers(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	public void createUser(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		
		String email = request.getParameter("email");
		
	}
}