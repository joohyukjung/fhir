package ca.uhn.fhir.jpa.starter.myhw.store.jpa;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagreRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagrehistRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagrehistTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.TfnDynagreStore;
import ca.uhn.fhir.jpa.starter.myhw.store.mapper.TfnDynagreMapper;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.WithdrawRequestVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : TfnDynagreJpaStore.java
 * @Description : A004 활용동의 조회, E003 최근 방문 환자 인덱스 생성
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
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

@RequiredArgsConstructor
@Repository
public class TfnDynagreJpaStore implements TfnDynagreStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(TfnDynagreJpaStore.class);

	@Autowired
	private TfnDynagreRepository agreRepository;

	private final TfnDynagrehistRepository dynagrehistRepository;
	
	/**
	 * A001 활용동의 등록, A002 활용동의 변경
	 * @param agreementDatasetVO
	 * @return String
	 * @throws Exception 
	 * @throws ParseException
	 */
//	@Override
//	public void insertAgreementData(AgreementDatasetVO vo) throws Exception {
//
//		TfnDynagreTbo tfnDynagreTbo = TfnDynagreMapper.INSTANCE.agreementDatasetVoToTbo(vo);
//
//		LOGGER.info(" tfnDynagreTbo ::: {}", tfnDynagreTbo);
//
//		agreRepository.save(tfnDynagreTbo);
//
//		// 활용동의 이력 테이블
//		TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
//				.utilUserId(vo.getUserId())
//				.utilServiceCd(vo.getUtilizationServiceCode())
//				.dtstCd(vo.getDatasetCode())
//				.dtstNm(vo.getDatasetName())
//				.agreStcd(AgreementCode.AGREEMENT.getCode())
//				.agreDt(vo.getAgreementDateTime())
//				.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
//				.build();
//		dynagrehistRepository.save(tbo);
//		LOGGER.info(" dynagrehistRepository tbo ::: {}", tbo);
//		
//	}

	/**
	 * A001 활용동의 등록 , A002 활용동의 변경
	 * @param userId, residentRegistrationNumber
	 * @return int
	 */
//	@Override
//	public int deleteAgreementData(String userId) {
//		LOGGER.info(" deleteAgreementData ::: DELETE START");
//		int result = agreRepository.deleteAllByUserIdInQuery(userId);
//		LOGGER.info(" deleteAgreementData ::: DELETE END result == {}", result);
//		
//		return result;
//	}

	/**
	 * A002 활용동의 변경
	 * MHW사용자ID로 조회
	 */
//	@Override
//	public List<AgreementDatasetVO> selectUserList(String userId) {
//		
//		List<TfnDynagreTbo> tbos = agreRepository.findAllByUtilUserId(userId);
//		LOGGER.info(" selectUserList ::: tbos == {}", tbos);
//		
//		List<AgreementDatasetVO> result = TfnDynagreMapper.INSTANCE.tboListToAgreementDatasetVoList(tbos); // MAPSTRUCT
//		LOGGER.info(" selectUserList ::: result == {}", result);
//		
//		return result;
//	}

	/**
	 * MHW사용자ID, 동의상태로 조회
	 */
//	@Override
//	public List<AgreementDatasetVO> getUserListByUserIdAndAgreementStatus(String userId, String agreementStatus) {
//
//		List<TfnDynagreTbo> tbos = agreRepository.findAllByUtilUserIdAndAgreStcd(userId, agreementStatus);
//		LOGGER.info(" getUserList ::: tbos == {}", tbos);
//		
//		List<AgreementDatasetVO> result = TfnDynagreMapper.INSTANCE.tboListToAgreementDatasetVoList(tbos); // MAPSTRUCT
//		LOGGER.info(" getUserList ::: result == {}", result);
//		
//		return result;
//	}
	
	/**
	 * A002 활용동의 변경
	 * 동의로 업데이트 : 동의상태(동의), 동의일자, 철회일자
	 * @return
	 */
//	@Override
//	public int updateAgreement(AgreementDatasetVO vo, String agreDt) {
//		String utilUserId = vo.getUserId();
//		String dtstCd = vo.getDatasetCode();
//		String agreStcd = AgreementCode.AGREEMENT.getCode();
//		
//		int result = agreRepository.updateAgreementQuery(agreDt, utilUserId, dtstCd, agreStcd);
//		LOGGER.info(" updateAgreement ::: 데이터셋코드 {} 동의 ", dtstCd);
//		
//		// 활용동의 이력 테이블
//		TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
//				.utilUserId(vo.getUserId())
//				.utilServiceCd(vo.getUtilizationServiceCode())
//				.dtstCd(vo.getDatasetCode())
//				.dtstNm(vo.getDatasetName())
//				.agreStcd(agreStcd)
//				.agreDt(agreDt)
//				.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
//				.build();
//		dynagrehistRepository.save(tbo);
//		LOGGER.info(" dynagrehistRepository tbo ::: {}", tbo);
//		
//		return result;
//	}

	/**
	 * A002 활용동의 변경
	 * 철회로 업데이트 : 동의상태(동의), 동의일자, 철회일자
	 * @return
	 */
//	@Override
//	public int updateWithdraw(AgreementDatasetVO vo, String agreWhdwDt) {
//		String utilUserId = vo.getUserId();
//		String dtstCd = vo.getDatasetCode();
//		String agreStcd = AgreementCode.WITHDRAW.getCode();
//		
//		int result = agreRepository.updateWithdrawQuery(agreWhdwDt, utilUserId, dtstCd, agreStcd);
//		LOGGER.info(" updateAgreement ::: 데이터셋코드 {} 철회 ", dtstCd);
//		
//		// 활용동의 이력 테이블
//		TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
//				.utilUserId(vo.getUserId())
//				.utilServiceCd(vo.getUtilizationServiceCode())
//				.dtstCd(vo.getDatasetCode())
//				.dtstNm(vo.getDatasetName())
//				.agreStcd(agreStcd)
//				.agreWhdwDt(agreWhdwDt)
//				.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
//				.build();
//		dynagrehistRepository.save(tbo);
//		LOGGER.info(" dynagrehistRepository tbo ::: {}", tbo);
//		
//		
//		return result;
//	}
	
	
	/**
	 * A003 활용동의 철회
	 * @param withdrawRequestVO
	 * @return String
	 */
//	@Override
//	public String deleteWithdraw(WithdrawRequestVO vo, String dtstCd) {
//		
//		String userId = vo.getUserId();
//		String withdrawDateTime = vo.getWithdrawDateTime();
//		String withdrawCode = AgreementCode.WITHDRAW.getCode();
//		String utilizationServiceCode = vo.getUtilizationServiceCode();
//		
//		int result = agreRepository.updateAgreStcdByUtilUserIdInQuery(userId, withdrawDateTime, withdrawCode, dtstCd, utilizationServiceCode);
//		
//		LOGGER.info(" updateAgreementData ::: update END result == {}", result);
//		LOGGER.info(" ::::::: dataset korname == {}", DatasetCode.find(dtstCd).getDatasetKorName());
//		
//		// 활용동의 이력 테이블
//		TfnDynagrehistTbo tbo = TfnDynagrehistTbo.builder()
//				.utilUserId(vo.getUserId())
//				.utilServiceCd(vo.getUtilizationServiceCode())
//				.dtstCd(dtstCd)
//				.dtstNm(DatasetCode.find(dtstCd).getDatasetKorName())
//				.agreStcd(AgreementCode.WITHDRAW.getCode())
//				.agreWhdwDt(vo.getWithdrawDateTime())
//				.regDt(DateUtil.getDateStringNow("yyyyMMddHHmmss"))
//				.build();
//		dynagrehistRepository.save(tbo);
//		LOGGER.info(" dynagrehistRepository tbo ::: {}", tbo);
//		
//		
//		return "1";
//	}

	/**
	 * A004 활용동의 조회
	 * 전체 조회 : 조회조건 없음
	 * 
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
//	@Override
//	public List<AgreementDatasetVO> selectAgreementDatasetAll(UserAgreementVO userAgreementVO, List<String> agreementCode) {
//		// TODO 1개 조건만 입력될 경우 에러 뱉도록 정합성체크 필요
//		LOGGER.info(" userAgreementVO ::: {}", userAgreementVO);
//
//		List<TfnDynagreTbo> tbos = agreRepository.findAllByAgreStcdIn(agreementCode);
//
//		return TfnDynagreMapper.INSTANCE.tboListToAgreementDatasetVoList(tbos); // MAPSTRUCT
//
//	}

	/**
	 * A004 활용동의 조회
	 * 조건 조회 : 조회조건 있음
	 * @param userAgreementVO
	 * @return List<AgreementDatasetVO>
	 */
//	@Override
//	public List<AgreementDatasetVO> selectAgreementDatasetByCondition(UserAgreementVO userAgreementVO, List<String> agreementCode) {
//		// TODO 1개 조건만 입력될 경우 에러 뱉도록 정합성체크 필요
//		LOGGER.info(" userAgreementVO ::: {}", userAgreementVO);
//
//		// 조회조건 있을 경우 :: 조건 조회
//		List<TfnDynagreTbo> tbos = agreRepository.findByAgreStcdAndUtilUserIdAndAgreStcdIn(userAgreementVO.getAgreementStatus(), userAgreementVO.getUserId(), agreementCode);
//
//		return TfnDynagreMapper.INSTANCE.tboListToAgreementDatasetVoList(tbos); // MAPSTRUCT
//
//	}
	
	
//	/**
//	 * 활용동의한 MHW사용자ID 리스트 생성
//	 */
//	public List<String> getAgreUserList() {
//		List<String> agreementCode = Arrays.asList(AgreementCode.AGREEMENT.getCode());
//		List<TfnDynagreTbo> agreTbos = agreRepository.findAllByAgreStcdIn(agreementCode);
//		Set<String> agreUtilUserIdSet = new HashSet<String>();
//		for(TfnDynagreTbo tbo : agreTbos) {
//			agreUtilUserIdSet.add(tbo.getUtilUserId());
//		}
//		List<String> agreUtilUserIdList = new ArrayList<>(agreUtilUserIdSet);
//
//		return agreUtilUserIdList;
//	}



}
