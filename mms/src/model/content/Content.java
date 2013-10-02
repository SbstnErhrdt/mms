package model.content;

import model.DbControllable;

public class Content implements DbControllable {

	protected String name, content, modifier_email;
	protected java.sql.Timestamp lastModified;
	protected boolean archived, enabled;
	protected int version;

	
	public java.sql.Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(java.sql.Timestamp lastModified) {
		this.lastModified = lastModified;
	}	

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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
	
	public String getModifier_email() {
		return modifier_email;
	}

	public void setModifier_email(String modifier_email) {
		this.modifier_email = modifier_email;
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
