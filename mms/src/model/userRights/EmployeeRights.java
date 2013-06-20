package model.userRights;

import java.util.ArrayList;

import model.content.ModuleHandbook;

import util.Utilities;

public class EmployeeRights extends UserRights {
	private boolean canDeblockCriticalModule, canDeblockModule, isAdmin;
	private ArrayList<ModuleRights> moduleRightsList;
	private ArrayList<EventRights> eventRightsList;
	private ArrayList<StudycourseRights> studycourseRightsList;
	private ArrayList<SubjectRights> subjectRightsList;
	private ArrayList<ModuleHandbookRights> moduleHandbookRightsList;
	
	
	public EmployeeRights() {
		super(true);
		eventRightsList = new ArrayList<EventRights>();
		moduleRightsList = new ArrayList<ModuleRights>();
		subjectRightsList = new ArrayList<SubjectRights>();
		studycourseRightsList = new ArrayList<StudycourseRights>();
		moduleHandbookRightsList = new ArrayList<ModuleHandbookRights>();
	}
	
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
	
	public ArrayList<ModuleHandbookRights> getModuleHandbookRightsList() {
		return moduleHandbookRightsList;
	}



	public void setModuleHandbookRightsList(
			ArrayList<ModuleHandbookRights> moduleHandbookRightsList) {
		this.moduleHandbookRightsList = moduleHandbookRightsList;
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
		String valueNames = Utilities.arrayToString(toEmployeeValueNamesArray());
		return valueNames;
	}
	
	public String toEmployeeValues() {
		String values = Utilities.arrayToString(toEmployeeValuesArray());
		return values;
	}
	
	public String[] toEmployeeValueNamesArray() {
		String[] valueNames = {"canDeblockCriticalModule", "canDeblockModule", "isAdmin"};
		return valueNames;
	}
	
	public String[] toEmployeeValuesArray() {
		String[] values = {""+canDeblockCriticalModule, "" + canDeblockModule, "" + isAdmin};
		return values;
	}
	
	public String toString() {
		String string = "[";
		String[] values = toEmployeeValuesArray();
		String[] valueNames = toEmployeeValueNamesArray();
		for(int i=0; i<values.length-1; i++) {
			string += valueNames[i]+"="+values[i]+", ";
		}
		string += valueNames[values.length-1]+"="+values[values.length-1]+"]";
		string += "\n";
		for(EventRights er : eventRightsList) {
			string += er + "\n";
		}
		for(ModuleRights mr : moduleRightsList) {
			string += mr + "\n";
		}
		for(SubjectRights sr : subjectRightsList) {
			string += sr + "\n";
		}
		for(StudycourseRights scr : studycourseRightsList) {
			string += scr + "\n";
		}
		return string;
	}	
}
