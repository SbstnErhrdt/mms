package model.userRights;

import model.DbControllable;

public class UserRights implements DbControllable {
	private boolean canLogin;
	private boolean isEmployeeRights = false;

	public boolean isEmployeeRights() {
		return isEmployeeRights;
	}

	public void setEmployeeRights(boolean isEmployeeRights) {
		this.isEmployeeRights = isEmployeeRights;
	}

	public UserRights() {
		
	}
	
	public boolean getCanLogin() {
		return canLogin;
	}
	
	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}
	
	public String[] toValueNames() {
		String[] valueNames = {"canLogin"};
		return valueNames;
	}
	
	public String[] toValues() {
		String[] values = {"" + canLogin};
		return values;
	}
}