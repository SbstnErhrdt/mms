package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalVarDbController extends DbController {

	/**
	 * constructor
	 */
	public GlobalVarDbController() {
		super();
	}
	
	/**
	 * @param key
	 * @return the value that belongs to the passed key
	 */
	public String getGlobalVar(String key) {
		String query = "SELECT value FROM globalVars WHERE `key`=?";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, key);
			
			System.out.println("[db] getGlobalVar "+ ps);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String value = rs.getString(1);
				rs.close();
				return value;
			} else {
				rs.close();
				return null;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
