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
	
	private static DbController instance = null;
	
	public UserDbController() {
		super();
	}
	
	public boolean createUser(User user) {
		String query = "INSERT INTO users (";
				
		query += user.toValueNames()+") VALUES(";
		
		query += user.toValues() + ");";
		
		System.out.println(query);	// DEBUG
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		UserRights userRights = user.getUserRights();
		
		query = "INSERT INTO user_rights (email, ";
		
		query += userRights.toValueNames() + ") VALUES('"+user.getEmail() + "', ";
		
		query += userRights.toValues() + ");";
		
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
		
			// Names
			query += employee.toEmployeeValues() + ") VALUES(";
			
			// Values
			query += employee.toEmployeeValueNames() + ");";
			
			System.out.println(query);	// DEBUG
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			query = "INSERT INTO employee_rights (";
			
			EmployeeRights employeeRights = employee.getEmployeeRights();
			
			// Names
			query += employeeRights.toValueNames() + ") VALUES( ";
			
			// Values
			query += employeeRights.toValues() + ");";
			
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
					
					// Names
					query += moduleRights.toValueNames() + ") VALUES( ";
					
					// Values
					query += moduleRights.toValues() + ");";
					
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
							
					// Names
					query += eventRights.toValueNames() + ") VALUES( ";
					
					// Values
					query += eventRights.toValues() + ");";
					
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
								
					// Names
					query += studycourseRights.toValueNames() + ") VALUES( ";
					
					// Values
					query += studycourseRights.toValues() + ");";
					
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
										
					// Names
					query += subjectRights.toValueNames() + ") VALUES( ";
					
					// Values
					query += subjectRights.toValues() + ");";
					
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
		String[] valueNames = user.toValueNamesArray();
		String[] values = user.toValuesArray();
		
		for(int i = 0; i < valueNames.length-1; i++) {
			query += valueNames[i] + " = '" + values[i] + "', " ;
		}
		query += valueNames[valueNames.length-1] + " = '" + values[values.length-1] +"');";		
		query += " WHERE email = '" + email + "';";
		
		System.out.println(query);
		
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

		System.out.println(query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// delete UserRights of user
		query = "DELETE FROM user_rights WHERE email = '" + email + "';";
		
		System.out.println(query);
		
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
			
			System.out.println(query);
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			query = "DELETE FROM employee_rights WHERE email = '" + email + "';";
			
			System.out.println(query);
			
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
				
				System.out.println(query);
				
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
				System.out.println(query);
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
				System.out.println(query);
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
				System.out.println(query);
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
		
		// User
		String query = "SELECT " + user.toValueNames() +
				" FROM users WHERE email ='"+ 
                    user.getEmail() + "' AND password = '" + user.getPassword() + "';";
			
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if(rs.next()) {
				user.setFirstName(rs.getString(2));	// firstName
				user.setLastName(rs.getString(3)); 	// lastName
				user.setTitle(rs.getString(4));		// title
				user.setGraduation(rs.getString(5));// graduation
				user.setMatricNum(rs.getInt(7));	// matricNum
				user.setSemester(rs.getInt(8));		// semester
				
			} else {
				System.out.println("No user found with this email and password.");
				return null;
			}			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		UserRights userRights = new UserRights();
		
		// UserRights
		query = "SELECT " + userRights.toValueNames() +
				" FROM user_rights WHERE email ='"+user.getEmail() + "';";
			
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if(rs.next()) {
				userRights.setCanLogin(rs.getBoolean(1));	// canLogin
			} else {
				System.out.println("No user_rights found with this email");
				return null;
			}	
			user.setUserRights(userRights);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// are there any employee entries?
		Employee employee = new Employee(user.getEmail(), user.getPassword());
		
		query = "SELECT "+employee.toEmployeeValueNames()+ " FROM employees " +
				"WHERE email='"+user.getEmail()+"'";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			if(rs.next()) {
				user.setEmployee(true);		// isEmployee == true
				employee = (Employee) user;	// convert User to Employee
				//employee.setAddress(rs);
			} else {
				System.out.println("No employees found with this email");
				return user;		// return user
			}	
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	
		return user;
	}
}
