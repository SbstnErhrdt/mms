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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getServletPath();
		System.out.println("Path: " + path);
		
		try {	
			if(path.equals("/login")) {
				if(UserRoutes.login(request)) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/index.jsp");
					dispatcher.forward( request, response );
				}
			} else if(path.equals("/register")) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
				dispatcher.forward( request, response );
			} else if(path.equals("/completeregister")) {
				
				UserRoutes.register(request);
				System.out.println("registering complete");
			
			} else if(path.equals("/")) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/index.jsp");
				dispatcher.forward( request, response );
			}
		} catch (ServletException e) {
			e.printStackTrace();
		}	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}