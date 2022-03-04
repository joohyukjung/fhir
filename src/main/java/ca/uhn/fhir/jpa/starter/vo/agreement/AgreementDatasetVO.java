package ca.uhn.fhir.jpa.starter.vo.agreement;

import java.text.ParseException;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.code.PatternCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 2022-01-24 최초작성
 * @author smlee
 *
 */
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class AgreementDatasetVO {
	
	private String userId; // MH사용자ID
	                         
	private String userName; // 사용자이름
	
	private String utilizationServiceCode; // 활용서비스ID
	
	private String residentRegistrationNumber;	// 사용자 주민번호
	
	private String agreementStatus; // 동의상태코드
	
	private String datasetCode; // 데이터셋 코드

	private String datasetName; // 데이터셋 명
	
	private String agreementDateTime; 	// 동의일시
	
	private String withdrawDateTime; 	// 철회일시

	private String registrationDate; // 등록일자

		
	public static AgreementDatasetVO from(TfnDynagreTbo dynagreTbo){
		if(dynagreTbo == null) return null;
		return AgreementDatasetVO.builder()
				.userId(dynagreTbo.getUtilUserId())
				.utilizationServiceCode(dynagreTbo.getUtilServiceCd())
				.agreementStatus(dynagreTbo.getAgreStcd())
				.datasetCode(dynagreTbo.getDtstCd())
				.datasetName(dynagreTbo.getDtstNm())
				.agreementDateTime(dynagreTbo.getAgreDt())
				.withdrawDateTime(dynagreTbo.getAgreWhdwDt())
				.build();
		}	
	 
	// Builder custom
	public static class AgreementDatasetVOBuilder {
		public AgreementDatasetVOBuilder agreementDateTime(String agreDt){
			if(agreDt == null) return this;
			try {
				this.agreementDateTime = DateUtil.convertDateFormat(agreDt, PatternCode.DB_DATE_PATTERN.getPattern(), PatternCode.API_DATE_PATTERN.getPattern());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return this;
		}

		public AgreementDatasetVOBuilder withdrawDateTime(String agreWhdwDt){
			if(agreWhdwDt == null) return this;
			try {
				this.withdrawDateTime = DateUtil.convertDateFormat(agreWhdwDt, PatternCode.DB_DATE_PATTERN.getPattern(), PatternCode.API_DATE_PATTERN.getPattern());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return this;
		}
		
	}


}
