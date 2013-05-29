package routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


import controller.DbController;
import controller.UserDbController;

import model.User;
import model.userRights.UserRights;

public class UserRoutes extends Routes {
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
		
		User user = db.getUser(new User(email));
		
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
		User user = new User(email);
		db.deleteUser(user);
		
		String json = gson.toJson(user);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readUsers(HttpServletRequest request,
			HttpServletResponse response) {
		ArrayList<User> users = db.readUsers();		
		String json = gson.toJson(users);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	public void createUser(HttpServletRequest request,
			HttpServletResponse response) {
			
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		db.createUser(user);
		
		json = gson.toJson(new User(user.getEmail()));
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		
		String email = request.getParameter("email");
		
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		db.updateUser(user);
		
		json = gson.toJson(user);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}