package environment_data_collector;

import java.sql.Date;
import java.sql.Time;

public class ControllerData {
	private Date co_date;
	private Time co_time;
	private String dv_id;
	private int co_state;
	private int co_priority;
	
	public ControllerData (Date date, Time time, String id, int state, int priority) {
		this.co_date = date;
		this.co_time = time;
		this.dv_id = id;
		this.co_state = state;
		this.co_priority = priority;
	}
	
	public Date getDate() {
		return co_date;
	}
	
	public Time getTime() {
		return co_time;
	}
	
	public String getID() {
		return dv_id;
	}
	
	public int getState() {
		return co_state;
	}
	
	public int getPriority() {
		return co_priority;
	}
}
