package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnDynagreId;

/**
 * @InterfaceName : TfnDynagreRepository.java
 * @Description : A004 활용동의 조회, E003 최근 방문 환자 인덱스 생성
 * @Modification
 * 
 *                <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     활용동의 프로세스 변경(주민번호 암호화)
 * 
 *               </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
public interface TfnDynagreRepository extends JpaRepository<TfnDynagreTbo, TfnDynagreId>{

	/**
	 * A001 활용동의 등록 
	 * @param userId MHW사용자ID
	 * @return
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query( "DELETE FROM TFN_DYNAGRE T            \r\n" + 
			" WHERE 1=1                           \r\n" + 
			"   AND T.utilUserId = :UTIL_USER_ID  \r\n")
	int deleteAllByUserIdInQuery(@Param("UTIL_USER_ID") String userId);
	
	/**
	 * A002 활용동의 변경
	 * MHW사용자ID로 조회
	 * @param withdrawCode
	 * @return
	 */
	List<TfnDynagreTbo> findAllByUtilUserId(String userId);
	
	/**
	 * MHW사용자ID, 동의상태로 조회
	 * @param userId
	 * @return
	 */
	List<TfnDynagreTbo> findAllByUtilUserIdAndAgreStcd(String userId, String agreementStatus);
	
	/**
	 * A002 활용동의 변경
	 * 동의 상태로 변경
	 * @param userId
	 * @param withdrawDate
	 * @param withdrawCode
	 * @return
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query( "UPDATE TFN_DYNAGRE T           \r\n" + 
			"   SET   AGRE_STCD = :AGRE_STCD        \r\n" + 
			"	    , AGRE_DT = :AGRE_DT   \r\n" + 
			"	    , AGRE_WHDW_DT = NULL  \r\n" + 
			" WHERE 1=1\r\n" + 
			"   AND T.utilUserId = :UTIL_USER_ID   \r\n" + 
			"   AND T.dtstCd = :DTST_CD")
	int updateAgreementQuery(@Param("AGRE_DT") String agreDt, @Param("UTIL_USER_ID") String utilUserId, @Param("DTST_CD") String dtstCd, @Param("AGRE_STCD") String agreStcd);
	
	/**
	 * A002 활용동의 변경
	 * 철회 상태로 변경
	 * @param userId
	 * @param withdrawDate
	 * @param withdrawCode
	 * @return
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query( "UPDATE TFN_DYNAGRE T                     \r\n" + 
			"   SET  AGRE_STCD = :AGRE_STCD           \r\n" + 
			"	   , AGRE_DT = NULL                   \r\n" + 
			"	   , AGRE_WHDW_DT = :AGRE_WHDW_DT     \r\n" + 
			" WHERE 1=1                               \r\n" + 
			"   AND T.utilUserId = :UTIL_USER_ID      \r\n" + 
			"   AND T.dtstCd = :DTST_CD")
	int updateWithdrawQuery(@Param("AGRE_WHDW_DT") String agreWhdwDt, @Param("UTIL_USER_ID") String utilUserId, @Param("DTST_CD") String dtstCd, @Param("AGRE_STCD") String agreStcd);
	
	
	/**
	 * A003 활용동의 철회
	 * @param userId MHW사용자ID
	 * @param withdrawDate 철회일자
	 * @return
	 */
	@Transactional()
	@Modifying(clearAutomatically = true)
	@Query( "UPDATE TFN_DYNAGRE T                        \r\n" + 
			"   SET  AGRE_STCD = :AGRE_STCD              \r\n" + 
			"       ,AGRE_WHDW_DT = :AGRE_WHDW_DT        \r\n" + 
			"       ,AGRE_DT = NULL	                     \r\n" + 
			" WHERE 1=1                                  \r\n" + 
			"   AND T.utilUserId = :UTIL_USER_ID         \r\n" + 
			"   AND T.dtstCd = :DTST_CD                  \r\n" + 
			"   AND T.utilServiceCd = :UTIL_SERVICE_CD")
	int updateAgreStcdByUtilUserIdInQuery(@Param("UTIL_USER_ID") String userId, @Param("AGRE_WHDW_DT") String withdrawDateTime, @Param("AGRE_STCD") String withdrawCode, @Param("DTST_CD") String dtstCd, @Param("UTIL_SERVICE_CD") String utilizationServiceCode);
	
	/**
	 * A004 활용동의 조회
	 * 전체 조회 : 조회조건 없음, 활용동의한 데이터 전체 조회
	 * @return List<TfnDynagreTbo>
	 */
	List<TfnDynagreTbo> findAllByAgreStcdIn(List<String> agreementCode);
	
	/**
	 * A004 활용동의 조회
	 * 조건 조회 : 조회조건 있음, 활용동의한 데이터 조건 조회
	 * @param agreStcd 동의상태
	 * @param userId MHW사용자ID
	 * @param agreementCode 동의상태 리스트
	 * @returnList<TfnDynagreTbo>
	 */ 
	List<TfnDynagreTbo> findByAgreStcdAndUtilUserIdAndAgreStcdIn(String agreStcd, String userId, List<String> agreementCode);
	
	/**
	 * 조건 조회 : MHW사용자ID, 활용서비스코드
	 * @param userId MHW사용자ID
	 * @param utilServiceCd 활용서비스코드
	 * @return
	 */
	List<TfnDynagreTbo> findByUtilUserIdAndUtilServiceCd(String userId, String utilServiceCd);
	/////////////////////////////////////////////////////////////

	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * @return List<TfnDynagreTbo>
	 */
	List<TfnDynagreTbo> findDistinctByUtilUserIdAndAgreStcdIn(String utilUserId, List<String> agreementCode);


	
}
