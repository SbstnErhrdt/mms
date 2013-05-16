package model.content;

import java.util.ArrayList;
import java.util.List;

import model.content.ModuleHandbook;

public class Studycourse extends Content {
	
	private List<ModuleHandbook> moduleHandbooks = new ArrayList<ModuleHandbook>();
	
	// Konstruktor
	public Studycourse (int studycourseID, String name, boolean archived) {
		this.ID = studycourseID;
		this.name = name;
		this.archived = archived;
	}
	
	// Subjects
	public List<ModuleHandbook> getSubjectList() {
		return moduleHandbooks;
	}
	
	public void setSubjectList(List<ModuleHandbook> moduleHandbooks ) {
		this.moduleHandbooks = moduleHandbooks;
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
		String[] value = {""+ID, "'"+name+"'",""+archived };
		return value;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] value = {"studycourseID","name", "archived"};
		return value;
	}
	
}
