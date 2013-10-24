import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import routes.JsonErrorContainer;
import routes.JsonError;
import routes.UserRoutes;
import util.Utilities;

import bcrypt.BCrypt;
import bcrypt.BcryptTest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.Type;

import model.Employee;
import model.User;
import model.content.Event;
import model.content.Module;
import model.content.ModuleField;
import model.content.ModuleHandbook;
import model.content.ModuleTemplate;
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
		
		/*
		System.out.println(""+Utilities.createRandomHash());
		
		
		UserDbController udbc = new UserDbController();
		
		Employee emp = new Employee("email@ex-studios.net", "1234", "Sebastian", "Sehrhardt", "Herr Dipl. Ing.",
				"Bachelor", 1234567, 4, new EmployeeRights());
		emp = new Employee("neue_email@ex-studios.net", "1234", "Sebastian", "Der Zweite", "Herr Dr. Dipl. Ing.", "Bachelor", 123456, 4, new UserRights());
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
		try {
			udbc.createUser(emp);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		moduleIDs.add(248);
		moduleIDs.add(249);
		
		Event event = new Event(5, moduleIDs, "Ana IIa", 4, "gerhard.baur@uni-ulm.de", false, 
				"test", true, "irgend n Raum", "irgendwo", "Vorlesung", "Dienstag 10 bis 12", 
				"rob@rob.com", null);
		
		
		
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
		subjectIDs.add(248);
		subjectIDs.add(249);
		
		ArrayList<String> lecturers = new ArrayList<String>();
		lecturers.add("abc@uni-ulm.de");
		lecturers.add("efg@uni-ulm.de");
		
		Module module = new Module(0, 1, 20, 90, "Testmodul", "", "", "abc", "testmodule", 
				"6", "4", "german", "slomka@uni-ulm.de", "keine", "keine", "WiSe", "muendl", "bissele schwatze", 
				"dein Leben", "was soll des sein", "überleben", "Mikey Maus", false, true, false, false, null, 
				subjectIDs, lecturers);
		*/
		
		ContentDbController cdbc = new ContentDbController();
		
		//cdbc.createEvent(event);
		
		/*
		cdbc.updateEvent(event);
		
		event = cdbc.getEvent(5);
				
		System.out.println(event);
		*/
		// System.out.println(module);
		
		/*
		
		cdbc.createModule(module);
		
		module.setDuration(3);
		
		lecturers.add("test@testermann.de");
		
		lecturers = new ArrayList<String>();
		
		module.setLecturers(lecturers);
		
		cdbc.updateModule(module);
		
		module = cdbc.getModule(3);
		System.out.println(module);
		
		System.out.println(cdbc.getSubjectModules(248, false));
		*/
		
		/*
		Subject subject = new Subject(1, 104, 0, "Pflichtfach", "Testfach", false, null, false, "sopra@ex-studios.net", null);
		
		System.out.println(subject);
		
		cdbc.deleteSubject(subject);
		cdbc.createSubject(subject);
		
		subject.setArchived(true);
		
		cdbc.updateSubject(subject);
		
		subject = cdbc.getSubject(272);
		
		System.out.println(subject);
		*/
		
		
		/*
		Studycourse studycourse = new Studycourse(1, 2, "Bachelor", "Medieninformatik", false, null, false, "sopra@ex-studios.net", null);
		
		System.out.println(studycourse);
		
		cdbc.deleteStudycourse(studycourse);
		cdbc.createStudycourse(studycourse);
		
		studycourse.setName("Mädcheninformatik");
		
		cdbc.updateStudycourse(studycourse);
		
		studycourse = cdbc.getStudycourse(115);
		
		System.out.println(studycourse);
		
		
		
		ModuleHandbook mhb = new ModuleHandbook(2, "Neues Testmodulhandbuch", 2, "SoSe2013", false);
		
		System.out.println(mhb);
		
		cdbc.deleteModuleHandbook(mhb);
		cdbc.createModuleHandbook(mhb);
		
		mhb.setSemester("SoSe13");
		
		cdbc.updateModuleHandbook(mhb);
		
		mhb = cdbc.getModuleHandbook(mhb.getID());
		
		System.out.println(mhb);
		
		
		ArrayList<User> users = udbc.readUsers();
		
		json = gson.toJson(users);
		
		System.out.println(json);
		
		ArrayList<Event> events = cdbc.getModuleEvents(2, false);
		
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
				"", "", "", "immer wieder Sonntags");
		
		cdbc.createEvent(event);
		
		events = cdbc.getEvents(false);
		
		System.out.println(events);
		
		System.out.println(modules);
		
		
	
		System.out.println(gson.toJson(event));
		
		User user = new User("email@ex-studios.net", "1234");
		
		System.out.println(gson.toJson(user));
		
		JsonError je = new JsonError("unspecified eventID", "readEvent(...)");
		
		JsonErrorContainer jc = new JsonErrorContainer(je);
		
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
	
		ModuleHandbook mhb = new ModuleHandbook(5,"MHB", 1, 12, true, false, "dies und das", true);
		
		
		System.out.println(cdbc.getDeadline(mhb));
		
		System.out.println(cdbc.readModuleHandbooks());
		
		
		System.out.println(cdbc.getModuleHandbook(1));
		*/
		
		/*
		
		System.out.println(cdbc.getModuleHandbookSubjects(1, false));
		
		System.out.println(cdbc.getSubject(1));
		
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		
		System.out.println(date);
		
		System.out.println(udbc.readReducedUsers());
		
		String email = "rob.ausm.jungle@gmx.de";
		String password = "123";
		String pepper = "modulmanagementsystemsopra20122013";
		
		json = "{\"email\":\"rob.ausm.jungle@gmx.de\",\"firstName\":\"rob\",\"lastName\":\"ausmJungle\",\"password\":\"123\"}";
		
		User user = gson.fromJson(json, User.class);
		
		udbc.deleteUser(user);
		
		
		System.out.println("user: "+user);
		
		String pw = BCrypt.hashpw(password+pepper, BCrypt.gensalt());
		
		user.setPassword(pw);
		
		System.out.println("user with pw: "+user);
		
		email = user.getEmail();
		String hash = Utilities.createRandomHash();
		
		user.setUserRights(new UserRights(false));
		
		
		try {
			System.out.println(udbc.createUser(user));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(udbc.insertConfirmationHash(email, hash));
		
		user = gson.fromJson(json, User.class);
		
		user.setPassword(password+pepper);
		
		System.out.println(udbc.verifyUser(user));
		
		
		
		System.out.println(BCrypt.hashpw("rob"+pepper, BCrypt.gensalt()));
		
		
		
		try {
			System.out.println(udbc.updateUser(user));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		json = "{\"address\":\"Eigenes BÃ¼ro ganz oben neben Uwe SchÃ¶ning\",\"phoneNum\":\"0731 123456\",\"talkTime\":\"jederzeit\",\"employeeRights\":{\"canDeblockCriticalModule\":false,\"canDeblockModule\":false,\"isAdmin\":true,\"moduleRightsList\":[],\"eventRightsList\":[],\"studycourseRightsList\":[],\"subjectRightsList\":[],\"moduleHandbookRightsList\":[],\"canLogin\":true},\"firstName\":\"eugen\",\"lastName\":\"eugen\",\"email\":\"eugen@eugen.com\",\"isEmployee\":true,\"matricNum\":\"1234\",\"semester\":10,\"userRights\":{\"canLogin\":true},\"title\":\"Herr Lehrer\",\"graduation\":\"Lehramt\"}";
		
		user = gson.fromJson(json, User.class);
		
		Employee employee1 = gson.fromJson(json, Employee.class);
		
		user = employee1;
		
		System.out.println(user);
		
		
		
		try {
			udbc.deleteUser(user);
			user.setPassword(BCrypt.hashpw("eugen"+pepper, BCrypt.gensalt()));
			udbc.createUser(user);
			System.out.println(udbc.updateUser(user));
			System.out.println(udbc.getUser(new User(user.getEmail())));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*
		event.setRoom("H22");
		//cdbc.createEvent(event);
		
		
		cdbc.updateEvent(event);
		
		System.out.println(gson.toJson(cdbc.getEvent(5)));
		
		System.out.println(cdbc.cleanEventsVersionsTable(77));
		
		System.out.println(cdbc.cleanModuleVersionsTable(1));
		
		System.out.println(cdbc.cleanSubjectVersionsTable(1));
		
		System.out.println(cdbc.cleanStudycourseVersionsTable(3));
		
		System.out.println(cdbc.cleanModuleHandbookVersionsTable(1));
		
		
		System.out.println(gson.toJson(cdbc.getStudycourseVersions(1)));
		
		*/
	
		/*
		ModuleField mf1 = new ModuleField(-1, 1, "Testfeld1", "24");
		ModuleField mf2 = new ModuleField(-1, 6, "Testfeld2", "true");
		ModuleField mf3 = new ModuleField(-1, 3, "Testfeld3", "TestString");
		
		ArrayList<ModuleField> moduleFields = new ArrayList<ModuleField>();
		moduleFields.add(mf1);
		moduleFields.add(mf2);
		moduleFields.add(mf3);
		
		Module module = new Module(-1, "Testname", false, null, null, moduleFields);
		
		System.out.println(cdbc.createModule(module));
		
		//mf2.setFieldValue("updated");
		mf2.setModuleFieldID(-1);
		
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
		
		subjectIDs.add(467);
		subjectIDs.add(468);
		
		module.setSubjectIDs(subjectIDs);
		
		ArrayList<String> lecturers = new ArrayList<String>();
		
		lecturers.add("gerhard.baur@uni-ulm.de");
		lecturers.add("maurits.ortmanns@uni-ulm.de");
		
		module.setLecturers(lecturers);
		
		System.out.println(cdbc.updateModule(module));
		
		System.out.println(new Gson().toJson(cdbc.getModule(module.getID())));
		
		System.out.println(new Gson().toJson(cdbc.getModules(false)));
		
		System.out.println(new Gson().toJson(cdbc.getSubjectModules(467, false)));
		*/
		/*
		ModuleTemplate mt = cdbc.getModuleTemplate(1);
		System.out.println(mt.getModuleFields());
		System.out.println(new Gson().toJson(mt));
		
		mt.setTemplateName("TestTemplate");
		
		System.out.println(cdbc.createModuleTemplate(mt));
		
		mt.setTemplateName("UpdatedTemplate");
		
		ArrayList<ModuleField> mfs = mt.getModuleFields();
		
		mfs.add(new ModuleField(-1, 1, "Neu"));
		
		mt.setModuleFields(mfs);
		
		System.out.println(cdbc.updateModuleTemplate(mt));
		
		System.out.println(cdbc.deleteModuleTemplate(7));
		*/
		/*
		ModuleField mf = new ModuleField(-1, 1, "Test");
		
		cdbc.createModuleField(mf);
		
		mf.setFieldType(2);
		
		cdbc.updateModuleField(mf);
		
		cdbc.getModuleField(637);
		*/
		
		ModuleField mf1 = new ModuleField(-1, 1, "Testfeld1", "24");
		ModuleField mf2 = new ModuleField(-1, 6, "Testfeld2", "true");
		ModuleField mf3 = new ModuleField(-1, 3, "Testfeld3", "TestString");
		
		ArrayList<ModuleField> moduleFields = new ArrayList<ModuleField>();
		moduleFields.add(mf1);
		moduleFields.add(mf2);
		moduleFields.add(mf3);
		
		Module module = new Module(-1, "Testname", false, null, null, moduleFields);
		
		System.out.println(new Gson().toJson(module));
		
		System.out.println(cdbc.createModule(module));
		
		//mf2.setFieldValue("updated");
		mf2.setModuleFieldID(-1);
		
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
		
		subjectIDs.add(467);
		subjectIDs.add(468);
		
		module.setSubjectIDs(subjectIDs);
		
		ArrayList<String> lecturers = new ArrayList<String>();
		
		lecturers.add("gerhard.baur@uni-ulm.de");
		lecturers.add("maurits.ortmanns@uni-ulm.de");
		
		module.setLecturers(lecturers);
		
		System.out.println(cdbc.updateModule(module));
		
		System.out.println(new Gson().toJson(cdbc.getModule(module.getID())));
		
		System.out.println(new Gson().toJson(cdbc.getModules(false)));
		
		System.out.println(new Gson().toJson(cdbc.getSubjectModules(467, false)));
		
	}
}
