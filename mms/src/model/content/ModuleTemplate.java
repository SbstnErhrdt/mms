package model.content;

import java.util.ArrayList;

public class ModuleTemplate {
	
	private int templateID;
	private String templateName;
	private ArrayList<ModuleField> moduleFields;

	/**
	 * constructor
	 */
	public ModuleTemplate() {
		super();
	}
	
	/**
	 * constructor
	 * 
	 * @param templateName
	 * @param moduleFields
	 */
	public ModuleTemplate(String templateName,
			ArrayList<ModuleField> moduleFields) {
		super();
		this.templateName = templateName;
		this.moduleFields = moduleFields;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public ArrayList<ModuleField> getModuleFields() {
		return moduleFields;
	}

	public void setModuleFields(ArrayList<ModuleField> moduleFields) {
		this.moduleFields = moduleFields;
	}

	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
}
