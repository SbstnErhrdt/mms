package routes;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.EventRights;
import model.userRights.ModuleHandbookRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;
import model.userRights.UserRights;
import util.Utilities;
import bcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.EmailController;
import controller.UserDbController;

public class UserRoutes extends Routes {
	private UserDbController db;
	private Gson gson = new Gson();
	private final String pepper = "modulmanagementsystemsopra20122013";

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
		respond(response, json);
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
					respond(response, json);
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
		respond(response, json);
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
				System.out.println("actorUser is employee");
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to read all users (actorUser is no employee)", 
						"readUsers(...)")));
				respond(response, json);
				return;
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to read all users (actorUser is not logged in)", 
					"readUsers(...)")));
			respond(response, json);
			return;
		}
		
		ArrayList<User> users = db.readReducedUsers();		
		json = gson.toJson(users);
		
		respond(response, json);
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	/**
	 * creates the corresponding user object passed in the request body as json object
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 * @throws SQLException 
	 */
	public void createUser(HttpServletRequest request,
			HttpServletResponse response) throws SQLException {		
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
				respond(response, json);
				return;
			} else {
				System.out.println("actorUser is admin");
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to create users (actorUser is no employee (and therefore no admin))", 
					"createUser(...)")));
			respond(response, json);
			return;
		}
		
		json = getRequestBody(request);
		
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		
		String password = obj.get("password").getAsString();
		
		User user = gson.fromJson(json, User.class);
		
		if(user == null) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"invalid user object (conversion gson.fromJson(json, User.class) failed)", 
					"createUser(...)")));
			respond(response, json);
			return;
		} 
		
		
		// user is Employee?
		if(user.isEmployee()) {
			Employee employee = gson.fromJson(json, Employee.class);
			user = employee;
		}
		
		user.setPassword(BCrypt.hashpw(password+pepper, BCrypt.gensalt()));
		
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
		respond(response, json);
	}

	/**
	 * updates the corresponding user object with the data passed in the request body as json object
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 * @throws SQLException 
	 */
	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) throws SQLException {
		
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
				respond(response, json);
				return;
			} else {
				System.out.println("actorUser equals user to user");
				
				User oldUser = db.getUser(user);
				
				// check, if user changed his own rights
				if(!oldUser.isEmployee() && user.isEmployee() || oldUser.isEmployee() && !user.isEmployee()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to change his own rights (right=isEmployee) (actorUser is no admin)", 
							"updateUser(...)")));		
					respond(response, json);
					return;
				} else if(user.isEmployee() && oldUser.isEmployee()) {
					Employee employee = (Employee) user;
					Employee oldEmployee = (Employee) oldUser;
						
					EmployeeRights eR = employee.getEmployeeRights();
					EmployeeRights oER = oldEmployee.getEmployeeRights();
					
					// check EmployeeRights
					if(eR.isCanDeblockCriticalModule() && !oER.isCanDeblockCriticalModule() || 
							!eR.isCanDeblockCriticalModule() && oER.isCanDeblockCriticalModule() ||
							eR.isCanDeblockModule() && !oER.isCanDeblockModule() ||
							!eR.isCanDeblockModule() && oER.isCanDeblockModule() ||
							eR.isAdmin() && !oER.isAdmin() ||
							!eR.isAdmin() && oER.isAdmin()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"actorUser is not allowed to change his own rights (rights=EmployeeRights) (actorUser is no admin)", 
								"updateUser(...)")));		
						respond(response, json);
						return;
					}
					// check ContentRights
					if(!eR.getEventRightsList().isEmpty() && oER.getEventRightsList().isEmpty() ||
							eR.getEventRightsList().isEmpty() && !oER.getEventRightsList().isEmpty() ||
							!eR.getModuleRightsList().isEmpty() || oER.getModuleRightsList().isEmpty() || 
							eR.getModuleRightsList().isEmpty() || !oER.getModuleRightsList().isEmpty() ||
							!eR.getSubjectRightsList().isEmpty() && oER.getSubjectRightsList().isEmpty() ||
							eR.getSubjectRightsList().isEmpty() && !oER.getSubjectRightsList().isEmpty() ||
							!eR.getModuleHandbookRightsList().isEmpty() && oER.getModuleHandbookRightsList().isEmpty() ||
							eR.getModuleHandbookRightsList().isEmpty() && !oER.getModuleHandbookRightsList().isEmpty() ||
							!eR.getStudycourseRightsList().isEmpty() && oER.getStudycourseRightsList().isEmpty() ||
							eR.getStudycourseRightsList().isEmpty() && !oER.getStudycourseRightsList().isEmpty() ||
							// list sizes
							eR.getEventRightsList().size() != oER.getEventRightsList().size() ||
							eR.getModuleRightsList().size() != oER.getModuleRightsList().size() ||
							eR.getSubjectRightsList().size() != oER.getSubjectRightsList().size() ||
							eR.getModuleHandbookRightsList().size() != oER.getModuleHandbookRightsList().size() ||
							eR.getStudycourseRightsList().size() != oER.getStudycourseRightsList().size()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"actorUser is not allowed to change his own rights (rights=ContentRightsList) (actorUser is no admin)", 
								"updateUser(...)")));		
						respond(response, json);
						return;
					} else {
						// check ContentRights
						
						ArrayList<EventRights> eventRightsList = eR.getEventRightsList();
						ArrayList<EventRights> oldEventRightsList = oER.getEventRightsList();
						
						// check if EventRights changed
						for(EventRights er : eventRightsList) {
							if(!oldEventRightsList.contains(er)) {
								json = gson.toJson(new JsonErrorContainer(new JsonError(
										"actorUser is not allowed to change his own rights (rights=EventRights) (actorUser is no admin)", 
										"updateUser(...)")));		
								respond(response, json);
								return;								
							}
						}
						
						ArrayList<ModuleRights> moduleRightsList = eR.getModuleRightsList();
						ArrayList<ModuleRights> oldModuleRightsList = oER.getModuleRightsList();
						
						// check if ModuleRights changed
						for(ModuleRights mr : moduleRightsList) {
							if(!oldModuleRightsList.contains(mr)) {
								json = gson.toJson(new JsonErrorContainer(new JsonError(
										"actorUser is not allowed to change his own rights (rights=ModuleRights) (actorUser is no admin)", 
										"updateUser(...)")));		
								respond(response, json);
								return;								
							}
						}
						
						ArrayList<SubjectRights> subjectRightsList = eR.getSubjectRightsList();
						ArrayList<SubjectRights> oldSubjectRightsList = oER.getSubjectRightsList();
						
						// check if SubjectRights changed
						for(SubjectRights sr : subjectRightsList) {
							if(!oldSubjectRightsList.contains(sr)) {
								json = gson.toJson(new JsonErrorContainer(new JsonError(
										"actorUser is not allowed to change his own rights (rights=SubjectRights) (actorUser is no admin)", 
										"updateUser(...)")));		
								respond(response, json);
								return;								
							}
						}						

						ArrayList<ModuleHandbookRights> moduleHandbookRightsList = eR.getModuleHandbookRightsList();
						ArrayList<ModuleHandbookRights> oldModuleHandbookRightsList = oER.getModuleHandbookRightsList();
						
						// check if ModuleHandbookRights changed
						for(ModuleHandbookRights mhr : moduleHandbookRightsList) {
							if(!oldModuleHandbookRightsList.contains(mhr)) {
								json = gson.toJson(new JsonErrorContainer(new JsonError(
										"actorUser is not allowed to change his own rights (rights=ModuleHandbookRights) (actorUser is no admin)", 
										"updateUser(...)")));		
								respond(response, json);
								return;								
							}
						}	

						ArrayList<StudycourseRights> studycourseRightsList = eR.getStudycourseRightsList();
						ArrayList<StudycourseRights> oldStudycourseRightsList = oER.getStudycourseRightsList();
						
						// check if StudycourseRights changed
						for(StudycourseRights scr : studycourseRightsList) {
							if(!oldStudycourseRightsList.contains(scr)) {
								json = gson.toJson(new JsonErrorContainer(new JsonError(
										"actorUser is not allowed to change his own rights (rights=StudycourseRights) (actorUser is no admin)", 
										"updateUser(...)")));		
								respond(response, json);
								return;								
							}
						}	
					}
				}		
			}
		}
		
		// user is Employee?
		if(user.isEmployee()) {
			Employee employee = gson.fromJson(json, Employee.class);
			user = employee;
		}
		
		if(db.updateUser(user)) {
			json = gson.toJson(user);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"db.updateUser(user) failed", 
					"updateUser(...)")));
		}
		
		respond(response, json);
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
				
			// add pepper to encrypt password later
			userPassword = userPassword+pepper;
			
			System.out.println("email: "+userEmail+", password (with pepper): "+userPassword);
			
			
			User user = new User(userEmail, userPassword);
			
			System.out.println(user);
			
			// check, if there such an email and (hashed) password combination
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
		respond(response, json);
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
			respond(response, json);
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
				respond(response, json);
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
	 * @throws SQLException 
	 */
	public void register(HttpServletRequest request,
			HttpServletResponse response) throws SQLException {
		String json = getRequestBody(request);
		
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		
		String password = obj.get("password").getAsString();
		password = BCrypt.hashpw(password+pepper, BCrypt.gensalt());
		
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
				
				
				if(db.createUser(user)) {			
					if(db.insertConfirmationHash(email, hash)) {
						json = gson.toJson(user);
					} else {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"registration failed (db.insertConfirmationHash(email, hash) failed)", 
								"register(...)")));
					} 
					if(!EmailController.sendEmail(email, hash)) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"registration failed (sendEmail(...) to users email address failed)", 
								"register(...)")));
					}
				} else {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"registration failed (db.createUser(user) failed)", 
							"register(...)")));
				}
			}
		}
		respond(response, json);
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
		respond(response, json);
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
		respond(response, json);	
	}
	
	/**
	 * closes database connection 
	 */
	public void closeConnection() {
		db.closeConnection();
	}
}