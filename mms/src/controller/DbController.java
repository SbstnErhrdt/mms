package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbController {

	protected Connection db = null;
	private static DbController instance = null;
	private final String user = "sopratest";
	private final String pw = "sopratest";
	private final String driver = "org.gjt.mm.mysql.Driver";
	private final String url = "jdbc:mysql://localhost:3306/sopra?zeroDateTimeBehavior=convertToNull";

	/**
	 * constructor
	 */
	protected DbController() {	
		try {
			Class.forName(driver).newInstance();
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
	 * @param x
	 * @return a string with x question marks, seperated with ","
	 */
	protected String getXQuestionMarks(int x) {
		String string = "";
		for (int i = 0; i < x - 1; i++) {
			string += "?, ";
		}
		string += "?";
		return string;
	}

	/**
	 * closes the database connection
	 */
	public void closeConnection() {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}