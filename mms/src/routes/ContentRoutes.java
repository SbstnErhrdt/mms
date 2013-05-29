package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.content.Event;
import model.content.Module;
import model.content.Studycourse;
import model.content.Subject;

import com.google.gson.Gson;

import controller.ContentDbController;

public class ContentRoutes extends Routes{
	private ContentDbController db;
	private Gson gson = new Gson();

	// ####################################################
	// GET Methods
	// ####################################################
	
	public void readEvent(HttpServletRequest request,
			HttpServletResponse response) {
		int eventID = Integer.parseInt(request.getParameter("eventID"));
		
		Event event = db.getEvent(eventID);
		
		String json = gson.toJson(event);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void deleteEvent(HttpServletRequest request,
			HttpServletResponse response) {
		int eventID = Integer.parseInt(request.getParameter("eventID"));
		
		if(db.deleteEvent(new Event(eventID))) {
			String json = gson.toJson(eventID);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void readEvents(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		
		ArrayList<Event> events = db.getModuleEvents(moduleID);
		
		String json = gson.toJson(events);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readModule(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		
		Module module = db.getModule(moduleID);
		
		String json = gson.toJson(module);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void deleteModule(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		
		if(db.deleteModule(new Module(moduleID))) {
			String json = gson.toJson(moduleID);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void readModules(HttpServletRequest request,
			HttpServletResponse response) {
		int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
		ArrayList<Module> modules = db.getSubjectModules(subjectID);
		
		String json = gson.toJson(modules);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSubject(HttpServletRequest request,
			HttpServletResponse response) {
		int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
		Subject subject = db.getSubject(subjectID);
		
		String json = gson.toJson(subject);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void deleteSubject(HttpServletRequest request,
			HttpServletResponse response) {
		int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
		if(db.deleteSubject(new Subject(subjectID))) {
			String json = gson.toJson(subjectID);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	public void readSubjects(HttpServletRequest request,
			HttpServletResponse response) {
		int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
		ArrayList<Subject> subjects = db.getStudycourseSubjects((studycourseID));
		
		String json = gson.toJson(subjects);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void readStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
		Studycourse studycourse = db.getStudycourse(studycourseID);
		
		String json = gson.toJson(studycourse);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void deleteStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
		if(db.deleteStudycourse(new Studycourse(studycourseID))) {
			String json = gson.toJson(studycourseID);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	public void readStudycourses(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void readModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void deleteModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void readModuleHandbooks(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	public void createEvent(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void updateEvent(HttpServletRequest request,
			HttpServletResponse response) {
		int eventID = Integer.parseInt(request.getParameter("eventID"));
		
		String json = getRequestBody(request);
		
		Event event = gson.fromJson(json, Event.class);
		
		if(db.updateEvent(event)) {
			json = gson.toJson(event);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	public void createModule(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void updateModule(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void createSubject(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void updateSubject(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void createStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void updateStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
