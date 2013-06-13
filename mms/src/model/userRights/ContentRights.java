package model.userRights;

import util.Utilities;
import model.DbControllable;

public class ContentRights implements DbControllable {
	protected boolean canEdit, canDelete;
	
	
	public ContentRights() {
	}
	
	// Getter
	public boolean getCanEdit() {
		return canEdit;
	}
	
	public boolean getCanDelete() {
		return canDelete;
	}
	
	// Setter
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	
	public String toValueNames() {
		String valueNames = Utilities.arrayToString(toValueNamesArray());
		return valueNames;
	}
	
	public String toValues() {
		String values = Utilities.arrayToString(toValuesArray());
		return values;
	}

	@Override
	public String[] toValuesArray() {
		// nothing (overwritten later)
		return null;
	}

	@Override
	public String[] toValueNamesArray() {
		// nothing (overwritten later)
		return null;
	}
	
	public String toString() {
		String string = "[";
		String[] values = toValuesArray();
		String[] valueNames = toValueNamesArray();
		for(int i=0; i<values.length-2; i++) {
			string += valueNames[i]+"="+values[i]+", ";
		}
		string += valueNames[values.length-2]+"="+values[values.length-2];
		return string;
	}	
	
}
