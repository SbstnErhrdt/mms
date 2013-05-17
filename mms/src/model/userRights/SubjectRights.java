package model.userRights;

public class SubjectRights extends ContentRights {
	private int subjectID;

	public int getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	} 
	
	public String toValueNames() {
		String valueNames = super.toValueNames() + ", subjectID";
		return valueNames;
	}
	
	public String toValues() {
		String values = super.toValues() + ", "+subjectID;
		return values;
	}
	
	public String[] toValueNamesArray() {
		String[] valueNames = super.toValueNamesArray();
		valueNames[3] = "subjectID";
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = super.toValuesArray();
		values[3] = "" + subjectID;
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", subjectID="+subjectID+"]";
		return string;
	}
}
