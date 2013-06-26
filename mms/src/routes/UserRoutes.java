package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.EmailController;
import controller.UserDbController;

import model.Employee;
import model.User;
import model.userRights.UserRights;

import util.Utilities;

public class UserRoutes extends Routes {
	private UserDbController db;
	private Gson gson = new Gson();

	public UserRoutes() {
		db = new UserDbController();
	}
	
	// ####################################################
	// GET Methods
	// ####################################################
	
	/**
	 * reads the corresponding user object with the email parameter in the query 
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 */
	public void readUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		if(request.getParameter("email") != null) {
			String email = request.getParameter("email");
			User user = db.getUser(new User(email));
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
							"unspecified email parameter in query", 
							"readUser(...)")));
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * deletes the corresponding user object with the email parameter in the query 
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 */
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
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"db.deleteUser(user) failed", 
						"deleteUser(...)")));
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified email parameter in query", 
					"deleteUser(...)")));
			}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads all user objects and writes them in the response as json object
	 * @param request
	 * @param response
	 */
	public void readUsers(HttpServletRequest request,
			HttpServletResponse response) {
		
		String json;
		
		// check rights
		User actorUser = getActorUser(request);
		if(actorUser != null) {
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(!actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is no admin");
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"not allowed to read all users (actorUser is no admin)", 
							"readUsers(...)")));
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to read all users (actorUser is no employee (and therefore no admin))", 
						"readUsers(...)")));
				try { 
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to read all users (actorUser is not logged in)", 
					"readUsers(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		ArrayList<User> users = db.readReducedUsers();		
		json = gson.toJson(users);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	/**
	 * creates the corresponding user object passed in the request body as json object
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 */
	public void createUser(HttpServletRequest request,
			HttpServletResponse response) {		
		String json;
		
		User actorUser = getActorUser(request);

		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is no admin");
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to create users (actorUser is no employee (and therefore no admin))", 
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
		
		// validate email
		if(!Utilities.validateEmail(user.getEmail())) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"user to create has invalid email", 
					"createUser(...)")));
		} else if(db.createUser(user)) {
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"db.createUser(user) failed", 
					"createUser(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * updatess the corresponding user object with the data passed in the request body as json object
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 */
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
				System.out.println("actorUser does not equal user to update");
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to delete this user (actorUser is no admin and does not equal user to update)", 
						"updateUser(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
				System.out.println("actorUser equals user to user");
			}
		}
		
		if(db.updateUser(user)) {
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"db.updateUser(user) failed", 
					"updateUser(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * checks if the passed json user object contains valid email and password combination
	 * and checks if the user can login
	 * if yes: writes the whole json user object in the response
	 * if no: returns error message
	 * @param request
	 * @param response
	 */
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
			
			if(user != null) {
				user = db.getUser(user);				
				if(!user.getUserRights().getCanLogin()){					
					// User cannot login (email not verified)
					System.out.println("user "+user+" cannot login (email not verified)");
					json = gson.toJson(new JsonErrorContainer(new JsonError("user cannot login (email not verified)", "login(...)")));
				} else if(user.getEmail().equals(email)) {
					
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
						json = gson.toJson(new JsonErrorContainer(new JsonError("db.insertUserHash(email, hash) failed", 
								"login(...)")));
					}
				} else {					
					System.out.println("wrong email parameter.");
					json = gson.toJson(new JsonErrorContainer(new JsonError("wrong email parameter in query", 
							"login(...)")));
				}
			} else {				
				json = gson.toJson(new JsonErrorContainer(new JsonError("wrong email or password", 
						"login(...)")));
				}
		} else {			
			json = gson.toJson(new JsonErrorContainer(new JsonError("unspecified email parameter in query", 
					"login(...)")));
		}		
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return true if a valid hash was passed in the cookie, false if no valid hash was passsed in the cookie
	 */
	public boolean verifyUserHash(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
	
		String hash = "";
		String email = "";
		
		if(cookies == null) {
			String json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				String json = gson.toJson(new JsonErrorContainer(new JsonError(
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

	/**
	 * singns up a new user object passed in the request body as json object and makes 
	 * sure that this user has no advanced rights and valid email, therefore it invokes an
	 * email confirmation process 
	 * @param request
	 * @param response
	 */
	public void register(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		
		String password = obj.get("password").getAsString();
		
		User user = gson.fromJson(json, User.class);
				
		if(user == null) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"registration failed (user object is null)", 
					"register(...)")));
		} else {
			user.setPassword(password);	// set password manually
			if(user.isEmployee()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"registration failed (cannot register users who are employees)", 
						"register(...)")));
			} else if(!Utilities.validateEmail(user.getEmail())) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"registration failed (invalid email)", 
						"register(...)")));
			} else {
				String email = user.getEmail();
				String hash = Utilities.createRandomHash();
				
				// overwrite userRights if any, so the user can't register with advanced rights
				user.setUserRights(new UserRights(false));	// canLogin == false
				
				if(!EmailController.sendEmail(email, hash)) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"registration failed (sendEmail(...) to users email address failed)", 
							"register(...)")));
				} else if(db.createUser(user)) {			
					if(db.insertConfirmationHash(email, hash)) {
						json = gson.toJson(user);
					} else {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"registration failed (db.insertConfirmationHash(email, hash) failed)", 
								"register(...)")));
					}
				} else {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"registration failed (db.createUser(user) failed)", 
							"register(...)")));
				}
			}
		}
		try { 
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the active user associated with the passed cookie sessionID
	 * @param request
	 * @param response
	 */
	public void readActiveUser(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		
		String sessionID = request.getSession().getId();
		
		String email = db.getHashEmail(sessionID);
		
		if(email == null) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"no email found for this sessionID", 
					"readActiveUser(...)")));
		} else {
			User user = db.getUser(new User(email));
			
			if(user != null) {
				json = gson.toJson(user);
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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

	/**
	 * checks if the passed token is a valid hash for an existing email
	 * @param request
	 * @param response
	 */
	public void confirmEmail(HttpServletRequest request,
			HttpServletResponse response) {
		
		String json = "";
		
		if(request.getParameter("token") != null) {
			String token = request.getParameter("token");
			
			String email = db.getConfirmationEmail(token);
			
			if(email != null) {
				db.deleteConfirmationHash(email);
				json = "{\"success\":true}";
				
			} else {
				json = "{\"success\":false}";
			}
			
		} else { 
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified token parameter in query", 
					"confirmEmail(...)")));
		}
		
		try { 
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * closes database connection 
	 */
	public void closeConnection() {
		db.closeConnection();
	}
}