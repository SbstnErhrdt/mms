package model.content;

public class Event extends Content {

	
	/* DATENBANK
	 * INT eventID
	 * INT modules_moduleID
	 * VAR name
	 * VAR sws
	 * VAR lecturer_email
	 * BOOL archived
	 */
	
	
	private String lecturer_email;
	private int sws;
	
	// Konstruktor
	public Event(int eventID, int modules_moduleID, String name, int sws, String lecturer_email, boolean archived) {
		this.ID = eventID;
		this.parentID = modules_moduleID;
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

	@Override
	public String toValues() {
		String value = arrayToString(toValuesArray());
		return value;
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {""+ID, ""+parentID, "'"+name+"'", ""+sws, ""+"'"+lecturer_email+"'", ""+archived};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"eventID", "modules_moduleID", "name", "sws", "lecturer_email, archieved"};
		return valueNames;
	}
	
	
}
