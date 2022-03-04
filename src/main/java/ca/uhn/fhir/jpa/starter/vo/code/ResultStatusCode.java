package ca.uhn.fhir.jpa.starter.vo.code;

import lombok.Getter;

/**
 * 응답 데이터 상태 코드
 * 
 * @author Cheol-Ho Choi
 *
 */
@Getter
public enum ResultStatusCode {

	SUCCESS("200", "SUCCESS", "성공")
  , FAIL("500", "FAIL", "실패")
  , NOT_AGREEMENT("", "NOT AGREEMENT", "미동의")
  , BAD_REQUEST("400", "BAD REQUEST", "잘못된 요청")
  ;
	
	private String code;
	private String engName;
	private String korName;
	
	ResultStatusCode(String code, String engName, String korName) {
		// 
		this.code = code;
		this.engName = engName;
		this.korName = korName;
	}
}
