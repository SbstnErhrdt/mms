package controller;

import handler.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String path = request.getServletPath();
		System.out.println("Path: " + path);
		UserRoutes userRoutes = new UserRoutes();
		ContentRoutes contentRoutes = new ContentRoutes();
		
		
		// ####################################################
		// Content
		// ####################################################
		
		// read Event
		if(path.equals("/read/event")) {
			contentRoutes.readEvent(request, response);
		/// delete Event
		} else if(path.equals("delete/event")) {
			contentRoutes.deleteEvent(request, response);
		// read Events
		} else if(path.equals("read/events")) {
			contentRoutes.readEvents(request, response);
		}
			
		// read Module
		else if(path.equals("read/module")) {
			contentRoutes.readModule(request, response);
		// delete Module
		} else if(path.equals("delete/module")) {
			contentRoutes.deleteModule(request, response);
		// read Modules
		} else if(path.equals("read/modules")) {
			contentRoutes.readModules(request, response);
		}
		
		// read Subject
		else if(path.equals("read/subject")) {
			contentRoutes.readSubject(request, response);
		// delete Module
		} else if(path.equals("delete/subject")) {
			contentRoutes.deleteSubject(request, response);
		// read Modules
		} else if(path.equals("read/subjects")) {
			contentRoutes.readSubjects(request, response);
		}
		
		// read Studycourse
		else if(path.equals("read/studycourse")) {
			contentRoutes.readStudycourse(request, response);
		// delete Studycourse
		} else if(path.equals("delete/studycourse")) {
			contentRoutes.deleteStudycourse(request, response);
		// read Studycourses
		} else if(path.equals("read/studycourses")) {
			contentRoutes.readStudycourses(request, response);
		}
		
		// read ModuleHandbook
		else if(path.equals("read/modulehandbook")) {
			contentRoutes.readModuleHandbook(request, response);
		// delete ModuleHandbook
		} else if(path.equals("delete/modulehandbook")) {
			contentRoutes.deleteModuleHandbook(request, response);
		// read ModuleHandbooks
		} else if(path.equals("read/modulehandbooks")) {
			contentRoutes.readModuleHandbooks(request, response);
		}		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}