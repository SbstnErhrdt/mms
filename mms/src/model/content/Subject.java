package model.content;

import java.util.ArrayList;
import java.util.List;

public class Subject extends Content {

	/*	DATABASE
	 * 	subjectID	int(5)
	 * 	module_handbooks_moduleHandbookID	int(5)
	 *  studycourses_sudycourseID	int(5)
	 *  name var(100)
	 */
	
	
	private int subjectID, module_handbooks_moduleHandbookID, studycourses_sudycourseID;
	private String name;
	
	private List<Module> modules = new ArrayList<Module>();
	
	
	// Konstruktor
	public Subject (int subjectID, int module_handbooks_moduleHandbookID, int studycourses_sudycourseID, String name) {
		this.subjectID = subjectID; 
		this.module_handbooks_moduleHandbookID = module_handbooks_moduleHandbookID;
		this.studycourses_sudycourseID = studycourses_sudycourseID;
		this.name = name;
	}
	
	
	// Getter & Setter
	public int getSubjectID() {
		return subjectID;
	}


	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}


	public int getModule_handbooks_moduleHandbookID() {
		return module_handbooks_moduleHandbookID;
	}


	public void setModule_handbooks_moduleHandbookID(
			int module_handbooks_moduleHandbookID) {
		this.module_handbooks_moduleHandbookID = module_handbooks_moduleHandbookID;
	}


	public int getStudycourses_sudycourseID() {
		return studycourses_sudycourseID;
	}


	public void setStudycourses_sudycourseID(int studycourses_sudycourseID) {
		this.studycourses_sudycourseID = studycourses_sudycourseID;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}	
	
	
	// Modules
	public List<Module> getModules(Subject subject) {		
		return this.modules;		
	}
	
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	
	
}
