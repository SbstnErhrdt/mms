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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + moduleHandbookID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleHandbookRights other = (ModuleHandbookRights) obj;
		if (moduleHandbookID != other.moduleHandbookID)
			return false;
		return true;
	}
}
