package model.content;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Deadline {
	private boolean sose;
	private int year;
	private Date deadlineDate;
	private String deadline;
	
	/**
	 * constructor
	 * @param sose
	 * @param year
	 * @param deadline
	 */
	public Deadline(boolean sose, int year, Date deadlineDate) {
		super();
		this.sose = sose;
		this.year = year;
		this.deadlineDate = deadlineDate;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// user the correct date format yyyy-MM-dd
		deadline = df.format(deadlineDate);
	}
	
	/**
	 * constructor
	 * @param sose
	 * @param year
	 */
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
		return deadlineDate;
	}

	public void setDeadline(Date deadline) {
		this.deadlineDate = deadline;
	}
}
