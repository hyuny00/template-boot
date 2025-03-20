package com.futechsoft.framework.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class CryptUtil {
	
	
	 public static String encryptPassword(String password, String id) throws Exception {

			if (password == null) return "";
			if (id == null) return ""; 
			
			byte[] hashValue = null;
		
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.reset();
			md.update(id.getBytes());
			
			hashValue = md.digest(password.getBytes());
		
			return new String(Base64.encodeBase64(hashValue));
    }
	 
	 
	 

}
