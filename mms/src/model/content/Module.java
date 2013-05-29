package model.content;

import java.util.ArrayList;
import java.util.List;

import model.content.Event;

public class Module extends Content {

	/*	DATABASE
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
	 *  archived boolean
	 */
	
	private int duration;
	private String token, englishTitle, lp, sws, language, director_email, requirement, learningTarget, content, literature; 
	
	// CHILDREN EVENTS
	private List<Event> events = new ArrayList<Event>();
	
	// PARENT SUBJECTS
	private ArrayList<Integer> subjectIDs;
	
	// Konstruktor
	public Module(int moduleID, String name, ArrayList<Integer> subjectIDs, String token, String englishTitle, String lp, String sws, 
			String language, int duration, String director_email, String requirement, String learningTarget, String content, String literature, boolean archived) {
		this.ID = moduleID;
		this.name = name;
		this.subjectIDs = subjectIDs;
		this.token = token;
		this.englishTitle = englishTitle;
		this.lp = lp;
		this.sws = sws;
		this.language = language;
		this.duration = duration;
		this.director_email = director_email;
		this.requirement = requirement;
		this.learningTarget = learningTarget;
		this.content = content;
		this.literature = literature;
		this.archived = archived;
	}
	
	public Module(int moduleID) {
		this.ID = moduleID;
	}

	// Getter & Setter
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
		return englishTitle;
	}


	public void setEnlishTitle(String enlishTitle) {
		this.englishTitle = enlishTitle;
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
	
	// EVENTS
	public List<Event> getEventList() {
		return events;
	}
	
	public void setEventList(List<Event> events ) {
		this.events = events;
	}
		
	// ADD EVENT
	public void addEvent(Event event) {
		events.add(event);
	}
	// REMOVE EVENT
	public void removeEvent(Event event) {
		events.remove(event);
	}
	
	// TODO: ADD - REMOVE LECTURE
	
	
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
		String[] values = {""+ID, "'"+name+"'", "'"+token+"'", "'"+englishTitle+"'", "'"+lp+"'", ""+sws, "'"+language+"'",
				""+duration, "'"+director_email+"'", "'"+requirement+"'", "'"+learningTarget+"'", "'"+content+"'", "'"+literature+"'", ""+archived}; 
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"moduleID", "name", "token", "englishTitle", "lp", "sws", "language",
			"duration", "director_email", "requirement", "learningTarget", "content", "literature", "archived"}; 
		return valueNames;
	}
	
	public String toString() {
		String string = "[";
		String[] values = toValuesArray();
		String[] valueNames = toValueNamesArray();
		for(int i=0; i<values.length-1; i++) {
			string += valueNames[i]+"="+values[i]+", ";
		}
		string += valueNames[values.length-1]+"="+values[values.length-1];
		string += ", subjectIDs={";
		for(int i=0; i<subjectIDs.size()-1; i++) {
			string += subjectIDs.get(i) + ",";
		}
		string += subjectIDs.get(subjectIDs.size()-1) + "}]";
		
		return string;
	}
	
}
