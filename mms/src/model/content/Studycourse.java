package model.content;

import util.Utilities;

public class Studycourse extends Content {
	
	private int studycourseID, current_moduleHandbook;

	// Konstruktor
	public Studycourse (int studycourseID, int current_moduleHandbook, String name, boolean archived) {
		this.studycourseID = studycourseID;
		this.current_moduleHandbook = current_moduleHandbook;
		this.name = name;
		this.archived = archived;
	}
	
	public Studycourse(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
	@Override
	public String toValues() {
		String value = Utilities.arrayToString(toValuesArray());
		return value;
	}

	@Override
	public String toValueNames() {
		String value = Utilities.arrayToString(toValueNamesArray());
		return value;
	}
	
	@Override
	public String[] toValuesArray() {
		String[] value = {""+studycourseID, ""+current_moduleHandbook, "'"+name+"'",""+archived };
		return value;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] value = {"studycourseID", "current_moduleHandbook", "name", "archived"};
		return value;
	}

	public int getID() {
		return studycourseID;
	}

	public void setID(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
}
