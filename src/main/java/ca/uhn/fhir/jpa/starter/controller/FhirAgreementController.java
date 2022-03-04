package ca.uhn.fhir.jpa.starter.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.WithdrawRequestVO;
import ca.uhn.fhir.to.BaseController;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirAgreementController.java
 * @Description : 활용동의 API
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     활용동의 프로세스 변경(주민번호 암호화)
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */

@Validated
@RequiredArgsConstructor
@RestController
public class FhirAgreementController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FhirAgreementController.class);

	private final TfnDynagreServiceLogic tfnDynagreService;

	/**
	 * A001 활용동의 등록 
	 * @param agreementDatasetVoList
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/agre")
	public ResponseEntity<List<AgreementDatasetVO>> createAgreementData(@Valid @RequestBody List<AgreementDatasetVO> agreementDatasetVoList){
		return ResponseEntity.ok(tfnDynagreService.createAgreementData(agreementDatasetVoList));
	}
	
	/**
	 * A002 활용동의 변경
	 * @param agreementDatasetVoList
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value="/agre")
	public ResponseEntity<List<AgreementDatasetVO>> updateAgreementData(@Valid @RequestBody List<AgreementDatasetVO> agreementDatasetVoList) throws Exception {
		return ResponseEntity.ok(tfnDynagreService.updateAgreementData(agreementDatasetVoList));
	}

	/**
	 * A003 활용동의 철회
	 * @param withdrawRequestVO
	 * @return
	 * @throws InvalidKeySpecException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@PostMapping(value="/whdw")
	public ResponseEntity<List<AgreementDatasetVO>> deleteAgreementData(@Valid @RequestBody WithdrawRequestVO withdrawRequestVO) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		return ResponseEntity.ok(tfnDynagreService.deleteAgreementData(withdrawRequestVO));
	}

	
	/**
	 * A004 활용동의 조회
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
	@PostMapping(value = "/agre/patient")
	public ResponseEntity<List<AgreementDatasetVO>> getAgreementDataset(@Valid @RequestBody UserAgreementVO userAgreementVO) {
		return ResponseEntity.ok(tfnDynagreService.getAgreementDatasetByCondition(userAgreementVO));
	}

}
