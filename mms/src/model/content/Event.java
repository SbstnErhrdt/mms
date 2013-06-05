package model.content;

import java.util.ArrayList;

public class Event extends Content {

	
	/* DATENBANK
	 * INT eventID
	 * VAR name
	 * VAR sws
	 * VAR lecturer_email
	 * BOOL archived
	 */
	
	
	private String lecturer_email;
	private int sws;
	private ArrayList<Integer> moduleIDs;
	
	// Konstruktor
	public Event(int eventID) {
		this.ID = eventID;
	}
	
	public Event(int eventID, ArrayList<Integer> moduleIDs, String name, int sws, String lecturer_email, boolean archived) {
		this.ID = eventID;
		this.moduleIDs = moduleIDs;
		this.name = name;
		this.sws = sws;
		this.lecturer_email = lecturer_email;
		this.archived = archived;
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
		String value = arrayToString(toValueNamesArray());
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
		String value = arrayToString(toValuesArray());
		return value;
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {""+ID, "'"+name+"'", ""+sws, ""+"'"+lecturer_email+"'", ""+archived};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "name", "sws", "lecturer_email", "archived"};
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
	
}
