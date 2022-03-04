package ca.uhn.fhir.jpa.starter.vo.code;

import lombok.Getter;

@Getter
public enum AgreementCode {

	NOT_AGREEMENT("01", "미동의")
  , AGREEMENT("02", "동의")
  , WITHDRAW("03", "동의 철회")
	
  ;
	
	private String code;
	private String name;
	
	AgreementCode(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
