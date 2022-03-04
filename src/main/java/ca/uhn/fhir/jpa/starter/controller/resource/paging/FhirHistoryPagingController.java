package ca.uhn.fhir.jpa.starter.controller.resource.paging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.exception.ApiValidationError;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.ConditionService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.MedicationRequestService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.ProcedureService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.PagingUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.code.InquiryPeriodSearchCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourceListVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourcePageVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName : FhirHistoryPagingController.java
 * @Description : 의료데이터 조회(mediDataLinkService)
 * @Modification
 * 
 *               <pre>
 *  수정일		수정자	수정내용
 * ---------- ------- ------------------------
 * 2022.02.23	최철호	최초작성
 * 2022.03.02	최철호	Resource Service 적용
 *               </pre>
 * 
 * @author Cheol-Ho Choi
 * @since 2022-03-02
 * @version 1.0
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class FhirHistoryPagingController extends BaseController {
	private final TfnDynagreServiceLogic agreementService;
	private final ConditionService conditionService;
	private final MedicationRequestService medicationRequestService;
	private final ProcedureService procedureService;

	private CaptureInterceptor interceptor = new CaptureInterceptor();

	/**
	 * 내역정보 조회 API - 진단내역 조회_페이징(getConditionPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/condition/{provideInstitutionCode}")
	public Map<String, Object> getConditionPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirHistoryPagingController getConditionPage Start :::: {}", LocalDateTime.now());
		log.info("FhirHistoryPagingController getConditionPage condition :::: {}", JsonUtil.toJsonString(condition));
		log.info("FhirHistoryPagingController getConditionPage provideInstitutionCode :::: {}", provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.CONDITION.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.CONDITION);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return conditionService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}

	/**
	 * 내역정보 조회 API - 약물처방내역 조회_페이징(getMedicationRequestPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/medicationRequest/{provideInstitutionCode}")
	public Map<String, Object> getMedicationRequestPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirHistoryPagingController getMedicationRequestPage Start :::: {}", LocalDateTime.now());
		log.info("FhirHistoryPagingController getMedicationRequestPage condition :::: {}",
				JsonUtil.toJsonString(condition));
		log.info("FhirHistoryPagingController getMedicationRequestPage provideInstitutionCode :::: {}",
				provideInstitutionCode);

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.MEDICATIONREQUEST.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.MEDICATIONREQUEST);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return medicationRequestService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}

	/**
	 * 내역정보 조회 API - 수술내역 조회_페이징(getProcedurePage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/procedure/{provideInstitutionCode}")
	public Map<String, Object> getProcedurePage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirHistoryPagingController getProcedurePage Start :::: {}", LocalDateTime.now());
		log.info("FhirHistoryPagingController getProcedurePage condition :::: {}", JsonUtil.toJsonString(condition));
		log.info("FhirHistoryPagingController getProcedurePage provideInstitutionCode :::: {}", provideInstitutionCode);

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.PROCEDURE.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PROCEDURE);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return procedureService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}
}
