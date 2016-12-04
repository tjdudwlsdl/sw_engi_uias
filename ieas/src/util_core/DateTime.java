package util_core;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTime {
	private Date date;
	private Time time;
	private long datetime;
	
	public static long getLocalDateTime(String timeZoneID) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
		return calendar.getTimeInMillis();
	}
	
	public DateTime(String timeZoneID) {
		this.datetime = getLocalDateTime(timeZoneID);
		this.date = new Date(datetime);
		this.time = new Time(datetime);
	}
	
	public Date getDate() {
		return date;
	}
	
	public Time getTime() {
		return time;
	}
}
