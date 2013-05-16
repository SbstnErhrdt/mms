package model;

import model.userRights.EmployeeRights;
import util.*;

public class Employee extends User {
	private boolean isEmployee = true;
	private String address, phoneNum, talkTime;
	private EmployeeRights employeeRights;

	public EmployeeRights getEmployeeRights() {
		return employeeRights;
	}

	public void setEmployeeRights(EmployeeRights employeeRights) {
		this.employeeRights = employeeRights;
	}

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
		String[] values = {"'" + address + "'", "'" + phoneNum + "'", "'" + talkTime + "'"};
		return values;
	}

	public String[] toEmployeeValueNamesArray() {
		String[] valueNames = {"address", "phoneNum", "talkTime"};
		return valueNames;
	}
	
	public String toEmployeeValues() {
		return Utilities.arrayToString(toEmployeeValuesArray());
	}
	
	public String toEmployeeValueNames() {
		return Utilities.arrayToString(toEmployeeValueNamesArray());
	}
	

}
