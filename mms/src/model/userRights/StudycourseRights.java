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
}


