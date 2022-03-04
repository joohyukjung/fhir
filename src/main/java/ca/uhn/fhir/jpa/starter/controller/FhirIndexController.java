package ca.uhn.fhir.jpa.starter.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnMdsidxTbo;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.RecentlyVisitDatasearchVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;
import ca.uhn.fhir.to.BaseController;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirIndexController.java
 * @Description : 방문환자 API
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.17 이선민     인덱스 등록 수정
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
public class FhirIndexController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FhirIndexController.class);

	private final TfnMdsidxServiceLogic service;

	/**
	 * E001 최근 방문일자 조회
	 * @param recentlyVisitDatasearchVO
	 * @return List<VisitPatientDatasetVO>
	 */
	@PostMapping(value = "/visit")
	public ResponseEntity<List<VisitPatientDatasetVO>> getVisitPatientIndex(
			@Valid @RequestBody RecentlyVisitDatasearchVO recentlyVisitDatasearchVO) {
		return ResponseEntity.ok(service.getVisitPatientIndex(recentlyVisitDatasearchVO));
	}

	/**
	 * E002 사용자ID기반 최근 방문일자 조회
	 * @param recentlyVisitDatasearchVO
	 * @return
	 */
	@PostMapping(value = "/visit/patient")
	public ResponseEntity<List<VisitPatientDatasetVO>> getVisitPatientIndexByPatient(
			@Valid @RequestBody RecentlyVisitDatasearchVO recentlyVisitDatasearchVO) {
		return ResponseEntity.ok(service.getVisitPatientIndexByPatient(recentlyVisitDatasearchVO));
	}
	
	/**
	 * E003 최근 방문 환자 인덱스 생성
	 * 존재하면 update, 없으면 insert
	 * 활용동의 철회한 사용자에 대한 인덱스는 생성하지 않음
	 * @param visitPatientIndexVO
	 * @return String
	 * @throws Exception 
	 */
	@PostMapping(value = "/visit/index")
	public ResponseEntity<List<VisitPatientDatasetVO>> upsertVisitPatientIndex(
			@Valid @RequestBody VisitPatientIndexVO visitPatientIndexVO) throws Exception {
		return ResponseEntity.ok(service.upsertVisitPatientIndex(visitPatientIndexVO));
	}
	
//	@Autowired
//	private MessageSourceAccessor messageSourceAccessor;

//	// 메시지 테스트 start
////	String message = messageSourceAccessor.getMessage("AGRE0000"); // 메시지
////	LOGGER.info("getVisitPatientIndexByCondition message ::: {}", message);
////	if(result != null) {
////		throw ExMessageUtil.createMyhwBizException(AgreementExceptionConstants.AGRE_0000);
////	}
//	// 메시지 테스트 end		


	// E004 방문환자 배치상태 변경
		
}