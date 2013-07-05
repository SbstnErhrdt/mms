package model.content;

import java.sql.Timestamp;

import util.Utilities;

public class Studycourse extends Content {
	
	public int getCurrent_moduleHandbook() {
		return current_moduleHandbook;
	}

	public void setCurrent_moduleHandbook(int current_moduleHandbook) {
		this.current_moduleHandbook = current_moduleHandbook;
	}

	private int studycourseID, current_moduleHandbook;

	// Konstruktor
	/**
	 * constructor
	 * @param studycourseID
	 * @param current_moduleHandbook
	 * @param name
	 * @param archived
	 * @param content
	 * @param enabled
	 * @param modifier_email
	 * @param lastModified
	 */
	public Studycourse (int studycourseID, int current_moduleHandbook, 
			String name, boolean archived, String content, boolean enabled, 
			String modifier_email, Timestamp lastModified) {
		this.studycourseID = studycourseID;
		this.current_moduleHandbook = current_moduleHandbook;
		this.name = name;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
		this.modifier_email = modifier_email;
		this.lastModified = lastModified;
	}
	
	/**
	 * constructor
	 * @param studycourseID
	 */
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
		String[] value = {""+studycourseID, ""+current_moduleHandbook, "'"+name+"'",""+archived, 
				"'"+content+"'", ""+enabled};
		return value;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] value = {"studycourseID", "current_moduleHandbook", "name", "archived", 
				"content", "enabled", "modifier_email", "lastModified"};
		return value;
	}

	public int getID() {
		return studycourseID;
	}

	public void setID(int studycourseID) {
		this.studycourseID = studycourseID;
	}
	
}
