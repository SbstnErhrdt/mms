package routes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Stdlib;

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
								"not allowed to delete this event (eventID: "+eventID+") (actorUser has no EventRights)", 
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
								"not allowed to delete this event (eventID: "+eventID+") (no fitting EventRights found or canDelete=false)", 
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
						"not allowed to delete this event (eventID: "+eventID+") (actorUser is no employee)", 
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
								"not allowed to delete this module (moduleID: "+moduleID+") (actorUser has no ModuleRights)", 
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
								"not allowed to delete this module (moduleID: "+moduleID+") (no fitting ModuleRights found or canDelete=false)", 
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
						"not allowed to delete this module (moduleID: "+moduleID+") (actorUser is no employee)", 
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
								"not allowed to delete this subject (subjectID: "+subjectID+") (actorUser has no SubjectRights)", 
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
								"not allowed to delete this subject (subjectID: "+subjectID+") (no fitting SubjectRights found or canDelete=false)", 
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
						"not allowed to delete this subject (subjectID: "+subjectID+") (actorUser is no employee)", 
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
								"not allowed to delete this studycourse (studycourseID: "+studycourseID+") (actorUser has no StudycourseRights)", 
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
								"not allowed to delete this studycourse (studycourseID: "+studycourseID+") (no fitting StudycourseRights found or canDelete=false)", 
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
						"not allowed to delete this studycourse (studycourseID: "+studycourseID+") (actorUser is no employee)", 
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
		
		User actorUser = getActorUser(request);
		
		String json = getRequestBody(request);
	
		Event event = gson.fromJson(json, Event.class);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				ArrayList<Integer> moduleIDs = event.getModuleIDs();
				
				ArrayList<ModuleRights> actorUserModuleRightsList = actorEmployee.getEmployeeRights().getModuleRightsList();
				ArrayList<Integer> allowedModuleIDs = new ArrayList<Integer>();
				
				boolean canCreateChilds = false;
				for(int moduleID : moduleIDs) {
					for(ModuleRights mr : actorUserModuleRightsList) {
						if(moduleID == mr.getModuleID()) {
							if(mr.getCanCreateChilds()) {
								System.out.println("actorUser is allowed to create childs for one of the modules (moduleID: "+moduleID+")");
								allowedModuleIDs.add(moduleID);
								canCreateChilds = true;
							} else {
								System.out.println("actorUser is not allowed to create childs for one of the modules (moduleID: "+moduleID+")");
							}
						}
					}
				}
				if(!canCreateChilds) {
					json = gson.toJson(new JsonContent(new JsonError(
							"actorUser is not allowed to create events for any of these moduleIDs (moduleIDs: "+moduleIDs+")", 
							"createEvent(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} else {
					// only set moduleIDs the actorUser is allowed to create childs for
					event.setModuleIDs(allowedModuleIDs);
				}
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"actorUser is not allowed to create Events (actorUser is no employee)", 
					"createEvent(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

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
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				ArrayList<EventRights> actorUserEventRightsList = actorEmployee.getEmployeeRights().getEventRightsList();
				if(actorUserEventRightsList.isEmpty()) {
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this event (eventID: "+event.getID()+") (actorUser has no EventRights)", 
							"updateEvent(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				boolean canUpdate = false;
				for(EventRights er : actorUserEventRightsList) {
					if(er.getEventID() == event.getID()) {
						if(er.getCanEdit()) {
							System.out.println("actorUser is allowed to update this event");
							canUpdate = true;
						} else {
							System.out.println("actorUser is not allowed to update this event");
							canUpdate = false;
						}
					}
				}
				if(!canUpdate) {	// no entry found or canUpdate=false
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this event (eventID: "+event.getID()+") (no fitting EventRights found or canDelete=false)", 
							"updateEvent(...)")));		
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
					"not allowed to update this event (eventID: "+event.getID()+") (actorUser is no employee)", 
					"updateEvent(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		
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
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				ArrayList<Integer> subjectIDs = module.getSubjectIDs();
				
				ArrayList<SubjectRights> actorUserSubjectRightsList = actorEmployee.getEmployeeRights().getSubjectRightsList();
				ArrayList<Integer> allowedSubjectIDs = new ArrayList<Integer>();
				
				boolean canCreateChilds = false;
				for(int subjectID : subjectIDs) {
					for(SubjectRights sr : actorUserSubjectRightsList) {
						if(subjectID == sr.getSubjectID()) {
							if(sr.getCanCreateChilds()) {
								System.out.println("actorUser is allowed to create " +
										"childs for one of the subjects (subjectID: "+subjectID+")");
								allowedSubjectIDs.add(subjectID);
								canCreateChilds = true;
							} else {
								System.out.println("actorUser is not allowed to create " +
										"childs for one of the subjects (subjectID: "+subjectID+")");
							}
						}
					}
				}
				if(!canCreateChilds) {
					json = gson.toJson(new JsonContent(new JsonError(
							"actorUser is not allowed to create modules for any of these subjectIDs " +
							"(subjectIDs: "+subjectIDs+")", 
							"createModule(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} else {
					// only set moduleIDs the actorUser is allowed to create childs for
					module.setSubjectIDs(allowedSubjectIDs);
				}
			}
		} else {
			json = gson.toJson(new JsonContent(new JsonError(
					"actorUser is not allowed to create modules (actorUser is no employee)", 
					"createModule(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}		
		
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
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				ArrayList<ModuleRights> actorUserModuleRightsList = actorEmployee.getEmployeeRights().getModuleRightsList();
				if(actorUserModuleRightsList.isEmpty()) {
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this module (moduleID: "+module.getID()+") (actorUser has no ModuleRights)", 
							"updateModule(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				boolean canUpdate = false;
				for(ModuleRights mr : actorUserModuleRightsList) {
					if(mr.getModuleID() == module.getID()) {
						if(mr.getCanEdit()) {
							System.out.println("actorUser is allowed to update this module");
							canUpdate = true;
						} else {
							System.out.println("actorUser is not allowed to update this module");
							canUpdate = false;
						}
					}
				}
				if(!canUpdate) {	// no entry found or canDelete=false
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this module (moduleID: "+module.getID()+") (no fitting ModuleRights found or canDelete=false)", 
							"updateModule(...)")));		
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
					"not allowed to delete this module (moduleID: "+module.getID()+") (actorUser is no employee)", 
					"updateModule(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
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
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				int studycourseID = subject.getStudycourses_studycourseID();
				
				ArrayList<StudycourseRights> actorUserStudycourseRightsList = actorEmployee.getEmployeeRights().getStudycourseRightsList();
				
				boolean canCreateChilds = false;
				for(StudycourseRights scr : actorUserStudycourseRightsList) {
					if(studycourseID == scr.getStudycourseID()) {
						if(scr.getCanCreateChilds()) {
							System.out.println("actorUser is allowed to create " +
									"childs for this studycourses (studycourseID: "+studycourseID+")");
							canCreateChilds = true;
						} else {
							System.out.println("actorUser is not allowed to create " +
									"childs for this studycourses (studycourseID: "+studycourseID+")");
						}
					}
				}
				if(!canCreateChilds) {
					json = gson.toJson(new JsonContent(new JsonError(
							"actorUser is not allowed to create subjects for this studycourseID " +
							"(studycourseID: "+studycourseID+")", 
							"createSubject(...)")));		
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
					"actorUser is not allowed to create subjects (actorUser is no employee)", 
					"createSubject(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}		
				
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
	
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				ArrayList<SubjectRights> actorUserSubjectRightsList = actorEmployee.getEmployeeRights().getSubjectRightsList();
				if(actorUserSubjectRightsList.isEmpty()) {
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this subject (subjectID: "+subject.getID()+") (actorUser has no SubjectRights)", 
							"updateSubject(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				boolean canUpdate = false;
				for(SubjectRights sr : actorUserSubjectRightsList) {
					if(sr.getSubjectID() == subject.getID()) {
						if(sr.getCanEdit()) {
							System.out.println("actorUser is allowed to update this subject");
							canUpdate = true;
						} else {
							System.out.println("actorUser is not allowed to update this subject");
							canUpdate = false;
						}
					}
				}
				if(!canUpdate) {	// no entry found or canDelete=false
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this subject (subjectID: "+subject.getID()+") (no fitting SubjectRights found or canDelete=false)", 
							"updateSubject(...)")));		
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
					"not allowed to update this subject (subjectID: "+subject.getID()+") (actorUser is no employee)", 
					"updateSubject(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
			
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
		
		User actorUser = getActorUser(request);
		
		// check rights (only admins can create studycourses)
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("user is no admin");
				
				json = gson.toJson(new JsonContent(new JsonError(
						"actorUser is not allowed to create studycourses (actorUser is no admin)", 
						"createStudycourse(...)")));
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			System.out.println("user is no employee");	
			json = gson.toJson(new JsonContent(new JsonError(
					"actorUser is not allowed to create studycourses " +
					"(actorUser is no employee and therefore no admin)", 
					"createStudycourse(...)")));
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
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
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				ArrayList<StudycourseRights> actorUserStudycourseRightsList = actorEmployee.getEmployeeRights().getStudycourseRightsList();
				if(actorUserStudycourseRightsList.isEmpty()) {
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to delete this studycourse (studycourseID: "+studycourse.getID()+") (actorUser has no StudycourseRights)", 
							"updateStudycourse(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				boolean canUpdate = false;
				for(StudycourseRights scr : actorUserStudycourseRightsList) {
					if(scr.getStudycourseID() == studycourse.getID()) {
						if(scr.getCanEdit()) {
							System.out.println("actorUser is allowed to update this studycourse");
							canUpdate = true;
						} else {
							System.out.println("actorUser is not allowed to update this studycourse");
							canUpdate = false;
						}
					}
				}
				if(!canUpdate) {	// no entry found or canDelete=false
					json = gson.toJson(new JsonContent(new JsonError(
							"not allowed to update this studycourse (studycourseID: "+studycourse.getID()+") (no fitting StudycourseRights found or canDelete=false)", 
							"updateStudycourse(...)")));		
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
					"not allowed to update this studycourse (studycourseID:"+studycourse.getID()+") (actorUser is no employee)", 
					"updateStudycourse(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}			
			
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

	public void closeConnection() {
		db.closeConnection();		
	}
}
