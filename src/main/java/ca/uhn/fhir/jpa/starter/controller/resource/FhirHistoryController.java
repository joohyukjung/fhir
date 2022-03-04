package ca.uhn.fhir.jpa.starter.controller.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.ConditionService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.MedicationRequestService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.ProcedureService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.code.InquiryPeriodSearchCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourceListVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirHistoryController.java
 * @Description : 의료데이터 조회(mediDataLinkService)
 * @Modification
 * 
 *               <pre>
 *  수정일		수정자	수정내용
 * ---------- ------- ------------------------
 * 2022.01.24	최철호	최초작성
 * 2022.02.07	최철호	Response Data 수정
 * 2022.03.02	최철호	Resource Service 적용
 *               </pre>
 * 
 * @author Cheol-Ho Choi
 * @since 2022-03-02
 * @version 1.0
 */

@Validated
@RestController
@RequiredArgsConstructor
public class FhirHistoryController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FhirHistoryController.class);

	private final TfnDynagreServiceLogic agreementService;
	private final ConditionService conditionService;
	private final MedicationRequestService medicationRequestService;
	private final ProcedureService procedureService;

	private CaptureInterceptor interceptor = new CaptureInterceptor();

	/**
	 * 내역정보 조회 API - 진단내역 조회(getConditionList)
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/condition")
	public List<Map<String, Object>> getConditionList(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition) throws Exception {

		LOGGER.info("FhirHistoryController getConditionList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirHistoryController getConditionList condition :::: {}", JsonUtil.toJsonString(condition));
		
		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.CONDITION.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.CONDITION);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return conditionService.getResourceList(client, parser, condition);
	}

	/**
	 * 내역정보 조회 API - 약물처방내역 조회(getMedicationRequestList)
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/medicationRequest")
	public List<Map<String, Object>> getMedicationRequestList(HttpServletRequest theServletRequest,
			HomeRequest theRequest, HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition)
			throws Exception {

		LOGGER.info("FhirHistoryController getMedicationRequestList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirHistoryController getMedicationRequestList condition :::: {}", JsonUtil.toJsonString(condition));

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.MEDICATIONREQUEST.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.MEDICATIONREQUEST);
		}

		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return medicationRequestService.getResourceList(client, parser, condition);
	}

	/**
	 * 내역정보 조회 API - 수술내역 조회(getProcedureList)
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/procedure")
	public List<Map<String, Object>> getProcedureList(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition) throws Exception {

		LOGGER.info("FhirHistoryController getMedicationRequestList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirHistoryController getMedicationRequestList condition :::: {}", JsonUtil.toJsonString(condition));

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.PROCEDURE.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PROCEDURE);
		}
		
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return procedureService.getResourceList(client, parser, condition);
	}
}