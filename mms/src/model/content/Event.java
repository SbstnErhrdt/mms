package model.content;

import java.sql.Timestamp;
import java.util.ArrayList;

import util.Utilities;

public class Event extends Content {

	
	/* DATENBANK
	 * INT eventID
	 * VAR name
	 * VAR sws
	 * VAR lecturer_email
	 * BOOL archived
	 */
	
	private String lecturer_email, room, place, times, type;	

	private int eventID, sws;
	private ArrayList<Integer> moduleIDs;
	
	// Konstruktor
	/**
	 * constructor
	 * @param eventID
	 */
	public Event(int eventID) {
		this.eventID = eventID;
	}
	
	/**
	 * constructor
	 * @param eventID
	 * @param moduleIDs
	 * @param name
	 * @param sws
	 * @param lecturer_email
	 * @param archived
	 * @param content
	 * @param enabled
	 * @param room
	 * @param place
	 * @param type
	 * @param times
	 * @param modifier_email
	 * @param lastModified
	 */
	public Event(int eventID, ArrayList<Integer> moduleIDs, String name, 
			int sws, String lecturer_email, boolean archived, String content, 
			boolean enabled, String room, String place, String type, String times, 
			String modifier_email, Timestamp lastModified) {
		this.eventID = eventID;
		this.moduleIDs = moduleIDs;
		this.name = name;
		this.sws = sws;
		this.lecturer_email = lecturer_email;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
		this.room = room;
		this.place = place;
		this.type = type;	
		this.times = times;
		this.modifier_email = modifier_email;
		this.lastModified = lastModified;
	}
	
	public Event() {
		// TODO Auto-generated constructor stub
	}

	// Getter & Setter
	public String getRoom() {
		return room;
	}
	
	public void setRoom(String room) {
		this.room = room;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getSws() {
		return sws;
	}
	public void setSws(int sws) {
		this.sws = sws;
	}
	public String getLecturer_email() {
		return lecturer_email;
	}
	public void setLecturer_email(String lecturer_email) {
		this.lecturer_email = lecturer_email;
	}
	
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	@Override
	public String toValueNames() {
		String value = Utilities.arrayToString(toValueNamesArray());
		return value;
	}
	
	public ArrayList<Integer> getModuleIDs() {
		return moduleIDs;
	}

	public void setModuleIDs(ArrayList<Integer> moduleIDs) {
		this.moduleIDs = moduleIDs;
	}

	@Override
	public String toValues() {
		String value = Utilities.arrayToString(toValuesArray());
		return value;
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {""+eventID, "'"+name+"'", ""+sws, ""+"'"+lecturer_email+"'", 
				""+archived, "'"+content+"'", ""+enabled, "'"+room+"'", "'"+place+"'", 
				"'"+type+"'", "'"+times+"'"};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "name", "sws", "lecturer_email", "archived", 
				"content", "enabled", "room", "place", "type", "times", "modifier_email", "lastModified"};
		return valueNames;
	}

	public int getID() {
		return eventID;
	}

	public void setID(int eventID) {
		this.eventID = eventID;
	}

	@Override
	public String toString() {
		return "Event [lecturer_email=" + lecturer_email + ", room=" + room
				+ ", place=" + place + ", times=" + times + ", type=" + type
				+ ", eventID=" + eventID + ", sws=" + sws + ", moduleIDs="
				+ moduleIDs + ", name=" + name + ", content=" + content
				+ ", modifier_email=" + modifier_email + ", lastModified="
				+ lastModified + ", archived=" + archived + ", enabled="
				+ enabled + ", version=" + version + "]";
	}
	
}
