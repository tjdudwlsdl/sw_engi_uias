package com.team8;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


// 기상청 RSS활용을 위한 장소/지역코드 유틸리티 클래스입니다.
public class LocationUtil {
	private static final String TOP_URL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
	private static final String MDL_URL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.%s.json.txt";
	private static final String LEAF_URL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf.%s.json.txt";
	
	// 주어진 장소 정보로부터 지역 코드를 가져옵니다.
	public static String getLocationCode(String top, String mid, String leaf) {
		String code = "";
		try {
			code = getLocalCode(TOP_URL, top);
			code = getLocalCode(String.format(MDL_URL, code), mid);
			code = getLocalCode(String.format(LEAF_URL, code), leaf);
			return code;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	// 시/도 선택을 위한 리스트를 가져옵니다.
	public static String[] getTopLocationList() {
		return getLocationList(TOP_URL);
	}
	
	// 시/군/구 선택을 위한 리스트르 가져옵니다.
	public static String[] getMdlLocationList(String local) {
		return getLocationList(String.format(MDL_URL, local));
	}
	
	// 동/읍/면 선택을 위한 리스트를 가져옵니다.
	public static String[] getLeafLocationList(String local) {
		return getLocationList(String.format(LEAF_URL, local));
	}
	
	// 주어진 URL로부터 장소 리스트를 가져옵니다.
	private static String[] getLocationList(String localUrl) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray arr = getJSONArray(localUrl);
			JSONObject obj;
			for(int i=0; i<arr.size(); i++) {
				obj = (JSONObject)arr.get(i);
				list.add(obj.get("value").toString());
			}
			String[] locations = new String[0];
			return list.toArray(locations);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	// 주어진 URL로부터 local value와 일치하는 code를 가져옵니다.
	private static String getLocalCode(String localUrl, String local) 
			throws Exception{
		JSONObject obj;
		JSONArray arr = getJSONArray(localUrl);
		
		// Code 찾기
		for(int i=0; i<arr.size(); i++) {
			obj = (JSONObject)arr.get(i);
			if(obj.get("value").toString().equals(local)) {
				return obj.get("code").toString();
			}
		}
		
		throw new Exception("Can't found code");
	}
	

	// 주어진 URL로부터 JSON 배열 전체를 가져옵니다.
	private static JSONArray getJSONArray(String localUrl) throws Exception{
		URL url = new URL(localUrl);
		InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
		JSONArray arr = (JSONArray)JSONValue.parse(isr);
		isr.close();
		return arr;
	}
}
