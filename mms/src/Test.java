import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.EventRights;
import model.userRights.ModuleRights;
import model.userRights.UserRights;
import controller.UserDbController;

public class Test {
	public static void main(String[] args) {
		UserDbController udbc = new UserDbController();
		
		Employee emp = new Employee("email@ex-studios.net", "1234", "Sebastian", "Sehrhardt", "Herr Dipl. Ing.",
				"Bachelor", 1234567, 4, new EmployeeRights(), true);
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
		
		System.out.println("emp.getEmployeeRights().getEventRightsList():");
		System.out.println(emp.getEmployeeRights().getEventRightsList());
		
		ModuleRights moduleRights = new ModuleRights();
		moduleRights.setModuleID(1);
		moduleRights.setCanCreate(true);
		moduleRights.setCanEdit(true);
		moduleRights.setCanDelete(false);
		
		emp.getEmployeeRights().addModuleRights(moduleRights);
		
		//if(udbc.createUser(user)) System.out.println("user "+user+" created successfully");
		
		// if(udbc.deleteUser(user)) System.out.println("user "+user+" deleted succesfully");
		
		User user1 = new User("email@ex-studios.net", "1234");
		//user1.setEmployee(true);
		//user1 = udbc.getUser(user1);
		
		//System.out.println(user1);
		//System.out.println(user1.getUserRights());
		
		udbc.deleteUser(emp);
		udbc.createUser(emp);
		emp = (Employee) udbc.getUser(user1);
		
		
		System.out.println(emp);
		System.out.println(emp.getEmployeeRights());
	}
}
