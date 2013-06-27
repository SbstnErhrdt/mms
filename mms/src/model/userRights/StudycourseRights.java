package model.userRights;

public class StudycourseRights extends ContentRights {
	private int studycourseID;
	private boolean canCreateChilds;

	public int getStudycourseID() {
		return studycourseID;
	}

	public void setStudycourseID(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
	public boolean getCanCreateChilds() {
		return canCreateChilds;
	}

	public void setCanCreateChilds(boolean canCreateChilds) {
		this.canCreateChilds = canCreateChilds;
	}	
	
	public String[] toValueNamesArray() {
		String[] valueNames = {"studycourseID", "canEdit", "canDelete", "canCreateChilds"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+studycourseID, ""+canEdit, ""+canDelete, ""+canCreateChilds};
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", studycourseID="+studycourseID+"]";
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (canCreateChilds ? 1231 : 1237);
		result = prime * result + studycourseID;
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
		StudycourseRights other = (StudycourseRights) obj;
		if (canCreateChilds != other.canCreateChilds)
			return false;
		if (studycourseID != other.studycourseID)
			return false;
		return true;
	}
}


