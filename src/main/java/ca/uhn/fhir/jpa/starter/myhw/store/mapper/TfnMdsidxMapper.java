package ca.uhn.fhir.jpa.starter.myhw.store.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;

/**
 * @ClassName : TfnMdsidxMapper.java
 * @Description : TfnMdsidx 매핑
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.03 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.03
 * @version 1.0
 * @see
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TfnMdsidxMapper {
	TfnMdsidxMapper INSTANCE = Mappers.getMapper(TfnMdsidxMapper.class);
	
	/**
	 * E001 최근 방문일자 조회
	 * @param tbo
	 * @return
	 */
	@IterableMapping(qualifiedByName = "tboToVisitPatientDatasetVo")
	List<VisitPatientDatasetVO> tboListToVisitPatientDatasetVoList(List<TfnMdsidxTbo> tbos);
	
	@Named("tboToVisitPatientDatasetVo")
	@Mapping(source="utilUserId", target="userId") // MHW사용자ID
	@Mapping(source="patId", target="patientId") // 제공기관환자ID
	@Mapping(source="pvsnInstCd", target="provideInstitutionCode") // 제공기관코드
	@Mapping(source="rcbPrctYmd", target="recentlyVisitDate") // 최근방문일자
	@Mapping(source="fhirPatId", target="fhirPatientId") // FHIR환자ID
	VisitPatientDatasetVO tboToVisitPatientDatasetVo(TfnMdsidxTbo tbo);
	

	/**
	 * 조건조회_fhir
	 * @param tbo
	 * @return
	 */
	@IterableMapping(qualifiedByName = "tboToFhirVisitPatientDatasetVo")
	List<FhirVisitPatientDatasetVO> tboListToFhirVisitPatientDatasetVoList(List<TfnMdsidxTbo> tbos);
	
	@Named("tboToFhirVisitPatientDatasetVo")
	@Mapping(source="utilUserId", target="utilUserId") // MH사용자ID
	@Mapping(source="pvsnInstCd", target="pvsnInstCd") // 제공기관코드(MHW채번)
	@Mapping(source="fhirPatId", target="fhirPatientId") // FHIR환자ID
	@Mapping(source="fhirPvsnInstCd", target="fhirOrganizationId") // FHIR의료기관
	FhirVisitPatientDatasetVO tboToFhirVisitPatientDatasetVo(TfnMdsidxTbo tbo);
	

	/**
	 * E003 인덱스 생성
	 */
	@Mapping(source="userId", target="utilUserId")
	@Mapping(source="utilizationServiceCode", target="utilServiceCd")
	@Mapping(source="provideInstitutionCode", target="pvsnInstCd")
	@Mapping(source="patientId", target="patId")
	@Mapping(source="careInstitutionSign", target="cisn")
	@Mapping(source="fhirPatientId", target="fhirPatId")
	@Mapping(source="fhirOrganizationId", target="fhirPvsnInstCd")
	@Mapping(target="regYmd", expression="java(getDateStringNow())")
	@Mapping(source="recentlyVisitDate", target="rcbPrctYmd")
	@Mapping(source="lastModificationDateTime", target="lastMdfcnDt")
	TfnMdsidxTbo visitPatientIndexVoToTbo(VisitPatientIndexVO vo) throws Exception;
	
	default String getDateStringNow() throws Exception{
		return DateUtil.getDateStringNow("yyyyMMdd");
	}
}
