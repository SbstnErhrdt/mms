package model;

import java.util.ArrayList;


import model.userRights.UserRights;


import controller.DbController;

public class User implements DbControllable {
	public String firstName, lastName, title, email;
	protected String graduation;
	private String password;
	private boolean isEmployee = false;
	
	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	protected int matricNum, semester;
	
	protected UserRights userRights;
	
	protected boolean emailVerified;
	
	private String[] attrInOrder = {"firstName", "lastName", "title", "email",
	                                "graduation", "password", "matricNum", "semester",
	                                "emailVerified"};
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public User(String firstName, String lastName, String title, String email,
			String graduation, String password, int matricNum, int semester,
			UserRights rights, boolean emailVerified) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.email = email;
		this.graduation = graduation;
		this.password = password;
		this.matricNum = matricNum;
		this.semester = semester;
		this.userRights = rights;
		this.emailVerified = emailVerified;
	}
	
	public ArrayList getAttributes() {
		ArrayList attr = new ArrayList();
		attr.add(firstName);
		attr.add(lastName);
		attr.add(title);
		attr.add(email);
		attr.add(graduation);
		attr.add(password);
		attr.add(matricNum);
		attr.add(semester);
		attr.add(rights); // KEIN OBJECT?
		attr.add(emailVerified);
		return attr;
	}
	
	public boolean registerToDb() {
		if(DbController.getInstance().createUser(this)) return true;
		else return false;
	}
	
	public boolean changeToDb() {
		if(DbController.getInstance().update(this)) return true;
		else return false;
	}
	
	public String[] getValueNames() {
		return attrInOrder;
	}
	
	public String[] getValues() {
		String[] values = {"" + firstName, "" + lastName, "" +  title, "" + email,
            "" + graduation, "" + password, "" + matricNum, "" + semester,
            "" + rights, "" + emailVerified};
        return values;
	}

	public int getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}

	public static boolean login() {
		User user = (User) DbController.getInstance().read(this);
		return false;
	}
	
	public UserRights getUserRights() {
		return userRights;
	}

	@Override
	public String[] toValues() {
		return attrInOrder;;
	}

	@Override
	public String[] toValueNames() {
		String[] values = {"" + firstName, "" + lastName, "" +  title, "" + email,
	            "" + graduation, "" + password, "" + matricNum, "" + semester,
	            "" + emailVerified};
		return values;
	}

}