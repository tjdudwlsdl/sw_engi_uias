package com.team8.environment_data_collector;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

// 기상 정보 수집을 위한 클래스입니다.
public class WeatherIntelligenceCollector {
	private static final String RSSURL = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=";
	
	public static Weather getRecentWeather(String localcode) {
		try {
			URL url = new URL(RSSURL+localcode);
			
			Document doc = new SAXBuilder().build(url);
			
			Element rss = doc.getRootElement();
			Element channel = rss.getChild("channel");
			Element item = channel.getChild("item");
			Element description = item.getChild("description");
			
			// xml 정보로부터 날짜를 가져옴
			String tm = description.getChild("header").getChildText("tm");
			StringBuilder sb = new StringBuilder();
			sb.append(tm.substring(0, 4));
			sb.append('-');
			sb.append(tm.substring(4, 6));
			sb.append('-');
			sb.append(tm.substring(6, 8));
			Date date = Date.valueOf(sb.toString());
			
			// xml 정보로부터 기상 데이터를 가져옴
			Element data = description.getChild("body").getChild("data");
			String day = data.getChildText("day");
			String hour = data.getChildText("hour");
			String temp = data.getChildText("temp");
			String reh = data.getChildText("reh");
			String pty = data.getChildText("pty");	// 강수 형태  (없음(0), 비(1), 비/눈(2), 눈(3))
			String sky = data.getChildText("sky");	// 하늘 상태  (맑음(1), 구름조금(2), 구름많음(3), 흐림(4))
			String pop = data.getChildText("pop");	// 강수 확률  (%)
			
			// 날짜와 시간 정보 설정
			int h = Integer.parseInt(hour);
			if(h==24) {
				h=0;
				date = new Date(date.getTime() + (24*3600*1000));
			}
			else if(!day.equals("0")) {
				date = new Date(date.getTime() + (24*3600*1000));
			}
			Time time = Time.valueOf(String.format("%02d:00:00", h));
			
			return new Weather(localcode, date, time,
					Double.parseDouble(temp),
					Double.parseDouble(reh),
					Integer.parseInt(pty),
					Integer.parseInt(sky),
					Integer.parseInt(pop));
		}
		catch(Exception e) {
			return null;
		}
	}
}
