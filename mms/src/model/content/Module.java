package model.content;

import java.sql.Timestamp;
import java.util.ArrayList;

import util.Utilities;

public class Module extends Content {

	private int moduleID;
	private String director_email;
	private boolean isCritical;

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
		this.subjectIDs = new ArrayList<Integer>();
		this.lecturers_emails = new ArrayList<String>();
		this.moduleFields = new ArrayList<ModuleField>();
	}

	/**
	 * constructor
	 * 
	 * @param moduleID
	 */
	public Module(int moduleID) {
		super();
		this.moduleID = moduleID;
		this.subjectIDs = new ArrayList<Integer>();
		this.lecturers_emails = new ArrayList<String>();
		this.moduleFields = new ArrayList<ModuleField>();
	}

	/**
	 * constructor
	 * 
	 * @param moduleID
	 * @param name
	 * @param director_email
	 * @param lastModified
	 * @param modifier_email
	 * 
	 * @param enabled
	 */
	public Module(int moduleID, String name, String director_email, Timestamp lastModified,
			String modifier_email, boolean isCritical, boolean enabled) {
		this.moduleID = moduleID;
		this.name = name;
		this.director_email = director_email;
		this.lastModified = lastModified;
		this.modifier_email = modifier_email;
		this.isCritical = isCritical;
		this.enabled = enabled;
		this.subjectIDs = new ArrayList<Integer>();
		this.lecturers_emails = new ArrayList<String>();
		this.moduleFields = new ArrayList<ModuleField>();
	}

	/**
	 * constructor
	 * 
	 * @param moduleID
	 * @param name
	 * @param enabled
	 * @param subjectIDs
	 * @param lecturers_emails
	 * @param moduleFields
	 */
	public Module(int moduleID, String name, boolean enabled,
			ArrayList<Integer> subjectIDs, ArrayList<String> lecturers_emails,
			ArrayList<ModuleField> moduleFields) {
		super();
		this.moduleID = moduleID;
		this.name = name;
		this.enabled = enabled;
		this.subjectIDs = subjectIDs;
		this.lecturers_emails = lecturers_emails;
		this.moduleFields = moduleFields;
		this.subjectIDs = subjectIDs;
		this.lecturers_emails = lecturers_emails;
		this.moduleFields = moduleFields;
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
	
	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}
	
	public String getDirector_email() {
		return director_email;
	}

	public void setDirector_email(String director_email) {
		this.director_email = director_email;
	}

	public ArrayList<String> getLecturers_emails() {
		return lecturers_emails;
	}

	public void setLecturers_emails(ArrayList<String> lecturers_emails) {
		this.lecturers_emails = lecturers_emails;
	}

	@Override
	public String toString() {
		return "Module [moduleID=" + moduleID + ", isCritical=" + isCritical
				+ ", subjectIDs=" + subjectIDs + ", lecturers_emails="
				+ lecturers_emails + ", moduleFields=" + moduleFields
				+ ", name=" + name + ", content=" + content
				+ ", modifier_email=" + modifier_email + ", lastModified="
				+ lastModified + ", archived=" + archived + ", enabled="
				+ enabled + ", version=" + version + "]";
	}
	
	public Object getModuleFieldValue(String fieldName) {
		for(ModuleField mf : moduleFields) {
			if(mf.getFieldName().equals(fieldName)) {
				return mf.getFieldValue();
			}
		}
		return null;
	}
}

	
