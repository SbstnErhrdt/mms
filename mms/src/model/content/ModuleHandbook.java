package model.content;

public class ModuleHandbook extends Content {

	/*
	 * 	moduleHandbookID	int(5)
	 *  name	varchar(100)
	 *  studycourses_studycourseID	int(5)
	 *  semester	varchar(2)
	 *  archived tinyint(1)
	 */
	
	private boolean archived;
	private String name, semester;
	
	
	// Konstruktor
	public ModuleHandbook (int moduleHandbookID, String name, int studycourses_studycourseID, String semester,boolean archived) {
		this.ID = moduleHandbookID;
		this.name = name;
		this.parentID = studycourses_studycourseID;
		this.semester = semester;
		this.archived = archived;
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
	public String[] toValuesArray() {
		String[] values = {ID+"", "'"+name+"'",  parentID+"",  "'"+semester+"'",  archived+""};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] values = {"moduleHandbookID,", "name,", "studycourses_studycourseID,", "semester,", "archived,"};
		return values;
	}

	@Override
	public String toValues() {
		String values = ID+",'"+name+"',"+parentID+",'"+semester+"',"+archived; 
		return values;
	}

	@Override
	public String toValueNames() {
		String valueNames = "moduleHandbookID, name, studycourses_studycourseID, semester, archived";
		return valueNames;
	}
	
}
