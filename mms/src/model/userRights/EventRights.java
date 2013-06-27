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
		String[] valueNames = {"eventID", "canEdit", "canDelete"};
		return valueNames;
	}
	
	public String[] toValuesArray() {
		String[] values = {""+eventID, ""+canEdit, ""+canDelete};
		return values;
	}
	
	public String toString() {
		String string = super.toString();
		string += ", eventID="+eventID+"]";
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + eventID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventRights other = (EventRights) obj;
		if (eventID != other.eventID)
			return false;
		return true;
	}
}
