package com.team8.environment_data_collector;

import java.sql.Date;
import java.sql.Time;

// 기상정보 Model 클래스입니다.
public class Weather {
	private String localcode;
	private Date date;
	private Time time;
	private double temperature;
	private double humidity;
	private int pty;	// 강수 형태  (없음(0), 비(1), 비/눈(2), 눈(3))
	private int sky;	// 하늘 상태  (맑음(1), 구름조금(2), 구름많음(3), 흐림(4))
	private int pop;	// 강수 확률  (%)
	
	public Weather(String localcode, Date date, Time time, 
			double temperature, double humidity, int pty, int sky, int pop) {
		this.localcode = localcode;
		this.date = date;
		this.time = time;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pty = pty;
		this.sky = sky;
		this.pop = pop;
	}
	
	public String getLocalcode() {
		return localcode;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Time getTime() {
		return time;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public double getHumidity() {
		return humidity;
	}
	
	public int getPty() {
		return pty;
	}
	
	public int getSky() {
		return sky;
	}
	
	public int getPop() {
		return pop;
	}
}
