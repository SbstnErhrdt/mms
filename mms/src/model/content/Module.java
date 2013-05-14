package model.content;

import model.DbControllable;

public class Module implements DbControllable{

	/*
	 * 	moduleID	int(5)	
	 * 	name	varchar(100)	
	 * 	subjects_subjectID	int(5)
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
	 */
	
	private int moduleID, subjects_subjectID, duration;
	private String name, token, enlishTitle, lp, sws, language, director_email, requirement, learningTarget, content, literature; 
	
	
	// Konstruktor
	public Module(int moduleID, String name, int subjects_subjectID, String token, String enlishTitle, String lp, String sws, 
			String language, int duration, String director_email, String requirement, String learningTarget, String content, String literature) {
		this.moduleID = moduleID;
		this.name = name;
		this.subjects_subjectID = subjects_subjectID;
		this.token = token;
		this.enlishTitle = enlishTitle;
		this.lp = lp;
		this.sws = sws;
		this.language = language;
		this.duration = duration;
		this.director_email = director_email;
		this.requirement = requirement;
		this.learningTarget = learningTarget;
		this.content = content;
		this.literature = literature;
	}
	// Getter & Setter
	
	public int getModuleID() {
		return moduleID;
	}


	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}


	public int getSubjects_subjectID() {
		return subjects_subjectID;
	}


	public void setSubjects_subjectID(int subjects_subjectID) {
		this.subjects_subjectID = subjects_subjectID;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getEnlishTitle() {
		return enlishTitle;
	}


	public void setEnlishTitle(String enlishTitle) {
		this.enlishTitle = enlishTitle;
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


	public String getRequirement() {
		return requirement;
	}


	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}


	public String getLearningTarget() {
		return learningTarget;
	}


	public void setLearningTarget(String learningTarget) {
		this.learningTarget = learningTarget;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getLiterature() {
		return literature;
	}


	public void setLiterature(String literature) {
		this.literature = literature;
	}
	
	// TODO: ADD - REMOVE EVENT
	
	// TODO: ADD - REMOVE LECTURE
	
	
	@Override
	public String[] toValues() {
		String[] value = {"moduleID,", "name,", "subjects_subjectID,", "token,", "enlishTitle,", "lp,", "sws,", "language,", "duration,", "director_email,", "requirement,", "learningTarget," ,"content,", "literature" }; 
		return value;
	}

	
	@Override
	public String[] toValueNames() {
		String[] value = {moduleID+",",name+",",subjects_subjectID+",",token+",",enlishTitle+",",lp+",",sws+",",language+",",duration+",",director_email+",",requirement+",",learningTarget+",",content+",",literature+""}; 
		return value;
	}
	
}
