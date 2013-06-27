package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbController {

	protected Connection db = null;
	private static DbController instance = null;

	/**
	 * constructor
	 */
	protected DbController() {
		String url = "jdbc:mysql://localhost:3306/sopra";
		String user = "sopra";
		String pw = "sopra";

		String treiber = "org.gjt.mm.mysql.Driver";

		try {
			Class.forName(treiber).newInstance();
			db = DriverManager.getConnection(url, user, pw);
			instance = this;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return an instance of the DbController
	 */
	public static DbController getInstance() {
		if (instance == null) {
			instance = new DbController();
			return instance;
		} else
			return instance;
	}
	
	/**
	 * closes the database connection
	 */
	public void closeConnection() {
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}