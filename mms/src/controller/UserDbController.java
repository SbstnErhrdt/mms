package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.EventRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;
import model.userRights.UserRights;

public class UserDbController extends DbController {
	
	private Connection db = null;
	private static DbController instance = null;
	
	public UserDbController() {
		super();
	}
	
	public boolean createUser(User user) {
		String query = "INSERT INTO users (";
	
		String[] valueNames = user.toValueNames();  	
		String[] values = user.toValues();
				
		for(int i = 0; i < valueNames.length-1; i++) {
			query += valueNames[i] + ", ";
		}
		query += valueNames[valueNames.length-1] + ") VALUES( ";
		
		for(int i = 0; i < values.length-1; i++) {
			query += values[i] + ", ";
		}
		query += values[values.length-1] + ");";
		
		System.out.println(query);	// DEBUG
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		UserRights userRights = user.getUserRights();
		
		String[] userRightsValueNames = userRights.toValueNames();
		String [] userRightsValues = userRights.toValues();
		
		query = "INSERT INTO user_rights (";
		
		for(int i = 0; i < userRightsValueNames.length-1; i++) {
			query += userRightsValueNames[i] + ", ";
		}
		query += userRightsValueNames[userRightsValueNames.length-1] + ") VALUES( ";
		
		for(int i = 0; i < userRightsValues.length-1; i++) {
			query += userRightsValues[i] + ", ";
		}
		query += userRightsValues[userRightsValues.length-1] + ");";
		
		System.out.println(query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if(user.isEmployee()) {
			query = "INSERT INTO employees (";
			
			Employee employee = (Employee) user;
			
			String[] employeeValues = employee.toEmployeeValues();
			String[] employeeValueNames = employee.toEmployeeValueNames();
			
			// Names
			for(int i = 0; i < employeeValueNames.length-1; i++) {
				query += employeeValueNames[i] + ", ";
			}
			query += employeeValueNames[employeeValueNames.length-1] + ") VALUES( ";
			
			// Values
			for(int i = 0; i < employeeValues.length-1; i++) {
				query += employeeValues[i] + ", ";
			}
			query += employeeValues[employeeValues.length-1] + ");";
			
			System.out.println(query);	// DEBUG
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			query = "INSERT INTO employee_rights (";
			
			EmployeeRights employeeRights = employee.getEmployeeRights();
			
			String[] employeeRightsValueNames = employeeRights.toValueNames();
			String[] employeeRightsValues = employeeRights.toValues();
			
			// Names
			for(int i = 0; i < employeeRightsValueNames.length-1; i++) {
				query += employeeRightsValueNames[i] + ", ";
			}
			query += employeeRightsValueNames[employeeRightsValueNames.length-1] + ") VALUES( ";
			
			// Values
			for(int i = 0; i < employeeRightsValues.length-1; i++) {
				query += employeeRightsValues[i] + ", ";
			}
			query += employeeRightsValues[employeeRightsValues.length-1] + ");";
			
			System.out.println(query);	// DEBUG
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
		
			// check, if list is empty, else create new module_rights entries
			// ModuleRights
			ArrayList<ModuleRights> moduleRightsList = employeeRights.getModuleRightsList();
			if(!moduleRightsList.isEmpty()) {
				
				for(ModuleRights moduleRights : moduleRightsList) {
					query = "INSERT INTO module_rights (";
					
					String[] moduleRightsValueNames = moduleRights.toValueNames();
					String[] moduleRightsValues = moduleRights.toValues();
					
					// Names
					for(int i = 0; i < moduleRightsValueNames.length-1; i++) {
						query += moduleRightsValueNames[i] + ", ";
					}
					query += moduleRightsValueNames[moduleRightsValueNames.length-1] + ") VALUES( ";
					
					// Values
					for(int i = 0; i < moduleRightsValues.length-1; i++) {
						query += moduleRightsValues[i] + ", ";
					}
					query += moduleRightsValues[moduleRightsValues.length-1] + ");";
					
					System.out.println(query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			// EventRights
			ArrayList<EventRights> eventRightsList = employeeRights.getEventRightsList();
			if(!eventRightsList.isEmpty()) {
				
				for(EventRights eventRights : eventRightsList) {
					query = "INSERT INTO event_rights (";
					
					String[] eventRightsValueNames = eventRights.toValueNames();
					String[] eventRightsValues = eventRights.toValues();
					
					// Names
					for(int i = 0; i < eventRightsValueNames.length-1; i++) {
						query += eventRightsValueNames[i] + ", ";
					}
					query += eventRightsValueNames[eventRightsValueNames.length-1] + ") VALUES( ";
					
					// Values
					for(int i = 0; i < eventRightsValues.length-1; i++) {
						query += eventRightsValues[i] + ", ";
					}
					query += eventRightsValues[eventRightsValues.length-1] + ");";
					
					System.out.println(query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			// StudycourseRights
			ArrayList<StudycourseRights> studycourseRightsList = employeeRights.getStudycourseRightsList();
			if(!studycourseRightsList.isEmpty()) {
				
				for(StudycourseRights studycourseRights : studycourseRightsList) {
					query = "INSERT INTO event_rights (";
					
					String[] studycourseRightsValueNames = studycourseRights.toValueNames();
					String[] studycourseRightsValues = studycourseRights.toValues();
					
					// Names
					for(int i = 0; i < studycourseRightsValueNames.length-1; i++) {
						query += studycourseRightsValueNames[i] + ", ";
					}
					query += studycourseRightsValueNames[studycourseRightsValueNames.length-1] + ") VALUES( ";
					
					// Values
					for(int i = 0; i < studycourseRightsValues.length-1; i++) {
						query += studycourseRightsValues[i] + ", ";
					}
					query += studycourseRightsValues[studycourseRightsValues.length-1] + ");";
					
					System.out.println(query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			// SubjectRights
			ArrayList<SubjectRights> subjectRightsList = employeeRights.getSubjectRightsList();
			if(!subjectRightsList.isEmpty()) {
				
				for(SubjectRights subjectRights : subjectRightsList) {
					query = "INSERT INTO event_rights (";
					
					String[] subjectRightsValueNames = subjectRights.toValueNames();
					String[] subjectRightsValues = subjectRights.toValues();
					
					// Names
					for(int i = 0; i < subjectRightsValueNames.length-1; i++) {
						query += subjectRightsValueNames[i] + ", ";
					}
					query += subjectRightsValueNames[subjectRightsValueNames.length-1] + ") VALUES( ";
					
					// Values
					for(int i = 0; i < subjectRightsValues.length-1; i++) {
						query += subjectRightsValues[i] + ", ";
					}
					query += subjectRightsValues[subjectRightsValues.length-1] + ");";
					
					System.out.println(query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	
	public boolean updateUser(User user) {
		String query = "UPDATE users SET ";
		
		String email = user.getEmail();
		String[] valueNames = user.toValueNames();
		String[] values = user.toValues();
		
		for(int i = 0; i < valueNames.length-1; i++) {
			query += valueNames[i] + " = '" + values[i] + "', " ;
		}
		query += valueNames[valueNames.length-1] + " = '" + values[values.length-1] +"');";		
		query += " WHERE email = '" + email + "';";
		
		try {
			db.createStatement().executeUpdate(query);
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteUser(User user) {
		String email = user.getEmail();
		String query = "DELETE FROM users WHERE email = '" + email + "';";

		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// delete UserRights of user
		query = "DELETE FROM user_rights WHERE email = '" + email + "';";
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if(user.isEmployee()) {	
			Employee employee = (Employee) user;
			
			// delete Employee entries
			query = "DELETE FROM employees WHERE email = '" + email + "';";
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			query = "DELETE FROM employee_rights WHERE email = '" + email + "';";
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
		
			// delete ContentRights of user
			EmployeeRights employeeRights = (EmployeeRights) user.getUserRights();
			
			// check, if list is empty, else delete all objects from database
			// ModuleRights
			if(!employeeRights.getModuleRightsList().isEmpty()) {
				query = "DELETE FROM module_rights WHERE users_email = '" + email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// EventRights
			if(!employeeRights.getEventRightsList().isEmpty()) {
				query = "DELETE FROM event_rights WHERE users_email = '" + email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// StudycourseRights
			if(!employeeRights.getStudycourseRightsList().isEmpty()) {
				query = "DELETE FROM studycourse_rights WHERE user_email = '" + email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// SubjectRights
			if(!employeeRights.getSubjectRightsList().isEmpty()) {
				query = "DELETE FROM subject_rights WHERE user_email = '" + email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	public User getUser(User user) {
		
		String query = "SELECT SELECT firstName, lastname, title, email, "+
                    "graduation, password, matricNum, semester, "+
                    "emailVerified FROM users WHERE email ='"+ 
                    user.getEmail() + "' AND password = '" + user.getPassword() + "';";
			
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if(rs.next()) {
				User newUser = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), Integer.parseInt(rs.getString(7)), Integer.parseInt(rs.getString(8)), null,	// userRights are set later 
						Boolean.parseBoolean(rs.getString(10)));
				return newUser;
			} else {
				System.out.println("No user found with this email and password.");
				return null;
			}			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
