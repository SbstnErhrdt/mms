package model.content;

import java.sql.Timestamp;

import util.Utilities;

public class Subject extends Content {

	/*	DATABASE
	 * 	subjectID	int(5)
	 * 	studycourses_studycourseID	int(5)
	 *  name var(100)
	 *  archived bool
	 */
		
	private int subjectID, moduleHandbooks_moduleHandbookID, studycourses_studycourseID;
	
	private String type;
	
	
	// Konstruktor
	/**
	 * constructor
	 * @param subjectID
	 * @param studycourses_studycourseID
	 * @param moduleHandbooks_moduleHandbookID
	 * @param type
	 * @param name
	 * @param archived
	 * @param content
	 * @param enabled
	 * @param modifier_email
	 * @param lastModified
	 */
	public Subject (int subjectID, int studycourses_studycourseID, int moduleHandbooks_moduleHandbookID,
			String type, String name, boolean archived, String content, boolean enabled, String modifier_email, 
			Timestamp lastModified) {
		this.subjectID = subjectID; 
		this.studycourses_studycourseID = studycourses_studycourseID;
		this.moduleHandbooks_moduleHandbookID = moduleHandbooks_moduleHandbookID;
		this.type = type;
		this.name = name;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
		this.modifier_email = modifier_email;
		this.lastModified = lastModified;
	}

	/** 
	 * constructor
	 * @param subjectID
	 */
	public Subject(int subjectID) {
		this.subjectID = subjectID;
	}
	
	
	public Subject() {
		// TODO Auto-generated constructor stub
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
		String[] values = {""+subjectID, ""+studycourses_studycourseID, 
				""+moduleHandbooks_moduleHandbookID, "'"+type+"'", "'"+name+"'",
				""+archived, "'"+content+"'", ""+enabled};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] values = {"subjectID", "studycourses_studycourseID", 
				"module_handbooks_modulehandbookID", "type", "name", "archived", 
				"content", "enabled", "modifier_email", "lastModified"};
		return values;
	}


	public int getID() {
		return subjectID;
	}


	public void setID(int subjectID) {
		this.subjectID = subjectID;
	}

	public int getModuleHandbooks_moduleHandbookID() {
		return moduleHandbooks_moduleHandbookID;
	}

	public void setModuleHandbooks_moduleHandbookID(
			int moduleHandbooks_moduleHandbookID) {
		this.moduleHandbooks_moduleHandbookID = moduleHandbooks_moduleHandbookID;
	}

	public int getStudycourses_studycourseID() {
		return studycourses_studycourseID;
	}

	public void setStudycourses_studycourseID(int studycourses_studycourseID) {
		this.studycourses_studycourseID = studycourses_studycourseID;
	}	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
