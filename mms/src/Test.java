import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.Type;

import model.Employee;
import model.User;
import model.content.Event;
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
		eventRights.setCanCreate(true);
		eventRights.setCanEdit(true);
		eventRights.setCanDelete(false);
		
		emp.getEmployeeRights().addEventRights(eventRights);
		
		eventRights = new EventRights();
		eventRights.setEventID(6);
		eventRights.setCanCreate(false);
		eventRights.setCanEdit(true);
		eventRights.setCanDelete(false);
		
		emp.getEmployeeRights().addEventRights(eventRights);
		
		//System.out.println("emp.getEmployeeRights().getEventRightsList():");
		//System.out.println(emp.getEmployeeRights().getEventRightsList());
		
		ModuleRights moduleRights = new ModuleRights();
		moduleRights.setModuleID(1);
		moduleRights.setCanCreate(true);
		moduleRights.setCanEdit(true);
		moduleRights.setCanDelete(false);
		
		emp.getEmployeeRights().addModuleRights(moduleRights);
		
		SubjectRights sR = new SubjectRights();
		sR.setSubjectID(1);
		sR.setCanCreate(true);
		sR.setCanEdit(true);
		sR.setCanDelete(false);
		
		emp.getEmployeeRights().addSubjectRights(sR);
		
		StudycourseRights scR = new StudycourseRights();
		scR.setStudycourseID(1);
		scR.setCanCreate(true);
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
	
		/*
		udbc.deleteUser(emp);
		udbc.createUser(emp);
		
		emp = (Employee) udbc.getUser(user1);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(eventRights);
		
		udbc.updateUser(emp);
		
		System.out.println(emp);
		System.out.println(emp.getEmployeeRights());
		*/
		ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
		moduleIDs.add(1);
		moduleIDs.add(2);
		
		Event event = new Event(5, moduleIDs, "Ana IIa", 4, "gerhard.baur@uni-ulm.de", false);
		
		
		
		ContentDbController cdbc = new ContentDbController();
		
		//cdbc.createEvent(event);
		
		
		cdbc.updateEvent(event);
		
		event = cdbc.getEvent(5);
				
		System.out.println(event);
	}
}
