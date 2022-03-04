package ca.uhn.fhir.jpa.starter.myhw.service.logic;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagreRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagrehistRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnExtractRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnPnstkeyRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagrehistTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnExtractTbo;
import ca.uhn.fhir.jpa.starter.myhw.service.TfnDynagreService;
import ca.uhn.fhir.jpa.starter.myhw.store.TfnRsapubkeyStore;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.util.MyhwSeedEcbUtil;
import ca.uhn.fhir.jpa.starter.util.ObjectUtil;
import ca.uhn.fhir.jpa.starter.util.RSAUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.RsaVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.TfnPnstkeyDto;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.WithdrawRequestVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : TfnDynagreServiceLogic.java
 * @Description : 활용동의 API
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     활용동의 프로세스 변경(주민번호 암호화)
 * 2022.02.18 이선민     활용동의 등록시 암호화된 주민번호 적재
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
@RequiredArgsConstructor
@Service
public class TfnDynagreServiceLogic implements TfnDynagreService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TfnDynagreServiceLogic.class);
 
	private final TfnRsapubkeyStore tfnRsapubkeyStore;
	
	private final TfnExtractRepository extractRepository;
	
	private final TfnDynagreRepository agreRepository;
	
	private final TfnDynagrehistRepository dynagrehistRepository;

	private final TfnPnstkeyRepository pnstKeyRepository;
	
	/**
	 * A001 활용동의 등록 
	 * @param agreementDatasetVO
	 * @return String
	 */
	@Override
	public List<AgreementDatasetVO> createAgreementData(List<AgreementDatasetVO> agreementDatasetVoList){
		try {
			LOGGER.info(" createAgreementData 주민번호 암호화 PARAM 확인::: {}", agreementDatasetVoList);

			// 등록 전 MHW사용자ID에 대한 기존 정보 삭제
			agreRepository.deleteAllByUserIdInQuery(agreementDatasetVoList.get(0).getUserId());
			
			// 활용동의 등록
			for (AgreementDatasetVO vo : agreementDatasetVoList) {
				TfnDynagreTbo dynagreTbo = getAgreDynagreTbo(vo);
				LOGGER.info(" tfnDynagreTbo ::: {}", dynagreTbo);
				agreRepository.save(dynagreTbo);

				// 활용동의 이력 테이블
				createDynagrehist(vo);
			}
			// 추출대상 테이블 
			createExtract(agreementDatasetVoList.get(0));

			// 결과
			List<TfnDynagreTbo> result = agreRepository.findByAgreStcdAndUtilUserIdAndAgreStcdIn(AgreementCode.AGREEMENT.getCode(), agreementDatasetVoList.get(0).getUserId(), Arrays.asList(AgreementCode.AGREEMENT.getCode()));
			return result.stream().map(tbo -> AgreementDatasetVO.from(tbo)).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}	
	}

	/**
	 * A002 활용동의 변경
	 * FIXME 수정하기
	 * @param agreementDatasetVO
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<AgreementDatasetVO> updateAgreementData(List<AgreementDatasetVO> agreementDatasetVoList) throws Exception { 
		
		try {
			LOGGER.info(" updateAgreementData 주민번호 암호화 PARAM 확인::: {}", agreementDatasetVoList);
			
			String userId = agreementDatasetVoList.get(0).getUserId(); // MHW사용자ID

			// 파라미터로 받은 VOList에서 코드list생성
			List<String> paramCodeList = new ArrayList<String>();
			List<String> paramWithdrawDateList = new ArrayList<String>();
			for(AgreementDatasetVO vo: agreementDatasetVoList) {
				paramCodeList.add(vo.getDatasetCode());
				paramWithdrawDateList.add(vo.getAgreementDateTime());
			}
			Collections.sort(paramWithdrawDateList, Collections.reverseOrder());		
			String withdrawDate = paramWithdrawDateList.get(0); // 철회변경일시 // vo get테스트

			// MHW사용자ID로 조회한 데이터에 대한 코드LIST생성
			List<AgreementDatasetVO> exsistAgreDataVOList = agreRepository.findAllByUtilUserId(userId).stream().map(AgreementDatasetVO :: from).collect(Collectors.toList());

			List<String> exsistCodeList = new ArrayList<String>();
			for(AgreementDatasetVO vo: exsistAgreDataVOList) {
				exsistCodeList.add(vo.getDatasetCode());
			}
//			LOGGER.info(" updateAgreementData exsistAgreDataVOList ::: {}", exsistAgreDataVOList);
//			LOGGER.info(" updateAgreementData exsistCodeList ::: {}", exsistCodeList);
			
			// 기존 사용자 활용동의 데이터에서 동의/철회 상태별로 VO리스트 생성
			List<AgreementDatasetVO> agreeVOList = new ArrayList<AgreementDatasetVO>(); // 동의 리스트
			List<AgreementDatasetVO> withdrawVOList = new ArrayList<AgreementDatasetVO>(); // 철회 리스트
			exsistAgreDataVOList.stream().forEach(vo -> {
				if(vo.getAgreementStatus().equals(AgreementCode.AGREEMENT.getCode())) { 
					agreeVOList.add(vo); // 동의
				} else {
					withdrawVOList.add(vo); // 철회
				}
			});
//			LOGGER.info(" updateAgreementData agreeVOList ::: {}", agreeVOList);
//			LOGGER.info(" updateAgreementData withdrawVOList ::: {}", withdrawVOList);
			
			// 동의/철회 상태에 따라 각각의 로직 수행
			List<AgreementDatasetVO> updateAgreementVOList = new ArrayList<AgreementDatasetVO>(); // 동의상태로 변경할 데이터셋 VO리스트
			List<AgreementDatasetVO> updateWithdrawVOList = new ArrayList<AgreementDatasetVO>();  // 철회상태로 변경할 데이터셋 VO리스트
			List<AgreementDatasetVO> insertVOList = new ArrayList<AgreementDatasetVO>();          // 새로 추가된 데이터셋 VO리스트
			List<AgreementDatasetVO> removeList = new ArrayList<AgreementDatasetVO>();
			if (ValidationUtil.isListNotEmpty(agreeVOList)) {
				// 1. 동의상태 :: 1) updateAgree  
				for(String paramCode : paramCodeList) {
					removeList.addAll(agreeVOList.stream().filter(vo -> vo.getDatasetCode().equals(paramCode)).collect(Collectors.toList()));
				}
				// 1. 동의상태 :: 2) updateWithdraw  
				agreeVOList.removeAll(removeList);
				updateWithdrawVOList.addAll(agreeVOList);
				
			}
			if (ValidationUtil.isListNotEmpty(withdrawVOList)) {
				// 2. 철회상태 :: 1) updateAgree
				for(String paramCode : paramCodeList) {
					updateAgreementVOList.addAll(withdrawVOList.stream().filter(vo -> vo.getDatasetCode().equals(paramCode)).collect(Collectors.toList()));
				}
				
				// 2. 철회상태 :: 2) updateWithdraw
				updateWithdrawVOList.addAll(withdrawVOList.stream().filter(vo -> !paramCodeList.contains(vo.getDatasetCode())).collect(Collectors.toList()));
				
			}
//			LOGGER.info(" updateAgreementData updateAgreementVOList ::: {}", updateAgreementVOList);
//			LOGGER.info(" updateAgreementData updateWithdrawVOList ::: {}", updateWithdrawVOList);

			// update agree
			if (ValidationUtil.isListNotEmpty(updateAgreementVOList)) {
				List<AgreementDatasetVO> updateAgreementVOTempList = new ArrayList<AgreementDatasetVO>();
				for (AgreementDatasetVO paramVO : updateAgreementVOList) {
					updateAgreementVOTempList.addAll(agreementDatasetVoList.stream().filter(vo -> vo.getDatasetCode().equals(paramVO.getDatasetCode())).collect(Collectors.toList()));
				}

				for (AgreementDatasetVO vo : updateAgreementVOTempList) {
					TfnDynagreTbo dynagreTbo = getAgreDynagreTbo(vo);
					agreRepository.updateAgreementQuery(dynagreTbo.getAgreDt(), dynagreTbo.getUtilUserId(), dynagreTbo.getDtstCd(), AgreementCode.AGREEMENT.getCode()); // vo get테스트
					
					// 활용동의 이력 테이블
					createDynagrehist(vo); // vo get테스트
				}

			}
			// update Withdraw
			if (ValidationUtil.isListNotEmpty(updateWithdrawVOList)) {			
				for (AgreementDatasetVO vo :updateWithdrawVOList) {
					TfnDynagreTbo dynagreTbo = getWhdwDynagreTbo(vo, withdrawDate);
					agreRepository.updateWithdrawQuery(dynagreTbo.getAgreWhdwDt(), dynagreTbo.getUtilUserId(), dynagreTbo.getDtstCd(), AgreementCode.WITHDRAW.getCode());
					
					// 활용동의 이력 테이블
					createDynagrehist(vo, withdrawDate);
				}
			}

			// 3. INSERT
			paramCodeList.removeAll(exsistCodeList);
			for(String paramCode : paramCodeList) {
				insertVOList.addAll(agreementDatasetVoList.stream().filter(vo -> vo.getDatasetCode().equals(paramCode)).collect(Collectors.toList()));
			}

			for (AgreementDatasetVO vo : insertVOList) {
				TfnDynagreTbo dynagreTbo = getAgreDynagreTbo(vo);
				LOGGER.info(" tfnDynagreTbo ::: {}", dynagreTbo);
				agreRepository.save(dynagreTbo);

				// 활용동의 이력 테이블
				createDynagrehist(vo);
			}
			// 결과
			List<TfnDynagreTbo> result = agreRepository.findByUtilUserIdAndUtilServiceCd(agreementDatasetVoList.get(0).getUserId(), agreementDatasetVoList.get(0).getUtilizationServiceCode());
			return result.stream().map(tbo -> AgreementDatasetVO.from(tbo)).collect(Collectors.toList());
			
		} catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
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
	public List<AgreementDatasetVO> deleteAgreementData(WithdrawRequestVO withdrawRequestVO) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		try {
			LOGGER.info(" deleteAgreementData 주민번호 암호화 PARAM 확인::: {}", withdrawRequestVO);
			
			// MHW사용자ID로 모든 데이터셋 리스트 생성
			List<AgreementDatasetVO> exsistAgreDataVOList = agreRepository.findAllByUtilUserId(withdrawRequestVO.getUserId()).stream().map(AgreementDatasetVO :: from).collect(Collectors.toList());

			// MHW사용자ID로 모든 데이터셋에 대한 상태코드 철회로 변경
			for (AgreementDatasetVO vo : exsistAgreDataVOList) {
				TfnDynagreTbo dynagreTbo = getWhdwDynagreTbo(vo, withdrawRequestVO.getWithdrawDateTime());
				agreRepository.updateAgreStcdByUtilUserIdInQuery(dynagreTbo.getUtilUserId(), dynagreTbo.getAgreWhdwDt(), AgreementCode.WITHDRAW.getCode(), dynagreTbo.getDtstCd(), dynagreTbo.getUtilServiceCd());

				// 활용동의 이력 테이블
				createDynagrehist(vo, withdrawRequestVO.getWithdrawDateTime());
			}
			// 추출대상 테이블 
			createExtract(withdrawRequestVO);

			// 결과
			List<TfnDynagreTbo> result = agreRepository.findByAgreStcdAndUtilUserIdAndAgreStcdIn(AgreementCode.WITHDRAW.getCode(), withdrawRequestVO.getUserId(), Arrays.asList(AgreementCode.WITHDRAW.getCode()));
			return result.stream().map(tbo -> AgreementDatasetVO.from(tbo)).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * A004 활용동의 조회
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
	@Override
	public List<AgreementDatasetVO> getAgreementDatasetByCondition(UserAgreementVO userAgreementVO) {

		List<String> agreementCode = Arrays.asList(AgreementCode.AGREEMENT.getCode());
		
		try {
			if (ObjectUtil.isEmptyObject(userAgreementVO)) {
				// 전체 조회 : 조회 조건 없을 경우
				LOGGER.info(" getAgreementDatasetByCondition 전체조회 : 조회 조건 없을 경우");
				
				return agreRepository.findAllByAgreStcdIn(agreementCode).stream().map(tbo -> AgreementDatasetVO.from(tbo)).collect(Collectors.toList());
			} else {
				// 조건 조회 : 조회조건 있을 경우
				LOGGER.info(" getAgreementDatasetByCondition 조건 조회 : 조회조건 있을 경우");
				return agreRepository.findByAgreStcdAndUtilUserIdAndAgreStcdIn(userAgreementVO.getAgreementStatus(), userAgreementVO.getUserId(), agreementCode).stream().map(tbo -> AgreementDatasetVO.from(tbo)).collect(Collectors.toList());
			}
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
	
	////////////////////////////////////////////////////////////////////////////////////
	
	// 동의 tbo 생성
	private TfnDynagreTbo getAgreDynagreTbo(AgreementDatasetVO vo) throws InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException, ParseException {
		TfnDynagreTbo dynagreTbo = TfnDynagreTbo.builder()
				.utilUserId(vo.getUserId())
				.utilServiceCd(vo.getUtilizationServiceCode())
				.agreStcd(vo.getAgreementStatus())
				.agreDt(vo.getAgreementDateTime())
				.dtstCd(vo.getDatasetCode())
				.dtstNm(vo.getDatasetName())
				.build();
		return dynagreTbo;
	}
	
	
	// 철회 tbo 생성
	private TfnDynagreTbo getWhdwDynagreTbo(AgreementDatasetVO vo, String withdrawDateTime) throws InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException, ParseException {
		TfnDynagreTbo dynagreTbo = TfnDynagreTbo.builder()
				.utilUserId(vo.getUserId())
				.utilServiceCd(vo.getUtilizationServiceCode())
				.agreStcd(vo.getAgreementStatus())
				.agreWhdwDt(withdrawDateTime)
				.dtstCd(vo.getDatasetCode())
				.dtstNm(vo.getDatasetName())
				.build();
		return dynagreTbo;
	}
	
	
	// 활용동의 이력테이블 - 동의
	private void createDynagrehist(AgreementDatasetVO vo) throws ParseException, InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException {
			TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
					.utilUserId(vo.getUserId())
					.utilServiceCd(vo.getUtilizationServiceCode())
					.dtstCd(vo.getDatasetCode())
					.dtstNm(DatasetCode.find(vo.getDatasetCode()).getDatasetKorName())
					.agreStcd(AgreementCode.AGREEMENT.getCode())
					.agreDt(vo.getAgreementDateTime())
					.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
					.build();
			dynagrehistRepository.save(tbo);
			LOGGER.info(" dynagrehistRepository동의 tbo ::: {}", tbo);
	}

	// 활용동의 이력테이블 - 철회			
	private void createDynagrehist(AgreementDatasetVO vo, String withdrawDateTime) throws InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException, ParseException {
		TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
				.utilUserId(vo.getUserId())
				.utilServiceCd(vo.getUtilizationServiceCode())
				.dtstCd(vo.getDatasetCode())
				.dtstNm(DatasetCode.find(vo.getDatasetCode()).getDatasetKorName())
				.agreStcd(AgreementCode.WITHDRAW.getCode())
				.agreWhdwDt(withdrawDateTime) 
				.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
				.build();
		dynagrehistRepository.save(tbo);
		LOGGER.info(" dynagrehistRepository철회 tbo ::: {}", tbo);
	}

	// 추출대상 테이블 - 동의
	private void createExtract(AgreementDatasetVO agreementDatasetVO) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// 복호화
		TfnPnstkeyDto dto = TfnPnstkeyDto.from(pnstKeyRepository.findAll().get(0));
		String unpackedRrno = MyhwSeedEcbUtil.SEED_ECB_Decrypt(dto.getSecKeyVal(),
				agreementDatasetVO.getResidentRegistrationNumber());

		List<RsaVO> rsaVOList = tfnRsapubkeyStore.selectAllKey();
		LOGGER.info(" createExtract rsaVOList ::: {}", rsaVOList);
		
		for (RsaVO vo : rsaVOList) {
			TfnExtractTbo tbo = TfnExtractTbo.builder()
					.utilUserId(agreementDatasetVO.getUserId())
					.utilServiceCd(agreementDatasetVO.getUtilizationServiceCode())
					.pvsnInstCd(vo.getProvideInstitutionCode())
//					.rrno(RSAUtil.encryptRSA(agreementDatasetVO.getResidentRegistrationNumber(), vo.getPubKey()))
					.rrno(RSAUtil.encryptRSA(unpackedRrno, vo.getPubKey()))
					.agreStcd(AgreementCode.AGREEMENT.getCode())
					.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
					.build();
			extractRepository.save(tbo);
			LOGGER.info(" createExtract tbo ::: {}", tbo);
		}
	}

	
	// 추출대상 테이블 - 철회	
	private void createExtract(WithdrawRequestVO withdrawRequestVO) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// 복호화
		TfnPnstkeyDto dto = TfnPnstkeyDto.from(pnstKeyRepository.findAll().get(0));
		String unpackedRrno = MyhwSeedEcbUtil.SEED_ECB_Decrypt(dto.getSecKeyVal(),
				withdrawRequestVO.getResidentRegistrationNumber());
		
		List<RsaVO> rsaVOList = tfnRsapubkeyStore.selectAllKey(); 
		LOGGER.info(" createExtract rsaVOList ::: {}", rsaVOList);

		for (RsaVO vo : rsaVOList) {			
			TfnExtractTbo tbo = TfnExtractTbo.builder()
					.utilUserId(withdrawRequestVO.getUserId())
					.utilServiceCd(withdrawRequestVO.getUtilizationServiceCode())
					.pvsnInstCd(vo.getProvideInstitutionCode())
//					.rrno(RSAUtil.encryptRSA(withdrawRequestVO.getResidentRegistrationNumber(), vo.getPubKey()))
					.rrno(RSAUtil.encryptRSA(unpackedRrno, vo.getPubKey()))
					.agreStcd(AgreementCode.WITHDRAW.getCode())
					.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
					.build();
			LOGGER.info(" createExtract tbo ::: {}", tbo);
			extractRepository.save(tbo);			
		}
	}
}
