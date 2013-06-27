package model.userRights;

public class ModuleRights extends ContentRights {
	private int moduleID;
	private boolean canCreateChilds;

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	
	public boolean getCanCreateChilds() {
		return canCreateChilds;
	}

	public void setCanCreateChilds(boolean canCreateChilds) {
		this.canCreateChilds = canCreateChilds;
	}	
	
	public String[] toValueNamesArray() {
		String[] valueNames = {"moduleID", "canEdit", "canDelete", "canCreateChilds"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+moduleID, ""+canEdit, ""+canDelete, ""+canCreateChilds};
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", moduleID="+moduleID+"]";
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (canCreateChilds ? 1231 : 1237);
		result = prime * result + moduleID;
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
		ModuleRights other = (ModuleRights) obj;
		if (canCreateChilds != other.canCreateChilds)
			return false;
		if (moduleID != other.moduleID)
			return false;
		return true;
	}
}
