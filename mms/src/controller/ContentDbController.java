package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// IMPORT MODLES
import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;

public class ContentDbController extends DbController {

	// EVENT ERSTELLEN
	public boolean createEvent(Event event) {

		String[]values = event.toValues();
		String[]valueNames = event.toValueNames();
		int length = values.length;
		
		// COLUM NAMES
		String query = "INSERT INTO modules (";
		
		for(int i = 0; i < length-1; i++) {
			
			query += valueNames[i]+", ";
			
		}
		query += valueNames[length-1]+")";

		query += "VALUES (";
		
		for(int i = 0; i < length-1; i++) {
			
			query += values[i]+", ";
			
		}
		query += values[length-1]+");";
		

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

		String query = "DELETE FROM events ";
		query += "WHERE eventID = " + event.getEventID() + ";";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// MODUL ERSTELLEN
	public boolean createModule(Module module) {
		
		String[]values = module.toValues();
		String[]valueNames = module.toValueNames();
		int length = values.length;
		
		// COLUM NAMES
		String query = "INSERT INTO modules (";
		
		for(int i = 0; i < length-1; i++) {
			
			query += valueNames[i]+", ";
			
		}
		query += valueNames[length-1]+")";

		query += "VALUES (";
		
		for(int i = 0; i < length-1; i++) {
			
			query += values[i]+", ";
			
		}
		query += values[length-1]+");";
		

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}

	// MODUL UPDATEN
	public boolean updateModule(Module module) {

		String query = "UPDATE modules ";
		
		String[] values = module.toValues();
		String[] valueNames = module.toValueNames();
		int length = values.length;
		
		for(int i = 0; i < length-1; i++) {
			
			query += values[i] + " = " + valueNames[i] + ",";
			
		}
		
		query += values[length-1] + " = " + valueNames[length-1];
		
		query += "WEHRE "+module.getModuleID()+";";
		

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	

	// MODUL ENTFERNEN
	public boolean deleteModule(Module module) {

		String query = "DELETE FROM modules ";
		query += "WHERE eventID = " + module.getModuleID() + ";";

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// MODUL HOLEN
	public Module getModule(Module module) {

		String query = "SELECT FROM modules WHERE moduleID = " + module.getModuleID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				Module newModule = new Module(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14));
				return newModule;
			} else {
				System.out.println("No Event found with this ID.");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// MODUL EVENTS LISTE
	// Holt die Events eines Modules und gibt diese in einer ArrayList aus 
	public boolean getModuleEvents(Module module) {
		
		List<Event> events = new ArrayList<Event>();
		
		String query = "SELECT * FROM events WEHRE modules_moduleID = " + module.getModuleID();
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				Event event = new Event(rs.getInt(1), rs.getInt(2),	rs.getString(3), rs.getString(4), rs.getString(5));
				events.add(event);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		module.setEventList(events);
		
		return true;
	}
	
	
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
