package ca.uhn.fhir.jpa.starter.controller.resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.OrganizationService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.PatientService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.PractitionerService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirInfoController.java
 * @Description : 의료데이터 조회(mediDataLinkService)
 * @Modification
 * <pre>
 *  수정일              	수정자        	수정내용
 * ----------- ------- ------------------------
 * 2022.01.19	최철호	최초작성
 * 2022.01.26 	최철호	Index 매핑 로직 추가
 * 2022.02.07	최철호	Response Data 수정
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
public class FhirInfoController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FhirInfoController.class);

	private final TfnDynagreServiceLogic agreementService;
	private final PatientService patientService;
	private final OrganizationService organizationService;
	private final PractitionerService practitionerService;
	
	private CaptureInterceptor interceptor = new CaptureInterceptor();
	
	/**
	 * 의료정보 조회 API - 환자정보  조회(getPatientList)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/patient" })
	public List<Map<String, Object>> getPatientList(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition) throws Exception {

		LOGGER.info("FhirInfoController getPatientList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoController getPatientList condition :::: {}", JsonUtil.toJsonString(condition));
		
		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.PATIENT.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PATIENT);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();
		
		return patientService.getResourceList(client, parser, condition);
	}
	
	/**
	 * 의료정보 조회 API - 의료기관정보 조회(getOrganizationList)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/organization" })
	public List<Map<String, Object>> getOrganizationList(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition) throws Exception {

		LOGGER.info("FhirInfoController getOrganizationList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoController getOrganizationList condition :::: {}", JsonUtil.toJsonString(condition));

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.ORGANIZATION.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.ORGANIZATION);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);		
		IParser parser = getContext(theRequest).newJsonParser();
		
		return organizationService.getResourceList(client, parser, condition);
	}
	
	/**
	 * 의료정보 조회 API - 의료진정보 조회(getPractitionerList)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/practitioner" })
	public List<Map<String, Object>> getPractitionerList(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition) throws Exception {

		LOGGER.info("FhirInfoController getPractitionerList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirInfoController getPractitionerList condition :::: {}", JsonUtil.toJsonString(condition));

		// 활용 동의 정합성 체크
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(), condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.PRACTITIONER.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PRACTITIONER);
		}
		
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return practitionerService.getResourceList(client, parser, condition);
	}
}
