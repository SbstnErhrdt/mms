package model.content;

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
	
	
	private String lecturer_email;
	private int eventID, sws;
	private ArrayList<Integer> moduleIDs;
	
	// Konstruktor
	public Event(int eventID) {
		this.eventID = eventID;
	}
	
	public Event(int eventID, ArrayList<Integer> moduleIDs, String name, 
			int sws, String lecturer_email, boolean archived, String content, 
			boolean enabled) {
		this.eventID = eventID;
		this.moduleIDs = moduleIDs;
		this.name = name;
		this.sws = sws;
		this.lecturer_email = lecturer_email;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
	}
	
	// Getter & Setter
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
				""+archived, "'"+content+"'", ""+enabled};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "name", "sws", "lecturer_email", "archived", "content", "enabled"};
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
		string += ", moduleIDs={";
		for(int i=0; i<moduleIDs.size()-1; i++) {
			string += moduleIDs.get(i) + ",";
		}
		string += moduleIDs.get(moduleIDs.size()-1) + "}]";
		
		return string;
	}

	public int getID() {
		return eventID;
	}

	public void setID(int eventID) {
		this.eventID = eventID;
	}
	
}
