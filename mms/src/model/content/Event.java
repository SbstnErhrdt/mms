package model.content;

import model.DbControllable;

public class Event extends Content {

	
	/* DATENBANK
	 * INT eventID
	 * INT modules_moduleID
	 * VAR name
	 * VAR sws
	 * VAR lecturer_email
	 */
	
	
	private String lecturer_email;
	private int sws;
	
	
	// Konstruktor
	public Event(int eventID, int modules_moduleID, String name, int sws, String lecturer_email) {
		this.ID = eventID;
		this.parentID = modules_moduleID;
		this.name = name;
		this.sws = sws;
		this.lecturer_email = lecturer_email;
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
		String[] array = toValueNamesArray();
		String valueNames = "";
		for(int i=0; i<array.length-1; i++) {
			valueNames += array[i] + ", ";
		}
		valueNames += array[array.length-1];
		return valueNames;
	}

	@Override
	public String toValues() {
		String[] array = toValuesArray();
		String values = "";
		for(int i=0; i<array.length-1; i++) {
			values += array[i] + ", ";
		}
		values += array[array.length-1];
		return values;
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {""+ID, ""+parentID, "'"+name+"'", ""+sws, ""+"'"+lecturer_email+"'", ""+archieved};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "modules_moduleID", "name", "sws", "lecturer_email, archieved"};
		return valueNames;
	}
	
	
}
