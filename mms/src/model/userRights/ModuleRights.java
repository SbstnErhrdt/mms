package model.userRights;

public class ModuleRights extends ContentRights {
	private int moduleID;

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	
	public String[] toValueNames() {
		String[] valueNames = super.toValueNames();
		valueNames[4] = "moduleID";
		return valueNames;
	}
	
	public String[] toValueStrings() {
		String[] values = super.toValues();
		values[4] = "" + moduleID;
		return values;
	}
}
