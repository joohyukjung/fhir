package ca.uhn.fhir.jpa.starter.vo.code;

import lombok.Getter;

/**
 * 검사 항목 카테고리
 * 
 * @author Cheol-Ho Choi
 */

@Getter
public enum CategoryCode {

	// Observation Category
	LABORATORY("specimen", "진단검사")
  , VITAL_SIGNS("Functional", "기타검사")
	
  	// DiagnosticReport Category
  , IMAGING("RAD", "영상검사")
  , PATHOLOGY("SP", "병리검사")
  ;
	
	private String code;
	private String name;
	
	CategoryCode(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
