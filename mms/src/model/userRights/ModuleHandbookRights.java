package model.userRights;

import util.Utilities;

public class ModuleHandbookRights extends ContentRights {
	private int moduleHandbookID;
	private boolean canCreateChilds;
	
	/**
	 * constructor
	 * @param moduleHandbookID
	 * @param canDelete
	 * @param canEdit
	 */
	public ModuleHandbookRights(int moduleHandbookID, boolean canDelete,
			boolean canEdit, boolean canCreateChilds) {
		this.moduleHandbookID = moduleHandbookID;
		this.canDelete = canDelete;
		this.canEdit = canEdit;
		this.canCreateChilds = canCreateChilds;
	}

	public boolean getCanCreateChilds() {
		return canCreateChilds;
	}

	public void setCanCreateChilds(boolean canCreateChilds) {
		this.canCreateChilds = canCreateChilds;
	}

	/**
	 * constructor
	 */
	public ModuleHandbookRights() {
	}

	public int getModuleHandbookID() {
		return moduleHandbookID;
	}

	public void setModuleHandbookID(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	
	public String[] toValueNamesArray() {
		String[] valueNames = {"module_handbooks_moduleHandbookID", "canEdit", "canDelete", "canCreateChilds"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+moduleHandbookID, ""+canEdit, ""+canDelete, ""+canCreateChilds};
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
		result = prime * result + (canCreateChilds ? 1231 : 1237);
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
		if (canCreateChilds != other.canCreateChilds)
			return false;
		if (moduleHandbookID != other.moduleHandbookID)
			return false;
		return true;
	}
}
