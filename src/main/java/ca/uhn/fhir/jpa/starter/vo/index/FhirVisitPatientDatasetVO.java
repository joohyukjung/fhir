package ca.uhn.fhir.jpa.starter.vo.index;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 내부 호출하는 서비스에 대한 VO
 * 
 * @author smlee
 *
 */
@Data
@Builder
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FhirVisitPatientDatasetVO {
	
	private String utilUserId; // MH사용자ID
	
	private String pvsnInstCd; // 제공기관코드(MHW채번)
	
	private String fhirPatientId; // FHIR환자ID
	
	private String fhirOrganizationId; // FHIR의료기관
	
	public static FhirVisitPatientDatasetVO from(TfnMdsidxTbo tbo) {
		FhirVisitPatientDatasetVO vo = FhirVisitPatientDatasetVO.builder()
				.utilUserId(tbo.getUtilUserId())
				.pvsnInstCd(tbo.getPvsnInstCd())
				.fhirPatientId(tbo.getFhirPatId())
				.fhirOrganizationId(tbo.getFhirPvsnInstCd())
				.build();
		return vo;
	}
	
}
