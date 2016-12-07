package environment_data_collector;

import java.sql.Date;
import java.sql.Time;

public class SensorData {
	private Date se_date;
	private Time se_time;
	private String dv_id;
	private double se_temperature;
	private double se_humidity;
	private double se_co2;
	
	public SensorData (Date date, Time time, String id, double temp, double reh, double co2) {
		this.se_date = date;
		this.se_time = time;
		this.dv_id = id;
		this.se_temperature = temp;
		this.se_humidity = reh;
		this.se_co2 = co2;
	}
	
	public Date getDate() {
		return se_date;
	}
	public Time getTime() {
		return se_time;
	}
	public String getID() {
		return dv_id;
	}
	public double getTemperature() {
		return se_temperature;
	}
	public double getHumidity() {
		return se_humidity;
	}
	public double getCO2() {
		return se_co2;
	}
}
