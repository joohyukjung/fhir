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
public class ResultResourcePageVO<T> {

	/** 결과 상태 */
	private String status;
	
	/** 서비스 ID */
	private String serviceUID;
	
	/** 제공기관 코드 */
	private String provideInstitutionCode;
	
	private long totalItemCount;

    private int totalPages;
	
	private List<T> entry;
	
	//
	public void setSuccess(String serviceUID, String provideInstitutionCode, long totalItemCount, int totalPages, List<T> entry) {
		this.status = ResultStatusCode.SUCCESS.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = provideInstitutionCode;
		this.totalItemCount = totalItemCount;
		this.totalPages = totalPages;
		this.entry = entry;
	}
	
	// 동의 정보 없음
	public void setNotAgreemnet(String serviceUID) {
		this.status = ResultStatusCode.NOT_AGREEMENT.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = null;
		this.totalItemCount = 0;
		this.totalPages = 0;
		this.entry = null;
	}
	
	// Bad Request
	public void setBadRequest(String serviceUID) {
		this.status = ResultStatusCode.BAD_REQUEST.getEngName();
		this.serviceUID = serviceUID;
		this.provideInstitutionCode = null;
		this.totalItemCount = 0;
		this.totalPages = 0;
		this.entry = null;
	}
}
