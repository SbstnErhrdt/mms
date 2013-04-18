package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import model.User;
import model.userRights.UserRights;


public class DbController {
	private Connection db = null;
	private static DbController instance = null;

	private DbController() {
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
		
		System.out.println(query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		UserRights userRights = user.getUserRights();
		
		String[] userRightsValueNames = userRights.toValueNames();
		String [] userRightsValues = userRights.toValues();
		
		query = "INSERT INTO users (";
		
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
		
		return true;
	}
	

	public Object read(Object obj) {
		
		String query = "";
		
		String className = obj.getClass().getSimpleName();
		
		// User
		if(className.equals("User")) {
			User user = (User) obj;
			int id = user.getId();
			query ="SELECT firstName, lastname, title, email, "+
                    "graduation, password, matricNum, semester, "+
                    "rights, emailVerified FROM users WHERE email = \"" + user.getEmail() + "\" AND password = \"" + user.getPassword() + "\";";  	
		}
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			// User
			if(className.equals("User")) {
				if(rs.next()) {
					User user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), Integer.parseInt(rs.getString(7)), Integer.parseInt(rs.getString(8)), new UserRights(rs.getString(9)), 
									Boolean.parseBoolean(rs.getString(10)));
					return user;
				} else {
					System.out.println("No user found with this email and password.");
					return null;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean update(Object obj) {
		String query = "";
		
		if(obj.getClass().getSimpleName().equals("User")) {
			User user = (User) obj;
			int id = user.getId();
			ArrayList<String> values = user.getAttributes();
			String[] valueNames = user.getAttrNames();
			query = "UPDATE users SET ";
			for(int i = 0; i < valueNames.length-1; i++) {
				query += valueNames[i] + " = \"" + values.get(i) + "\", " ;
			}
			query += valueNames[valueNames.length-1] + " = \"" + values.get(values.size()-1) +"\");";		
			query += " WHERE id = " + id;
		}
		
		try {
			db.createStatement().executeUpdate(query);
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void delete(Object obj) {
		String query = "";
		if(obj.getClass().getSimpleName().equals("User")) {
			User user = (User) obj;
			int id = user.getId();
			query = "DELETE FROM users WHERE id = " + id + ";";
		}
		try {
			db.createStatement().executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}