package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bcrypt.BCrypt;

import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.EventRights;
import model.userRights.ModuleHandbookRights;
import model.userRights.ModuleRights;
import model.userRights.StudycourseRights;
import model.userRights.SubjectRights;
import model.userRights.UserRights;

public class UserDbController extends DbController {
	
	/**
	 * constructor
	 */
	public UserDbController() {
		super();
	}
	
	/**
	 * @param user
	 * @return true, if the user was created successfully
	 */
	public boolean createUser(User user) throws SQLException{
		user.setEmail(user.getEmail().toLowerCase());
		
		String query = "INSERT INTO users (";
				
		query += user.toValueNames()+") VALUES(";
		
		query += getXQuestionMarks(8) + ")";
		
		System.out.println("[db] "+query);	// DEBUG
				
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getTitle());
			ps.setString(5, user.getGraduation());
			ps.setString(6, user.getPassword());
			ps.setInt(7, user.getMatricNum());
			ps.setInt(8, user.getSemester());
			
			System.out.println("[db] [db] createUser: " + ps);		// DEBUG
			
			ps.executeUpdate();
			db.commit();
			
			ps.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw e;
			//return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		UserRights userRights = user.getUserRights();
		
		query = "INSERT INTO user_rights (email, ";
		
		query += userRights.toValueNames() + ") VALUES('"+user.getEmail() + "', ";
		
		query += userRights.toValues() + ");";
		
		System.out.println("[db] "+query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if(user.isEmployee()) {
			query = "INSERT INTO employees (";
			
			Employee employee = null;
			try {
				employee = (Employee) user;
				
				if(employee.getEmployeeRights() == null) {
					employee.setEmployeeRights(new EmployeeRights(false,false, false));
				}
				
			} catch(ClassCastException e) {
				employee = new Employee(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), 
						user.getTitle(), user.getGraduation(), user.getMatricNum(), user.getSemester(), user.getUserRights());
			}
										
			// Names
			query += employee.toEmployeeValueNames() + ") VALUES(";
			
			// Values
			query += employee.toEmployeeValues() + ");";
			
			System.out.println("[db] "+query);	// DEBUG
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			query = "INSERT INTO employee_rights (email, ";
			
			EmployeeRights employeeRights = employee.getEmployeeRights();
			
			// Names
			query += employeeRights.toEmployeeValueNames() + ") VALUES(";
			
			// Values
			query += "'"+user.getEmail() + "', " + employeeRights.toEmployeeValues() + ");";
			
			System.out.println("[db] "+query);	// DEBUG
			
			try {
				db.createStatement().executeUpdate(query);
			} catch(SQLException e) {
				e.printStackTrace();
				return false;
			}

			// EventRights
			ArrayList<EventRights> eventRightsList = employeeRights.getEventRightsList();
			if(!eventRightsList.isEmpty()) {
				
				for(EventRights eventRights : eventRightsList) {
					query = "INSERT INTO event_rights (users_email, ";
							
					// Names
					query += eventRights.toValueNames() + ") VALUES('"+user.getEmail()+"', ";
					
					// Values
					query += eventRights.toValues() + ");";
					
					System.out.println("[db] "+query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			
			
			// check, if list is empty, else create new module_rights entries
			// ModuleRights
			ArrayList<ModuleRights> moduleRightsList = employeeRights.getModuleRightsList();
			if(!moduleRightsList.isEmpty()) {
				
				for(ModuleRights moduleRights : moduleRightsList) {
					query = "INSERT INTO module_rights (users_email, ";
					
					// Names
					query += moduleRights.toValueNames() + ") VALUES('"+user.getEmail()+"', ";
					
					// Values
					query += moduleRights.toValues() + ");";
					
					System.out.println("[db] "+query);	// DEBUG
					
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
					query = "INSERT INTO subject_rights (users_email, ";
										
					// Names
					query += subjectRights.toValueNames() + ") VALUES('"+user.getEmail()+"', ";
					
					// Values
					query += subjectRights.toValues() + ");";
					
					System.out.println("[db] "+query);	// DEBUG
					
					try {
						db.createStatement().executeUpdate(query);
					} catch(SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			}			
			
			// ModuleHandbookRights
			ArrayList<ModuleHandbookRights> moduleHandbookRightsList = employeeRights.getModuleHandbookRightsList();
			if(!moduleHandbookRightsList.isEmpty()) {
				
				for(ModuleHandbookRights mhbr : moduleHandbookRightsList) {
					query = "INSERT INTO module_handbook_rights (users_email, ";
							
					// Names
					query += mhbr.toValueNames() + ") VALUES('"+user.getEmail()+"', ";
					
					// Values
					query += mhbr.toValues() + ");";
					
					System.out.println("[db] "+query);	// DEBUG
					
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
					query = "INSERT INTO studycourse_rights (users_email, ";
								
					// Names
					query += studycourseRights.toValueNames() + ") VALUES('"+user.getEmail()+"', ";
					
					// Values
					query += studycourseRights.toValues() + ");";
					
					System.out.println("[db] "+query);	// DEBUG
					
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
	
	
	/**
	 * 
	 * @param user
	 * @return true, if the user was updated successfully
	 */
	public boolean updateUser(User user) throws SQLException{
		
		User oldUser = getUser(new User(user.getEmail()));
		
		// Update: delete and re-create
		if(deleteUser(user)){
			user.setPassword(oldUser.getPassword());	// get back password
			if(createUser(user)) return true;
		} 
		return false;
	}
	
	/**
	 * Deletes a user, the database will delete all referring entries automatically
	 * @param user
	 * @return true, if the user was deleted successfully
	 */
	public boolean deleteUser(User user) {
		String email = user.getEmail();
		String query = "DELETE FROM users WHERE email = '" + email + "';";

		System.out.println("[db] "+query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @param user
	 * @return the whole user object that belongs to the email of the passed user
	 */
	public User getUser(User user) {	
		// User
		String query = "SELECT " + user.toValueNames() +
				" FROM users WHERE email ='"+ user.getEmail()+"';";
			
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if(rs.next()) {
				user.setFirstName(rs.getString(2));	// firstName
				user.setLastName(rs.getString(3)); 	// lastName
				user.setTitle(rs.getString(4));		// title
				user.setGraduation(rs.getString(5));// graduation
				user.setPassword(rs.getString(6)); 	// password
				user.setMatricNum(rs.getInt(7));	// matricNum
				user.setSemester(rs.getInt(8));		// semester
			} else {
				System.out.println("[db] [db] No user found with this email.");
				rs.close();
				return null;
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		UserRights userRights = new UserRights();
		
		// UserRights
		query = "SELECT " + userRights.toValueNames() +
				" FROM user_rights WHERE email ='"+user.getEmail() + "';";
			
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if(rs.next()) {
				userRights.setCanLogin(rs.getBoolean(1));	// canLogin
			} else {
				System.out.println("[db] [db] No user_rights found with this email");
				rs.close();
				return null;
			}	
			rs.close();
			user.setUserRights(userRights);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// are there any employee entries?
		// new Employee-object with same attributes
		Employee employee = new Employee(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), 
				user.getTitle(), user.getGraduation(), user.getMatricNum(), user.getSemester(), user.getUserRights());
		
		query = "SELECT "+employee.toEmployeeValueNames()+ " FROM employees " +
				"WHERE email='"+user.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			if(rs.next()) {
				user.setEmployee(true);		// isEmployee == true
				// rs.getString(1) is email
				employee.setAddress(rs.getString(2));	// address
				employee.setPhoneNum(rs.getString(3));	// phoneNum
				employee.setTalkTime(rs.getString(4)); 	// talkTime
			} else {
				System.out.println("[db] [db] No employees found with this email");
				rs.close();
				return user;		// return user
			}	
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// EmployeeRights
		EmployeeRights employeeRights = new EmployeeRights();
		employeeRights.setCanLogin(userRights.getCanLogin());
		
		query = "SELECT "+employeeRights.toEmployeeValueNames() +
				" FROM employee_rights WHERE email='"+user.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			if(rs.next()) {
				employeeRights.setCanDeblockCriticalModule(rs.getBoolean(1)); // canDeblockCriticalModule
				employeeRights.setCanDeblockModule(rs.getBoolean(2)); 	// canDeblockModule
				employeeRights.setAdmin(rs.getBoolean(3));				// isAdmin
			} else {
				System.out.println("[db] [db] No employee_rights found with this email");
				return employee;		// return user
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// ContentRights
		// EventRights
		ArrayList<EventRights> eventRightsList = new ArrayList<EventRights>();
		EventRights eventRights = new EventRights();;
		
		query = "SELECT "+eventRights.toValueNames()+
				" FROM event_rights WHERE users_email='"+employee.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			while(rs.next()) {
				eventRights.setEventID(rs.getInt(1)); 		// eventID
				eventRights.setCanEdit(rs.getBoolean(2)); 	// canEdit
				eventRights.setCanDelete(rs.getBoolean(3)); // canDelete
				
				eventRightsList.add(eventRights);
				eventRights = new EventRights();
			} 
			if(eventRightsList.isEmpty()) {
				System.out.println("[db] No event_rights found with this users_email");
			} else {
				// set eventRightsList of EmployeeRights
				employeeRights.setEventRightsList(eventRightsList);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// ModuleRights
		ArrayList<ModuleRights> moduleRightsList = new ArrayList<ModuleRights>();
		ModuleRights moduleRights = new ModuleRights();
		
		query = "SELECT "+moduleRights.toValueNames()+
				" FROM module_rights WHERE users_email='"+employee.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			while(rs.next()) {
				moduleRights.setModuleID(rs.getInt(1)); 	// moduleID
				moduleRights.setCanEdit(rs.getBoolean(2)); 	// canEdit
				moduleRights.setCanDelete(rs.getBoolean(3));// canDelete
				moduleRights.setCanCreateChilds(rs.getBoolean(4)); // canCreateChilds
				moduleRightsList.add(moduleRights);
				moduleRights = new ModuleRights();
			} 
			if(moduleRightsList.isEmpty()) {
				System.out.println("[db] No module_rights found with this users_email");
			} else {
				// set moduleRightsList of EmployeeRights
				employeeRights.setModuleRightsList(moduleRightsList);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}

		// SubjectRights
		ArrayList<SubjectRights> subjectRightsList = new ArrayList<SubjectRights>();
		SubjectRights subjectRights = new SubjectRights();
		
		query = "SELECT "+subjectRights.toValueNames()+
				" FROM subject_rights WHERE users_email='"+employee.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			while(rs.next()) {
				subjectRights.setSubjectID(rs.getInt(1)); 		// subjectID
				subjectRights.setCanEdit(rs.getBoolean(2)); 	// canEdit
				subjectRights.setCanDelete(rs.getBoolean(3));	// canDelete
				subjectRights.setCanCreateChilds(rs.getBoolean(4)); // canCreateChilds
				subjectRightsList.add(subjectRights);
				subjectRights = new SubjectRights();
			} 
			if(subjectRightsList.isEmpty()) {
				System.out.println("[db] No subject_rights found with this users_email");
			} else {
				// set subjectRightsList of EmployeeRights
				employeeRights.setSubjectRightsList(subjectRightsList);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}

		// ModuleHandbookRights
		ArrayList<ModuleHandbookRights> moduleHandbookRightsList = new ArrayList<ModuleHandbookRights>();
		ModuleHandbookRights moduleHandbookRights = new ModuleHandbookRights();
		
		query = "SELECT "+moduleHandbookRights.toValueNames()+
				" FROM module_handbook_rights WHERE users_email='"+employee.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			while(rs.next()) {
				moduleHandbookRights.setModuleHandbookID(rs.getInt(1)); 	// moduleHandbookID
				moduleHandbookRights.setCanEdit(rs.getBoolean(2)); 			// canEdit
				moduleHandbookRights.setCanDelete(rs.getBoolean(3));		// canDelete
				moduleHandbookRights.setCanCreateChilds(rs.getBoolean(4)); 	// canCreateChilds
				moduleHandbookRightsList.add(moduleHandbookRights);
				moduleHandbookRights = new ModuleHandbookRights();
			} 
			if(moduleHandbookRightsList.isEmpty()) {
				System.out.println("[db] No module_handbook_rights found with this users_email");
			} else {
				// set moduleHandbookRightsList of EmployeeRights
				employeeRights.setModuleHandbookRightsList(moduleHandbookRightsList);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}		
		
		// StudycourseRights
		ArrayList<StudycourseRights> studycourseRightsList = new ArrayList<StudycourseRights>();
		StudycourseRights studycourseRights = new StudycourseRights();
		
		query = "SELECT "+studycourseRights.toValueNames()+
				" FROM studycourse_rights WHERE users_email='"+employee.getEmail()+"';";
		
		System.out.println("[db] "+query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			while(rs.next()) {
				studycourseRights.setStudycourseID(rs.getInt(1)); 	// studycourseID
				studycourseRights.setCanEdit(rs.getBoolean(2)); 	// canEdit
				studycourseRights.setCanDelete(rs.getBoolean(3));	// canDelete
				studycourseRights.setCanCreateChilds(rs.getBoolean(4)); // canCreateChilds
				studycourseRightsList.add(studycourseRights);
				studycourseRights = new StudycourseRights();
			} 
			if(studycourseRightsList.isEmpty()) {
				System.out.println("[db] No studycourse_rights found with this users_email");
			} else {
				// set studycourseRightsList of EmployeeRights
				employeeRights.setStudycourseRightsList(studycourseRightsList);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		employee.setEmployeeRights(employeeRights);
		
		return employee;
	}
	
	/**
	 * @return a list of all users
	 */
	public ArrayList<User> readUsers() {
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT email FROM users;";
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);		
			while(rs.next()) {
				String email = rs.getString(1);		// email
				users.add(getUser(new User(email)));
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return users;
	}
	
	/**
	 * @return a list of all users, but only with reduced number of attributes
	 */
	public ArrayList<User> readReducedUsers() {
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT email, firstName, lastName FROM users;";
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);		
			while(rs.next()) {
				User user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
				
				EmployeeRights er = getEmployeeRights(user);
				
				if(er != null) {
					Employee employee = new Employee(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), 
							user.getTitle(), user.getGraduation(), user.getMatricNum(), user.getSemester(), user.getUserRights());
					employee.setEmployeeRights(er);
					employee.setEmployee(true);
					users.add(employee);
				} else {
					user.setEmployee(false);
					users.add(user);
				}
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}	
		
		return users;
	}
	
	/**
	 * @param user
	 * @return the employeerights that belong to the passed user
	 */
	private EmployeeRights getEmployeeRights(User user) {
	String query = "SELECT canDeblockModule, canDeblockCriticalModule, isAdmin " +
			"FROM employee_rights WHERE email=?;";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
	
			ps.setString(1, user.getEmail());
			
			ResultSet rs = ps.executeQuery();
			
			db.commit();
			
			if(rs.next()) {
				EmployeeRights er = new EmployeeRights(rs.getBoolean(1), rs.getBoolean(2), 
						rs.getBoolean(3));
				rs.close();
				return er;
			} else {
				rs.close();
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param user
	 * @return the user, if the user was verified successfully, else null
	 */
	public User verifyUser(User user) {
		String query = "SELECT " + user.toValueNames() + " FROM users WHERE email=?;";	
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, user.getEmail());
			
			System.out.println(ps);
			
			ResultSet rs = ps.executeQuery();
			
			db.commit();
			
			if(rs.next()) {
				String dbPassword = rs.getString(6);
				if(BCrypt.checkpw(user.getPassword(), dbPassword)) {	
					user.setFirstName(rs.getString(2));	// firstName
					user.setLastName(rs.getString(3)); 	// lastName
					user.setTitle(rs.getString(4));		// title
					user.setGraduation(rs.getString(5));// graduation
					user.setMatricNum(rs.getInt(7));	// matricNum
					user.setSemester(rs.getInt(8));		// semester
				} else {
					System.out.println("[db] wrong password");
					rs.close();
					return null;
				}
			} else {
				System.out.println("[db] No user found with this email");
				rs.close();
				return null;
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	/**
	 * @param email
	 * @param hash
	 * @return true, if the hash was inserted successfully
	 */
	public boolean insertUserHash(String email, String hash) {
		String query = "DELETE FROM user_hashes WHERE users_email=?";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, email);
			
			ps.executeUpdate();
			db.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		query = "INSERT INTO user_hashes(users_email, hash) VALUES(?,?);";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, email);
			ps.setString(2, hash);
			
			ps.executeUpdate();
			db.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * @param email
	 * @param hash
	 * @return true, if there is such an email-hash combination in the database
	 */
	public boolean verifyUserHash(String email, String hash) {
		
		String query = "SELECT * FROM user_hashes " +
				"WHERE users_email=? AND "+
				"hash=?;";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setString(1, email);
			ps.setString(2, hash);
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			if(rs.next())  {
				rs.close();
				return true;
			} else {
				rs.close();
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param hash
	 * @return the email that belongs to the passed hash
	 */
	public String getHashEmail(String hash) {
		String query = "SELECT users_email FROM user_hashes " +
				"WHERE hash=?;";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setString(1, hash);
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			if(rs.next()) {
				String email = rs.getString(1);
				rs.close();
				return email;
			} else {
				rs.close();
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param email
	 * @param hash
	 * @return true, if the confirmation hash was successfully inserted
	 */
	public boolean insertConfirmationHash(String email, String hash) {
		String query = "INSERT INTO user_confirmation_hashes(users_email, hash) " +
				"VALUES(?,?);";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setString(1, email);
			ps.setString(2, hash);
			
			ps.executeUpdate();
			db.commit();
		
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param hash
	 * @return the email that belongs to the passed hash
	 */
	public String getConfirmationEmail(String hash) {
	
		String query = "SELECT users_email FROM user_confirmation_hashes " +
				"WHERE hash=?;";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setString(1, hash);
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			if(rs.next()) {
				String email = rs.getString(1);
				rs.close();
				updateCanLogin(email, true);
				return email;
			} else {
				rs.close();
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * sets canLogin of the user that belongs to the passed email as passed 
	 * @param email
	 * @param canLogin
	 */
	private void updateCanLogin(String email, boolean canLogin) {
		String query = "UPDATE user_rights SET canLogin=? WHERE email=?;";
		
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setBoolean(1, canLogin);
			ps.setString(2, email);
			
			ps.executeUpdate();
			db.commit();
	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param email
	 * @return true, if the confirmation hash was deleted successfully
	 */
	public boolean deleteConfirmationHash(String email) {
		String query = "DELETE FROM user_confirmation_hashes WHERE users_email=?";
	
		System.out.println("[db] "+query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setString(1, email);
			
			ps.executeUpdate();
			db.commit();
		
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ichScheissAufDichSeb() {
		String sql = "ALTER TABLE  `users` CHANGE  `email`  `email` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , "+
				"CHANGE  `firstName`  `firstName` VARCHAR( 30 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,"+
				"CHANGE  `lastName`  `lastName` VARCHAR( 30 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,"+
				"CHANGE  `title`  `title` VARCHAR( 20 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"+
				"CHANGE  `matricNum`  `matricNum` INT( 8 ) NULL DEFAULT NULL ,"+
				"CHANGE  `current_semester`  `current_semester` INT( 2 ) NULL DEFAULT NULL ,"+
				"CHANGE  `graduation`  `graduation` VARCHAR( 20 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"+
				"CHANGE  `password`  `password` VARCHAR( 64 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL";
		
		try {
			db.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql = "UPDATE users SET password='$2a$10$aNjD8GgINLZvzJ3.6bJOdOCnk3YoXtceVTfTEeqBzYmYOvE3Dkcfi'" +
				" WHERE email='rob@rob.com';";
		
		try {
			db.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @return the email that belongs to the passed firstName and lastname or null if no entry found
	 */
	public String getUserEmail(String firstName, String lastName) {
		String query = "SELECT email FROM users WHERE firstName=? AND lastName=?;";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String email = rs.getString(1);
				rs.close();
				return email;
			} else {
				rs.close();
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
}
