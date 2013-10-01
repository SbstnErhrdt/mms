package model.content;

import java.util.ArrayList;

import util.Utilities;

public class Module extends Content {
	
	private int moduleID;
	
	// parent subjects
	private ArrayList<Integer> subjectIDs;
	
	// lecturers
	private ArrayList<String> lecturers_emails;
	
	// content fields
	private ArrayList<ModuleField> moduleFields;

	/**
	 * constructor 
	 */
	public Module() {
		super();
	}

	/** 
	 * constructor
	 * @param moduleID
	 */
	public Module(int moduleID) {
		super();
		this.moduleID = moduleID;
	}
	
	/** 
	 * constructor
	 * @param moduleID
	 * @param subjectIDs
	 * @param lecturers_emails
	 * @param moduleFields
	 */
	public Module(int moduleID, String name, ArrayList<Integer> subjectIDs,
			ArrayList<String> lecturers_emails,
			ArrayList<ModuleField> moduleFields) {
		super();
		this.moduleID = moduleID;
		this.subjectIDs = subjectIDs;
		this.lecturers_emails = lecturers_emails;
		this.moduleFields = moduleFields;
		this.name = name;
	}

	public int getID() {
		return moduleID;
	}

	public void setID(int moduleID) {
		this.moduleID = moduleID;
	}

	public ArrayList<Integer> getSubjectIDs() {
		return subjectIDs;
	}

	public void setSubjectIDs(ArrayList<Integer> subjectIDs) {
		this.subjectIDs = subjectIDs;
	}

	public ArrayList<String> getLecturers() {
		return lecturers_emails;
	}

	public void setLecturers(ArrayList<String> lecturers_emails) {
		this.lecturers_emails = lecturers_emails;
	}

	public ArrayList<ModuleField> getModuleFields() {
		return moduleFields;
	}

	public void setModuleFields(ArrayList<ModuleField> moduleFields) {
		this.moduleFields = moduleFields;
	}	
}
