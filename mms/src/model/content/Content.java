package model.content;

import model.DbControllable;

public class Content implements DbControllable {

	protected String name;
	protected int ID, parentID;
	protected boolean archived;

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public int getParentID() {
		return parentID;
	}

	public boolean isArchived() {
		return archived;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Override
	public String toValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] toValuesArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] toValueNamesArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String arrayToString(String[] array) {
		String string = "";
		for(int i=0; i<array.length-1; i++) {
			string += array[i] + ", ";
		}
		string += array[array.length-1];
		return string;
	}

}
