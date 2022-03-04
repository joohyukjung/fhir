package ca.uhn.fhir.jpa.starter.vo.common;

import java.util.List;

import ca.uhn.fhir.jpa.starter.vo.code.ResultStatusCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultResourceListVO<T> {

	private String status;
	
	private String serviceUID;
	
	/** 제공기관 코드 */
	private String provideInstitutionCode;
	
	private List<T> entry;
	
	// 
	public void setSuccess(String serviceUID, String provideInstitutionCode, List<T> entry) {
		this.status = ResultStatusCode.SUCCESS.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = provideInstitutionCode;
		this.entry = entry;
	}
	
	// 동의 정보 없음
	public void setNotAgreemnet(String serviceUID) {
		this.status = ResultStatusCode.NOT_AGREEMENT.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = null;
		this.entry = null;
	}
	
	// Bad Request
	public void setBadRequest(String serviceUID) {
		this.status = ResultStatusCode.BAD_REQUEST.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = null;
		this.entry = null;
	}
}
