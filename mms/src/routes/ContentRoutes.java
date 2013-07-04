package routes;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import model.User;
import model.content.Deadline;
import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;
import model.userRights.EventRights;
import model.userRights.ModuleHandbookRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.ContentDbController;

public class ContentRoutes extends Routes{
	private ContentDbController db;
	private Gson gson = new Gson();
	private java.sql.Date currentDate;
	
	public ContentRoutes() {
		db = new ContentDbController();
		currentDate = new java.sql.Date(System.currentTimeMillis());
	}

	// ####################################################
	// GET Methods
	// ####################################################
	
	/**
	 * reads the event associated with the passed eventID parameter in the request query
	 * and writes it in the response as json object
	 * @param request
	 * @param response
	 */
	public void readEvent(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		User actorUser = getActorUser(request);
		
		
		if(request.getParameter("eventID") != null) {
			int eventID = Integer.parseInt(request.getParameter("eventID"));
			Event event = db.getEvent(eventID);
			
			if(event == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"no event with this eventID (eventID: "+eventID+")", 
						"readEvent(...)")));		
			} else if(!event.isEnabled() && actorUser == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this event (eventID: "+eventID+", event is not enabled)", 
						"readEvent(...)")));
			} else if(!event.isEnabled() && !actorUser.isEmployee()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this event (eventID: "+eventID+", event is not enabled)", 
						"readEvent(...)")));
			} else {
				json = gson.toJson(event);
			}
			
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError("unspecified eventID", "readEvent(...)")));
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * deletes the event associated with the passed eventID parameter in the request query
	 * and writes the eventID in the response if successfully deleted
	 * @param request
	 * @param response
	 */
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified eventID in query", 
					"deleteEvent(...)")));
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reads the events associated with the passed moduleID parameter in the request query
	 * or, if no parameter was specified, all events and writes them in the reponse as json array object
	 * @param request
	 * @param response
	 */
	public void readEvents(HttpServletRequest request,
			HttpServletResponse response) {		
		
		User actorUser = getActorUser(request);
		
		boolean getOnlyEnabled = false;
		
		// non Employees can only see enabled content
		if(actorUser != null) {
			if(!actorUser.isEmployee()) getOnlyEnabled = true;
		} else getOnlyEnabled = true;
		
		String json = "";
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			
			ArrayList<Event> events = db.getModuleEvents(moduleID, getOnlyEnabled);
			
			json = gson.toJson(events);			
		} else {
			ArrayList<Event> events = db.getEvents(getOnlyEnabled);	
			json = gson.toJson(events);
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the module associated with the passed moduleID parameter in the request query
	 * @param request
	 * @param response
	 */
	public void readModule(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("moduleID") != null) {
			int moduleID = Integer.parseInt(request.getParameter("moduleID"));
			Module module = db.getModule(moduleID);
			if(module == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"no module with this moduleID (moduleID: "+moduleID+")", 
						"readModule(...)")));		
			} else if(!module.isEnabled() && actorUser == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this module " +
						"(moduleID: "+moduleID+", module is not enabled)", 
						"readModule(...)")));
			} else if(!module.isEnabled() && !actorUser.isEmployee()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this module " +
						"(moduleID: "+moduleID+", module is not enabled)", 
						"readModule(...)")));
			} else {				
				json = gson.toJson(module);
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified moduleID in query", 
					"readModule(...)")));
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * deletes the module that belongs to the moduleID passed in the query
	 * @param request
	 * @param response
	 */
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified moduleID in query", 
					"deleteModule(...)")));
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the modules associated with the passed subjectID parameter in the request query
	 * or, if no parameter was specified, all modules and writes them in the response as json array object
	 * @param request
	 * @param response
	 */
	public void readModules(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		User actorUser = getActorUser(request);
		
		boolean getOnlyEnabled = false;
		
		// non Employees can only see enabled content
		if(actorUser != null) {
			if(!actorUser.isEmployee()) getOnlyEnabled = true;
		} else getOnlyEnabled = true;
		
		if(request.getParameter("subjectID") != null) {		
			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
		
			ArrayList<Module> modules = db.getSubjectModules(subjectID, getOnlyEnabled);
		
			json = gson.toJson(modules);
		} else {
			ArrayList<Module> modules = db.getModules(getOnlyEnabled);
			
			json = gson.toJson(modules);
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the subject associated with the passed subjectID parameter in the request query
	 * @param request
	 * @param response
	 */
	public void readSubject(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("subjectID") != null) {		

			int subjectID = Integer.parseInt(request.getParameter("subjectID"));
			
			Subject subject = db.getSubject(subjectID);
			
			if(subject == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"no subject with this subjectID (subjectID: "+subjectID+")", 
						"readSubject(...)")));		
			} else if(!subject.isEnabled() && actorUser == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this subject " +
						"(subjectID: "+subjectID+", subject is not enabled)", 
						"readSubject(...)")));
			} else if(!subject.isEnabled() && !actorUser.isEmployee()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this subject " +
						"(subjectID: "+subjectID+", subject is not enabled)", 
						"readSubject(...)")));
			} else {
				json = gson.toJson(subject);
			}

		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified subjectID in query", 
					"readSubject(...)")));
		}
			
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * deletes the subject that belongs to the subjectID passed in the query
	 * @param request
	 * @param response
	 */
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified subjectID in query", 
					"deleteSubject(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * reads the events associated with the passed studycourseID or moduleHandbookID parameter in the request query
	 * or, if no parameter was specified, all subjects and writes them in the response as json array object
	 * @param request
	 * @param response
	 */
	public void readSubjects(HttpServletRequest request,
			HttpServletResponse response) {
		
		User actorUser = getActorUser(request);
		
		boolean getOnlyEnabled = false;
		
		// non Employees can only see enabled content
		if(actorUser != null) {
			if(!actorUser.isEmployee()) getOnlyEnabled = true;
		} else getOnlyEnabled = true;
		
		ArrayList<Subject> subjects = null;
		
		String json = "";
		
		if(request.getParameter("studycourseID") != null) {
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
			subjects = db.getStudycourseSubjects(studycourseID, getOnlyEnabled);
		} else if(request.getParameter("moduleHandbookID") != null) {
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
			subjects = db.getModuleHandbookSubjects(moduleHandbookID, getOnlyEnabled);
		} else {
			subjects = db.getSubjects(getOnlyEnabled);
		}
		json = gson.toJson(subjects);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * reads the studycourse associated with the passed studycourseID parameter in the request query
	 * @param request
	 * @param response
	 */
	public void readStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		User actorUser = getActorUser(request);
		
		if(request.getParameter("studycourseID") != null) {		
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
			Studycourse studycourse = db.getStudycourse(studycourseID);
		
			if(studycourse == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"no studycourse with this studycourseID (studycourseID: "+studycourseID+")", 
						"readStudycourse(...)")));		
			} else if(!studycourse.isEnabled() && actorUser == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this studycourse " +
						"(studycourseID: "+studycourseID+", studycourse is not enabled)", 
						"readStudycourse(...)")));
			} else if(!studycourse.isEnabled() && !actorUser.isEmployee()) {  
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this studycourse " +
						"(studycourseID: "+studycourseID+", studycourse is not enabled)", 
						"readStudycourse(...)")));
			} else {
				json = gson.toJson(studycourse);
			}

		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified studycourseID in query", 
					"readStudycourse(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * deletes the studycourse that belongs to the studycourseID passed in the query
	 * @param request
	 * @param response
	 */
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified studycourseID in query", 
					"deleteStudycourse(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * reads all studycourses and writes them in the response as json array object
	 * @param request
	 * @param response
	 */
	public void readStudycourses(HttpServletRequest request,
			HttpServletResponse response) {

		User actorUser = getActorUser(request);
		
		boolean getOnlyEnabled = false;
		
		// non Employees can only see enabled content
		if(actorUser != null) {
			if(!actorUser.isEmployee()) getOnlyEnabled = true;
		} else getOnlyEnabled = true;
		
		ArrayList<Studycourse> studycourses = db.readStudycourses(getOnlyEnabled);
		
		String json = gson.toJson(studycourses);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * reads the modulehandbook that belongs to the moduleHandbookID passed in the query
	 * @param request
	 * @param response
	 */
	public void readModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		if(request.getParameter("moduleHandbookID") != null) {	
			
			User actorUser = getActorUser(request);
			
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
		
			ModuleHandbook moduleHandbook = db.getModuleHandbook(moduleHandbookID);
		
			if(moduleHandbook == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"no moduleHandbook with this moduleHandbookID (moduleHandbookID: " +
						""+moduleHandbookID+")", 
						"readModuleHandbook(...)")));		
			} else if(!moduleHandbook.isEnabled() && actorUser == null) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this moduleHandbook " +
						"(moduleHandbookID: "+moduleHandbookID+", moduleHandbook is not enabled)", 
						"readModuleHandbook(...)")));
			} else if(!moduleHandbook.isEnabled() && !actorUser.isEmployee()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to read this moduleHandbook " +
						"(moduleHandbookID: "+moduleHandbookID+", moduleHandbook is not enabled)", 
						"readModuleHandbook(...)")));
			} else {
				json = gson.toJson(moduleHandbook);
			}

		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified moduleHandbookID in query", 
					"readModuleHandbook(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	/**
	 * deletes the modulehandbook that belongs to the moduleID passed in the query
	 * @param request
	 * @param response
	 */
	public void deleteModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";
		
		
		if(request.getParameter("moduleHandbookID") != null) {						
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
			
			User actorUser = getActorUser(request);
			
			// check rights
			if(actorUser.isEmployee()) {
				Employee actorEmployee = (Employee) actorUser;
				if(actorEmployee.getEmployeeRights().isAdmin()) {
					System.out.println("actorUser is admin");
				} else {
					ArrayList<ModuleHandbookRights> actorUserModuleHandbookRightsList = actorEmployee.getEmployeeRights().getModuleHandbookRightsList();
					if(actorUserModuleHandbookRightsList.isEmpty()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to delete this moduleHandbook (moduleHandbookID: "+moduleHandbookID+") (actorUser has no StudycourseRights)", 
								"deleteModuleHandbook(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					boolean canDelete = false;
					for(ModuleHandbookRights mhbr : actorUserModuleHandbookRightsList) {
						if(mhbr.getModuleHandbookID() == moduleHandbookID) {
							if(mhbr.getCanDelete()) {
								System.out.println("actorUser is allowed to delete this moduleHandbook");
								canDelete = true;
							} else {
								System.out.println("actorUser is not allowed to delete this moduleHandbook");
								canDelete = false;
							}
						}
					}
					if(!canDelete) {	// no entry found or canDelete=false
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to delete this moduleHandbook (moduleHandbookID: "+moduleHandbookID+") (no fitting ModuleHandbookRights found or canDelete=false)", 
								"deleteModuleHandbook(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to delete this moduleHandbook (moduleHandbookID: "+moduleHandbookID+") (actorUser is no employee)", 
						"deleteModuleHandbook(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			
			if(db.deleteModuleHandbook(new ModuleHandbook(moduleHandbookID))) {
				json = "{\"moduleHandbookID\":"+moduleHandbookID+"}";
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified moduleHandbookID in query", 
					"deleteModuleHandbook(...)")));
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * reads the moduleHandbooks associated with the passed studycourseID parameter in the request query
	 * or, if no parameter was specified, moduleHandbooks and writes them in the response as json array object
	 * @param request
	 * @param response
	 */
	public void readModuleHandbooks(HttpServletRequest request,
			HttpServletResponse response) {
		
		User actorUser = getActorUser(request);
		
		boolean getOnlyEnabled = false;
		
		// non Employees can only see enabled content
		if(actorUser != null) {
			if(!actorUser.isEmployee()) getOnlyEnabled = true;
		} else getOnlyEnabled = true;
		
		String json = "";
		ArrayList<ModuleHandbook> moduleHandbooks = null;
		if(request.getParameter("studycourseID") != null) {						
			int studycourseID = Integer.parseInt(request.getParameter("studycourseID"));
		
			moduleHandbooks = db.readStudycourseModuleHandbooks(studycourseID, getOnlyEnabled);
		} else {
			moduleHandbooks = db.readModuleHandbooks(getOnlyEnabled);
		}
		
		json = gson.toJson(moduleHandbooks);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * reads the deadline of the passed modulehandbook
	 * @param request
	 * @param response
	 */
	public void readDeadline(HttpServletRequest request, HttpServletResponse response) {
				
		String json;
		
		if(request.getParameter("moduleHandbookID") != null) {
			int moduleHandbookID = Integer.parseInt(request.getParameter("moduleHandbookID"));
			
			ModuleHandbook moduleHandbook = db.getModuleHandbook(moduleHandbookID);
			
			json = gson.toJson(db.getDeadline(moduleHandbook));
		} else if(request.getParameter("sose") != null && request.getParameter("year") != null){
			boolean sose = Boolean.parseBoolean(request.getParameter("sose"));
			int year = Integer.parseInt(request.getParameter("year"));
			
			json = gson.toJson(db.getDeadline(sose, year));
		
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified moduleHandbookID and unspecified sose or year in query", 
					"readDeadline(...)"))); 
		}
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * reads all deadlines
	 * @param request
	 * @param response
	 */
	public void readDeadlines(HttpServletRequest request, HttpServletResponse response) {
		String json;
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser == null) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to read deadlines (actorUser is not logged in)", 
					"deleteDeadline(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if(!actorUser.isEmployee()) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to read deadlines (actorUser is no employee)", 
					"deleteDeadline(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}	
		
		json = gson.toJson(db.getDeadlines());
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * deletes the deadline that belongs to the sose and year passed in the query
	 * @param request
	 * @param response
	 */
	public void deleteDeadline(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is no admin");
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to delete deadlines (actorUser is no admin)", 
						"deleteDeadline()")));
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
					"not allowed to delete deadlines (actorUser is no employee (and therefore no admin))", 
					"deleteDeadline(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}		
		
		if(request.getParameter("sose") != null && request.getParameter("year") != null) {
			boolean sose = Boolean.parseBoolean(request.getParameter("sose"));
			int year = Integer.parseInt(request.getParameter("year"));
			
			if(db.deleteDeadline(sose, year));
			
			json = gson.toJson(new Deadline(sose, year));
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"unspecified sose or year parameter in query", 
					"deleteDeadline(...)"))); 
		}
		
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
	 * @param request
	 * @param response
	 */
	public void createEvent(HttpServletRequest request,
			HttpServletResponse response) {
		
		User actorUser = getActorUser(request);
		
		boolean actorUserIsAdmin = false;
		
		String json = getRequestBody(request);
	
		Event event = gson.fromJson(json, Event.class);
	
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				actorUserIsAdmin = true;
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				if(event.isEnabled()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to create events that are enabled " +
							"(actorUser is no admin)", 
							"createEvent(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				
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
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			// give the user all rights for his created event
			if(!actorUserIsAdmin) {
				db.createOwnerEventRights(actorUser.getEmail(), event.getID());
			}
			json = gson.toJson(event);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"d.createEvent(event) failed", 
					"createEvent(...)")));
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
	 */
	public void updateEvent(HttpServletRequest request,
			HttpServletResponse response) {		
		String json = getRequestBody(request);
		
		Event event = gson.fromJson(json, Event.class);
		
		// enabling?
		boolean enabling = false;
		boolean enabled = false;
		if(request.getParameter("enabled") != null) {
			enabled = Boolean.parseBoolean(request.getParameter("enabled"));
			enabling = true;
		}
		
		User actorUser = getActorUser(request);		
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {		
				if(db.getEariliestDeadline(event) != null) {
					if(db.getEariliestDeadline(event).after(currentDate)) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"deadline expired for this event " +
								"(eventID: "+event.getID()+")",
								"updateEvent(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;	
					}
				}
				if(enabling) {
					if(!actorEmployee.getEmployeeRights().isCanDeblockModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this event " +
								"(eventID: "+event.getID()+") " +
								"(actorUser cannot enable content)", 
								"updateEvent(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					} 
				} else {
					ArrayList<EventRights> actorUserEventRightsList = actorEmployee.getEmployeeRights().getEventRightsList();
					if(actorUserEventRightsList.isEmpty()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to update this event (eventID: "+event.getID()+") (actorUser is no employee)", 
					"updateEvent(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// enable or disable event
		if(enabling) {
			if(db.enableEvent(event.getID(), enabled)) {
				json = "{\"eventID\":"+event.getID()+"}";
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"db.enableEvent(event.getID(), enabled) failed", 
						"updateEvent(...)")));
			}
		// update event content
		} else {
			if(db.updateEvent(event)) {
				json = gson.toJson(event);
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"db.updateEvent(event) failed", 
						"updateEvent(...)")));	
			}
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
	 */
	public void createModule(HttpServletRequest request,
			HttpServletResponse response) {		
		String json = getRequestBody(request);
		
		Module module = gson.fromJson(json, Module.class);
		
		boolean actorUserIsAdmin = false;
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				actorUserIsAdmin = true;
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				if(module.isEnabled()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to create modules that are enabled " +
							"(actorUser is no admin)", 
							"createModule(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				
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
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			// give the user all rights for his created module
			if(!actorUserIsAdmin) {
				db.createOwnerModuleRights(actorUser.getEmail(), module.getID());
			}
			json = gson.toJson(module);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	public void updateModule(HttpServletRequest request,
			HttpServletResponse response) {		
		String json = getRequestBody(request);
		
		Module module = gson.fromJson(json, Module.class);
		
		User actorUser = getActorUser(request);
		
		// enabling?
		boolean enabling = false;
		boolean enabled = false;
		if(request.getParameter("enabled") != null) {
			enabled = Boolean.parseBoolean(request.getParameter("enabled"));
			enabling = true;
		}
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
					
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				if(db.getEariliestDeadline(module) != null) {
					if(db.getEariliestDeadline(module).after(currentDate)) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"deadline expired for this module " +
								"(moduleID: "+module.getID()+")",
								"updateModule(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;	
					}
				}
				if(enabling) {
					if(!module.isCritical() && !actorEmployee.getEmployeeRights().isCanDeblockModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this module (moduleID: "+module.getID()+") (actorUser cannot enable content)", 
								"updateModule(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					} else if(module.isCritical() && !actorEmployee.getEmployeeRights().isCanDeblockCriticalModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this module (moduleID: "+module.getID()+") " +
										"(actorUser cannot deblock critical modules)", 
								"updateModule(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					} 
				} else {
					ArrayList<ModuleRights> actorUserModuleRightsList = actorEmployee.getEmployeeRights().getModuleRightsList();
					if(actorUserModuleRightsList.isEmpty()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
						json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to delete this module (moduleID: "+module.getID()+") (actorUser is no employee)", 
					"updateModule(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// enable or disable module
		if(enabling) {
			if(db.enableModule(module.getID(), enabled)) {
				json = "{\"moduleID\":"+module.getID()+"}";
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"db.enableModule(module.getID(), enabled) failed", 
						"updateModule(...)")));
			}
		// update module content
		} else {
			if(db.updateModule(module)) {
				json = gson.toJson(module);
			} else {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"db.updateEvent(event) failed", 
						"updateModule(...)")));	
			}
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
	 */
	public void createSubject(HttpServletRequest request,
			HttpServletResponse response) {		
		String json = getRequestBody(request);
		
		Subject subject = gson.fromJson(json, Subject.class);
		
		User actorUser = getActorUser(request);
		
		boolean actorUserIsAdmin = false;
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				actorUserIsAdmin = true;
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
				
				if(subject.isEnabled()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to create subjects that are enabled " +
							"(actorUser is no admin)", 
							"createSubject(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				
				int moduleHandbookID = subject.getModuleHandbooks_moduleHandbookID();
				
				ArrayList<ModuleHandbookRights> actorUserModuleHandbookRightsList = actorEmployee.getEmployeeRights().getModuleHandbookRightsList();
				
				boolean canCreateChilds = false;
				for(ModuleHandbookRights mhbr : actorUserModuleHandbookRightsList) {
					if(moduleHandbookID == mhbr.getModuleHandbookID()) {
						if(mhbr.getCanCreateChilds()) {
							System.out.println("actorUser is allowed to create " +
									"childs for this modulehandbook (moduleHandbookID: "+moduleHandbookID+")");
							canCreateChilds = true;
						} else {
							System.out.println("actorUser is not allowed to create " +
									"childs for this modulehandbook (moduleHandbookID: "+moduleHandbookID+")");
						}
					}
				}
				if(!canCreateChilds) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to create subjects for this moduleHandbookID " +
							"(moduleHandbookID: "+moduleHandbookID+")", 
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			// give the user all rights for his created subject
			if(!actorUserIsAdmin) {
				db.createOwnerSubjectRights(actorUser.getEmail(), subject.getID());
			}
			json = gson.toJson(subject);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	public void updateSubject(HttpServletRequest request,
			HttpServletResponse response) {		
		String json = getRequestBody(request);
		
		Subject subject = gson.fromJson(json, Subject.class);
	
		User actorUser = getActorUser(request);
		
		// enabled changed?
		boolean enabledChanged = false;
		Subject oldSubject = db.getSubject(subject.getID());
		if(oldSubject.isEnabled() != subject.isEnabled()) {
			enabledChanged = true;
		}
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				if(enabledChanged) {
					if(!actorEmployee.getEmployeeRights().isCanDeblockModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this subject " +
								"(subjectID: "+subject.getID()+") " +
								"(actorUser cannot enable content)", 
								"updateSubject(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				} else if(db.getEariliestDeadline(subject) != null) {
					if(db.getEariliestDeadline(subject).after(currentDate)) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"deadline expired for this subject " +
								"(subjectID: "+subject.getID()+")",
								"updateSubject(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;	
					}
				}
				ArrayList<SubjectRights> actorUserSubjectRightsList = actorEmployee.getEmployeeRights().getSubjectRightsList();
				if(actorUserSubjectRightsList.isEmpty()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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

	/**
	 * @param request
	 * @param response
	 */
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
				
				json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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

	/**
	 * @param request
	 * @param response
	 */
	public void updateStudycourse(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		Studycourse studycourse = gson.fromJson(json, Studycourse.class);
		
		User actorUser = getActorUser(request);
		
		// enabled changed?
		boolean enabledChanged = false;
		Studycourse oldStudycourse = db.getStudycourse(studycourse.getID());
		if(oldStudycourse.isEnabled() != studycourse.isEnabled()) {
			enabledChanged = true;
		}
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				if(enabledChanged) {
					if(!actorEmployee.getEmployeeRights().isCanDeblockModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this studycourse " +
								"(studycourseID: "+studycourse.getID()+") " +
								"(actorUser cannot enable content)", 
								"updateStudycourse(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				} 
				ArrayList<StudycourseRights> actorUserStudycourseRightsList = actorEmployee.getEmployeeRights().getStudycourseRightsList();
				if(actorUserStudycourseRightsList.isEmpty()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"not allowed to update this studycourse (studycourseID: "+studycourse.getID()+") (actorUser has no StudycourseRights)", 
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
				if(!canUpdate) {	// no entry found or canUpdate=false
					json = gson.toJson(new JsonErrorContainer(new JsonError(
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
			json = gson.toJson(new JsonErrorContainer(new JsonError(
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

	/**
	 * @param request
	 * @param response
	 */
	public void createModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		ModuleHandbook moduleHandbook = gson.fromJson(json, ModuleHandbook.class);
		
		User actorUser = getActorUser(request);
	
		boolean actorUserIsAdmin = false;
		
		if(moduleHandbook.isEnabled() && !actorUser.isEmployee()) {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"actorUser is not allowed to create moduleHandbooks that are enabled " +
					"(actorUser is no employee and therefore no admin)", 
					"createModuleHandbook(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(moduleHandbook.isEnabled() && !actorEmployee.getEmployeeRights().isAdmin()) {
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"actorUser is not allowed to create moduleHandbooks that are enabled " +
						"(actorUser is no admin)", 
						"createModuleHandbook(...)")));		
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				actorUserIsAdmin = true;
				System.out.println("actorUser is admin");
			} else {
				System.out.println("actorUser is no admin");
			
			
				int studycourseID = moduleHandbook.getStudycourses_studycourseID();
				
				ArrayList<StudycourseRights> actorUserStudycourseRightsList = actorEmployee.getEmployeeRights().getStudycourseRightsList();
				
				boolean canCreateChilds = false;
				for(StudycourseRights scr : actorUserStudycourseRightsList) {
					if(studycourseID == scr.getStudycourseID()) {
						if(scr.getCanCreateChilds()) {
							System.out.println("actorUser is allowed to create " +
									"childs for this studycourse (studycourseID: "+studycourseID+")");
							canCreateChilds = true;
						} else {
							System.out.println("actorUser is not allowed to create " +
									"childs for this studycourseID (studycourseID: "+studycourseID+")");
						}
					}
				}
				if(!canCreateChilds) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"actorUser is not allowed to create modulehandboks for this studycourse " +
							"(studycourseID: "+studycourseID+")", 
							"createModuleHandbook(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} 
			}
		}
		
		if(db.createModuleHandbook(moduleHandbook)) {
			// give the user all rights for his created modulehandbook
			if(!actorUserIsAdmin) {
				db.createOwnerModuleHandbookRights(actorUser.getEmail(), moduleHandbook.getID());
			}

			json = gson.toJson(moduleHandbook);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	public void updateModuleHandbook(HttpServletRequest request,
			HttpServletResponse response) {
		String json = getRequestBody(request);
		
		ModuleHandbook moduleHandbook = gson.fromJson(json, ModuleHandbook.class);
		
		// enabled changed?
		boolean enabledChanged = false;
		ModuleHandbook oldModuleHandbook = db.getModuleHandbook(moduleHandbook.getID());
		if(oldModuleHandbook.isEnabled() != moduleHandbook.isEnabled()) {
			enabledChanged = true;
		}	
		
		User actorUser = getActorUser(request);
		
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is admin");
			} else {
				if(enabledChanged) {
					if(!actorEmployee.getEmployeeRights().isCanDeblockModule()) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"not allowed to enable or disable this moduleHandbook " +
								"(moduleHandbookID: "+moduleHandbook.getID()+") " +
								"(actorUser cannot enable content)", 
								"updateStudycourse(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				} else if(db.getEariliestDeadline(moduleHandbook) != null) {
					if(db.getEariliestDeadline(moduleHandbook).after(currentDate)) {
						json = gson.toJson(new JsonErrorContainer(new JsonError(
								"deadline expired for this moduleHandbook " +
								"(moduleHandbookID: "+moduleHandbook.getID()+")",
								"updateModuleHandbook(...)")));		
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;	
					}
				}
			
				ArrayList<ModuleHandbookRights> actorUserModuleHandbookRightsList = actorEmployee.getEmployeeRights().getModuleHandbookRightsList();
				if(actorUserModuleHandbookRightsList.isEmpty()) {
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"not allowed to update this moduleHandbook (moduleHandbookID: "+moduleHandbook.getID()+") (actorUser has no ModuleHandbookRights)", 
							"updateModuleHandbook(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				boolean canUpdate = false;
				for(ModuleHandbookRights mhbr : actorUserModuleHandbookRightsList) {
					if(mhbr.getModuleHandbookID() == moduleHandbook.getID()) {
						if(mhbr.getCanEdit()) {
							System.out.println("actorUser is allowed to update this moduleHandbook");
							canUpdate = true;
						} else {
							System.out.println("actorUser is not allowed to update this moduleHandbook");
							canUpdate = false;
						}
					}
				}
				if(!canUpdate) {	// no entry found or canUpdate=false
					json = gson.toJson(new JsonErrorContainer(new JsonError(
							"not allowed to update this moduleHandbook (moduleHandbookID: "+moduleHandbook.getID()+") (no fitting StudycourseRights found or canDelete=false)", 
							"updateModuleHandbook(...)")));		
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"not allowed to update this moduleHandbook (moduleHandbookID:"+moduleHandbook.getID()+") (actorUser is no employee)", 
					"updateModuleHandbook(...)")));		
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if(db.updateModuleHandbook(moduleHandbook)) {
			json = gson.toJson(moduleHandbook);
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * creates a new deadline in database
	 * @param request
	 * @param response
	 */
	public void createDeadline(HttpServletRequest request,
			HttpServletResponse response) {
		String json;
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is no admin");
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to create deadlines (actorUser is no admin)", 
						"createDeadline()")));
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
					"not allowed to create deadlines (actorUser is no employee (and therefore no admin))", 
					"createDeadline(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		json = getRequestBody(request);
		
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		
		String deadlineString = obj.get("deadline").getAsString();
		
		boolean sose = obj.get("sose").getAsBoolean();
		
		int year = obj.get("year").getAsInt();
		
		DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
		
		java.sql.Date deadlineDate;
		Deadline deadline = null;
		
		try {
			deadlineDate = new java.sql.Date(df.parse(deadlineString).getTime());
			deadline = new Deadline(sose, year, deadlineDate);
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		}
		
	
		if(db.createDeadline(deadline)) {
			json = gson.toJson(deadline);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"db.createDeadline(deadline) failed", 
					"createDeadline(...)")));
		}	
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * updates an existing deadline 
	 * @param request
	 * @param response
	 */
	public void updateDeadline(HttpServletRequest request, HttpServletResponse response) {
		
		String json;
		
		User actorUser = getActorUser(request);
		
		// check rights
		if(actorUser.isEmployee()) {
			Employee actorEmployee = (Employee) actorUser;
			if(!actorEmployee.getEmployeeRights().isAdmin()) {
				System.out.println("actorUser is no admin");
				json = gson.toJson(new JsonErrorContainer(new JsonError(
						"not allowed to update deadlines (actorUser is no admin)", 
						"updateDeadline()")));
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
					"not allowed to update deadlines (actorUser is no employee (and therefore no admin))", 
					"updateDeadline(...)")));
			try { 
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		json = getRequestBody(request);
	
		Deadline deadline = gson.fromJson(json, Deadline.class);
		
		if(db.updateDeadline(deadline)) {
			json = gson.toJson(deadline);
		} else {
			json = gson.toJson(new JsonErrorContainer(new JsonError(
					"db.updateDeadline(...) failed", 
					"updateDeadline(...)")));
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
