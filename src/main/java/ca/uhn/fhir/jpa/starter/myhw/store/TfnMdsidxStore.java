package ca.uhn.fhir.jpa.starter.myhw.store;

import java.util.List;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;

/**
 * @InterfaceName : TfnMdsidxStore.java
 * @Description : E001 최근 방문일자 조회, E003 최근 방문 환자 인덱스 생성
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
public interface TfnMdsidxStore {

	
	/**
	 * E001 최근 방문일자 조회 :: 미입력 전체조회
	 * 활용동의 철회한 데이터는 조회되지 않음
	 * 
	 * @param baseDate
	 * @return List<VisitPatientDatasetVO>
	 */
//	List<VisitPatientDatasetVO> selectByBaseDate(String baseDate, List<String> agreUtilUserIdList);
	
	/**
	 * E001 최근 방문일자 조회 :: 입력 조건조회
	 * TODO 활용동의 철회한 데이터는 조회되지 않음
	 * 
	 * @param baseDate
	 * @param PvsnInstCd
	 * @return List<VisitPatientDatasetVO>
	 */
//	List<VisitPatientDatasetVO> selectByBaseDateAndPvnsnInstCd(String baseDate, List<String> PvsnInstCd, List<String> agreUtilUserIdList);
	
	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 있을 경우
	 * 
	 */
//	List<VisitPatientDatasetVO> selectIndexByCondition(String userId, String utilizationServiceCode, List<String> provideInstitutionCode);
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 없을 경우
	 * 
	 */
//	List<VisitPatientDatasetVO> selectIndex(String userId, String utilizationServiceCode);
	
	
	/**
	 * E002 (가제)최근 방문일자 조회 사람
	 * 기관코드 없을 경우
	 */
//	List<VisitPatientDatasetVO> selectByUserList(List<String> userList);
	
	/**
	 * E002 (가제)최근 방문일자 조회 사람
	 * 기관코드 있을 경우
	 */
//	List<VisitPatientDatasetVO> selectByUserListAndPvsnInstCdList(List<String> userList, List<String> pvsnInstCdList);
	
	
	/**
	 * 내부호출 조건조회_fhir 1
	 * 기관코드 리스트
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
//	List<FhirVisitPatientDatasetVO> selectFhirVisitPatientIndexByUtilUserIdAndPvsnInstCd(String utilUserId, List<String> PvsnInstCd);
	
	/**
	 * 내부호출 조건조회_fhir 2
	 * 기관코드 
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return
	 */
//	List<FhirVisitPatientDatasetVO> selectFhirVisitPatientIndexByUtilUserIdAndPvsnInstCd(String utilUserId, String PvsnInstCd);
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 존재하면 update, 없으면 insert
	 * 
	 * @param tfnMdsidxTbo
	 * @return String
	 */
//	String upsertVisitPatientIndex(TfnMdsidxTbo tfnMdsidxTbo);
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 최근방문일자 rcbPrctYmd 조회
	 * 
	 * @param tbo
	 * @return
	 */
//	TfnMdsidxTbo selectRecentlyVisitDate(TfnMdsidxTbo tbo);
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 활용동의 상태 철회인 사용자를 제외하기 위한 목록 조회
	 * 
	 * @param rrno
	 * @return List<TfnDynagreTbo>
	 */
//	List<TfnDynagreTbo> selectUtilUserIdByUtilUserIdAndAgreementCode(String utilUserId, List<String> agreementCode);
}
