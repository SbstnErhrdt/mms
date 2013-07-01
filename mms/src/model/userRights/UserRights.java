package model.userRights;

import model.DbControllable;

public class UserRights implements DbControllable {
	
	private boolean canLogin;

	// Konstruktor	
	/**
	 * constructor
	 */
	public UserRights() {
	}
	
	// Konstruktor	
	/**
	 * constructor
	 * @param canLogin
	 */
	public UserRights(boolean canLogin) {
			this.canLogin = canLogin;
	}
	
	public boolean getCanLogin() {
		return canLogin;
	}
	
	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}
	
	public String toValueNames() {
		String valueNames = "canLogin";
		return valueNames;
	}
	
	public String toValues() {
		String values = "" + canLogin;
		return values;
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {""+canLogin};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"canLogin"};
		return valueNames;
	}
	
	public String toString() {
		return ""+canLogin;
	}
}