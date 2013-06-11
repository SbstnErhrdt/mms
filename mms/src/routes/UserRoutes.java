package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

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
		String json;
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			User user = db.getUser(new User(email));
			json = gson.toJson(user);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified email\", "+
						"\"method\" : \"readUser(...)\""+
					" }}";	
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			User user = new User(email);
			if(db.deleteUser(user))	json = gson.toJson(user);
			else {
				json = "{"+
						"\"error\": { "+
							"\"message\": \"db.deleteUser(user) failed\", "+
							"\"method\" : \"deleteUser(...)\""+
						" }}";
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified email\", "+
						"\"method\" : \"deleteUser(...)\""+
					" }}";	
		}
		
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
		
		if(db.createUser(user)) {
			json = gson.toJson(user);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"db.createUser(user) failed\", "+
						"\"method\" : \"createUser(...)\""+
					" }}";	
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		if(db.updateUser(user)) {
			json = gson.toJson(user);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"db.updateUser(user) failed\", "+
						"\"method\" : \"updateUser(...)\""+
					" }}";	
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) {
		
		String json;
		
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			json = getRequestBody(request);
			User user = gson.fromJson(json, User.class);
			
			user = db.verifyUser(user);
			
			if(user != null) {
				if(user.getEmail().equals(email)) {
					System.out.println("user verified!");
					
					HttpSession session = request.getSession();
				
					String sessionID = session.getId();
					if(db.insertUserHash(email, sessionID)) {
						json = "[" + gson.toJson(db.getUser(user));
						
						json += ", {\"jsessionID\" : "+"\""+sessionID+"\"}]";
				
						// write email in session
						session.setAttribute("email", "test");
						
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
							"\"message\": \"wrong email or password\", "+
							"\"method\" : \"login(...)\""+
						" }}";
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified email parameter\", "+
						"\"method\" : \"login(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String createRandomHash() {
		double randomDouble = Math.random()+Math.random();
		return ""+randomDouble;
	}

	public boolean verifyUserHash(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		
		String email = "";
		String hash = "";
		
		if(cookies == null) {
			String json = "{"+
					"\"error\": { "+
					"\"message\": \"no valid hash found (request.getCookies() == null)\", "+
					"\"method\" : \"verifyUserHash(...)\""+
				" }}";
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {
		
			for(Cookie c : cookies) {
				if(c.getName().equals("email")) {
					email = c.getValue();
				} else if(c.getName().equals("hash")) {
					hash = c.getValue();
				}
			}
			
			if(db.verifyUserHash(email, hash)) return true;
			else {
				String json = "{"+
						"\"error\": { "+
						"\"message\": \"no valid hash found\", "+
						"\"method\" : \"verifyUserHash(...)\""+
					" }}";
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
	}
}