package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.User;
import model.userRights.EmployeeRights;
import model.userRights.UserRights;

public class DbController {

	private Connection db = null;
	private static DbController instance = null;

	protected DbController() {
		String url = "jdbc:mysql://localhost:3306/mms";
		String user = "root";
		String pw = "";

		String treiber = "org.gjt.mm.mysql.Driver";

		try {
			Class.forName(treiber).newInstance();
			db = DriverManager.getConnection(url, user, pw);
			instance = this;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static DbController getInstance() {
		if (instance == null) {
			instance = new DbController();
			return instance;
		} else
			return instance;

	}

	// USER ERSTELLEN
	public boolean createUser(User user) {

		/*
		 * DATENBANK User
		 * 
		 * VAR email VAR firstName VAR lastName VAR title INT matricNum INT
		 * current_semester VAR graduation VAR password
		 */

		String query = "INSERT INTO users (";

		String[] valueNames = user.toValueNames();
		String[] values = user.toValues();

		// Namen der Spalten
		for (int i = 0; i < valueNames.length; i++) {
			query += valueNames[i];
		}
		query += ")";

		// Inhalt der Spalten
		for (int i = 0; i < values.length ; i++) {
			query += values[i];
		}
		query += ")";

		// query +=
		// "VALUES ("+user.getEmail()+","+user.getFirstName()+","+user.getLastName()+","+user.getTitle()+","+user.getMatricNum()+","+user.getSemester()+","+user.getGraduation()+","+user.getPassword()+")";

		System.out.println("CREATE USER: " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// Userrechte erstellen
		UserRights userRights = user.getUserRights();

		String[] userRightsValueNames = userRights.toValueNames();
		String[] userRightsValues = userRights.toValues();

		query = "INSERT INTO user_rights (";

		for (int i = 0; i < userRightsValueNames.length; i++) {
			query += userRightsValueNames[i];
		}
		query += ") VALUES( ";

		for (int i = 0; i < userRightsValues.length; i++) {
			query += userRightsValues[i];
		}
		query += ");";

		System.out.println(query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// USERDATEN HOLEN
	public User getUser(User user) {

		String query = "SELECT SELECT firstName, lastname, title, email, "
				+ "graduation, password, matricNum, semester, "
				+ "rights, emailVerified FROM users WHERE email ='"
				+ user.getEmail() + ";";

		System.out.println(query);

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				User newUser = new User(rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), Integer.parseInt(rs.getString(7)),
						Integer.parseInt(rs.getString(8)), null, // userRights
																	// are set
																	// later
						Boolean.parseBoolean(rs.getString(10)));
				return newUser;
			} else {
				System.out
						.println("No user found with this email and password.");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// USER UPDATEN
	public boolean updateUser(User user) {
		String query = "UPDATE users SET ";

		String email = user.getEmail();
		String[] valueNames = user.toValueNames();
		String[] values = user.toValues();

		for (int i = 0; i < valueNames.length; i++) {
			query += valueNames[i] + " = '" + values[i] + "', ";
		}
		query += "');";
		query += " WHERE email = '" + email + "';";

		try {
			db.createStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// USER ENTFERNEN
	public boolean deleteUser(User user) {

	

		String email = user.getEmail();
		String query = "DELETE FROM users WHERE email = '" + email + "';";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// delete UserRights of user
		query = "DELETE FROM user_rights WHERE email = '" + email + "';";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		if (user.isEmployee()) {
			Employee employee = (Employee) user;

			// delete Employee entries
			query = "DELETE FROM employees WHERE email = '" + email + "';";

			try {
				db.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			query = "DELETE FROM employee_rights WHERE email = '" + email
					+ "';";

			try {
				db.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			// delete ContentRights of user
			EmployeeRights employeeRights = (EmployeeRights) user
					.getUserRights();

			// check, if list is empty, else delete all objects from database
			// ModuleRights
			if (!employeeRights.getModuleRightsList().isEmpty()) {
				query = "DELETE FROM module_rights WHERE users_email = '"
						+ email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// EventRights
			if (!employeeRights.getEventRightsList().isEmpty()) {
				query = "DELETE FROM event_rights WHERE users_email = '"
						+ email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// StudycourseRights
			if (!employeeRights.getStudycourseRightsList().isEmpty()) {
				query = "DELETE FROM studycourse_rights WHERE user_email = '"
						+ email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			// SubjectRights
			if (!employeeRights.getSubjectRightsList().isEmpty()) {
				query = "DELETE FROM subject_rights WHERE user_email = '"
						+ email + "';";
				try {
					db.createStatement().executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}

			return true;
		}
		return true;

	}

	// EVENT ERSTELLEN
	public boolean createEvent(Event event) {
		
		return true;
	}
	
	// TODO: EVENT UPDATEN
	
	// TODO: EVENT ENTFERNEN
		
	// TODO: MODUL ERSTELLEN 
	
	// TODO: MODUL UPDATEN
	
	// TODO: MODUL ENTFERNEN
	
	// TODO: MODULHANDBOOK ERSTELLEN
	
	// TODO: MODULHANDBOOK UPDATEN
	
	// TODO: MODULHANDBOOK ENTFERNEN
	
	// TODO: STUDYCOURSE ERSTELLEN
	
	// TODO: STUDYCOURSE UPDATEN
	
	// TODO: STUDYCOURSE ENTFERNEN
	
	// TODO: SUBJECT ERSTELLEN
	
	// TODO: SUBJECT UPDATEN
	
	// TODO: SUBJECT ENTFERNEN
}