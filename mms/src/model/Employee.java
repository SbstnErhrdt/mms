package model;

import model.userRights.EmployeeRights;
import model.userRights.UserRights;
import util.*;

public class Employee extends User {
	private String address, phoneNum, talkTime;
	private EmployeeRights employeeRights;

	public Employee(String email, String password, String address, String phoneNum, String talkTime)  {
		super(email, password);
		this.employeeRights = new EmployeeRights();
		this.address = address;
		this.phoneNum = phoneNum;
		this.talkTime = talkTime;
		isEmployee = true;
	}
	
	public Employee(String email, String password, String firstName, String lastName, String title,
			String graduation, int matricNum, int semester,
			UserRights rights) {
		super(firstName, lastName, title, email, graduation, password, 
				matricNum, semester, rights);
		this.employeeRights = new EmployeeRights();
		isEmployee = true;
	}
	
	public EmployeeRights getEmployeeRights() {
		return employeeRights;
	}

	public void setEmployeeRights(EmployeeRights employeeRights) {
		this.employeeRights = employeeRights;
	}

	public Employee(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(String talkTime) {
		this.talkTime = talkTime;
	}
	
	public String[] toEmployeeValuesArray() {	
		String[] values = {"'"+email+"'", "'" + address + "'", "'" + phoneNum + "'", "'" + talkTime + "'"};
		return values;
	}

	public String[] toEmployeeValueNamesArray() {
		String[] valueNames = {"email", "address", "phoneNum", "talkTime"};
		return valueNames;
	}
	
	public String toEmployeeValues() {
		return Utilities.arrayToString(toEmployeeValuesArray());
	}
	
	public String toEmployeeValueNames() {
		return Utilities.arrayToString(toEmployeeValueNamesArray());
	}
	
	
	public String toString() {
		return "["+Utilities.arrayToString(toValuesArray())+
				", "+ Utilities.arrayToString(toEmployeeValuesArray())+"]";
	}

}
