package model.userRights;

import java.util.ArrayList;

public class EmployeeRights extends UserRights {
	private boolean canDeblockCriticalModule, canDeblockModule, isAdmin;
	private ArrayList<ModuleRights> moduleRightsList;
	private ArrayList<EventRights> eventRightsList;
	private ArrayList<StudycourseRights> studycourseRightsList;
	private ArrayList<SubjectRights> subjectRightsList;
	
	private boolean isEmployeeRights = true;
	
	public EmployeeRights(boolean canDeblockCriticalModule, boolean canDeblockModule, 
			boolean isAdmin) {
		super(true);
		this.canDeblockCriticalModule = canDeblockCriticalModule;
		this.canDeblockModule = canDeblockModule;
		this.isAdmin = isAdmin;
		eventRightsList = new ArrayList<EventRights>();
		moduleRightsList = new ArrayList<ModuleRights>();
		subjectRightsList = new ArrayList<SubjectRights>();
		studycourseRightsList = new ArrayList<StudycourseRights>();
		
	}
	
	public ArrayList<ModuleRights> getModuleRightsList() {
		return moduleRightsList;
	}



	public void setModuleRightsList(ArrayList<ModuleRights> moduleRightsList) {
		this.moduleRightsList = moduleRightsList;
	}



	public ArrayList<StudycourseRights> getStudycourseRightsList() {
		return studycourseRightsList;
	}



	public void setStudycourseRightsList(
			ArrayList<StudycourseRights> studycourseRightsList) {
		this.studycourseRightsList = studycourseRightsList;
	}



	public ArrayList<SubjectRights> getSubjectRightsList() {
		return subjectRightsList;
	}



	public void setSubjectRightsList(ArrayList<SubjectRights> subjectRightsList) {
		this.subjectRightsList = subjectRightsList;
	}



	public boolean isEmployeeRights() {
		return isEmployeeRights;
	}



	public void setEmployeeRights(boolean isEmployeeRights) {
		this.isEmployeeRights = isEmployeeRights;
	}



	public EmployeeRights() {
		super();
	}
	
	
	
	public boolean isCanDeblockCriticalModule() {
		return canDeblockCriticalModule;
	}



	public void setCanDeblockCriticalModule(boolean canDeblockCriticalModule) {
		this.canDeblockCriticalModule = canDeblockCriticalModule;
	}



	public boolean isCanDeblockModule() {
		return canDeblockModule;
	}



	public void setCanDeblockModule(boolean canDeblockModule) {
		this.canDeblockModule = canDeblockModule;
	}



	public boolean isAdmin() {
		return isAdmin;
	}



	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}



	public ArrayList<EventRights> getEventRightsList() {
		return eventRightsList;
	}



	public void setEventRightsList(ArrayList<EventRights> eventRightsList) {
		this.eventRightsList = eventRightsList;
	}

	
	public void addModuleRights(ModuleRights moduleRights) {
		moduleRightsList.add(moduleRights);
	}
	
	
	public void addEventRights(EventRights eventRights) {
		eventRightsList.add(eventRights);
	}
	
	
	public void addStudycourseRights(StudycourseRights studycourseRights) {
		studycourseRightsList.add(studycourseRights);
	}
	
	
	public void addSubjectRights(SubjectRights subjectRights) {
		subjectRightsList.add(subjectRights);
	}
	
	
	public void removeModuleRights(ModuleRights moduleRights) {
		moduleRightsList.remove(moduleRights);
	}
	
	
	public void removeEventRights(EventRights eventRights) {
		eventRightsList.remove(eventRights);
	}
	
	
	public void removeStudycourseRights(StudycourseRights studycourseRights) {
		studycourseRightsList.remove(studycourseRights);
	}
	
	
	public void removeSubjectRights(SubjectRights subjectRights) {
		subjectRightsList.remove(subjectRights);
	}
	
	public String toEmployeeValueNames() {
		String valueNames = "canDeblockCriticalModule, canDeblockModule, isAdmin";
		return valueNames;
	}
	
	public String toEmployeeValues() {
		String values = canDeblockCriticalModule+", "+canDeblockModule+", "+isAdmin;
		return values;
	}
	
	public String[] toValueEmployeeNamesArray() {
		String[] valueNames = {"canDeblockCriticalModule", "canDeblockModule", "isAdmin"};
		return valueNames;
	}
	
	public String[] toEmployeeValuesArray() {
		String[] values = {""+canDeblockCriticalModule, "" + canDeblockModule, "" + isAdmin};
		return values;
	}
	
}
