package model.content;

import model.DbControllable;

public class ModuleHandbook implements DbControllable{

	/*
	 * 	moduleHandbookID	int(5)
	 *  name	varchar(100)
	 *  studycourses_studycourseID	int(5)
	 *  semester	varchar(2)
	 *  archived tinyint(1)
	 */
	
	private int moduleHandbookID, studycourses_studycourseID, semester;
	private boolean archived;
	private String name;
	
	
	// Konstruktor
	public ModuleHandbook (int moduleHandbookID, String name, int studycourses_studycourseID, int semester,boolean archived) {
		this.moduleHandbookID = moduleHandbookID;
		this.name = name;
		this.studycourses_studycourseID = studycourses_studycourseID;
		this.semester = semester;
		this.archived = archived;
	}
	
	// Getter & Setter
	public int getModuleHandbookID() {
		return moduleHandbookID;
	}

	public void setModuleHandbookID(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	public int getStudycourses_studycourseID() {
		return studycourses_studycourseID;
	}

	public void setStudycourses_studycourseID(int studycourses_studycourseID) {
		this.studycourses_studycourseID = studycourses_studycourseID;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	@Override
	public String[] toValues() {
		String[] values = {moduleHandbookID+",", name+",",  studycourses_studycourseID+",",  semester+",",  archived+",", };
		return values;
	}

	@Override
	public String[] toValueNames() {
		String[] values = {"moduleHandbookID,", "name,", "studycourses_studycourseID,", "semester,", "archived,"};
		return values;
	}
	
}
