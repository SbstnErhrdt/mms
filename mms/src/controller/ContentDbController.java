package controller;

import java.sql.PreparedStatement;
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

		// GET VALUENAMES & VALUES
		String values = event.toValues();
		String valueNames = event.toValueNames();

		// QUERY
		String query = "INSERT INTO events (" + valueNames + ") VALUES ("
				+ values + ");";
		System.out.println("db:createEvent " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// CREATE ENTRY IN TABLE EVENTS_MODULES
		query = "INSERT INTO events_modules(eventID, moduleID) VALUES ("+event.getID()+", ?)";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ArrayList<Integer> moduleIDs = event.getModuleIDs();
			for(int moduleID : moduleIDs) {
				ps.setInt(1, moduleID);
				ps.executeUpdate();
				db.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	// EVENT UPDATEN
	public boolean updateEvent(Event event) {

		// GET VALUENAMES & VALUES
		String[] valueNames = event.toValueNamesArray();
		String[] values = event.toValuesArray();

		// QUERY
		String query = "UPDATE events SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + "=" + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + "="
				+ values[values.length - 1];
		query += " WHERE eventID = " + event.getID() + ";";

		System.out.println("db:updateEvent " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// UPDATE EVENTS_MODULES: delete and recreate
		query = "DELETE FROM events_modules WHERE eventID="+event.getID();
		
		System.out.println(query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		query = "INSERT INTO events_modules(eventID, moduleID) VALUES ("+event.getID()+", ?)";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ArrayList<Integer> moduleIDs = event.getModuleIDs();
			for(int moduleID : moduleIDs) {
				ps.setInt(1, moduleID);
				ps.executeUpdate();
				db.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// EVENT HOLEN
	public Event getEvent(int eventID) {
		Event newEvent = new Event(eventID);
		
		String query = "SELECT "+newEvent.toValueNames()+" FROM events WHERE eventID = " + eventID;
		System.out.println("db:getEvent " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newEvent = new Event(rs.getInt(1), new ArrayList<Integer>(),
						rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getBoolean(5));
				rs.close();

			} else {
				System.out.println("No Event found with this ID.");
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		// moduleIDs
		query = "SELECT moduleID FROM events_modules WHERE eventID="+eventID;
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
			while(rs.next()) {
				moduleIDs.add(rs.getInt(1));
			}
			newEvent.setModuleIDs(moduleIDs);
			return newEvent;
		} catch(SQLException e) {
			e.printStackTrace();
			return newEvent;
		}
	}

	// EVENT ENTFERNEN
	public boolean deleteEvent(Event event) {

		String query = "DELETE FROM events ";
		query += "WHERE eventID = " + event.getID() + ";";
		System.out.println("db:deleteEvent " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// ####################################################
	// MODULE
	// ####################################################

	// MODULE ERSTELLEN
	public boolean createModule(Module module) {

		// GET VALUENAMES & VALUES
		String values = module.toValues();
		String valueNames = module.toValueNames();

		// QUERY
		String query = "INSERT INTO modules (" + valueNames + ") VALUES ("
				+ values + ")";
		System.out.println("db:createModule " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// CREATE ENTRY IN TABLE modules_subjects
		query = "INSERT INTO modules_subjects(moduleID, subjectID) VALUES ("+module.getID()+", ?)";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ArrayList<Integer> subjectIDs = module.getSubjectIDs();
			for(int subjectID : subjectIDs) {
				ps.setInt(1, subjectID);
				ps.executeUpdate();
				db.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// MODULE UPDATEN
	public boolean updateModule(Module module) {

		// GET VALUENAMES & VALUES
		String[] valueNames = module.toValueNamesArray();
		String[] values = module.toValuesArray();

		// QUERY
		String query = "UPDATE modules SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + "="
				+ values[values.length - 1];
		query += " WHERE moduleID = " + module.getID() + ";";

		System.out.println("db:updateModule " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// UPDATE modules_subjects: delete and recreate
		query = "DELETE FROM modules_subjects WHERE moduleID="+module.getID();
		
		System.out.println(query);
		
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		query = "INSERT INTO modules_subjects(moduleID, subjectID) VALUES ("+module.getID()+", ?)";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
			
			ArrayList<Integer> subjectIDs = module.getSubjectIDs();
			for(int subjectID : subjectIDs) {
				ps.setInt(1, subjectID);
				ps.executeUpdate();
				db.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// MODULE HOLEN
	public Module getModule(int moduleID) {
		Module newModule = new Module(moduleID);
		String query = "SELECT "+newModule.toValueNames()+" FROM modules WHERE moduleID = " + moduleID;
		System.out.println("db:getModule " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newModule = new Module(rs.getInt(1), rs.getString(2),
						new ArrayList<Integer>(), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13),
						rs.getBoolean(14));
				rs.close();
			} else {
				System.out.println("No Event found with this ID.");
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		// subjectIDs
		query = "SELECT subjectID FROM modules_subjects WHERE moduleID="+moduleID;
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
			while(rs.next()) {
				subjectIDs.add(rs.getInt(1));
			}
			newModule.setSubjectIDs(subjectIDs);
			return newModule;
		} catch(SQLException e) {
			e.printStackTrace();
			return newModule;
		}	
	}

	// MODULE ENTFERNEN
	public boolean deleteModule(Module module) {

		String query = "DELETE FROM modules ";
		query += "WHERE eventID = " + module.getID() + ";";
		System.out.println("db:deleteModule " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// MODUL EVENTS LISTE
	// Holt die Events eines Modules und gibt diese in einer ArrayList aus
	public boolean getModuleEvents(Module module) {

		List<Event> events = new ArrayList<Event>();

		String query = "SELECT * FROM events WEHRE modules_moduleID = "
				+ module.getID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				Event event = new Event(rs.getInt(1), new ArrayList<Integer>(),
						rs.getString(3), rs.getInt(4), rs.getString(5),
						rs.getBoolean(6));
				events.add(event);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		module.setEventList(events);

		return true;
	}

	// ####################################################
	// SUBJECT
	// ####################################################

	// SUBJECT ERSTELLEN
	public boolean createSubject(Subject subject) {

		// GET VALUENAMES & VALUES
		String values = subject.toValues();
		String valueNames = subject.toValueNames();

		// QUERY
		String query = "INSERT INTO subjects (" + valueNames + ") VALUES ("
				+ values + ");";
		System.out.println("db:createSubject " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// SUBJECT UPDATEN
	public boolean updateSubject(Subject subject) {

		// GET VALUENAMES & VALUES
		String[] valueNames = subject.toValueNamesArray();
		String[] values = subject.toValuesArray();

		// QUERY
		String query = "UPDATE subjects SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + " = '"
				+ values[values.length - 1] + "') ";
		query += "WHERE subjectID = " + subject.getID() + ";";

		System.out.println("db:updateSubject " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// SUBJECT HOLEN
	public Subject getSubject(Subject subject) {

		String query = "SELECT FROM subjects WHERE moduleID = "
				+ subject.getID() + ";";
		System.out.println("db:getSubject " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				Subject newSubject = new Subject(rs.getInt(1), rs.getInt(2),
						rs.getString(3), rs.getBoolean(4));
				rs.close();
				return newSubject;

			} else {
				System.out.println("No Subject found with this ID.");
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// SUBJECT ENTFERNEN
	public boolean deleteSubject(Subject subject) {

		String query = "DELETE FROM subjects ";
		query += "WHERE subjectID = " + subject.getID() + ";";
		System.out.println("db:deleteSubject " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// SUBJECT MODULE LISTE
	// Holt die Modules eines Subjects und gibt diese in einer ArrayList aus
	public boolean getSubjectModules(Subject subject) {

		List<Module> modules = new ArrayList<Module>();

		String query = "SELECT * FROM modules WEHRE subjects_subjectID = "
				+ subject.getID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				Module newModule = new Module(rs.getInt(1), rs.getString(2),
						new ArrayList<Integer>(), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getInt(9), rs.getString(10), rs.getString(11),
						rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getBoolean(15));
				modules.add(newModule);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		subject.setModules(modules);

		return true;
	}

	// ####################################################
	// MODULE HANDBOOKS
	// ####################################################

	// MODULE HANDBOOK ERSTELLEN
	public boolean createModuleHandbook(ModuleHandbook moduleHandbook) {

		// GET VALUENAMES & VALUES
		String values = moduleHandbook.toValues();
		String valueNames = moduleHandbook.toValueNames();

		// QUERY
		String query = "INSERT INTO module_handbooks (" + valueNames
				+ ") VALUES (" + values + ");";
		System.out.println("db:createModuleHandbook " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// MODULE HANDBOOK UPDATEN
	public boolean updateModuleHandbook(ModuleHandbook moduleHandbook) {

		// GET VALUENAMES & VALUES
		String[] valueNames = moduleHandbook.toValueNamesArray();
		String[] values = moduleHandbook.toValuesArray();

		// QUERY
		String query = "UPDATE module_handbooks SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + " = '"
				+ values[values.length - 1] + "') ";
		query += "WHERE moduleHandbookID = " + moduleHandbook.getID() + ";";

		System.out.println("db:updateModuleHandbook " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// MODULE HANDBOOK HOLEN
	public ModuleHandbook getModuleHandbook(ModuleHandbook moduleHandbook) {

		String query = "SELECT FROM module_handbooks WHERE moduleID = "
				+ moduleHandbook.getID() + ";";
		System.out.println("db:getModuleHandbook " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				ModuleHandbook newModuleHandbook = new ModuleHandbook(
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getString(4), rs.getBoolean(5));
				rs.close();
				return newModuleHandbook;

			} else {
				System.out.println("No ModuleHandbook found with this ID.");
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// MODULE HANDBOOK ENTFERNEN
	public boolean deleteModuleHandbook(ModuleHandbook moduleHandbook) {

		String query = "DELETE FROM module_handbooks ";
		query += "WHERE subjectID = " + moduleHandbook.getID() + ";";
		System.out.println("db:deleteModuleHandbook " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// MODULE HANDBOOK SUBJECT LISTE
	// Holt die Modules eines Subjects und gibt diese in einer ArrayList aus
	public boolean getModuleHandbookSubjects(ModuleHandbook moduleHandbook) {

		List<Subject> subjects = new ArrayList<Subject>();

		String query = "SELECT * FROM subjects WEHRE module_handbooks_moduleHandbookID = "
				+ moduleHandbook.getID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				Subject newSubject = new Subject(rs.getInt(1), rs.getInt(2),
						rs.getString(3), rs.getBoolean(4));

				subjects.add(newSubject);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		moduleHandbook.setSubjectList(subjects);

		return true;
	}

	// ####################################################
	// STUDYCOURSES
	// ####################################################

	// STUDYCOURSE ERSTELLEN
	public boolean createStudycourse(Studycourse studycourse) {

		// GET VALUENAMES & VALUES
		String values = studycourse.toValues();
		String valueNames = studycourse.toValueNames();

		// QUERY
		String query = "INSERT INTO studycourses (" + valueNames
				+ ") VALUES (" + values + ");";
		System.out.println("db:createStudycourse " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// STUDYCOURSE UPDATEN
	public boolean updateStudycourse(Studycourse studycourse) {

		// GET VALUENAMES & VALUES
		String[] valueNames = studycourse.toValueNamesArray();
		String[] values = studycourse.toValuesArray();

		// QUERY
		String query = "UPDATE studycourses SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + " = '"
				+ values[values.length - 1] + "') ";
		query += "WHERE studycourseID = " + studycourse.getID() + ";";

		System.out.println("db:updateStudycourse " + query);

		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// STUDYCOURSE HOLEN
	public Studycourse getStudycourse(Studycourse studycourse) {

		String query = "SELECT FROM studycourses WHERE moduleID = "
				+ studycourse.getID() + ";";
		System.out.println("db:getStudycourse " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				Studycourse newStudycourse = new Studycourse(
						rs.getInt(1), rs.getString(2), rs.getBoolean(3));
				rs.close();
				return newStudycourse;

			} else {
				System.out.println("No Studycourse found with this ID.");
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// STUDYCOURSE ENTFERNEN
	public boolean deleteStudycourse(Studycourse studycourse) {

		String query = "DELETE FROM studycourses ";
		query += "WHERE subjectID = " + studycourse.getID() + ";";
		System.out.println("db:deleteStudycourse " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// STUDYCOURSE SUBJECT LISTE
	// Holt die ModuleHandbooks eines Studycourses und gibt diese in einer ArrayList aus
	public boolean getStudycourseSubjects(Studycourse studycourse) {

		List<ModuleHandbook> moduleHandbooks = new ArrayList<ModuleHandbook>();

		String query = "SELECT * FROM module_handbooks WEHRE studycourses_studycourseID = "
				+ studycourse.getID() + ";";

		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				ModuleHandbook newModuleHandbook = new ModuleHandbook(
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getString(4), rs.getBoolean(5));

				moduleHandbooks.add(newModuleHandbook);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		studycourse.setSubjectList(moduleHandbooks);

		return true;
	}

}
