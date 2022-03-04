package ca.uhn.fhir.jpa.starter.vo.index;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VisitPatientDatasetVO {
	// MH사용자ID
	private String userId;
	
	// 제공기관환자ID
	private String patientId;
	
	// 제공기관코드
	private String provideInstitutionCode;
	
	// 최근방문일자
	private String recentlyVisitDate;
	
	// FHIR환자ID
	private String fhirPatientId;
	
	
	public static VisitPatientDatasetVO from(TfnMdsidxTbo tbo) {
		VisitPatientDatasetVO vo = VisitPatientDatasetVO.builder()
				.userId(tbo.getUtilUserId())
				.patientId(tbo.getPatId())
				.provideInstitutionCode(tbo.getPvsnInstCd())
				.recentlyVisitDate(tbo.getRcbPrctYmd())
				.fhirPatientId(tbo.getFhirPatId())
				.build();
		return vo;
	}
}
