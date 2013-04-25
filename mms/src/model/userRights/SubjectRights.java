package model.userRights;

public class SubjectRights extends ContentRights {
	private int subjectID;

	public int getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	} 
	
	public String[] toValueNames() {
		String[] valueNames = super.toValueNames();
		valueNames[4] = "subjectID";
		return valueNames;
	}
	
	public String[] toValueStrings() {
		String[] values = super.toValues();
		values[4] = "" + subjectID;
		return values;
	}
}
