package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.UserDbController;

import model.Employee;
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
		String json;
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			User user = db.getUser(new User(email));
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
							"unspecified email parameter in query", 
							"readUser(...)")));
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
		
		User actorUser = getActorUser(request);
		
		boolean hasRights = false;
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				hasRights = false;
				System.out.println("actorUser is no admin");
			} else {
				hasRights = true;
				System.out.println("actorUser is admin");
			}
		}
		
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			
			// check rights
			if(!hasRights) {
				if(!email.equals(actorUser.getEmail())) {
					System.out.println("actorUser does not equal user to delete");
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to delete this user (actorUser is no admin and does not equal user to delete)", 
							"deleteUser(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} else {
					System.out.println("actorUser equals user to delete");
				}
			}
			
			User user = new User(email);
			if(db.deleteUser(user))	json = gson.toJson(user);
			else {
				json = gson.toJson(new JsonContent(new JsonError(
						"db.deleteUser(user) failed", 
						"deleteUser(...)")));
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified email parameter in query", 
					"deleteUser(...)")));
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
		String json;
		
		User actorUser = getActorUser(request);

		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is no admin");
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to create users (actorUser is no admin)", 
						"createUser(...)")));
				try { 
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
				System.out.println("actorUser is admin");
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"not allowed to create users (actorUser is employee (and therefore no admin))", 
					"createUser(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		if(db.createUser(user)) {
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"db.createUser(user) failed", 
					"createUser(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		
		User actorUser = getActorUser(request);
		
		boolean hasRights = false;
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				hasRights = false;
				System.out.println("actorUser is no admin");
			} else {
				hasRights = true;
				System.out.println("actorUser is admin");
			}
		}
		
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		// check rights
		if(!hasRights) {
			if(!user.getEmail().equals(actorUser.getEmail())) {
				System.out.println("actorUser does not equal user to delete");
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to delete this user (actorUser is no admin and does not equal user to update)", 
						"updateUser(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
				System.out.println("actorUser equals user to delete");
			}
		}
		
		if(db.updateUser(user)) {
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"db.updateUser(user) failed", 
					"updateUser(...)")));
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
			String email = request.getParameter("email").toLowerCase();
			json = getRequestBody(request);
			System.out.println(json);
			JsonObject obj = gson.fromJson(json, JsonObject.class);
			
			String userEmail = obj.get("email").getAsString().toLowerCase();
			String userPassword = obj.get("password").getAsString();
			
			System.out.println("email: "+userEmail+", password: "+userPassword);
			
			User user = new User(userEmail, userPassword);
			
			System.out.println(user);
			
			user = db.verifyUser(user);
		
			System.out.println("DEBUG1");
			
			System.out.println(user);
			
			if(user != null) {
				user = db.getUser(user);				
				if(!user.getUserRights().getCanLogin()){
					System.out.println("DEBUG3");
					
					// User cannot login (email not verified)
					System.out.println("user "+user+" cannot login (email not verified)");
					json = gson.toJson(new JsonContent(new JsonError("user cannot login (email not verified)", "login(...)")));
				} else if(user.getEmail().equals(email)) {
					System.out.println("DEBUG4");
					
					System.out.println("user "+user+" verified!");
					
					HttpSession session = request.getSession();
				
					String sessionID = session.getId();
					if(db.insertUserHash(email, sessionID)) {
						System.out.println("DEBUG5");
						
						json = "[" + gson.toJson(db.getUser(user));
						
						json += ", {\"jsessionID\" : "+"\""+sessionID+"\"}]";
						
						System.out.println(json);
				
						// write email in session
						session.setAttribute("email", email);
						
						System.out.println("user "+user+" logged in successfully");
					} else {
						System.out.println("DEBUG6");
						
						json = gson.toJson(new JsonContent(new JsonError("db.insertUserHash(email, hash) failed", 
								"login(...)")));
					}
				} else {
					System.out.println("DEBUG7");
					
					System.out.println("wrong email parameter.");
					json = gson.toJson(new JsonContent(new JsonError("wrong email parameter in query", 
							"login(...)")));
				}
			} else {
				System.out.println("DEBUG8");
				
				json = gson.toJson(new JsonContent(new JsonError("wrong email or password", 
						"login(...)")));
				}
		} else {
			System.out.println("DEBUG9");
			
			json = gson.toJson(new JsonContent(new JsonError("unspecified email parameter in query", 
					"login(...)")));
		}
		System.out.println("DEBUG10");
		
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean verifyUserHash(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
	
		String hash = "";
		String email = "";
		
		if(cookies == null) {
			String json = gson.toJson(new JsonContent(new JsonError(
					"no valid hash found (request.getCookies() == null)", 
					"verifyUserHash(...)")));
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {	
			hash = request.getSession().getId();
			email = (String) request.getSession().getAttribute("email");
			
			System.out.println("email="+email+", hash="+hash);
			
			if(db.verifyUserHash(email, hash)) return true;
			else {
				String json = gson.toJson(new JsonContent(new JsonError(
						"no valid hash found", 
						"verifyUserHash(...)")));
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
	}

	public void register(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		User user = gson.fromJson(json, User.class);
		
		if(user == null) {
			json = gson.toJson(new JsonContent(new JsonError(
					"user object is null", 
					"register(...)")));
		} else {
			if(user.isEmployee()) {
				json = gson.toJson(new JsonContent(new JsonError(
						"cannot register users who are employees", 
						"register(...)")));
			} else {
				user.setUserRights(new UserRights(false));	// canLogin == false
				db.createUser(user);
				json = gson.toJson(user);
			}
		}
		try { 
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readActiveUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		
		String sessionID = request.getSession().getId();
		
		String email = db.getHashEmail(sessionID);
		
		if(email == null) {
			json = gson.toJson(new JsonContent(new JsonError(
					"no email found for this sessionID", 
					"readActiveUser(...)")));
		} else {
			User user = db.getUser(new User(email));
			
			if(user != null) {
				json = gson.toJson(user);
			} else {
				json = gson.toJson(new JsonContent(new JsonError(
						"no user found for this email", 
						"readActiveUser(...)")));
			}
		}
		try { 
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}