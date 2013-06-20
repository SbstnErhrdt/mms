package model.content;

import java.sql.Date;

public class Deadline {
	private boolean sose;
	private int year;
	private Date deadline;
	
	public Deadline(boolean sose, int year, Date deadline) {
		super();
		this.sose = sose;
		this.year = year;
		this.deadline = deadline;
	}
	
	public Deadline(boolean sose, int year) {
		this.sose = sose;
		this.year = year;
	}

	public boolean isSose() {
		return sose;
	}

	public void setSose(boolean sose) {
		this.sose = sose;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
}
