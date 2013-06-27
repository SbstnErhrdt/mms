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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (canCreateChilds ? 1231 : 1237);
		result = prime * result + subjectID;
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
		SubjectRights other = (SubjectRights) obj;
		if (canCreateChilds != other.canCreateChilds)
			return false;
		if (subjectID != other.subjectID)
			return false;
		return true;
	}
}
