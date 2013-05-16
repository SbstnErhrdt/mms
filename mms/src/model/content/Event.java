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
	private int eventID, modules_moduleID, sws;
	
	// Konstruktor
	public Event(int eventID, int modules_moduleID, String name, int sws, String lecturer_email) {
		this.eventID = eventID;
		this.modules_moduleID = modules_moduleID;
		this.name = name;
		this.sws = sws;
		this.lecturer_email = lecturer_email;
	}
	
	// Getter & Setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public int getModules_moduleID() {
		return modules_moduleID;
	}
	public void setModules_moduleID(int modules_moduleID) {
		this.modules_moduleID = modules_moduleID;
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
		String[] values = {""+eventID, ""+modules_moduleID, "'"+name+"'", ""+sws, ""+"'"+lecturer_email+"'", ""+archieved};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "modules_moduleID", "name", "sws", "lecturer_email, archieved"};
		return valueNames;
	}
	
	
}
