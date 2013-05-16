package model.content;

import java.util.ArrayList;
import java.util.List;

import model.content.Subject;

public class ModuleHandbook extends Content {

	/*
	 * 	moduleHandbookID	int(5)
	 *  name	varchar(100)
	 *  studycourses_studycourseID	int(5)
	 *  semester	varchar(2)
	 *  archived boolean
	 */
	
	private String name, semester;
	private List<Subject> subjects = new ArrayList<Subject>();
	
	
	
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

	// Subjects
	public List<Subject> getSubjectList() {
		return subjects;
	}
	
	public void setSubjectList(List<Subject> subjects ) {
		this.subjects = subjects;
	}
	
	
	
	@Override
	public String toValues() {
		String value = arrayToString(toValuesArray());
		return value;
	}

	
	@Override
	public String toValueNames() {
		String value = arrayToString(toValueNamesArray());
		return value;
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

	
}
