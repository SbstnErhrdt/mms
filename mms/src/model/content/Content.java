package model.content;

import model.DbControllable;

public class Content implements DbControllable {

	protected String name;
	protected boolean archived;

	public String getName() {
		return name;
	}

	public boolean isArchived() {
		return archived;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public String toString() {
		String string = "[";
		String[] values = toValuesArray();
		String[] valueNames = toValueNamesArray();
		for(int i=0; i<values.length-1; i++) {
			string += valueNames[i]+"="+values[i]+", ";
		}
		string += valueNames[values.length-1]+"="+values[values.length-1]+"]";
		
		return string;
	}

}
