package com.team8.account_device_management;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.InputStreamReader;
import java.net.URL;

// 기상청 RSS 활용을 위한 지역 코드를 가져오는 클래스입니다.
public class LocationCodeCreator {
	private static final String TOP_LOCATION_URL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
	
	public String getLocationCode(String top, String mid, String leaf) {
		try {
			URL url = new URL("JSON 주소");
			InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
			JSONObject object = (JSONObject)JSONValue.parse(isr);
			
			
		}
		catch (Exception e) {
			return null;
		}
		
		
	}
}
