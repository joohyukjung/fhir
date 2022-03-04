package ca.uhn.fhir.jpa.starter.myhw.service;

import java.text.ParseException;
import java.util.List;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.RecentlyVisitDatasearchVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;

/**
 * @InterfaceName : TfnMdsidxService.java
 * @Description : 최근 방문일자 조회, 최근 방문 환자 인덱스 생성
 * @Modification
 * 
 *                <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 
 *               </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
public interface TfnMdsidxService {
	

	/**
	 * E001 최근 방문일자 조회
	 * @param recentlyVisitDatasearchVO
	 * @return List<VisitPatientDatasetVO>
	 */
	List<VisitPatientDatasetVO> getVisitPatientIndex(RecentlyVisitDatasearchVO recentlyVisitDatasearchVO);
	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * @param recentlyVisitDatasearchVO
	 * @return
	 */
	List<VisitPatientDatasetVO> getVisitPatientIndexByPatient(RecentlyVisitDatasearchVO recentlyVisitDatasearchVO);
	
	/**
	 * 내부호출 리스트 조건 조회_fhir
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
	List<FhirVisitPatientDatasetVO> getFhirVisitPatientIndexByCondition(String utilUserId, List<String> PvsnInstCd);
	
	/**
	 * 내부호출 단건 조회_fhir
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
	FhirVisitPatientDatasetVO getFhirVisitPatientIndexByCondition(String utilUserId, String PvsnInstCd);
	
	
	/**
	 * E003 최근 방문 환자 인덱스 생성 
	 * 주민등록번호로 MH사용자ID 목록 조회
	 * @return List<String>
	 */
//	List<String> getUtilUserIdByRrno(String rrno);
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * @param visitPatientIndexVO
	 * @return String
	 * @throws ParseException 
	 * @throws Exception 
	 */
	List<VisitPatientDatasetVO> upsertVisitPatientIndex(VisitPatientIndexVO visitPatientIndexVO) throws ParseException, Exception;
	
}
