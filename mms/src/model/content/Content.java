package model.content;

import model.DbControllable;

public class Content implements DbControllable {

	protected String name;
	protected int ID, parentID;
	protected boolean archieved;

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public int getParentID() {
		return parentID;
	}

	public boolean isArchieved() {
		return archieved;
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

	public void setArchieved(boolean archieved) {
		this.archieved = archieved;
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

}
