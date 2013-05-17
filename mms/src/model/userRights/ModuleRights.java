package model.userRights;

public class ModuleRights extends ContentRights {
	private int moduleID;

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	
	public String toValueNames() {
		String valueNames = super.toValueNames() + ", moduleID";
		return valueNames;
	}
	
	public String toValues() {
		String values = super.toValues() + ", "+moduleID;
		return values;
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
