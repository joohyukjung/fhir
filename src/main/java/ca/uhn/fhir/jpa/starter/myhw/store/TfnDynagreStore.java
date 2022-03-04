package ca.uhn.fhir.jpa.starter.myhw.store;

import java.util.List;

import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.WithdrawRequestVO;

/**
 * @InterfaceName : TfnDynagreStore.java
 * @Description : A004 활용동의 조회, E003 최근 방문 환자 인덱스 생성
 * @Modification
 * 
 *                <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     활용동의 프로세스 변경(주민번호 암호화)
 *               </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
public interface TfnDynagreStore {

	/**
	 * A001 활용동의 등록, A002 활용동의 변경
	 * @param agreementDatasetVO
	 * @return String
	 * @throws Exception 
	 */
//	void insertAgreementData(AgreementDatasetVO agreementDatasetVO) throws Exception;
	
	/**
	 * A001 활용동의 등록 , A002 활용동의 변경
	 * @param agreementDatasetVO
	 * @return int
	 */
//	int deleteAgreementData(String userId);
	
	/**
	 * A002 활용동의 변경
	 * MHW사용자ID로 조회
	 * @return
	 */
//	List<AgreementDatasetVO> selectUserList(String userId);
	
	/**
	 * 
	 * MHW사용자ID, 동의상태로 조회
	 * @param agreementStatus
	 * @return
	 */
//	List<AgreementDatasetVO> getUserListByUserIdAndAgreementStatus(String userId, String agreementStatus);
	
	/**
	 * A002 활용동의 변경
	 * 동의 상태로 변경
	 * 업데이트 : 동의상태(동의), 동의일자, 철회일자
	 * @return
	 */
//	int updateAgreement(AgreementDatasetVO agreementDatasetVO, String agreDt);
	
	/**
	 * A002 활용동의 변경
	 * 철회 상태로 변경
	 * 업데이트 : 동의상태(동의), 동의일자, 철회일자
	 * @return
	 */
//	int updateWithdraw(AgreementDatasetVO agreementDatasetVO, String agreWhdwDt);
	
	
	/**
	 * A003 활용동의 철회
	 * @param withdrawRequestVO
	 * @return String
	 */
//	String deleteWithdraw(WithdrawRequestVO withdrawRequestVO, String dtstCd);
	
	 
	/**
	 * A004 활용동의 조회
	 * 전체 조회 : 조회조건 없음
	 * @param userAgreementVO
	 * @param agreementCode
	 * @return
	 */
//	List<AgreementDatasetVO> selectAgreementDatasetAll(UserAgreementVO userAgreementVO, List<String> agreementCode);
	
	/**
	 * A004 활용동의 조회
	 * 조건 조회 : 조회조건 있음
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
//	List<AgreementDatasetVO> selectAgreementDatasetByCondition(UserAgreementVO userAgreementVO, List<String> agreementCode);
	
	/**
	 * A004 활용동의 조회
	 * 활용동의한 MHW사용자ID 리스트 생성
	 * TODO get -> select로 변경하기
	 * @return
	 */
//	List<String> getAgreUserList();
}
