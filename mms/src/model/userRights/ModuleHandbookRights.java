package model.userRights;

import util.Utilities;

public class ModuleHandbookRights extends ContentRights {
	private int moduleHandbookID;
	
	public ModuleHandbookRights(int moduleHandbookID, boolean canDelete,
			boolean canEdit) {
		this.moduleHandbookID = moduleHandbookID;
		this.canDelete = canDelete;
		this.canEdit = canEdit;
	}

	public ModuleHandbookRights() {
		// TODO Auto-generated constructor stub
	}

	public int getModuleHandbookID() {
		return moduleHandbookID;
	}

	public void setModuleHandbookID(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	
	public String[] toValueNamesArray() {
		String[] valueNames = {"module_handbooks_moduleHandbookID", "canEdit", "canDelete"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+moduleHandbookID, ""+canEdit, ""+canDelete};
		return values;
	}
	
	public String toString() {
		String string = Utilities.arrayToString(toValuesArray());
		return string;
	}
}
