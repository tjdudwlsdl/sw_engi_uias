package environment_data_analyzer;

import javax.swing.plaf.ActionMapUIResource;

import environment_data_collector.ControllerData;
import environment_data_collector.SensorData;
import environment_data_collector.Weather;
import util_core.DateTime;

public class EnvironmentAnalyzer {
	public enum WEATHER { SUNNY, PARTLY_CLOUDY, MOSTLY_CLOUDY, CLOUDY, RAIN, RAIN_SNOW, SNOW }
	public static WEATHER getWeatherType(int sky, int pty) throws Exception{
		switch(pty) {
		case 1: // 비
			return WEATHER.RAIN;
			
		case 2: // 비 / 눈
			return WEATHER.RAIN_SNOW;
			
		case 3: // 눈
			return WEATHER.SNOW;
			
		default: // 그 외
				switch(sky) {
				case 0: // 맑음
					return WEATHER.SUNNY;
					
				case 1: // 구름 조금
					return WEATHER.PARTLY_CLOUDY;
					
				case 2: // 구름 많음
					return WEATHER.MOSTLY_CLOUDY;
					
				case 3: // 흐림
					return WEATHER.CLOUDY;
					
					default:
						throw new Exception();
				}
		}
	}
	
	public static String getWeatherString(int sky, int pty) throws Exception {
		switch(getWeatherType(sky, pty)) {
		case SUNNY:
			return "Sunny";
		case PARTLY_CLOUDY:
			return "Partly Cloudy";
		case MOSTLY_CLOUDY:
			return "Mostly Cloudy";
		case CLOUDY:
			return "Cloudy";
		case RAIN:
			return "Rain";
		case RAIN_SNOW:
			return "Rain/Snow";
		case SNOW:
			return "Snow";
		
		default:
			throw new Exception();
		}
	}
	
	public static AnalysisResult AnalyzeEnvironment(Weather weather, SensorData se, ControllerData co) {
		boolean hasAction = false;
		int act = 0;
		String pushMessage = null;
		DateTime dt = new DateTime("Asia/Seoul");
		
		// No data;
		if(weather == null && se == null) {
			return new AnalysisResult(hasAction, act, pushMessage);
		}
		
		do {
		if(weather != null) {
			// 비가 올 경우
			if(weather.getPty() != 0 && co != null && co.getState() != 0) {
				hasAction = true;
				act = 0;
				break;
			}
		}
		if(se != null) {
			// 이산화탄소 높을 시
			if(se.getCO2() > 1500 && co != null && co.getState() != 100) {
				hasAction = true;
				act = 100;
				break;
			}
		}
		}while(false);
		
		
		// 제어 권한 확인
		// 10분 이내에 직접 또는 예약 조작이 있었는지
		if(co != null && dt.getDateTimeDiff(co.getDate(), co.getTime()) < 600000l
				&& co.getPriority() < 2) {
			hasAction = false;
		}
		
		return new AnalysisResult(hasAction, act, pushMessage);
	}
}
