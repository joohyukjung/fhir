package ca.uhn.fhir.jpa.starter.myhw.store.jpa;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnDynagreRepository;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnMdsidxRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.TfnMdsidxStore;
import ca.uhn.fhir.jpa.starter.myhw.store.mapper.TfnMdsidxMapper;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;

/**
 * @ClassName : TfnMdsidxJpaStore.java
 * @Description : E001 최근 방문일자 조회, E003 최근 방문 환자 인덱스 생성, 내부호출 조건조회
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

@Repository
public class TfnMdsidxJpaStore implements TfnMdsidxStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(TfnMdsidxJpaStore.class);

	@Autowired
	private TfnMdsidxRepository idxRepository;

	@Autowired
	private TfnDynagreRepository agreRepository;
	
	/**
	 * E001 최근 방문일자 조회:: 미입력 전체조회
	 * 활용동의 철회한 데이터는 조회되지 않음
	 * @param baseDate
	 * @return List<VisitPatientDatasetVO>
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectByBaseDate(String baseDate, List<String> agreUtilUserIdList) {
//
//		// 등록일자, 동의유저
//		List<TfnMdsidxTbo> tbos = idxRepository.findByRegYmdAndUtilUserIdIn(baseDate, agreUtilUserIdList); 
//		LOGGER.info("readAll tbos ::: {}", tbos);
//
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(tbos); 
//
//	}
		
	/**
	 * E001 최근 방문일자 조회 :: 입력 조건조회
	 * TODO 활용동의 철회한 데이터는 조회되지 않음
     * @param baseDate
	 * @param PvsnInstCd
	 * @return List<VisitPatientDatasetVO>
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectByBaseDateAndPvnsnInstCd(String baseDate, List<String> pvsnInstCd, List<String> agreUtilUserIdList) {
//
////		List<TfnMdsidxTbo> tbos = idxRepository.findByRegYmdAndPvsnInstCdIn(baseDate, pvsnInstCd); // 등록일자, 기관코드 조회
//		List<TfnMdsidxTbo> tbos = idxRepository.findByRegYmdAndPvsnInstCdInAndUtilUserIdIn(baseDate, pvsnInstCd, agreUtilUserIdList); // 등록일자, 기관코드 조회
//		LOGGER.info("readByCondition tbos ::: {}", tbos);
//		
//		// MAPSTRUCT
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(tbos); 
//		
//		// 원본
////		return tbos.stream().map(TfnMdsidxTbo::toDatasetVO).collect((Collectors.toList()));
//	}

	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 있을 경우
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectIndexByCondition(String userId, String utilizationServiceCode, List<String> provideInstitutionCode) {
//		
//		List<TfnMdsidxTbo> result = idxRepository.findAllByUtilUserIdAndUtilServiceCdAndPvsnInstCdIn(userId, utilizationServiceCode, provideInstitutionCode);
//		LOGGER.info("selectByUserId result ::: {}", result);
//		
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(result);
//	}
//	
	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * 기관코드 없을 경우
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectIndex(String userId, String utilizationServiceCode) {
//		
//		List<TfnMdsidxTbo> result = idxRepository.findAllByUtilUserIdAndUtilServiceCd(userId, utilizationServiceCode );
//		LOGGER.info("selectByUserId result ::: {}", result);
//
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(result);
//	}
	 
	
	
	/**
	 * E002 (가제)최근 방문일자 조회 사람
	 * 기관코드 없을 경우
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectByUserList(List<String> userList) {
//		List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdIn(userList);
//		
//		// MAPSTRUCT
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(tbos);
//		// 원본
////		return tbos.stream().map(TfnMdsidxTbo::toDatasetVO).collect((Collectors.toList()));
//
//	}
	
	/**
	 * E002 (가제)최근 방문일자 조회 사람
	 * 기관코드 있을 경우
	 */
//	@Override
//	public List<VisitPatientDatasetVO> selectByUserListAndPvsnInstCdList(List<String> userList,
//			List<String> pvsnInstCdList) {
//		
//		List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdInAndPvsnInstCdIn(userList, pvsnInstCdList);
//		
//		// MAPSTRUCT
//		return TfnMdsidxMapper.INSTANCE.tboListToVisitPatientDatasetVoList(tbos);
//		// 원본
////		return tbos.stream().map(TfnMdsidxTbo::toDatasetVO).collect((Collectors.toList()));
//	}

	
	/**
	 * 조건조회_fhir 1
	 * 기관코드 리스트
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
//	@Override
//	public List<FhirVisitPatientDatasetVO> selectFhirVisitPatientIndexByUtilUserIdAndPvsnInstCd(String utilUserId, List<String> pvsnInstCd) {
//
//		List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdAndPvsnInstCdIn(utilUserId, pvsnInstCd); // ID, 기관코드 조회
//		LOGGER.info("readByCondition tbos ::: {}", tbos);
//		
//		// mapstruct
////		return TfnMdsidxMapper.INSTANCE.tboListToFhirVisitPatientDatasetVoList(tbos); 
//		
//		// 원본
//		return tbos.stream().map(TfnMdsidxTbo::toOurDatasetVO).collect((Collectors.toList()));
//	}

	/**
	 * 조건조회_fhir 2
	 * 기관코드 
	 * @param utilUserId
	 * @param PvsnInstCd
	 * @return List<FhirVisitPatientDatasetVO>
	 */
//	@Override
//	public List<FhirVisitPatientDatasetVO> selectFhirVisitPatientIndexByUtilUserIdAndPvsnInstCd(String utilUserId,
//			String pvsnInstCd) {
//		
//		List<TfnMdsidxTbo> tbos = idxRepository.findByUtilUserIdAndPvsnInstCd(utilUserId, pvsnInstCd); // ID, 기관코드 조회
//		LOGGER.info("readByCondition tbos ::: {}", tbos);
//		
//		// mapstruct
//		return TfnMdsidxMapper.INSTANCE.tboListToFhirVisitPatientDatasetVoList(tbos); 
//
//	}

	
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 존재하면 update, 없으면 insert
	 * 
	 * @param tfnMdsidxTbo
	 * @return String
	 */
//	@Override
//	public String upsertVisitPatientIndex(TfnMdsidxTbo tfnMdsidxTbo) {
//		
//		idxRepository.save(tfnMdsidxTbo);
//		LOGGER.info("saveIndex finish");
//
//		return "1";
//	}
//
//	/**
//	 * E003 최근 방문 환자 인덱스 생성
//	 * 최근방문일자 rcbPrctYmd 조회
//	 */
//	@Override
//	public TfnMdsidxTbo selectRecentlyVisitDate(TfnMdsidxTbo tbo) {
//		String utilUserIdString = tbo.getUtilUserId();
//		String patId = tbo.getPatId();
//		String pvsnInstCd = tbo.getPvsnInstCd();
//		
//		TfnMdsidxTbo result = idxRepository.findByUtilUserIdAndPatIdAndPvsnInstCd(utilUserIdString, patId, pvsnInstCd);
//		
//		return result;
//	}
//
//	/**
//	 * E003 최근 방문 환자 인덱스 생성
//	 * 활용동의 상태 철회인 사용자를 제외하기 위한 목록 조회 
//	 */
//	@Override
//	public List<TfnDynagreTbo> selectUtilUserIdByUtilUserIdAndAgreementCode(String utilUserId, List<String> agreementCode) {
//
//		List<TfnDynagreTbo> tbos = agreRepository.findDistinctByUtilUserIdAndAgreStcdIn(utilUserId, agreementCode);
//
//		LOGGER.info("getUtilUserId tbos ::: {}", tbos);
//
//		return tbos;
//	}






}
