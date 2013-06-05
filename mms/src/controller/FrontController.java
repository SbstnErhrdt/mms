package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routes.UserRoutes;
import routes.ContentRoutes;


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

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	doGet(request, response);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String path = request.getServletPath();
		System.out.println("GET-Request, Path: " + path);
		UserRoutes userRoutes = new UserRoutes();
		ContentRoutes contentRoutes = new ContentRoutes();		
		
		// ####################################################
		// Content
		// ####################################################
		
		// read Event
		if(path.equals("/read/event")) {
			contentRoutes.readEvent(request, response);
		// delete Event
		} else if(path.equals("/delete/event")) {
			contentRoutes.deleteEvent(request, response);
		// read Events
		} else if(path.equals("/read/events")) {
			contentRoutes.readEvents(request, response);
		}
			
		// read Module
		else if(path.equals("/read/module")) {
			contentRoutes.readModule(request, response);
		// delete Module
		} else if(path.equals("/delete/module")) {
			contentRoutes.deleteModule(request, response);
		// read Modules
		} else if(path.equals("/read/modules")) {
			contentRoutes.readModules(request, response);
		}
		
		// read Subject
		else if(path.equals("/read/subject")) {
			contentRoutes.readSubject(request, response);
		// delete Module
		} else if(path.equals("/delete/subject")) {
			contentRoutes.deleteSubject(request, response);
		// read Modules
		} else if(path.equals("/read/subjects")) {
			contentRoutes.readSubjects(request, response);
		}
		
		// read Studycourse
		else if(path.equals("/read/studycourse")) {
			contentRoutes.readStudycourse(request, response);
		// delete Studycourse
		} else if(path.equals("/delete/studycourse")) {
			contentRoutes.deleteStudycourse(request, response);
		// read Studycourses
		} else if(path.equals("/read/studycourses")) {
			contentRoutes.readStudycourses(request, response);
		}
		
		// read ModuleHandbook
		else if(path.equals("/read/modulehandbook")) {
			contentRoutes.readModuleHandbook(request, response);
		// delete ModuleHandbook
		} else if(path.equals("/delete/modulehandbook")) {
			contentRoutes.deleteModuleHandbook(request, response);
		// read ModuleHandbooks
		} else if(path.equals("/read/modulehandbooks")) {
			contentRoutes.readModuleHandbooks(request, response);
		}	
		
		// ####################################################
		// User
		// ####################################################
		
		// read User
		else if(path.equals("/read/user")) {
			userRoutes.readUser(request, response);
		// delete User
		} else if(path.equals("/delete/user")) {
			userRoutes.deleteUser(request, response);
		// read Users
		} else if(path.equals("/read/users")) {
			userRoutes.readUsers(request, response);
		}
		
		// set Headers
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "accept, origin, x-requested-with, content-type");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "15");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		System.out.println("POST-Request, Path: " + path);
		UserRoutes userRoutes = new UserRoutes();
		ContentRoutes contentRoutes = new ContentRoutes();
				
		// ####################################################
		// Content
		// ####################################################
		
		// create Event
		if(path.equals("/create/event")) {
			contentRoutes.createEvent(request, response);
		// update Event
		} else if(path.equals("/update/event")) {
			contentRoutes.updateEvent(request, response);
		}
		
		// create Module
		else if(path.equals("/create/module")) {
			contentRoutes.createModule(request, response);
		// update Module
		} else if(path.equals("/update/module")) {
			contentRoutes.updateModule(request, response);
		}
		
		// create Subject
		else if(path.equals("/create/subject")) {
			contentRoutes.createSubject(request, response);
		// update Subject
		} else if(path.equals("/update/subject")) {
			contentRoutes.updateSubject(request, response);
		}
		
		// create Studycourse
		else if(path.equals("/create/studycourse")) {
			contentRoutes.createStudycourse(request, response);
		// update Studycourse
		} else if(path.equals("/update/studycourse")) {
			contentRoutes.updateStudycourse(request, response);
		}
		
		// create ModuleHandbook
		else if(path.equals("/create/modulehandbook")) {
			contentRoutes.createModuleHandbook(request, response);
		// update ModuleHandbook
		} else if(path.equals("/update/modulehandbook")) {
			contentRoutes.updateModuleHandbook(request, response);
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
		
		// set Headers
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "accept, origin, x-requested-with, content-type");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "15");
	}

}