package ca.uhn.fhir.jpa.starter.util;

public class StringUtil {
	
	// 문자열 자르기
	public static String substr(String str, String cutPt) {
		
		return str.substring(str.lastIndexOf(cutPt) + 1);
	}
}
