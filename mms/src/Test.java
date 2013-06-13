import java.util.ArrayList;

import routes.JsonContent;
import routes.JsonError;
import routes.UserRoutes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.Type;

import model.Employee;
import model.User;
import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;
import model.userRights.EmployeeRights;
import model.userRights.EventRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;
import model.userRights.UserRights;
import controller.ContentDbController;
import controller.UserDbController;

public class Test {
	public static void main(String[] args) {
		UserDbController udbc = new UserDbController();
		
		Employee emp = new Employee("email@ex-studios.net", "1234", "Sebastian", "Sehrhardt", "Herr Dipl. Ing.",
				"Bachelor", 1234567, 4, new EmployeeRights(), true);
		emp = new Employee("neue_email@ex-studios.net", "1234", "Sebastian", "Der Zweite", "Herr Dr. Dipl. Ing.", "Bachelor", 123456, 4, new UserRights(), false);
		emp.setAddress("Am Arsch der Welt");
		emp.setEmployeeRights(new EmployeeRights(true, false, false));
		
		EventRights eventRights = new EventRights();
		eventRights.setEventID(5);
		eventRights.setCanEdit(true);
		eventRights.setCanDelete(false);
		
		emp.getEmployeeRights().addEventRights(eventRights);
		
		eventRights = new EventRights();
		eventRights.setEventID(6);
		eventRights.setCanEdit(true);
		eventRights.setCanDelete(false);
		
		emp.getEmployeeRights().addEventRights(eventRights);
		
		//System.out.println("emp.getEmployeeRights().getEventRightsList():");
		//System.out.println(emp.getEmployeeRights().getEventRightsList());
		
		ModuleRights moduleRights = new ModuleRights();
		moduleRights.setModuleID(1);
		moduleRights.setCanCreateChilds(true);
		moduleRights.setCanEdit(true);
		moduleRights.setCanDelete(false);
		
		emp.getEmployeeRights().addModuleRights(moduleRights);
		
		SubjectRights sR = new SubjectRights();
		sR.setSubjectID(1);
		sR.setCanCreateChilds(true);
		sR.setCanEdit(true);
		sR.setCanDelete(false);
		
		emp.getEmployeeRights().addSubjectRights(sR);
		
		StudycourseRights scR = new StudycourseRights();
		scR.setStudycourseID(1);
		scR.setCanCreateChilds(true);
		scR.setCanEdit(true);
		scR.setCanDelete(false);
		
		emp.getEmployeeRights().addStudycourseRights(scR);
		
		//if(udbc.createUser(user)) System.out.println("user "+user+" created successfully");
		
		// if(udbc.deleteUser(user)) System.out.println("user "+user+" deleted succesfully");
		
		User user1 = new User("neue_email@ex-studios.net", "1234");
		//user1.setEmployee(true);
		//user1 = udbc.getUser(user1);
		
		//System.out.println(user1);
		//System.out.println(user1.getUserRights());
	
		
		udbc.deleteUser(emp);
		udbc.createUser(emp);
		
		emp = (Employee) udbc.getUser(user1);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(eventRights);
		
		System.out.println(json);
		EventRights er = gson.fromJson(json, EventRights.class);
		
		System.out.println(er);
		
		json = gson.toJson(emp);
		
		System.out.println(json);
				
		
		
		//udbc.updateUser(emp);
		
		//System.out.println(emp);
		//System.out.println(emp.getEmployeeRights());
	
		ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
		moduleIDs.add(1);
		moduleIDs.add(2);
		
		Event event = new Event(5, moduleIDs, "Ana IIa", 4, "gerhard.baur@uni-ulm.de", false, 
				"test", true, "irgend n Raum", "irgendwo", "Vorlesung");
		
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
		subjectIDs.add(1);
		subjectIDs.add(2);
		
		Module module = new Module(3, "Testmodul3", subjectIDs, "", "", "5 LP", "3", 
				"Deutsch", 2, "gerhard.baur@uni-ulm.de", "", "", "", "", false, true, false);
		
		
		ContentDbController cdbc = new ContentDbController();
		
		//cdbc.createEvent(event);
		
		/*
		cdbc.updateEvent(event);
		
		event = cdbc.getEvent(5);
				
		System.out.println(event);
		*/
		// System.out.println(module);
		
		// cdbc.createModule(module);
		
		//module.setDuration(3);
		
		//cdbc.updateModule(module);
		/*
		module = cdbc.getModule(3);
		System.out.println(module);
		*/
		/*
		Subject subject = new Subject(3, 1, "New TestSubject", false);
		
		System.out.println(subject);
		
		cdbc.deleteSubject(subject);
		cdbc.createSubject(subject);
		
		subject.setArchived(true);
		
		cdbc.updateSubject(subject);
		
		subject = cdbc.getSubject(3);
		
		System.out.println(subject);
		
		Studycourse studycourse = new Studycourse(2, "Maedcheninformatik", false);
		
		System.out.println(studycourse);
		
		cdbc.deleteStudycourse(studycourse);
		cdbc.createStudycourse(studycourse);
		
		studycourse.setName("Mädcheninformatik");
		
		cdbc.updateStudycourse(studycourse);
		
		studycourse = cdbc.getStudycourse(2);
		
		System.out.println(studycourse);
		
		ModuleHandbook mhb = new ModuleHandbook(2, "Neues Testmodulhandbuch", 2, "SoSe2013", false);
		
		System.out.println(mhb);
		
		cdbc.deleteModuleHandbook(mhb);
		cdbc.createModuleHandbook(mhb);
		
		mhb.setSemester("SoSe13");
		
		cdbc.updateModuleHandbook(mhb);
		
		mhb = cdbc.getModuleHandbook(mhb.getID());
		
		System.out.println(mhb);
		*/
		
		ArrayList<User> users = udbc.readUsers();
		
		json = gson.toJson(users);
		
		System.out.println(json);
		
		ArrayList<Event> events = cdbc.getModuleEvents(2);
		
		System.out.println(gson.toJson(events));
		
		ArrayList<Module> modules = cdbc.getSubjectModules(2);
		
		System.out.println(modules);
		
		ArrayList<Subject> subjects = cdbc.getStudycourseSubjects(1);
		
		System.out.println(subjects);
		
		ArrayList<Studycourse> studycourses = cdbc.readStudycourses();
		
		System.out.println(studycourses);
		
		
		ArrayList<ModuleHandbook> mhbs = cdbc.readStudycourseModuleHandbooks(1);
		
		System.out.println(mhbs);
		
		subjects = cdbc.getModuleHandbookSubjects(1);
			
		System.out.println(subjects);
		
		
		event = new Event(6, moduleIDs, "Ana I", 8, "gerhard.baur@uni-ulm.de", false, "test", true,
				"", "", "");
		
		cdbc.createEvent(event);
		
		events = cdbc.getEvents();
		
		System.out.println(events);
		
		System.out.println(modules);
		
		
	
		System.out.println(gson.toJson(event));
		
		User user = new User("email@ex-studios.net", "1234");
		
		System.out.println(gson.toJson(user));
		
		JsonError je = new JsonError("unspecified eventID", "readEvent(...)");
		
		JsonContent jc = new JsonContent(je);
		
		System.out.println(gson.toJson(je));
		
		System.out.println(gson.toJson(jc));
		
		System.out.println(cdbc.getModuleHandbook(1));
		
		
		json = gson.toJson(event);
		
		System.out.println(json);
		
		json = "{\"moduleIDs\":[2],\"name\":\"Tuerrsteherrei\",\"sws\":3,\"lecturer_email\":\"Harkan@Oehmer.de\"}";
		
		System.out.println(json);
		
		event = gson.fromJson(json, Event.class);
		
		
		System.out.println(event);
		
		
		
		cdbc.createEvent(event);
	
		
	}
}
