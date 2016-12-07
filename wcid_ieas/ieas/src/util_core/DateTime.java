package util_core;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTime {
	private Date date;
	private Time time;
	
	private static long getLocalDateTime(String timeZoneID) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
		return calendar.getTimeInMillis();
	}
	
	public DateTime(String timeZoneID) {
		long datetime = getLocalDateTime(timeZoneID);
		this.date = Date.valueOf(new Date(datetime).toString());
		this.time = Time.valueOf(new Time(datetime).toString());
	}
	
	public DateTime(String year, String month, String day, String hour, String minute)
						throws Exception {
		
		int y = Integer.parseInt(year);
		int m;
		switch(month) {
		case "Jan":
			m=1; break;
		case "Feb":
			m=2; break;
		case "Mar":
			m=3; break;
		case "Apr":
			m=4; break;
		case "May":
			m=5; break;
		case "Jun":
			m=6; break;
		case "Jul":
			m=7; break;
		case "Aug":
			m=8; break;
		case "Sep":
			m=9; break;
		case "Oct":
			m=10; break;
		case "Nov":
			m=11; break;
		case "Dec":
			m=12; break;
		default:
			throw new Exception();
		}
		int d = Integer.parseInt(day);
		int h = Integer.parseInt(hour);
		int min = Integer.parseInt(minute);
		
		String dateStr = String.format("%04d-%02d-%02d", y,m,d);
		String timeStr = String.format("%02d:%02d:00", h,min);
		
		this.date = Date.valueOf(dateStr);
		this.time = Time.valueOf(timeStr);
		
	}
	
	public Date getDate() {
		return date;
	}
	
	public Time getTime() {
		return time;
	}
	
	public long getDateTime() {
		return date.getTime()+time.getTime();
	}
	
	public long getDateTimeDiff(Date date, Time time) {
		return getDateTime() - (date.getTime()+time.getTime());
	}
}
