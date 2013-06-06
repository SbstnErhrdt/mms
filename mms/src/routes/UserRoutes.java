package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import controller.UserDbController;

import model.User;

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
		
		if(db.deleteUser(user)) {
			String json = gson.toJson(user);
			
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		
		if(db.createUser(user)) {
			json = gson.toJson(new User(user.getEmail()));
			
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		if(db.updateUser(user)) {
			json = gson.toJson(user);
			
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		user = db.verifyUser(user);
		
		if(user != null) {
			if(user.getEmail().equals(email)) {
				System.out.println("user verified!");
				
				String hash = createRandomHash();
				
				if(db.insertUserHash(email, hash)) {
					Cookie hashCookie = new Cookie("hash", hash);
					hashCookie.setMaxAge(60*60*12);
					response.addCookie(hashCookie);
					Cookie emailCookie = new Cookie("email", email);
					emailCookie.setMaxAge(60*60*12);
					response.addCookie(emailCookie);
					
					json = gson.toJson(db.getUser(user));
			
					System.out.println("user "+user+" logged in successfully");
				} else {
					json = "{"+
							"\"error\": { "+
								"\"message\": \"db.insertUserHash(email, hash) failed\", "+
								"\"method\" : \"login(...)\""+
							" }}";
				}
			} else {
				System.out.println("wrong email parameter.");
				
				json = "{"+
						"\"error\": { "+
							"\"message\": \"wrong email parameter\", "+
							"\"method\" : \"login(...)\""+
						" }}";
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"no user with this email and password\", "+
						"\"method\" : \"login(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String createRandomHash() {
		double randomDouble = Math.random();
		return ""+randomDouble;
	}

	public boolean verifyUserHash(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		String email = "";
		String hash = "";
		
		for(Cookie c : cookies) {
			if(c.getName().equals("email")) {
				email = c.getValue();
			} else if(c.getName().equals("hash")) {
				hash = c.getValue();
			}
		}
		
		if(db.verifyUserHash(email, hash)) return true;
		else return false;
	}
}