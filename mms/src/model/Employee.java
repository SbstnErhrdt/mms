package model;

public class Employee extends User {
	private boolean isEmployee = true;

	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public Employee(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}

}
