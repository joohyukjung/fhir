package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
/**
 * @InterfaceName : TfnMdsidxRepository.java
 * @Description : E001 최근 방문일자 조회, 내부호출 조건조회
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
public interface TfnMdsidxRepository extends JpaRepository<TfnMdsidxTbo, String>{

	/**
	 * E001 최근 방문일자 조회 :: 미입력 전체조회
	 * @param baseDate
	 * @return List<TfnMdsidxTbo>
	 */
	List<TfnMdsidxTbo> findByRegYmdAndUtilUserIdIn(String baseDate, List<String> agreUtilUserIdList);
	
	
	/**
	 * E001 최근 방문일자 조회 :: 입력 조건조회
	 * 등록일자, 제공기관코드
	 * @param baseDate
	 * @param PvsnInstCd
	 * @return List<TfnMdsidxTbo>
	 */
	List<TfnMdsidxTbo> findByRegYmdAndPvsnInstCdInAndUtilUserIdIn(String baseDate, List<String> PvsnInstCd, List<String> agreUtilUserIdList); 
	
	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 있을 경우
	 * 
	 * @param userId
	 * @param utilizationServiceCode
	 * @param provideInstitutionCode
	 * @return
	 */
	List<TfnMdsidxTbo> findAllByUtilUserIdAndUtilServiceCdAndPvsnInstCdIn(String userId, String utilizationServiceCode, List<String> provideInstitutionCode);
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 없을 경우
	 * 
	 * @param userId
	 * @param utilizationServiceCode
	 * @param provideInstitutionCode
	 * @return
	 */
	List<TfnMdsidxTbo> findAllByUtilUserIdAndUtilServiceCd(String userId, String utilizationServiceCode);

	
	/**
	 * 조회조건_fhir 1
	 * 기관코드 리스트
	 * @param utilUserId
	 * @param provideInstitutionCode
	 * @return
	 */
	List<TfnMdsidxTbo> findByUtilUserIdAndPvsnInstCdIn(String utilUserId, List<String> PvsnInstCd);
	
	
	/**
	 * 조회조건_fhir 2
	 * 기관코드 
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return
	 */
	List<TfnMdsidxTbo> findByUtilUserIdAndPvsnInstCd(String utilUserId, String PvsnInstCd);
	
}
