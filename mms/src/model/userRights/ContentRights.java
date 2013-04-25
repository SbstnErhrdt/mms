package model.userRights;

import java.util.ArrayList;
import model.DbControllable;

public class ContentRights implements DbControllable {
	private boolean canEdit, canCreate, canDelete;
	
	
	public ContentRights() {
		
	}
	
	// Getter
	public boolean getCanEdit() {
		return canEdit;
	}
	
	public boolean getCanCreate() {
		return canCreate;
	}
	
	public boolean getCanDelete() {
		return canDelete;
	}
	
	// Setter
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	
	public String[] toValueNames() {
		String[] valueNames = {"canEdit", "canCreate", "canDelete", "ID"};
		return valueNames;
	}
	
	public String[] toValues() {
		String[] values = {"" + canEdit, "" + canCreate, "" + canDelete, "ID"};
		return values;
	}
	
	public ArrayList toValues2() {
		ArrayList values = new ArrayList();
		values.add(canEdit);
		values.add(canCreate);
		values.add(canDelete);
		return values;
	}
	
}
