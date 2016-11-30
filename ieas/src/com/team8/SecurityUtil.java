package com.team8;

import java.security.*;

// 보안 기능을 위한 유틸리티 클래스입니다.
public class SecurityUtil {
	
	// 문자열의 SHA-256 해쉬 결과를 String으로 반환합니다.
	public static String encryptSHA256(String str) {
		String sha = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte[] data = sh.digest();
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<data.length; i++) {
				sb.append(Integer.toString((data[i]&0xff)+0x100, 16).substring(1));
			}
			sha = sb.toString();
		}
		catch(NoSuchAlgorithmException e) {
			sha = null;
		}
		return sha;
	}
	
	// 256Bit 크기의 랜덤 Salt를 String으로 반환합니다.
	public static String createSalt() {
		byte[] data= new byte[32];
		String salt;
		try {
			SecureRandom.getInstanceStrong().nextBytes(data);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<data.length; i++) {
				sb.append(Integer.toString((data[i]&0xff)+0x100, 16).substring(1));
			}
			salt = sb.toString();
		}
		catch(Exception e) { 
			salt = null;
		}
		return salt;
	}
}
