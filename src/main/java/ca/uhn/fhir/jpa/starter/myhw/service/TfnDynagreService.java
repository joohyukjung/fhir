package ca.uhn.fhir.jpa.starter.myhw.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.WithdrawRequestVO;

/**
 * @InterfaceName : TfnDynagreService.java
 * @Description : 활용동의 조회, 최근 방문 환자 인덱스 생성
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
public interface TfnDynagreService {

	/**
	 * A001 활용동의 등록 
	 * @param agreementDatasetVO
	 * @return String
	 * @throws Exception 
	 */
	List<AgreementDatasetVO> createAgreementData(List<AgreementDatasetVO> agreementDatasetVoList) throws Exception;
	
	/**
	 * A002 활용동의 변경
	 * @param agreementDatasetVO
	 * @return String
	 * @throws Exception 
	 */
	List<AgreementDatasetVO> updateAgreementData(List<AgreementDatasetVO> agreementDatasetVoList) throws Exception;
	
	/**
	 * A003 활용동의 철회
	 * @param withdrawRequestVO
	 * @return String
	 * @throws InvalidKeySpecException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	List<AgreementDatasetVO> deleteAgreementData(WithdrawRequestVO withdrawRequestVO) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException;
		
	/**
	 * A004 활용동의 조회
	 * 조회 조건 없을 시 전체조회
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
	List<AgreementDatasetVO> getAgreementDatasetByCondition(UserAgreementVO userAgreementVO);
	
	

}
