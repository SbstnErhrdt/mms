package model.content;

import java.sql.Timestamp;
import java.util.ArrayList;

import util.Utilities;

public class Module extends Content {

	/*	DATABASE
	 * 	moduleID	int(5)	
	 * 	name	varchar(100)	
	 * 	token	varchar(60)	
	 * 	englishTitle	varchar(100)
	 * 	lp	varchar(10)	
	 * 	sws	varchar(10)	
	 * 	language	varchar(30)
	 * 	duration	int(20)
	 * 	director_email	varchar(100)
	 * 	requirement	text
	 * 	learningTarget	text
	 * 	content	text
	 * 	literature text
	 *  archived boolean
	 */
	
	private int moduleID, duration, effort_presenceTime, effort_preAndPost;
	private String token, englishTitle, lp, sws, language, director_email, requirement_formal, requirement_content,  
		rotation, performanceRecord, gradeFormation, basisFor, ilias, learningTarget, literature; 
	private boolean isCritical, periodicalRotation;
	
	// PARENT SUBJECTS
	private ArrayList<Integer> subjectIDs;
	
	// lecturers
	private ArrayList<String> lecturers_emails;

	/**
	 * constructor
	 * @param moduleID
	 * @param duration
	 * @param effort_presenceTime
	 * @param effort_preAndPost
	 * @param version
	 * @param name
	 * @param content
	 * @param modifier_email
	 * @param token
	 * @param englishTitle
	 * @param lp
	 * @param sws
	 * @param language
	 * @param director_email
	 * @param requirement_formal
	 * @param requirement_content
	 * @param rotation
	 * @param performanceRecord
	 * @param gradeFormation
	 * @param basisFor
	 * @param ilias
	 * @param learningTarget
	 * @param literature
	 * @param isCritical
	 * @param periodicalRotation
	 * @param archived
	 * @param enabled
	 * @param subjectIDs
	 * @param lecturers_emails
	 * @param lastModified
	 */
	public Module(int moduleID, int duration, int effort_presenceTime,
			int effort_preAndPost, String name, String content, 
			String modifier_email, String token, String englishTitle,
			String lp, String sws, String language, String director_email,
			String requirement_formal, String requirement_content,
			String rotation, String performanceRecord, String gradeFormation,
			String basisFor, String ilias, String learningTarget,
			String literature, boolean isCritical, boolean periodicalRotation,
			boolean archived, boolean enabled, java.sql.Timestamp lastModified, 
			ArrayList<Integer> subjectIDs, ArrayList<String> lecturers_emails) {
		super();
		this.moduleID = moduleID;
		this.duration = duration;
		this.effort_presenceTime = effort_presenceTime;
		this.effort_preAndPost = effort_preAndPost;
		this.name = name;
		this.content = content;
		this.modifier_email = modifier_email;
		this.token = token;
		this.englishTitle = englishTitle;
		this.lp = lp;
		this.sws = sws;
		this.language = language;
		this.director_email = director_email;
		this.requirement_formal = requirement_formal;
		this.requirement_content = requirement_content;
		this.rotation = rotation;
		this.performanceRecord = performanceRecord;
		this.gradeFormation = gradeFormation;
		this.basisFor = basisFor;
		this.ilias = ilias;
		this.learningTarget = learningTarget;
		this.literature = literature;
		this.isCritical = isCritical;
		this.periodicalRotation = periodicalRotation;
		this.archived = archived;
		this.enabled = enabled;
		this.lastModified = lastModified;
		this.subjectIDs = subjectIDs;
		this.lecturers_emails = lecturers_emails;
	}

	// Getter & Setter
	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public Module(int moduleID) {
		this.moduleID = moduleID;
	}

	public Module() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Integer> getSubjectIDs() {
		return subjectIDs;
	}

	public void setSubjectIDs(ArrayList<Integer> subjectIDs) {
		this.subjectIDs = subjectIDs;
	}

	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}
	

	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getLp() {
		return lp;
	}


	public void setLp(String lp) {
		this.lp = lp;
	}


	public String getSws() {
		return sws;
	}


	public void setSws(String sws) {
		this.sws = sws;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getDirector_email() {
		return director_email;
	}


	public void setDirector_email(String director_email) {
		this.director_email = director_email;
	}


	public String getLearningTarget() {
		return learningTarget;
	}


	public void setLearningTarget(String learningTarget) {
		this.learningTarget = learningTarget;
	}

	public String getLiterature() {
		return literature;
	}


	public void setLiterature(String literature) {
		this.literature = literature;
	}	


	public int getID() {
		return moduleID;
	}

	public void setID(int moduleID) {
		this.moduleID = moduleID;
	}

	public String getEnglishTitle() {
		return englishTitle;
	}

	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	public int getEffort_presenceTime() {
		return effort_presenceTime;
	}

	public void setEffort_presenceTime(int effort_presenceTime) {
		this.effort_presenceTime = effort_presenceTime;
	}

	public int getEffort_preAndPost() {
		return effort_preAndPost;
	}

	public void setEffort_preAndPost(int effort_preAndPost) {
		this.effort_preAndPost = effort_preAndPost;
	}

	public String getRequirement_formal() {
		return requirement_formal;
	}

	public void setRequirement_formal(String requirement_formal) {
		this.requirement_formal = requirement_formal;
	}

	public String getRequirement_content() {
		return requirement_content;
	}

	public void setRequirement_content(String requirement_content) {
		this.requirement_content = requirement_content;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getPerformanceRecord() {
		return performanceRecord;
	}

	public void setPerformanceRecord(String performanceRecord) {
		this.performanceRecord = performanceRecord;
	}

	public String getGradeFormation() {
		return gradeFormation;
	}

	public void setGradeFormation(String gradeFormation) {
		this.gradeFormation = gradeFormation;
	}

	public String getBasisFor() {
		return basisFor;
	}

	public void setBasisFor(String basisFor) {
		this.basisFor = basisFor;
	}

	public String getIlias() {
		return ilias;
	}

	public void setIlias(String ilias) {
		this.ilias = ilias;
	}

	public boolean isPeriodicalRotation() {
		return periodicalRotation;
	}

	public void setPeriodicalRotation(boolean periodicalRotation) {
		this.periodicalRotation = periodicalRotation;
	}

	public ArrayList<String> getLecturers() {
		return lecturers_emails;
	}

	public void setLecturers(ArrayList<String> lecturers) {
		this.lecturers_emails = lecturers;
	}
	
	
	@Override
	public String toValueNames() {
		String value = Utilities.arrayToString(toValueNamesArray());
		return value;
	}


	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"moduleID", "duration", "effort_presenceTime",
				"effort_preAndPost", "name", "content", 
				"modifier_email", "token", "englishTitle",
				"lp", "sws", "language", "director_email",
				"requirement_formal", "requirement_content",
				"rotation", "performanceRecord", "gradeFormation",
				"basisFor", "ilias", "learningTarget",
				"literature", "isCritical", "periodicalRotation",
				"archived", "enabled", "lastModified"};
		return valueNames;
	}

	@Override
	public String toString() {
		return "Module [moduleID=" + moduleID + ", duration=" + duration
				+ ", effort_presenceTime=" + effort_presenceTime
				+ ", effort_preAndPost=" + effort_preAndPost + ", token="
				+ token + ", englishTitle=" + englishTitle + ", lp=" + lp
				+ ", sws=" + sws + ", language=" + language
				+ ", director_email=" + director_email
				+ ", requirement_formal=" + requirement_formal
				+ ", requirement_content=" + requirement_content
				+ ", rotation=" + rotation + ", performanceRecord="
				+ performanceRecord + ", gradeFormation=" + gradeFormation
				+ ", basisFor=" + basisFor + ", ilias=" + ilias
				+ ", learningTarget=" + learningTarget + ", literature="
				+ literature + ", isCritical=" + isCritical
				+ ", periodicalRotation=" + periodicalRotation
				+ ", subjectIDs=" + subjectIDs + ", lecturers_emails="
				+ lecturers_emails + ", name=" + name + ", content=" + content
				+ ", modifier_email=" + modifier_email + ", lastModified="
				+ lastModified + ", archived=" + archived + ", enabled="
				+ enabled + ", version=" + version + "]";
	}
}
