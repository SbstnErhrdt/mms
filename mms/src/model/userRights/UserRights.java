package model.userRights;

import model.DbControllable;

public class UserRights implements DbControllable {
	
	private boolean canLogin;
	private boolean isEmployeeRights = false;

	// Konstruktor	
	public UserRights() {
		
	}
	
	// Konstruktor	
	public UserRights(boolean canLogin) {
			this.canLogin = canLogin;
	}
	
	// Getter & Setter	
	public boolean isEmployeeRights() {
		return isEmployeeRights;
	}

	public void setEmployeeRights(boolean isEmployeeRights) {
		this.isEmployeeRights = isEmployeeRights;
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
}