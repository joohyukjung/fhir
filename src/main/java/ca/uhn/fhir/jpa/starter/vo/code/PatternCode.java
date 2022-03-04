package ca.uhn.fhir.jpa.starter.vo.code;

import lombok.Getter;

@Getter
public enum PatternCode {
	
	  DB_DATE_PATTERN("yyyyMMddHHmmss")
	, API_DATE_PATTERN("yyyy-MM-dd HH:mm:ss")
	
	;

	private String pattern;
	
	PatternCode(String pattern) {
		this.pattern = pattern;
	}
}
