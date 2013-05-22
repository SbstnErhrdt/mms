package model.userRights;

public class ModuleRights extends ContentRights {
	private int moduleID;

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	
	public String[] toValueNamesArray() {
		String[] valueNames = super.toValueNamesArray();
		valueNames[3] = "moduleID";
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = super.toValuesArray();
		values[3] = "" + moduleID;
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", moduleID="+moduleID+"]";
		return string;
	}
	
}
