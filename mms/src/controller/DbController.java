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
		String user = "sopratest";
		String pw = "sopratest";

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
	 * @param x
	 * @return returns a string with x question marks, seperated with ","
	 */
	protected String getXQuestionMarks(int x) {
		String string = "";
		for(int i=0; i<x-1; i++) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}