package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import model.User;
import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;
import model.userRights.EventRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;

import com.google.gson.Gson;

import controller.ContentDbController;
import controller.UserDbController;

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
			json = gson.toJson(new JsonContent(new JsonError("unspecified eventID", "readEvent(...)")));
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
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("eventID") != null) {
			int eventID = Integer.parseInt(request.getParameter("eventID"));
			
			// check rights
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is admin");
				} else {
					ArrayList<EventRights> actorUserEventRightsList = actorEmployee.getEmployeeRights().getEventRightsList();
					if(actorUserEventRightsList.isEmpty()) {
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this event (actorUser has no EventRights)", 
								"deleteEvent(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					boolean canDelete = false;
					for(EventRights er : actorUserEventRightsList) {
						if(er.getEventID() == eventID) {
							if(er.getCanDelete()) {
								System.out.println("actorUser is allowed to delete this event");
								canDelete = true;
							} else {
								System.out.println("actorUser is not allowed to delete this event");
								canDelete = false;
							}
						}
					}
					if(!canDelete) {	// no entry found or canDelete=false
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this event (no fitting EventRights found or canDelete=false)", 
								"deleteEvent(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			} else {
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to delete this event (actorUser is no employee)", 
						"deleteEvent(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			
			if(db.deleteEvent(new Event(eventID))) {
				json = "{\"eventID\":"+eventID+"}";
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified eventID in query", 
					"deleteEvent(...)")));
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
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified moduleID in query", 
					"readModule(...)")));
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void deleteModule(HttpServletRequest request,
			HttpServletResponse response) {
		
		User actorUser = getActorUser(request);
		
		String json = "";
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			
			// check rights
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is admin");
				} else {
					ArrayList<ModuleRights> actorUserModuleRightsList = actorEmployee.getEmployeeRights().getModuleRightsList();
					if(actorUserModuleRightsList.isEmpty()) {
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this module (actorUser has no ModuleRights)", 
								"deleteModule(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					boolean canDelete = false;
					for(ModuleRights mr : actorUserModuleRightsList) {
						if(mr.getModuleID() == moduleID) {
							if(mr.getCanDelete()) {
								System.out.println("actorUser is allowed to delete this module");
								canDelete = true;
							} else {
								System.out.println("actorUser is not allowed to delete this module");
								canDelete = false;
							}
						}
					}
					if(!canDelete) {	// no entry found or canDelete=false
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this module (no fitting ModuleRights found or canDelete=false)", 
								"deleteModule(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			} else {
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to delete this module (actorUser is no employee)", 
						"deleteModule(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
						
			if(db.deleteModule(new Module(moduleID))) {
				json = "{\"moduleID\":"+moduleID+"}";
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified moduleID in query", 
					"deleteModule(...)")));
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
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified subjectID in query", 
					"readSubject(...)")));
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
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("subjectID") != null) {		
			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
			
			// check rights
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is admin");
				} else {
					ArrayList<SubjectRights> actorUserSubjectRightsList = actorEmployee.getEmployeeRights().getSubjectRightsList();
					if(actorUserSubjectRightsList.isEmpty()) {
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this subject (actorUser has no SubjectRights)", 
								"deleteSubject(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					boolean canDelete = false;
					for(SubjectRights sr : actorUserSubjectRightsList) {
						if(sr.getSubjectID() == subjectID) {
							if(sr.getCanDelete()) {
								System.out.println("actorUser is allowed to delete this subject");
								canDelete = true;
							} else {
								System.out.println("actorUser is not allowed to delete this subject");
								canDelete = false;
							}
						}
					}
					if(!canDelete) {	// no entry found or canDelete=false
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this subject (no fitting SubjectRights found or canDelete=false)", 
								"deleteSubject(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			} else {
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to delete this subject (actorUser is no employee)", 
						"deleteSubject(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
					
			if(db.deleteSubject(new Subject(subjectID))) {
				json = "{\"subjectID\":"+subjectID+"}";
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified subjectID in query", 
					"deleteSubject(...)")));
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
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified studycourseID in query", 
					"readStudycourse(...)")));
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
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("studycourseID") != null) {		
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
			
			// check rights
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is admin");
				} else {
					ArrayList<StudycourseRights> actorUserStudycourseRightsList = actorEmployee.getEmployeeRights().getStudycourseRightsList();
					if(actorUserStudycourseRightsList.isEmpty()) {
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this studycourse (actorUser has no StudycourseRights)", 
								"deleteStudycourse(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					boolean canDelete = false;
					for(StudycourseRights scr : actorUserStudycourseRightsList) {
						if(scr.getStudycourseID() == studycourseID) {
							if(scr.getCanDelete()) {
								System.out.println("actorUser is allowed to delete this studycourse");
								canDelete = true;
							} else {
								System.out.println("actorUser is not allowed to delete this studycourse");
								canDelete = false;
							}
						}
					}
					if(!canDelete) {	// no entry found or canDelete=false
						json = gson.toJson(new JsonContent(new JsonError(
								"not allowed to delete this studycourse (no fitting StudycourseRights found or canDelete=false)", 
								"deleteStudycourse(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			} else {
				json = gson.toJson(new JsonContent(new JsonError(
						"not allowed to delete this studycourse (actorUser is no employee)", 
						"deleteStudycourse(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}			
			
			if(db.deleteStudycourse(new Studycourse(studycourseID))) {
				json = "{\"studycourseID\":"+studycourseID+"}";
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified studycourseID in query", 
					"deleteStudycourse(...)")));
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
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified moduleHandbookID in query", 
					"readModuleHandbook(...)")));
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
				json = "{\"moduleHandbookID\":"+moduleHandbookID+"}";
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"unspecified moduleHandbookID in query", 
					"deleteModuleHandbook(...)")));
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
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"d.createEvent(event) failed", 
					"createEvent(...)")));
		}

		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateEvent(HttpServletRequest request,
			HttpServletResponse response) {		
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
