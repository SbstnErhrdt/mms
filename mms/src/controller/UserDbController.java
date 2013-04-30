package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.UserRights;

public class UserDbController extends DbController {
	
	private Connection db = null;
	private static DbController instance = null;
	
	public UserDbController() {
		super();
	}
	
	public boolean createUser(User user) {
		String query = "INSERT INTO users (";
	
		String[] valueNames = user.getValueNames();  	
		String[] values = user.getValues();
				
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
		}
		
		
		return true;
	}
	
	public boolean updateUser(User user) {
		String query = "UPDATE users SET ";
		
		String email = user.getEmail();
		String[] valueNames = user.getValueNames();
		String[] values = user.getValues();
		
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
