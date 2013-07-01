package model.content;

import util.Utilities;

public class ModuleHandbook extends Content {

	/*
	 * 	moduleHandbookID	int(5)
	 *  name	varchar(100)
	 *  studycourses_studycourseID	int(5)
	 *  semester	varchar(2)
	 *  archived boolean
	 */
	
	private int year;
	private boolean sose;
	private int moduleHandbookID, studycourses_studycourseID;
	
	// Konstruktor
	/**
	 * constructor
	 * @param moduleHandbookID
	 * @param name
	 * @param studycourses_studycourseID
	 * @param year
	 * @param sose
	 * @param archived
	 * @param content
	 * @param enabled
	 */
	public ModuleHandbook (int moduleHandbookID, String name, int studycourses_studycourseID, 
			int year, boolean sose, boolean archived, String content, boolean enabled) {
		this.moduleHandbookID = moduleHandbookID;
		this.name = name;
		this.studycourses_studycourseID = studycourses_studycourseID;
		this.year = year;
		this.sose = sose;
		this.archived = archived;
		this.content = content;
		this.enabled = enabled;
	}
	
	/**
	 * constructor
	 * @param moduleHandbookID
	 */
	public ModuleHandbook(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	// Getter & Setter
	public boolean isArchived() {
		return archived;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isSose() {
		return sose;
	}

	public void setSose(boolean sose) {
		this.sose = sose;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Override
	public String toValues() {
		String value = Utilities.arrayToString(toValuesArray());
		return value;
	}

	
	@Override
	public String toValueNames() {
		String value = Utilities.arrayToString(toValueNamesArray());
		return value;
	}
	
	
	@Override
	public String[] toValuesArray() {
		String[] values = {moduleHandbookID+"", "'"+name+"'", 
				studycourses_studycourseID+"", "'"+year+"'", ""+sose,   
				archived+"", ""+"'"+content+"'", ""+enabled};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] values = {"moduleHandbookID", "name", 
				"studycourses_studycourseID", "year", "sose", 
				"archived", "content", "enabled"};
		return values;
	}

	public int getID() {
		return moduleHandbookID;
	}

	public void setID(int moduleHandbookID) {
		this.moduleHandbookID = moduleHandbookID;
	}

	
}
