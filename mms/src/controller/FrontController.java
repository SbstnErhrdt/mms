package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routes.UserRoutes;
import routes.ContentRoutes;
import util.Utilities;


/**
 * Servlet implementation class FrontController
 */
@WebServlet("/")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	try {
	    	String path = request.getServletPath();
			System.out.println("OPTIONS-Request, Path: " + path);
	    	request.getSession(true);
	    	
	    	// set Headers
			response.setHeader("Access-Control-Allow-Origin", "http://sopra.ex-studios.net");
			response.setHeader("Access-Control-Allow-Headers", "accept, origin, withcredentials, x-requested-with, content-type");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Max-Age", "15");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Content-Type", "application/json; charset=utf-8");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserRoutes userRoutes = null;
		ContentRoutes contentRoutes = null;
		try {
			
			// new UserDbController().ichScheissAufDichSeb();
			
			String path = request.getServletPath();
			System.out.println("GET-Request, Path: " + path);	
			
			// set Headers
			response.setHeader("Access-Control-Allow-Origin", "http://sopra.ex-studios.net");
			response.setHeader("Access-Control-Allow-Headers", "accept, origin, x-requested-with, content-type");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Max-Age", "15");
			response.setHeader("Content-Type", "application/json; charset=utf-8");
		
			// of rights sensitive route, verify the user hash
			if(path.startsWith("/delete") || path.startsWith("/update") || path.equals("/read/activeUser")) {
				userRoutes = new UserRoutes();	
				if(!userRoutes.verifyUserHash(request, response)) {
					System.out.println("no valid hash found");
					System.out.println("closing database connections");
					userRoutes.closeConnection();
					return;
				} else System.out.println("user has valid hash");
				
				// active User
				if(path.equals("/read/activeUser")) {
					userRoutes.readActiveUser(request, response);
				}
			} 
			
			// ####################################################
			// Content
			// ####################################################
			
			// read Event
			else if(path.equals("/read/event")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readEvent(request, response);
			// delete Event
			} else if(path.equals("/delete/event")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteEvent(request, response);
			// read Events
			} else if(path.equals("/read/events")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readEvents(request, response);
			}
				
			// read Module
			else if(path.equals("/read/module")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readModule(request, response);
			// delete Module
			} else if(path.equals("/delete/module")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteModule(request, response);
			// read Modules
			} else if(path.equals("/read/modules")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readModules(request, response);
			// export module
			} else if(path.equals("/export/module")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.exportModule(request, response, this);
			}
			
			// read Subject
			else if(path.equals("/read/subject")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readSubject(request, response);
			// delete Subject
			} else if(path.equals("/delete/subject")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteSubject(request, response);
			// read Subjects
			} else if(path.equals("/read/subjects")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readSubjects(request, response);
			}
			
			// read Studycourse
			else if(path.equals("/read/studycourse")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readStudycourse(request, response);
			// delete Studycourse
			} else if(path.equals("/delete/studycourse")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteStudycourse(request, response);
			// read Studycourses
			} else if(path.equals("/read/studycourses")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readStudycourses(request, response);
			}
			
			// read ModuleHandbook
			else if(path.equals("/read/modulehandbook")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readModuleHandbook(request, response);
			// delete ModuleHandbook
			} else if(path.equals("/delete/modulehandbook")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteModuleHandbook(request, response);
			// read ModuleHandbooks
			} else if(path.equals("/read/modulehandbooks")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readModuleHandbooks(request, response);
			}
			
			// read ModuleTemplate
			else if(path.equals("/read/moduletemplate")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readModuleTemplate(request, response);
			// delete ModuleTemplate
			} else if(path.equals("/delete/moduletemplate")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteModuleTemplate(request, response);
			}
			
			// ####################################################
			// User
			// ####################################################
			
			// read User
			else if(path.equals("/read/user")) {
				userRoutes = new UserRoutes();
				userRoutes.readUser(request, response);
			// delete User
			} else if(path.equals("/delete/user")) {
				userRoutes = new UserRoutes();
				userRoutes.deleteUser(request, response);
			// read Users
			} else if(path.equals("/read/users")) {
				userRoutes = new UserRoutes();
				userRoutes.readUsers(request, response);
			}
			
			// logout
			else if(path.equals("/logout")) {
				request.getSession().invalidate();
			}
			
			// read Deadline
			else if(path.equals("/read/deadline")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readDeadline(request, response);
			// delete Deadline
			} else if(path.equals("/delete/deadline")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.deleteDeadline(request, response);
			// read Deadlines	
			} else if(path.equals("/read/deadlines")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.readDeadlines(request, response);
			}
			
			// confirm email
			else if(path.equals("/confirm")) {
				userRoutes = new UserRoutes();
				userRoutes.confirmEmail(request, response);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			response.getWriter().write(e + "\n"+Utilities.stackTraceToString(e));
		} finally {
			System.out.println("closing database connections");
			if(userRoutes != null) userRoutes.closeConnection();
			if(contentRoutes != null) contentRoutes.closeConnection();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserRoutes userRoutes = null;
		ContentRoutes contentRoutes = null;
		try {
			String path = request.getServletPath();
			System.out.println("POST-Request, Path: " + path);
		
			// set Headers
			response.setHeader("Access-Control-Allow-Origin", "http://sopra.ex-studios.net");
			response.setHeader("Access-Control-Allow-Headers", "accept, origin, x-requested-with, content-type");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Max-Age", "15");
			response.setHeader("Content-Type", "application/json; charset=utf-8");
	    
			// login
			if(path.equals("/login")) {
				userRoutes = new UserRoutes();
				userRoutes.login(request, response);
			// register
			} else if(path.equals("/register")) { 
				userRoutes = new UserRoutes();
				userRoutes.register(request, response);
			// import module
			} else if(path.equals("/import/modules")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.importModules(request, response, this);
			// check if the user is logged in correctly
			} else if(!(userRoutes = new UserRoutes()).verifyUserHash(request, response)) {
				System.out.println("no valid hash found");
				System.out.println("closing database connections");
				userRoutes.closeConnection();
				return;
			} else {
				System.out.println("user has valid hash");
			}
					
			// ####################################################
			// Content
			// ####################################################
			
			// create Event
			if(path.equals("/create/event")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createEvent(request, response);
			// update Event
			} else if(path.equals("/update/event")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateEvent(request, response);
			}
			
			// create Module
			else if(path.equals("/create/module")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createModule(request, response);
			// update Module
			} else if(path.equals("/update/module")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateModule(request, response);
			}
			
			// create Subject
			else if(path.equals("/create/subject")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createSubject(request, response);
			// update Subject
			} else if(path.equals("/update/subject")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateSubject(request, response);
			}
			
			// create Studycourse
			else if(path.equals("/create/studycourse")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createStudycourse(request, response);
			// update Studycourse
			} else if(path.equals("/update/studycourse")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateStudycourse(request, response);
			}
			
			// create ModuleHandbook
			else if(path.equals("/create/modulehandbook")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createModuleHandbook(request, response);
			// update ModuleHandbook
			} else if(path.equals("/update/modulehandbook")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateModuleHandbook(request, response);
			}
			
			// create ModuleTemplate
			else if(path.equals("/create/moduletemplate")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createModuleTemplate(request, response);
			// update ModuleTemplate
			} else if(path.equals("/update/moduletemplate")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateModuleTemplate(request, response);
			}
			
			// ####################################################
			// User
			// ####################################################
			
			// create User
			else if(path.equals("/create/user")) {
				userRoutes.createUser(request, response);
			// update User
			} else if(path.equals("/update/user")) {
				userRoutes.updateUser(request, response);
			}
			
			//create Deadline
			else if(path.equals("/create/deadline")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.createDeadline(request, response);
			// update Deadline
			} else if(path.equals("/update/deadline")) {
				contentRoutes = new ContentRoutes();
				contentRoutes.updateDeadline(request, response);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			response.getWriter().write(e + "\n"+Utilities.stackTraceToString(e));
		} finally {
			System.out.println("closing database connections");
			if(userRoutes != null) userRoutes.closeConnection();
			if(contentRoutes != null) contentRoutes.closeConnection();
		}
	}
}