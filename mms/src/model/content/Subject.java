package model.content;

import util.Utilities;

public class Subject extends Content {

	/*	DATABASE
	 * 	subjectID	int(5)
	 * 	studycourses_studycourseID	int(5)
	 *  name var(100)
	 *  archived bool
	 */
		
	private int subjectID, moduleHandbooks_moduleHandbookID, studycourses_studycourseID;
	
	
	// Konstruktor
	public Subject (int subjectID, int studycourses_studycourseID, int moduleHandbooks_moduleHandbookID,
			String name, boolean archived, String content, boolean enabled) {
		this.subjectID = subjectID; 
		this.studycourses_studycourseID = studycourses_studycourseID;
		this.moduleHandbooks_moduleHandbookID = moduleHandbooks_moduleHandbookID;
		this.name = name;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
	}
	
	public Subject(int subjectID) {
		this.subjectID = subjectID;
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
				""+moduleHandbooks_moduleHandbookID, "'"+name+"'",
				""+archived, "'"+content+"'", ""+enabled};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] values = {"subjectID", "studycourses_studycourseID", 
				"module_handbooks_modulehandbookID", "name", "archived", 
				"content", "enabled"};
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
}
