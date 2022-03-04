package ca.uhn.fhir.jpa.entity.myhw.tbo;

import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnMdsidxId;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.code.PatternCode;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnMdsidxTbo.java
 * @Description : 방문환자index Entity
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     최초등록,최종수정일자 데어터타입 변경
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
@Entity(name = "TFN_MDSIDX")
@IdClass(TfnMdsidxId.class)
@Table(name = "TFN_MDSIDX")
public class TfnMdsidxTbo {

	@Id
	@NotBlank(message = "utilUserId must not be blank")
	private String utilUserId; // 활용사용자ID

	@NotBlank(message = "utilServiceCd must not be blank")
	private String utilServiceCd; // 활용서비스ID
	
	@Id
	private String patId; // 환자ID

	@Id
	@NotBlank(message = "pvsnInstCd must not be blank")
	private String pvsnInstCd; // 제공기관코드

	private String cisn; // 요양기관기호

	private String rcbPrctYmd; // 최근진료일자

	private String regYmd; // 등록일자

	private String fhirPatId; // FHIR환자ID

	private String fhirPvsnInstCd; // FHIR의료기관코드

	private String lastMdfcnDt; // 최종수정일자

	public static TfnMdsidxTbo from(VisitPatientIndexVO vo) {
		TfnMdsidxTbo tbo = TfnMdsidxTbo.builder()
				.utilUserId(vo.getUserId())
				.utilServiceCd(vo.getUtilizationServiceCode())
				.patId(vo.getPatientId())
				.pvsnInstCd(vo.getProvideInstitutionCode())
				.cisn(vo.getCareInstitutionSign())
				.rcbPrctYmd(vo.getRecentlyVisitDate())
				.regYmd(vo.getRegistrationDate())
				.fhirPatId(vo.getFhirPatientId())
				.fhirPvsnInstCd(vo.getFhirOrganizationId())
				.lastMdfcnDt(vo.getLastModificationDateTime())
				.build();
		return tbo;
	}
	
	// Builder custom
	public static class TfnMdsidxTboBuilder {
		public TfnMdsidxTboBuilder lastMdfcnDt(String lastModificationDateTime){
			if(lastModificationDateTime == null) return this;
			try {
				this.lastMdfcnDt = DateUtil.convertDateFormat(lastModificationDateTime, PatternCode.API_DATE_PATTERN.getPattern(), PatternCode.DB_DATE_PATTERN.getPattern());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return this;
		}
	}
	
}
