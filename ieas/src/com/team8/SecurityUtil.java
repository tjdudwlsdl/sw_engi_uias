package com.team8;

import java.security.*;

public class SecurityUtil {
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
