package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.content.Event;

public class ContentDbController extends DbController {

	// EVENT ERSTELLEN
	public boolean createEvent(Event event) {

		String query = "INSERT INTO events (" + event.toValueNames() + ")";
		query += "VALUES (" + event.toValues() + ");";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// EVENT UPDATEN
	public boolean updateEvent(Event event) {

		String query = "UPDATE events SET ";
		
		String[] valueNames = event.toValueNamesArray();
		String[] values = event.toValuesArray();
		
		for(int i = 0; i < valueNames.length-1; i++) {
			query += valueNames[i] + " = '" + values[i] + "', " ;
		}
		query += valueNames[valueNames.length-1] + " = '" + values[values.length-1] +"');";		
		query += " WHERE eventID = " + event.getEventID() + ";";
		
		System.out.println(query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// EVENT HOLEN
	public Event getEvent(Event event) {

		String query = "SELECT FROM events WHERE eventID = "
				+ event.getEventID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				Event newEvent = new Event(rs.getInt(1), rs.getInt(2),
						rs.getString(3), rs.getString(4), rs.getString(5));
				return newEvent;
			} else {
				System.out.println("No Event found with this ID.");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// EVENT ENTFERNEN
	public boolean deleteEvent(Event event) {

		String query = "DELETE FROM events";
		query += "WHERE eventID = " + event.getEventID() + ";";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// TODO: MODUL ERSTELLEN

	// TODO: MODUL UPDATEN

	// TODO: MODUL ENTFERNEN

	// TODO: MODULHANDBOOK ERSTELLEN

	// TODO: MODULHANDBOOK UPDATEN

	// TODO: MODULHANDBOOK ENTFERNEN

	// TODO: STUDYCOURSE ERSTELLEN

	// TODO: STUDYCOURSE UPDATEN

	// TODO: STUDYCOURSE ENTFERNEN

	// TODO: SUBJECT ERSTELLEN

	// TODO: SUBJECT UPDATEN

	// TODO: SUBJECT ENTFERNEN
}
