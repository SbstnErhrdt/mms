package controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import util.Utilities;

import model.content.Deadline;
// IMPORT MODLES
import model.content.Event;
import model.content.Module;
import model.content.ModuleHandbook;
import model.content.Studycourse;
import model.content.Subject;

public class ContentDbController extends DbController {
	
	/**
	 * constructor
	 */
	public ContentDbController() {
		super();
	}
	
	// EVENT ERSTELLEN
	/**
	 * @param event
	 * @return true, if event was created successfully 
	 */
	public boolean createEvent(Event event) {

		// GET VALUENAMES & VALUES
		String[] valuesArray = event.toValuesArray();
		String[] valueNamesArray = event.toValueNamesArray();
		
		String[] newValuesArray = Arrays.copyOfRange(valuesArray, 1, valuesArray.length);
		String[] newValuesNamesArray = Arrays.copyOfRange(valueNamesArray, 1, valueNamesArray.length);
		
		String values = Utilities.arrayToString(newValuesArray);
		String valueNames =  Utilities.arrayToString(newValuesNamesArray);
		
		// QUERY
		String query = "INSERT INTO events (" +valueNames+ ") VALUES ("+values+");";
		
		System.out.println("db:createEvent " + query);
		
		try {
			Statement stmt = db.createStatement();	
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				event.setID(rs.getInt(1));
			    System.out.println("Generated eventID: " + event.getID());	    
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
				
		// CREATE ENTRY IN TABLE EVENTS_MODULES
		query = "INSERT INTO events_modules(eventID, moduleID) VALUES ("+event.getID()+", ?);";
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
	/**
	 * @param event
	 * @return true, if event was updated successfully
	 */
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
	/**
	 * @param eventID
	 * @return event with the specified eventID
	 */
	public Event getEvent(int eventID) {
		Event newEvent = new Event(eventID);
		
		String query = "SELECT "+newEvent.toValueNames()+" FROM events WHERE eventID="+eventID+";";
		System.out.println("db:getEvent " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newEvent = new Event(rs.getInt(1), new ArrayList<Integer>(),
						rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getBoolean(5), rs.getString(6), rs.getBoolean(7), rs.getString(8), 
						rs.getString(9), rs.getString(10), rs.getString(11));
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
			rs.close();
			return newEvent;
		} catch(SQLException e) {
			e.printStackTrace();
			return newEvent;
		}
	}

	// EVENT ENTFERNEN
	/**
	 * @param event
	 * @return true, if event was deleted successfully
	 */
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
	
	/**
	 * @param getOnlyEnabled
	 * @return list of events, if getOnlyEnabled is true, only events that have enabled=true are selected
	 */
	public ArrayList<Event> getEvents(boolean getOnlyEnabled) {
		ArrayList<Event> events = new ArrayList<Event>();

		Event event = new Event(0);
		
		String query = "SELECT "+event.toValueNames()+" FROM events";
		if(getOnlyEnabled) query += " WHERE enabled=true";
		query += ";"; 
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				int eventID = rs.getInt(1);
				event = null;
				event = new Event(eventID, new ArrayList<Integer>(),
						rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getBoolean(5), rs.getString(6), rs.getBoolean(7), rs.getString(8), 
						rs.getString(9), rs.getString(10), rs.getString(11));
				
				// moduleIDs
				ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
				query = "SELECT moduleID FROM events_modules WHERE eventID="+eventID+";";
				System.out.println(query);
				
				try {
					ResultSet rs1 = db.createStatement().executeQuery(query);
					while(rs1.next()) {
						moduleIDs.add(rs1.getInt(1)); 	// moduleID
					}
					event.setModuleIDs(moduleIDs);
				} catch(SQLException e) {
					e.printStackTrace();
				}
				events.add(event);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;
	}


	// ####################################################
	// MODULE
	// ####################################################

	// MODULE ERSTELLEN
	/**
	 * @param module
	 * @return true, if module was created successfully
	 */
	public boolean createModule(Module module) {

		// GET VALUENAMES & VALUES
		String[] valuesArray = module.toValuesArray();
		String[] valueNamesArray = module.toValueNamesArray();
		
		String[] newValuesArray = Arrays.copyOfRange(valuesArray, 1, valuesArray.length);
		String[] newValuesNamesArray = Arrays.copyOfRange(valueNamesArray, 1, valueNamesArray.length);
		
		String values = Utilities.arrayToString(newValuesArray);
		String valueNames =  Utilities.arrayToString(newValuesNamesArray);
		
		// QUERY
		String query = "INSERT INTO modules (" +valueNames+ ") VALUES ("+values+");";
		
		System.out.println("db:createModule " + query);
		
		try {
			Statement stmt = db.createStatement();	
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				module.setID(rs.getInt(1));
			    System.out.println("Generated eventID: " + module.getID());	    
			}
			stmt.close();
			rs.close();
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
	/**
	 * @param module
	 * @return true, if module was updated successfully
	 */
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
	/**
	 * @param moduleID
	 * @return module with specified moduleID
	 */
	public Module getModule(int moduleID) {
		Module newModule = new Module(moduleID);
		String query = "SELECT "+newModule.toValueNames()+" FROM modules WHERE moduleID="+moduleID+";";
		System.out.println("db:getModule " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newModule = new Module(rs.getInt(1), rs.getString(2),
						new ArrayList<Integer>(), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13),
						rs.getBoolean(14), rs.getBoolean(15), rs.getBoolean(16));
			} else {
				System.out.println("No Event found with this ID.");
				rs.close();
				return null;
			}
			rs.close();

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
			rs.close();
			return newModule;
		} catch(SQLException e) {
			e.printStackTrace();
			return newModule;
		}	
	}

	// MODULE ENTFERNEN
	/**
	 * @param module
	 * @return true, if module was deleted successfully
	 */
	public boolean deleteModule(Module module) {

		String query = "DELETE FROM modules ";
		query += "WHERE moduleID = " + module.getID() + ";";
		System.out.println("db:deleteModule " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @param getOnlyEnabled
	 * @return list of modules, if getOnlyEnabled is true, only modules that have enabled=true are selected
	 */
	public ArrayList<Module> getModules(boolean getOnlyEnabled) {
		ArrayList<Module> modules = new ArrayList<Module>();
		Module module = new Module(0);
		
		String query = "SELECT "+module.toValueNames()+" FROM modules";
		if(getOnlyEnabled) query += " WHERE enabled=true";
		query += ";";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				module = null;
				int moduleID = rs.getInt(1);
				module = new Module(moduleID, rs.getString(2),
						new ArrayList<Integer>(), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13),
						rs.getBoolean(14), rs.getBoolean(15), rs.getBoolean(16));
				
				// subjectIDs
				ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
				query = "SELECT subjectID FROM modules_subjects WHERE moduleID="+moduleID+";";
				System.out.println(query);
				
				try {
					ResultSet rs1 = db.createStatement().executeQuery(query);
					while(rs1.next()) {
						subjectIDs.add(rs1.getInt(1)); 	// subjectID
					}
					rs1.close();
					module.setSubjectIDs(subjectIDs);
				} catch(SQLException e) {
					e.printStackTrace();
				}		
				
				modules.add(module);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return modules;
	}

	// MODUL EVENTS LISTE
	// Holt die Events eines Modules und gibt diese in einer ArrayList aus
	/**
	 * @param moduleID
	 * @param getOnlyEnabled
	 * @return list of events, that belong to the module with the passed moduleID, if getOnlyEnabled = true, only enabled events are selected
	 * 
	 */
	public ArrayList<Event> getModuleEvents(int moduleID, boolean getOnlyEnabled) {

		ArrayList<Event> events = new ArrayList<Event>();

		Event event = new Event(0);
		
		String query = "SELECT "+event.toValueNames()+" FROM events " +
				"WHERE eventID IN (SELECT eventID FROM events_modules " +
				"WHERE moduleID="+ moduleID+")";
		if(getOnlyEnabled) query += " AND enabled=true";	
		query+=";";
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				int eventID = rs.getInt(1);
				event = null;
				event = new Event(eventID, new ArrayList<Integer>(),
						rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getBoolean(5), rs.getString(6), rs.getBoolean(7), rs.getString(8), 
						rs.getString(9), rs.getString(10), rs.getString(11));
				
				// moduleIDs
				ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
				query = "SELECT moduleID FROM events_modules WHERE eventID="+eventID+";";
				System.out.println(query);
				
				try {
					ResultSet rs1 = db.createStatement().executeQuery(query);
					while(rs1.next()) {
						moduleIDs.add(rs1.getInt(1)); 	// moduleID
					}
					event.setModuleIDs(moduleIDs);
				} catch(SQLException e) {
					e.printStackTrace();
				}
				events.add(event);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;
	}
	
	// ####################################################
	// SUBJECT
	// ####################################################

	// SUBJECT ERSTELLEN
	/**
	 * @param subject
	 * @return true, if the subject was created successfully
	 */
	public boolean createSubject(Subject subject) {

		// GET VALUENAMES & VALUES
		String[] valuesArray = subject.toValuesArray();
		String[] valueNamesArray = subject.toValueNamesArray();
		
		String[] newValuesArray = Arrays.copyOfRange(valuesArray, 1, valuesArray.length);
		String[] newValuesNamesArray = Arrays.copyOfRange(valueNamesArray, 1, valueNamesArray.length);
		
		String values = Utilities.arrayToString(newValuesArray);
		String valueNames =  Utilities.arrayToString(newValuesNamesArray);

		// QUERY
		String query = "INSERT INTO subjects (" + valueNames + ") VALUES ("
				+ values + ");";
		System.out.println("db:createSubject " + query);
		
		try {
			Statement stmt = db.createStatement();	
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				subject.setID(rs.getInt(1));
			    System.out.println("Generated eventID: " + subject.getID());	    
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}

	// SUBJECT UPDATEN
	/**
	 * @param subject
	 * @return true, if the subject was updated successfully
	 */
	public boolean updateSubject(Subject subject) {

		// GET VALUENAMES & VALUES
		String[] valueNames = subject.toValueNamesArray();
		String[] values = subject.toValuesArray();

		// QUERY
		String query = "UPDATE subjects SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + "="
				+ values[values.length - 1];
		query += " WHERE subjectID = " + subject.getID() + ";";

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
	/**
	 * @param subjectID
	 * @return the subject with the passed subjectID
	 */
	public Subject getSubject(int subjectID) {
		Subject newSubject = new Subject(subjectID);
		
		String query = "SELECT "+newSubject.toValueNames()+" FROM subjects WHERE subjectID="+subjectID+";";
		System.out.println("db:getSubject " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newSubject = new Subject(rs.getInt(1), rs.getInt(2), rs.getInt(3),
						rs.getString(4), rs.getBoolean(5), rs.getString(6), rs.getBoolean(7));
				rs.close();
				return newSubject;

			} else {
				System.out.println("No Subject found with this ID.");
				rs.close();
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// SUBJECT ENTFERNEN
	/**
	 * @param subject
	 * @return true, if the subject was deleted successfully
	 */
	public boolean deleteSubject(Subject subject) {

		String query = "DELETE FROM subjects ";
		query += "WHERE subjectID=" + subject.getID() + ";";
		System.out.println("db:deleteSubject " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	/**
	 * @param getOnlyEnabled
	 * @return list of subjects, if getOnlyEnabled = true, only enabled subjects are selected
	 */
	public ArrayList<Subject> getSubjects(boolean getOnlyEnabled) {
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		Subject subject = new Subject(0);
		
		String query = "SELECT "+subject.toValueNames()+" FROM subjects";
		if(getOnlyEnabled) query += " WHERE enabled=true";
		query += ";";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				subject = null;
				subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getInt(3), 
						rs.getString(4), rs.getBoolean(5), rs.getString(6), rs.getBoolean(7));

				subjects.add(subject);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}

	// SUBJECT MODULE LISTE
	// Holt die Modules eines Subjects und gibt diese in einer ArrayList aus
	/**
	 * @param subjectID
	 * @param getOnlyEnabled
	 * @return list of modules that belong to the subject with the passed subjectID, if getOnlyEnabled = true, only enabled modules are selected
	 */
	public ArrayList<Module> getSubjectModules(int subjectID, boolean getOnlyEnabled) {

		ArrayList<Module> modules = new ArrayList<Module>();
		Module module = new Module(0);
		
		String query = "SELECT "+module.toValueNames()+" FROM modules " +
				"WHERE moduleID IN (SELECT moduleID FROM modules_subjects " +
				"WHERE subjectID="+ subjectID+")";
		if(getOnlyEnabled) query += " AND enabled=true";
		query += ";";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				module = null;
				int moduleID = rs.getInt(1);
				module = new Module(moduleID, rs.getString(2),
						new ArrayList<Integer>(), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13),
						rs.getBoolean(14), rs.getBoolean(15), rs.getBoolean(16));
				
				// subjectIDs
				ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
				query = "SELECT subjectID FROM modules_subjects WHERE moduleID="+moduleID+";";
				System.out.println(query);
				
				try {
					ResultSet rs1 = db.createStatement().executeQuery(query);
					while(rs1.next()) {
						subjectIDs.add(rs1.getInt(1)); 	// subjectID
					}
					rs1.close();
					module.setSubjectIDs(subjectIDs);
				} catch(SQLException e) {
					e.printStackTrace();
				}		
				
				modules.add(module);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return modules;
	}

	// ####################################################
	// MODULE HANDBOOKS
	// ####################################################

	// MODULE HANDBOOK ERSTELLEN
	/**
	 * @param moduleHandbook
	 * @return true, if the moduleHandbook was created successfully
	 */
	public boolean createModuleHandbook(ModuleHandbook moduleHandbook) {

		// GET VALUENAMES & VALUES
		String[] valuesArray = moduleHandbook.toValuesArray();
		String[] valueNamesArray = moduleHandbook.toValueNamesArray();
		
		String[] newValuesArray = Arrays.copyOfRange(valuesArray, 1, valuesArray.length);
		String[] newValuesNamesArray = Arrays.copyOfRange(valueNamesArray, 1, valueNamesArray.length);
		
		String values = Utilities.arrayToString(newValuesArray);
		String valueNames =  Utilities.arrayToString(newValuesNamesArray);

		// QUERY
		String query = "INSERT INTO module_handbooks (" + valueNames
				+ ") VALUES (" + values + ");";
		System.out.println("db:createModuleHandbook " + query);
	
		try {
			Statement stmt = db.createStatement();	
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				moduleHandbook.setID(rs.getInt(1));
			    System.out.println("Generated eventID: " + moduleHandbook.getID());	    
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		

		return true;
	}

	// MODULE HANDBOOK UPDATEN
	/**
	 * @param moduleHandbook
	 * @return true, if the moduleHandbook was updated successfully
	 */
	public boolean updateModuleHandbook(ModuleHandbook moduleHandbook) {

		// GET VALUENAMES & VALUES
		String[] valueNames = moduleHandbook.toValueNamesArray();
		String[] values = moduleHandbook.toValuesArray();

		// QUERY
		String query = "UPDATE module_handbooks SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + "=" + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + "="
				+ values[values.length - 1];
		query += " WHERE moduleHandbookID=" + moduleHandbook.getID() + ";";

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
	/**
	 * @param moduleHandbookID
	 * @return the modulehandbook with the passed moduleHandbookID
	 */
	public ModuleHandbook getModuleHandbook(int moduleHandbookID) {
		ModuleHandbook newModuleHandbook = new ModuleHandbook(moduleHandbookID);
		
		String query = "SELECT "+newModuleHandbook.toValueNames()+
				" FROM module_handbooks WHERE moduleHandbookID="
				+ moduleHandbookID + ";";
		System.out.println("db:getModuleHandbook " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newModuleHandbook = new ModuleHandbook(
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), rs.getString(7), rs.getBoolean(8));
				rs.close();
				return newModuleHandbook;

			} else {
				System.out.println("No ModuleHandbook found with this ID.");
				rs.close();
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// MODULE HANDBOOK ENTFERNEN
	/**
	 * @param moduleHandbook
	 * @return true, if the modulehandbook was deleted successfully
	 */
	public boolean deleteModuleHandbook(ModuleHandbook moduleHandbook) {

		String query = "DELETE FROM module_handbooks ";
		query += "WHERE moduleHandbookID = " + moduleHandbook.getID() + ";";
		System.out.println("db:deleteModuleHandbook " + query);
		try {
			db.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * @param studycourseID
	 * @param getOnlyEnabled
	 * @return list of modulehandbooks, that belong to the studycourse with the passed studycourseID, if getOnlyEnabled = true, only enabled modulehandbooks are selected
	 */
	public ArrayList<ModuleHandbook> readStudycourseModuleHandbooks(int studycourseID, boolean getOnlyEnabled) {

		ArrayList<ModuleHandbook> moduleHandbooks = new ArrayList<ModuleHandbook>();
		
		ModuleHandbook moduleHandbook = new ModuleHandbook(0);
		
		String query = "SELECT "+moduleHandbook.toValueNames()+" FROM module_handbooks " +
				"WHERE studycourses_studycourseID="+studycourseID;
		if(getOnlyEnabled) query += " AND enabled=true";
		query += ";";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			while(rs.next()) {
				moduleHandbook = new ModuleHandbook(
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), rs.getString(7), rs.getBoolean(8));
				moduleHandbooks.add(moduleHandbook);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return moduleHandbooks;
	}

	// ####################################################
	// STUDYCOURSES
	// ####################################################

	// STUDYCOURSE ERSTELLEN
	/**
	 * @param studycourse
	 * @return true, if the studycourse was created successfully
	 */
	public boolean createStudycourse(Studycourse studycourse) {

		// GET VALUENAMES & VALUES
		String[] valuesArray = studycourse.toValuesArray();
		String[] valueNamesArray = studycourse.toValueNamesArray();
		
		String[] newValuesArray = Arrays.copyOfRange(valuesArray, 1, valuesArray.length);
		String[] newValuesNamesArray = Arrays.copyOfRange(valueNamesArray, 1, valueNamesArray.length);
		
		String values = Utilities.arrayToString(newValuesArray);
		String valueNames =  Utilities.arrayToString(newValuesNamesArray);

		// QUERY
		String query = "INSERT INTO studycourses (" + valueNames
				+ ") VALUES (" + values + ");";
		System.out.println("db:createStudycourse " + query);
		
		try {
			Statement stmt = db.createStatement();	
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				studycourse.setID(rs.getInt(1));
			    System.out.println("Generated eventID: " + studycourse.getID());	    
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 

		return true;
	}

	// STUDYCOURSE UPDATEN
	/**
	 * @param studycourse
	 * @return true, if the studycourse was updated successfully
	 */
	public boolean updateStudycourse(Studycourse studycourse) {

		// GET VALUENAMES & VALUES
		String[] valueNames = studycourse.toValueNamesArray();
		String[] values = studycourse.toValuesArray();

		// QUERY
		String query = "UPDATE studycourses SET ";

		for (int i = 0; i < valueNames.length - 1; i++) {
			query += valueNames[i] + " = " + values[i] + ", ";
		}
		query += valueNames[valueNames.length - 1] + " = "
				+ values[values.length - 1] + " ";
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
	/**
	 * @param studycourseID
	 * @return the studycourse with the passed studycourseID
	 */
	public Studycourse getStudycourse(int studycourseID) {
		Studycourse newStudycourse = new Studycourse(studycourseID);
		
		String query = "SELECT "+newStudycourse.toValueNames()+
				" FROM studycourses WHERE studycourseID="+studycourseID+";";
		System.out.println("db:getStudycourse " + query);
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			if (rs.next()) {
				newStudycourse = new Studycourse(
						rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getBoolean(4),
						rs.getString(5), rs.getBoolean(6));
				rs.close();
				return newStudycourse;

			} else {
				System.out.println("No Studycourse found with this ID.");
				rs.close();
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// STUDYCOURSE ENTFERNEN
	/**
	 * @param studycourse
	 * @return true, if the studycourse was deleted successfully
	 */
	public boolean deleteStudycourse(Studycourse studycourse) {

		String query = "DELETE FROM studycourses ";
		query += "WHERE studycourseID=" + studycourse.getID() + ";";
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
	// Holt die Subjects eines Studycourses und gibt diese in einer ArrayList aus
	/**
	 * @param studycourseID
	 * @param getOnlyEnabled
	 * @return list of subjects that belong to the studycourse with the passed studycourseID, if getOnlyEnabled = true, only enabled subjets are selected
	 */
	public ArrayList<Subject> getStudycourseSubjects(int studycourseID, boolean getOnlyEnabled) {

		ArrayList<Subject> subjects = new ArrayList<Subject>();
		Subject subject = new Subject(0);
		
		String query = "SELECT "+subject.toValueNames()+" FROM subjects " +
				"WHERE studycourses_studycourseID="+ studycourseID;
		if(getOnlyEnabled) query += " AND enabled=true";
		query += ";";
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				subject = null;
				subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getInt(3), 
						rs.getString(4), rs.getBoolean(5), rs.getString(6), rs.getBoolean(7));

				subjects.add(subject);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}
	
	// Holt die Subjedts eines ModuleHandbooks und gibt diese in einer ArrayList aus
	/**
	 * @param moduleHandbookID
	 * @param getOnlyEnabled
	 * @return list of subjects that belong to the modulehandbook with the passed moduleHandbokID, if getOnlyEnabled = true, only enabled subjets are selected
	 */
	public ArrayList<Subject> getModuleHandbookSubjects(int moduleHandbookID, boolean getOnlyEnabled) {

		ArrayList<Subject> subjects = new ArrayList<Subject>();
		Subject subject = new Subject(0);
		
		String query = "SELECT "+subject.toValueNames()+" FROM subjects " +
				"WHERE module_handbooks_moduleHandbookID="+ moduleHandbookID;
		if(getOnlyEnabled) query += " AND enabled=true";		
		query += ";";
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);

			while (rs.next()) {
				subject = null;
				subject = new Subject(rs.getInt(1), rs.getInt(2), rs.getInt(3), 
						rs.getString(4), rs.getBoolean(5), rs.getString(6), rs.getBoolean(7));
				subjects.add(subject);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}
	
	/*
	 * returns an ArrayList of all studycourses in the database
	 */
	/**
	 * @param getOnlyEnabled
	 * @return list of all studycourses, if getOnlyEnabled = true, only enabled studycourses are selected
	 */
	public ArrayList<Studycourse> readStudycourses(boolean getOnlyEnabled) {
		ArrayList<Studycourse> studycourses = new ArrayList<Studycourse>();
		
		Studycourse studycourse = new Studycourse (0);
		
		String query = "SELECT "+studycourse.toValueNames()+" FROM studycourses";
		if(getOnlyEnabled) query += " WHERE enabled=true";
		query += ";";
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			while(rs.next()) {
				studycourses.add(new Studycourse(rs.getInt(1), rs.getInt(2), 
						rs.getString(3), rs.getBoolean(4), rs.getString(5), rs.getBoolean(6)));
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return studycourses;
	}
	
	/**
	 * @param getOnlyEnabled
	 * @return list of all modulehandbooks, if getOnlyEnabled = true, only enabled modulehandbooks are selected
	 */
	public ArrayList<ModuleHandbook> readModuleHandbooks(boolean getOnlyEnabled) {
		ArrayList<ModuleHandbook> moduleHandbooks = new ArrayList<ModuleHandbook>();
		
		ModuleHandbook moduleHandbook = new ModuleHandbook(0);
		
		String query = "SELECT "+moduleHandbook.toValueNames()+" FROM module_handbooks";
		if(getOnlyEnabled) query += " WHERE enabled=true";
		query += ";";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			while(rs.next()) {
				moduleHandbook = new ModuleHandbook(
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), 
						rs.getString(7), rs.getBoolean(8));
				moduleHandbooks.add(moduleHandbook);
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return moduleHandbooks;
	}
	
	/**
	 * @param moduleHandbook
	 * @return the deadline that belongs to the passed modulehandbok
	 */
	public Deadline getDeadline(ModuleHandbook moduleHandbook) {
		boolean sose = moduleHandbook.isSose();
		int year = moduleHandbook.getYear();
		
		String query = "SELECT sose, year, deadline FROM content_deadlines WHERE sose=? AND year=?;";
		
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setBoolean(1, sose);
			ps.setInt(2, year);
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Deadline deadline = null;
			
			if(rs.next()) {
				deadline = new Deadline(rs.getBoolean(1), rs.getInt(2),
						rs.getDate(3));
			}
			rs.close();
			return deadline;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param sose
	 * @param year
	 * @return the deadline with the passed sose and year
	 */
	public Deadline getDeadline(boolean sose, int year) {		
		String query = "SELECT sose, year, deadline FROM content_deadlines WHERE sose=? AND year=?;";
		
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setBoolean(1, sose);
			ps.setInt(2, year);
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Deadline deadline = null;
			
			if(rs.next()) {
				deadline = new Deadline(rs.getBoolean(1), rs.getInt(2),
						rs.getDate(3));
			}
			rs.close();
			return deadline;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return list of all deadlines
	 */
	public ArrayList<Deadline> getDeadlines() {
		String query = "SELECT sose, year, deadline FROM content_deadlines " +
				"ORDER BY deadline DESC;";
		
		System.out.println(query);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(query);
			
			ArrayList<Deadline> deadlines = new ArrayList<Deadline>();
			
			Deadline deadline = null;
			
			while(rs.next()) {
				deadline = new Deadline(rs.getBoolean(1), rs.getInt(2),
						rs.getDate(3));
				deadlines.add(deadline);
			}
			
			rs.close();
			return deadlines;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 	
	}

	/**
	 * @param deadline
	 * @return true, if the deadline was updated successfully
	 */
	public boolean updateDeadline(Deadline deadline) {
		
		String query = "UPDATE content_deadlines SET deadline=? "+
				"WHERE sose=? AND year=?;";
		
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setDate(1, deadline.getDeadline());
			ps.setBoolean(2, deadline.isSose());
			ps.setInt(3, deadline.getYear());
			
			ps.executeUpdate();
			db.commit();
	
			return true;
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
	}

	/**
	 * @param deadline
	 * @return true, if the deadline was created successfully
	 */
	public boolean createDeadline(Deadline deadline) {
		String query = "INSERT INTO content_deadlines(deadline, sose, year) VALUES(?,?,?);";
		
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setDate(1, deadline.getDeadline());
			ps.setBoolean(2, deadline.isSose());
			ps.setInt(3, deadline.getYear());
			
			ps.executeUpdate();
			db.commit();
	
			return true;
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
	}

	/**
	 * @param sose
	 * @param year
	 * @return true, if the deadline was deleted successfully
	 */
	public boolean deleteDeadline(boolean sose, int year) {
		String query = "DELETE FROM content_deadlines WHERE sose=? AND year=?;";
		
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setBoolean(1, sose);
			ps.setInt(2, year);
			
			ps.executeUpdate();
			db.commit();
	
			return true;
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
	}

	/**
	 * @param event
	 * @return the earliest deadline that belongs to the passed event
	 */
	public java.sql.Date getEariliestDeadline(Event event) {
		String query = "SELECT deadline FROM content_deadlines WHERE (sose, year) " +
				"IN (SELECT sose, year FROM module_handbooks WHERE moduleHandbookID " +
				"IN (SELECT module_handbooks_modulehandbookID FROM subjects " +
				"WHERE subjectID IN (SELECT subjectID FROM modules_subjects " +
				"WHERE moduleID IN (SELECT moduleID FROM modules WHERE moduleID IN " +
				"(SELECT moduleID FROM events_modules WHERE eventID=?)))));";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setInt(1, event.getID());
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Date earliestDate = null;
			if(rs.next()) {
				earliestDate = rs.getDate(1);
				while(rs.next()) {
					if(earliestDate.before(rs.getDate(1))) earliestDate = rs.getDate(1);
				}
			}
			rs.close();
			return earliestDate;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * @param module
	 * @return the earliest deadline that belongs to the passed module
	 */
	public java.sql.Date getEariliestDeadline(Module module) {
		String query = "SELECT deadline FROM content_deadlines WHERE (sose, year) IN " +
				"(SELECT sose, year FROM module_handbooks WHERE moduleHandbookID IN " +
				"(SELECT module_handbooks_modulehandbookID FROM subjects WHERE subjectID IN " +
				"(SELECT subjectID FROM modules_subjects WHERE moduleID=?)));";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setInt(1, module.getID());
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Date earliestDate = null;
			if(rs.next()) {
				earliestDate = rs.getDate(1);
				while(rs.next()) {
					if(earliestDate.before(rs.getDate(1))) earliestDate = rs.getDate(1);
				}
			}
			rs.close();
			return earliestDate;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param subject
	 * @return the earliest deadline that belongs to the passed subject
	 */
	public java.sql.Date getEariliestDeadline(Subject subject) {
		String query = "SELECT deadline FROM content_deadlines WHERE (sose, year) IN " +
				"(SELECT sose, year FROM module_handbooks WHERE moduleHandbookID IN " +
				"(SELECT module_handbooks_modulehandbookID FROM subjects " +
				"WHERE subjectID=?))";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setInt(1, subject.getID());
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Date earliestDate = null;
			if(rs.next()) {
				earliestDate = rs.getDate(1);
				while(rs.next()) {
					if(earliestDate.before(rs.getDate(1))) earliestDate = rs.getDate(1);
				}
			}
			rs.close();
			return earliestDate;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param moduleHandbook
	 * @return the earliest deadline that belongs to the passed modulehandbook
	 */
	public java.sql.Date getEariliestDeadline(ModuleHandbook moduleHandbook) {
		String query = "SELECT deadline FROM content_deadlines WHERE (sose, year) IN " +
				"(SELECT sose, year FROM module_handbooks WHERE moduleHandbookID=?)";
		System.out.println(query);
		
		try {
			db.setAutoCommit(false);
			PreparedStatement ps = db.prepareStatement(query);
		
			ps.setInt(1, moduleHandbook.getID());
			
			ResultSet rs = ps.executeQuery();
			db.commit();
			
			Date earliestDate = null;
			if(rs.next()) {
				earliestDate = rs.getDate(1);
				while(rs.next()) {
					if(earliestDate.before(rs.getDate(1))) earliestDate = rs.getDate(1);
				}
			}
			rs.close();
			return earliestDate;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				db.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
