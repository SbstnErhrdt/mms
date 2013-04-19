package model.userRights;

public class StudycourseRights extends ContentRights {
	private int studycourseID;

	public int getStudycourseID() {
		return studycourseID;
	}

	public void setStudycourseID(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
	public String[] toValueNames() {
		String[] valueNames = super.toValueNames();
		valueNames[4] = "studycourseID";
		return valueNames;
	}
	
	public String[] toValueString() {
		String[] values = super.toValueStrings();
		values[4] = "" + studycourseID;
		return values;
	}
}


