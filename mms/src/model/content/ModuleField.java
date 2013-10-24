package model.content;

import util.Utilities;
import java.sql.Timestamp;

public class ModuleField {
	private int moduleFieldID, fieldType;
	private String fieldName;
	private Object fieldValue;
	private Timestamp timestamp;

	/**
	 * constructor
	 */
	public ModuleField() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param moduleFieldID
	 * @param fieldType
	 * @param fieldName
	 */
	public ModuleField(int moduleFieldID, int fieldType, String fieldName) {
		super();
		this.moduleFieldID = moduleFieldID;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
	}

	/**
	 * contructor
	 * 
	 * @param moduleFieldID
	 * @param fieldType
	 * @param fieldName
	 * @param fieldValueString
	 * @param timestamp
	 */
	public ModuleField(int moduleFieldID, int fieldType, String fieldName,
			String fieldValueString, Timestamp timestamp) {
		super();
		this.moduleFieldID = moduleFieldID;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.fieldValue = Utilities.parseType(fieldType, fieldValueString);
		this.timestamp = timestamp;
	}
	
	/**
	 * contructor
	 * 
	 * @param moduleFieldID
	 * @param fieldType
	 * @param fieldName
	 * @param fieldValueString
	 */
	public ModuleField(int moduleFieldID, int fieldType, String fieldName,
			String fieldValueString) {
		super();
		this.moduleFieldID = moduleFieldID;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.fieldValue = Utilities.parseType(fieldType, fieldValueString);
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getModuleFieldID() {
		return moduleFieldID;
	}

	public void setModuleFieldID(int moduleFieldID) {
		this.moduleFieldID = moduleFieldID;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValueString) {
		this.fieldValue = Utilities.parseType(fieldType, fieldValueString);
	}

	@Override
	public String toString() {
		return "ModuleField [moduleFieldID=" + moduleFieldID + ", fieldType="
				+ fieldType + ", fieldName=" + fieldName + ", fieldValue="
				+ fieldValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + fieldType;
		result = prime * result
				+ ((fieldValue == null) ? 0 : fieldValue.hashCode());
		result = prime * result + moduleFieldID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleField other = (ModuleField) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (fieldType != other.fieldType)
			return false;
		if (fieldValue == null) {
			if (other.fieldValue != null)
				return false;
		} else if (!fieldValue.equals(other.fieldValue))
			return false;
		if (moduleFieldID != other.moduleFieldID)
			return false;
		return true;
	}
}
