package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;

import com.google.gson.Gson;

import controller.ContentDbController;

public class ContentRoutes extends Routes{
	private ContentDbController db;
	private Gson gson = new Gson();
	
	public ContentRoutes() {
		db = new ContentDbController();
	}

	// ####################################################
	// GET Methods
	// ####################################################
	
	public void readEvent(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		if(request.getParameter("eventID") != null) {
			int eventID = Integer.parseInt(request.getParameter("eventID"));
			Event event = db.getEvent(eventID);
			json = gson.toJson(event);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified eventID\", "+
						"\"method\" : \"readEvent(...)\""+
					" }}";
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteEvent(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("eventID") != null) {
			int eventID = Integer.parseInt(request.getParameter("eventID"));
			if(db.deleteEvent(new Event(eventID))) {
				json = gson.toJson(eventID);
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified eventID\", "+
						"\"method\" : \"deleteEvent(...)\""+
					" }}";
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readEvents(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			
			ArrayList<Event> events = db.getModuleEvents(moduleID);
			
			json = gson.toJson(events);
		} else if(request.getParameter("subjectID") != null) {
			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
			
			ArrayList<Module> modules = db.getSubjectModules(subjectID);
			
			for(Module m : modules) {
				
			}
			
		} else {
			ArrayList<Event> events = db.getEvents();	
			json = gson.toJson(events);
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readModule(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			Module module = db.getModule(moduleID);
			json = gson.toJson(module);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified moduleID\", "+
						"\"method\" : \"readModule(...)\""+
					" }}";
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void deleteModule(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			if(db.deleteModule(new Module(moduleID))) {
				json = gson.toJson(moduleID);
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified moduleID\", "+
						"\"method\" : \"deleteModule(...)\""+
					" }}";		
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readModules(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("subjectID") != null) {		
			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
			ArrayList<Module> modules = db.getSubjectModules(subjectID);
		
			json = gson.toJson(modules);
		} else {
			ArrayList<Module> modules = db.getModules();
			
			json = gson.toJson(modules);
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSubject(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("subjectID") != null) {		

			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
			
			Subject subject = db.getSubject(subjectID);
			
			json = gson.toJson(subject);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified subjectID\", "+
						"\"method\" : \"readSubject(...)\""+
					" }}";		
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void deleteSubject(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("subjectID") != null) {		
			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
			if(db.deleteSubject(new Subject(subjectID))) {
				json = gson.toJson(subjectID);
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified subjectID\", "+
						"\"method\" : \"deleteSubject(...)\""+
					" }}";		
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void readSubjects(HttpServletRequest request,
			HttpServletResponse response) {
		ArrayList<Subject> subjects = null;
		
		String json = "";
		
		if(request.getParameter("studycourseID") != null) {
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
			subjects = db.getStudycourseSubjects(studycourseID);
		} else if(request.getParameter("moduleHandbookID") != null) {
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
			subjects = db.getModuleHandbookSubjects(moduleHandbookID);
		} else {
			subjects = db.getSubjects();
		}
		json = gson.toJson(subjects);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void readStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("studycourseID") != null) {		
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
			Studycourse studycourse = db.getStudycourse(studycourseID);
		
			json = gson.toJson(studycourse);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified studycourseID\", "+
						"\"method\" : \"readStudycourse(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void deleteStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("studycourseID") != null) {		
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
			if(db.deleteStudycourse(new Studycourse(studycourseID))) {
				json = gson.toJson(studycourseID);
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified studycourseID\", "+
						"\"method\" : \"deleteStudycourse(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void readStudycourses(HttpServletRequest request,
			HttpServletResponse response) {

		ArrayList<Studycourse> studycourses = db.readStudycourses();
		
		String json = gson.toJson(studycourses);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void readModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleHandbookID") != null) {				
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
		
			ModuleHandbook moduleHandbook = db.getModuleHandbook(moduleHandbookID);
		
			json = gson.toJson(moduleHandbook);
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified moduleHandbookID\", "+
						"\"method\" : \"readModuleHandbook(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	public void deleteModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleHandbookID") != null) {						
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
			if(db.deleteModuleHandbook(new ModuleHandbook(moduleHandbookID))) {
				json = gson.toJson(moduleHandbookID);
			}
		} else {
			json = "{"+
					"\"error\": { "+
						"\"message\": \"unspecified moduleHandbookID\", "+
						"\"method\" : \"deleteModuleHandbook(...)\""+
					" }}";
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void readModuleHandbooks(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		ArrayList<ModuleHandbook> moduleHandbooks = null;
		if(request.getParameter("studycourseID") != null) {						
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
			moduleHandbooks = db.readStudycourseModuleHandbooks(studycourseID);
		} else {
			moduleHandbooks = db.readModuleHandbooks();
		}
		
		json = gson.toJson(moduleHandbooks);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	// ####################################################
	// POST Methods
	// ####################################################

	public void createEvent(HttpServletRequest request,
			HttpServletResponse response) {
		
		String json = getRequestBody(request);
		
		Event event = gson.fromJson(json, Event.class);
		
		if(db.createEvent(event)) {
			json = gson.toJson(event);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		
		String json = getRequestBody(request);
		
		Module module = gson.fromJson(json, Module.class);
		
		if(db.createModule(module)) {
			json = gson.toJson(module);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateModule(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		
		String json = getRequestBody(request);
		
		Module module = gson.fromJson(json, Module.class);
		
		if(db.updateModule(module)) {
			json = gson.toJson(module);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createSubject(HttpServletRequest request,
			HttpServletResponse response) {
		int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
		String json = getRequestBody(request);
		
		Subject subject = gson.fromJson(json, Subject.class);
		
		if(db.createSubject(subject)) {
			json = gson.toJson(subject);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateSubject(HttpServletRequest request,
			HttpServletResponse response) {
		int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
		String json = getRequestBody(request);
		
		Subject subject = gson.fromJson(json, Subject.class);
		
		if(db.updateSubject(subject)) {
			json = gson.toJson(subject);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
		String json = getRequestBody(request);
		
		Studycourse studycourse = gson.fromJson(json, Studycourse.class);
		
		if(db.createStudycourse(studycourse)) {
			json = gson.toJson(studycourse);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
		String json = getRequestBody(request);
		
		Studycourse studycourse = gson.fromJson(json, Studycourse.class);
		
		if(db.updateStudycourse(studycourse)) {
			json = gson.toJson(studycourse);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
		
		String json = getRequestBody(request);
		
		ModuleHandbook moduleHandbook = gson.fromJson(json, ModuleHandbook.class);
		
		if(db.createModuleHandbook(moduleHandbook)) {
			json = gson.toJson(moduleHandbook);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
		
		String json = getRequestBody(request);
		
		ModuleHandbook moduleHandbook = gson.fromJson(json, ModuleHandbook.class);
		
		if(db.updateModuleHandbook(moduleHandbook)) {
			json = gson.toJson(moduleHandbook);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
