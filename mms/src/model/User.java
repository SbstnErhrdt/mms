package model;


import model.userRights.UserRights;


import controller.DbController;

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
	
	private String firstName, lastName, title, email, graduation, password;
	private boolean isEmployee = false;
	private boolean emailVerified;
	private int matricNum, semester;	
	private UserRights userRights;	
	
	
	// Konstruktor 
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	// Konstruktor 
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

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
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
	public String[] toValueNames() {
		String[] values = {"email,", "firstName,", "lastName,", "title,", "graduation,", "password,", "matricNum,", "current_semester"};
		return values;
	}

	@Override
	public String[] toValues() {
		String[] values = {email+",",firstName+",",lastName+",",title+",",graduation+",",password+",",matricNum+",",semester+""};
		return values;
	}

	
	

}