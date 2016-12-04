package auto_control_management;

import java.sql.Date;
import java.sql.Time;

// 사용자 동작 예약 클래스입니다.
public class Reservation {
	private int rs_id;
	private Date rs_act_date;
	private Time rs_act_time;
	private String mb_id;
	private int rs_act;

	public Reservation(int rs_id, Date rs_act_date, Time rs_act_time, String mb_id, int rs_act) {
		this.rs_id = rs_id;
		this.rs_act_date = rs_act_date;
		this.rs_act_time = rs_act_time;
		this.mb_id = mb_id;
		this.rs_act = rs_act;
	}
	
	public int getID() {
		return rs_id;
	}
	
	public Date getDate() {
		return rs_act_date;
	}
	
	public Time getTime() {
		return rs_act_time;
	}
	
	public String getMemberID() {
		return mb_id;
	}
	
	public int getAct() {
		return rs_act;
	}
}
