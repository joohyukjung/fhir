package ca.uhn.fhir.jpa.starter.controller.resource.paging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.OrganizationService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.PatientService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.PractitionerService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.PagingUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourcePageVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : InfoPagingController.java
 * @Description : 의료데이터 조회_페이징(mediDataLinkService)
 * @Modification
 * <pre>
 *  수정일              	수정자        	수정내용
 * ----------- ------- ------------------------
 * 2022.02.22	최철호	최초작성
 * 2022.03.02	최철호	Resource Service 적용
 * </pre>
 * 
 * @author Cheol-Ho Choi
 * @since 2022-03-02
 * @version 1.0
 */


@Validated
@RestController
@RequiredArgsConstructor
public class FhirInfoPagingController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FhirInfoPagingController.class);
	
	private final TfnDynagreServiceLogic agreementService;
	private final PatientService patientService;
	private final OrganizationService organizationService;
	private final PractitionerService practitionerService;
	
	private CaptureInterceptor interceptor = new CaptureInterceptor();
	
	/**
	 * 의료정보 조회 API - 환자정보  조회_페이징(getPatientPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws JsonProcessingException 
	 * @throws Exception
	 */
	@PostMapping(value = {"/patient/{provideInstitutionCode}"})
	public Map<String, Object> getPatientPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, 
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode, 
			@Valid @RequestBody DatasetPagingSearchVO condition) throws JsonProcessingException {
		LOGGER.info("FhirInfoPagingController getPatientPage Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoPagingController getPatientPage condition :::: {}", JsonUtil.toJsonString(condition));
		LOGGER.info("FhirInfoPagingController getPatientPage provideInstitutionCode :::: {}", provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.PATIENT.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PATIENT);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser 		  = getContext(theRequest).newJsonParser();

		return patientService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}
	
	/**
	 * 의료정보 조회 API - 의료기관정보 조회_페이징(getOrganizationPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = {"/organization/{provideInstitutionCode}"})
	public Map<String, Object> getOrganizationPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, 
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode, 
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {
		LOGGER.info("FhirInfoPagingController getOrganizationPage Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoPagingController getOrganizationPage condition :::: {}", JsonUtil.toJsonString(condition));
		
		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.ORGANIZATION.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.ORGANIZATION);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();
		
		return organizationService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}
	
	/**
	 * 의료정보 조회 API - 의료진정보 조회_페이징(getPractitionerPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/practitioner/{provideInstitutionCode}" })
	public Map<String, Object> getPractitionerPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, 
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode, 
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		LOGGER.info("FhirInfoPagingController getPractitionerPage Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoPagingController getPractitionerPage condition :::: {}", JsonUtil.toJsonString(condition));

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.PRACTITIONER.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PRACTITIONER);
		}
		
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();
		
		return practitionerService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}
}
