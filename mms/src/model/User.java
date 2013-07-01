package model;


import model.userRights.UserRights;

public class User implements DbControllable {
	
	/* DATENBANK User
	 * 
	 * VAR email
	 * VAR firstName
	 * VAR lastName
	 * VAR title
	 * INT matricNum
	 * INT current_semester
	 * VAR graduation
	 * VAR password
	 * 
	 * */
	
	protected String firstName = "", lastName= "", title= "", email = "", graduation = "";
	protected transient String password;
	protected boolean isEmployee = false;
	protected int matricNum, semester;	
	protected UserRights userRights;	
	
	
	// Konstruktor 
	/** 
	 * constructor
	 * @param email
	 * @param password
	 */
	public User(String email, String password) {
		this.email = email.toLowerCase();
		this.password = password;
	}
	
	/**
	 * constructor
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public User(String email, String firstName, String lastName) {
		this.email = email.toLowerCase();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	// Konstruktor 
	/**
	 * constructor
	 * @param firstName
	 * @param lastName
	 * @param title
	 * @param email
	 * @param graduation
	 * @param password
	 * @param matricNum
	 * @param semester
	 * @param rights
	 */
	public User(String firstName, String lastName, String title, String email,
			String graduation, String password, int matricNum, int semester,
			UserRights rights) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.email = email.toLowerCase();
		this.graduation = graduation;
		this.password = password;
		this.matricNum = matricNum;
		this.semester = semester;
		this.userRights = rights;
	}

		
	/**
	 * constructor
	 * @param email
	 */
	public User(String email) {
		this.email = email;
	}

	// Getter & Setter
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public int getMatricNum() {
		return matricNum;
	}

	public void setMatricNum(int matricNum) {
		this.matricNum = matricNum;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public UserRights getUserRights() {
		return userRights;
	}

	public void setUserRights(UserRights userRights) {
		this.userRights = userRights;
	}

	
	/* DATENBANK User
	 * 
	 * VAR email
	 * VAR firstName
	 * VAR lastName
	 * VAR title
	 * INT matricNum
	 * INT current_semester
	 * VAR graduation
	 * VAR password
	 * 
	 * */
	
	
	@Override
	public String toValueNames() {
		return arrayToString(toValueNamesArray());
	}

	@Override
	public String toValues() {
		return arrayToString(toValuesArray());
	}

	@Override
	public String[] toValuesArray() {
		String[] values = {"'"+email+"'", "'"+firstName+"'", "'"+lastName+"'", "'"+title+"'", "'"+graduation+"'",
				"'"+password+"'", ""+matricNum, ""+semester};
		return values;
	}

	@Override
	public String[] toValueNamesArray() {
		String[] valueNames = {"email", "firstName", "lastName", "title", "graduation",
				"password", "matricNum", "current_semester"};
		return valueNames;
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
		return "["+arrayToString(toValuesArray())+"]";
	}
}