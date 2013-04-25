package model.userRights;

public class EventRights extends ContentRights {
	private int eventID;

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public String[] toValueNames() {
		String[] valueNames = super.toValueNames();
		valueNames[4] = "eventID";
		return valueNames;
	}
	
	public String[] toValueStrings() {
		String[] values = super.toValues();
		values[4] = "" + eventID;
		return values;
	}
}
