package model.userRights;

public class SubjectRights extends ContentRights {
	private int subjectID;
	private boolean canCreateChilds;

	public int getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}
	
	public boolean getCanCreateChilds() {
		return canCreateChilds;
	}

	public void setCanCreateChilds(boolean canCreateChilds) {
		this.canCreateChilds = canCreateChilds;
	}	
	
	public String[] toValueNamesArray() {
		String[] valueNames = {"subjectID", "canEdit", "canDelete", "canCreateChilds"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+subjectID, ""+canEdit, ""+canDelete, ""+canCreateChilds};
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", subjectID="+subjectID+"]";
		return string;
	}
}
