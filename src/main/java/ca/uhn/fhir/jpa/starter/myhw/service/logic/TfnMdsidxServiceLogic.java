package ca.uhn.fhir.jpa.starter.myhw.service.logic;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagreRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnMdsidxRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException;
import ca.uhn.fhir.jpa.starter.myhw.service.TfnMdsidxService;
import ca.uhn.fhir.jpa.starter.myhw.store.mapper.TfnMdsidxMapper;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.RecentlyVisitDatasearchVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : TfnMdsidxServiceLogic.java
 * @Description : 방문환자 API 서비스 로직
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
@RequiredArgsConstructor
@Service
public class TfnMdsidxServiceLogic implements TfnMdsidxService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TfnMdsidxServiceLogic.class);
	
	private final TfnDynagreRepository agreRepository;
	
	private final TfnMdsidxRepository idxRepository;
	
	/**
	 * E001 최근 방문일자 조회
	 * 기관코드 미존재, 기관코드 존재
	 * 활용동의 철회한 데이터는 조회되지 않음
	 * @param recentlyVisitDatasearchVO
	 * @return List<VisitPatientDatasetVO>
	 */
	@Override
	public List<VisitPatientDatasetVO> getVisitPatientIndex(RecentlyVisitDatasearchVO recentlyVisitDatasearchVO) {
		
		try {
			String baseDate = recentlyVisitDatasearchVO.getBaseDate(); // (필수)기준일자
			List<String> pvsnInstCd = recentlyVisitDatasearchVO.getProvideInstitutionCode(); // 제공기관코드
			List<String> agreUtilUserIdList = getAgreUserList(); // 활용동의한 MHW사용자ID 리스트
		
			// FIXME 서비스 구분이 아니라, 동적쿼리 생성으로 코드 개선되어야함 
			if (ValidationUtil.isListNotEmpty(pvsnInstCd)) {
				// 기관리스트 있을 때 :: 조건조회
				List<TfnMdsidxTbo> tbos = idxRepository.findByRegYmdAndPvsnInstCdInAndUtilUserIdIn(baseDate, pvsnInstCd, agreUtilUserIdList); // 등록일자, 기관코드 조회
				LOGGER.info("getVisitPatientIndex tbos ::: {}", tbos);
				return tbos.stream().map(tbo -> VisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
				
			} else {
				// 기관리스트 없을 때 :: 전체조회
				List<TfnMdsidxTbo> tbos = idxRepository.findByRegYmdAndUtilUserIdIn(baseDate, agreUtilUserIdList); 
				LOGGER.info("getVisitPatientIndex tbos ::: {}", tbos);
				return tbos.stream().map(tbo -> VisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 미존재, 기관코드 존재
	 * 활용동의 철회한 데이터는 조회되지 않음
	 */
	@Override
	public List<VisitPatientDatasetVO> getVisitPatientIndexByPatient(RecentlyVisitDatasearchVO recentlyVisitDatasearchVO) {
		try {
			String userId = recentlyVisitDatasearchVO.getUserId();
			String utilizationServiceCode = recentlyVisitDatasearchVO.getUtilizationServiceCode();
			List<String> provideInstitutionCode = recentlyVisitDatasearchVO.getProvideInstitutionCode();
			
			// FIXME 서비스 구분이 아니라, 동적쿼리 생성으로 코드 개선되어야함 
			if (ValidationUtil.isListNotEmpty(provideInstitutionCode)) {
				// 기관코드 존재
				List<TfnMdsidxTbo> tbos = idxRepository.findAllByUtilUserIdAndUtilServiceCdAndPvsnInstCdIn(userId, utilizationServiceCode, provideInstitutionCode);
				LOGGER.info("getVisitPatientIndexByPatient tbos ::: {}", tbos);
				return tbos.stream().map(tbo -> VisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
				
			} else {
				// 기관코드 미존재
				List<TfnMdsidxTbo> tbos = idxRepository.findAllByUtilUserIdAndUtilServiceCd(userId, utilizationServiceCode);
				LOGGER.info("getVisitPatientIndexByPatient result ::: {}", tbos);
				return tbos.stream().map(tbo -> VisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 활용동의 철회한 사용자에 대한 인덱스는 생성하지 않음
	 * 
	 * @param visitPatientIndexVO
	 * @return String
	 * @throws ParseException 
	 */
	@Override
	public List<VisitPatientDatasetVO> upsertVisitPatientIndex(VisitPatientIndexVO visitPatientIndexVO) throws Exception {
		try {			
			TfnMdsidxTbo mdsidxTbo = TfnMdsidxTbo.from(visitPatientIndexVO);
			// 인덱스 생성
			idxRepository.save(mdsidxTbo);
			
			// 결과
			List<TfnMdsidxTbo> tbos = idxRepository.findAllByUtilUserIdAndUtilServiceCdAndPvsnInstCdIn(mdsidxTbo.getUtilUserId(), mdsidxTbo.getUtilServiceCd(), Arrays.asList(mdsidxTbo.getPvsnInstCd()));
			LOGGER.info("upsertVisitPatientIndex result ::: {}", tbos);
			return tbos.stream().map(tbo -> VisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 내부호출 조건조회_fhir 1 리스트조회
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
	@Override
	public List<FhirVisitPatientDatasetVO> getFhirVisitPatientIndexByCondition(String utilUserId, List<String> pvsnInstCd) {

		List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdAndPvsnInstCdIn(utilUserId, pvsnInstCd); // ID, 기관코드 조회
		LOGGER.info("getFhirVisitPatientIndexByCondition tbos ::: {}", tbos);
		return tbos.stream().map(tbo -> FhirVisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
	}
	
	/**
	 * 내부호출 조건조회_fhir 2 단건조회
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
	@Override
	public FhirVisitPatientDatasetVO getFhirVisitPatientIndexByCondition(String utilUserId, String pvsnInstCd) {
		try {
			List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdAndPvsnInstCd(utilUserId, pvsnInstCd); // ID, 기관코드 조회
			LOGGER.info("getFhirVisitPatientIndexByCondition tbos ::: {}", tbos);
			
			List<FhirVisitPatientDatasetVO> VOList = tbos.stream().map(tbo -> FhirVisitPatientDatasetVO.from(tbo)).collect(Collectors.toList());
			
			if (ValidationUtil.isListNotEmpty(VOList)) {
				return VOList.get(0);
			} else {
				LOGGER.info("getFhirVisitPatientIndexByCondition VOList [] 빈 리스트입니다");
				return null;
			}
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * 활용동의한 MHW사용자ID 리스트 생성
	 * FIXME JOIN으로 대체하여 추후에는 없앨 것
	 */
	private List<String> getAgreUserList() {
		List<TfnDynagreTbo> agreTbos = agreRepository.findAllByAgreStcdIn(Arrays.asList(AgreementCode.AGREEMENT.getCode()));
		Set<String> agreUtilUserIdSet = new HashSet<String>();
		for(TfnDynagreTbo tbo : agreTbos) {
			agreUtilUserIdSet.add(tbo.getUtilUserId());
		} 
		return new ArrayList<>(agreUtilUserIdSet);
	}
	
}
