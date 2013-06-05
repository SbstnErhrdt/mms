package model.userRights;

public class EventRights extends ContentRights {
	private int eventID;

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public String[] toValueNamesArray() {
		String[] valueNames = super.toValueNamesArray();
		valueNames[3] = "eventID";
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = super.toValuesArray();
		values[3] = "" + eventID;
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", eventID="+eventID+"]";
		return string;
	}
}
