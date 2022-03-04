package ca.uhn.fhir.jpa.starter.vo.index;

import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class VisitPatientIndexVO {
	// MH사용자ID
	@Size(min = 0, max = 9)
	private String userId;
	
	// 활용서비스ID
	@Size(min = 0, max = 10)
	private String utilizationServiceCode;
	
	// 제공기관환자ID
	private String patientId;
	
	// 제공기관코드
	private String provideInstitutionCode;
	
	// 요양기관기호
	private String careInstitutionSign;
	
	// 최근방문날짜
	private String recentlyVisitDate;
	
	// 등록일자
	private String registrationDate;
	
	// FHIR환자ID
	private String fhirPatientId;
	
	// FHIR의료기관ID
	private String fhirOrganizationId;
	
	// 최종수정일자
	private String lastModificationDateTime; // lastModificationDateTime 수정하기
	
	// 주민번호
//	private String residentRegistrationNumber;

}
