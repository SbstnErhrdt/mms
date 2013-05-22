package model.userRights;

public class StudycourseRights extends ContentRights {
	private int studycourseID;

	public int getStudycourseID() {
		return studycourseID;
	}

	public void setStudycourseID(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
	public String[] toValueNamesArray() {
		String[] valueNames = super.toValueNamesArray();
		valueNames[3] = "studycourseID";
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = super.toValuesArray();
		values[3] = "" + studycourseID;
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", studycourseID="+studycourseID+"]";
		return string;
	}
}


