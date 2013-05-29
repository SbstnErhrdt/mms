package model.content;

import java.util.ArrayList;
import java.util.List;

public class Subject extends Content {

	/*	DATABASE
	 * 	subjectID	int(5)
	 * 	studycourses_studycourseID	int(5)
	 *  name var(100)
	 *  archived bool
	 */
		
	private List<Module> modules = new ArrayList<Module>();
	
	
	// Konstruktor
	public Subject (int subjectID, int studycourses_studycourseID, String name, boolean archived) {
		this.ID = subjectID; 
		this.parentID = studycourses_studycourseID;
		this.name = name;
		this.archived = archived;
	}
	
	
	// Getter & Setter

	// entfallen - vererbung
	
	public Subject(int subjectID) {
		this.ID = subjectID;
	}


	// Modules
	public List<Module> getModules(Subject subject) {		
		return this.modules;		
	}
	
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	// MODULES
	public List<Module> getModulList() {
		return modules;
	}
	
	public void setEventList(List<Module> modules ) {
		this.modules = modules;
	}
	// ADD MODULE
	public void addModule(Module module) {
		modules.add(module);
	}
	// REMOVE MODULES
	public void removeModule(Module module) {
		modules.remove(module);
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
		String[] value = {""+ID, ""+parentID, "'"+name+"'",""+archived };
		return value;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		String[] value = {"subjectID", "studycourses_studycourseID", "name", "archived"};
		return value;
	}	
}
