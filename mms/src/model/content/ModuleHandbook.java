package model.content;

import util.Utilities;

public class ModuleHandbook extends Content {

	/*
	 * 	moduleHandbookID	int(5)
	 *  name	varchar(100)
	 *  studycourses_studycourseID	int(5)
	 *  semester	varchar(2)
	 *  archived boolean
	 */
	
	private String semester;
	private int moduleHandbookID, studycourses_studycourseID;
	
	// Konstruktor
	public ModuleHandbook (int moduleHandbookID, String name, int studycourses_studycourseID, 
			String semester, boolean archived, String content, boolean enabled) {
		this.moduleHandbookID = moduleHandbookID;
		this.name = name;
		this.studycourses_studycourseID = studycourses_studycourseID;
		this.semester = semester;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
	}
	
	public ModuleHandbook(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	// Getter & Setter
	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
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
		String[] values = {moduleHandbookID+"", "'"+name+"'", 
				studycourses_studycourseID+"",  "'"+semester+"'",  
				archived+"", ""+"'"+content+"'", ""+enabled};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] values = {"moduleHandbookID", "name", 
				"studycourses_studycourseID", "semester", 
				"archived", "content", "enabled"};
		return values;
	}

	public int getID() {
		return moduleHandbookID;
	}

	public void setID(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	
}
