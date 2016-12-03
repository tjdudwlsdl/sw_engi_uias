package com.team8.task;

import java.util.TimerTask;

import com.team8.environment_data_collector.Weather;
import com.team8.environment_data_collector.WeatherIntelligenceCollector;

public class WeatherCollectionTask extends TimerTask {
	public static final int INTERVAL = 3*3600*1000;

	@Override
	public void run() {
		Weather weather = WeatherIntelligenceCollector.getRecentWeather(localcode)
		
	}

}
