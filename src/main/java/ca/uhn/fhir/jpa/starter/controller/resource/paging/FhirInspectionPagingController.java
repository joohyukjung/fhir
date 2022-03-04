package ca.uhn.fhir.jpa.starter.controller.resource.paging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportMediaComponent;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.ImagingService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.LaboratoryService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.PathologyService;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.VitalSignsService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.PagingUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.CategoryCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.code.InquiryPeriodSearchCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
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
 * @ClassName : FhirInspectionPagingController.java
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
public class FhirInspectionPagingController extends BaseController {
	private final TfnMdsidxServiceLogic indexService;
	private final TfnDynagreServiceLogic agreementService;
	private final LaboratoryService laboratoryService;
	private final VitalSignsService vitalSignsService;
	private final ImagingService imagingService;
	private final PathologyService pathologyService;
	
	private CaptureInterceptor interceptor = new CaptureInterceptor();

	/**
	 * 의료정보 조회 API - 진단검사 조회_페이징(getObservationLaboratoryPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/observation/laboratory/{provideInstitutionCode}")
	public Map<String, Object> getObservationLaboratoryPage(HttpServletRequest theServletRequest,
			HomeRequest theRequest, HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirInspectionPagingController getObservationLaboratoryPage Start :::: {}", LocalDateTime.now());
		log.info("FhirInspectionPagingController getObservationLaboratoryPage condition :::: {}",
				JsonUtil.toJsonString(condition));
		log.info("FhirInspectionPagingController getObservationLaboratoryPage provideInstitutionCode :::: {}",
				provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.LABORATORY.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.LABORATORY);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return laboratoryService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}

	/**
	 * 의료정보 조회 API - 기타검사 조회_페이징(getObservationVitalPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/observation/vital-signs/{provideInstitutionCode}")
	public Map<String, Object> getObservationVitalPage(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirInspectionPagingController getObservationVitalPage Start :::: {}", LocalDateTime.now());
		log.info("FhirInspectionPagingController getObservationVitalPage condition :::: {}",
				JsonUtil.toJsonString(condition));
		log.info("FhirInspectionPagingController getObservationVitalPage provideInstitutionCode :::: {}",
				provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.VITAL_SIGNS.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.VITAL_SIGNS);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return vitalSignsService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}

	/**
	 * 의료정보 조회 API - 영상검사 조회_페이징(getDiagnosticReportImagingPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/diagnosticReport/imaging/{provideInstitutionCode}")
	public Map<String, Object> getDiagnosticReportImagingPage(HttpServletRequest theServletRequest,
			HomeRequest theRequest, HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirInspectionPagingController getDiagnosticReportImagingPage Start :::: {}", LocalDateTime.now());
		log.info("FhirInspectionPagingController getDiagnosticReportImagingPage condition :::: {}",
				JsonUtil.toJsonString(condition));
		log.info("FhirInspectionPagingController getDiagnosticReportImagingPage provideInstitutionCode :::: {}",
				provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.IMAGING.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.IMAGING);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return imagingService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}

	/**
	 * 의료정보 조회 API - 병리검사 조회_페이징(getDiagnosticReportPathologyPage)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param provideInstitutionCode
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/diagnosticReport/pathology/{provideInstitutionCode}")
	public Map<String, Object> getDiagnosticReportPathologyPage(HttpServletRequest theServletRequest,
			HomeRequest theRequest, HttpServletResponse theResponse,
			@PathVariable(name = "provideInstitutionCode") String provideInstitutionCode,
			@Valid @RequestBody DatasetPagingSearchVO condition) throws Exception {

		log.info("FhirInspectionPagingController getDiagnosticReportPathologyPage Start :::: {}", LocalDateTime.now());
		log.info("FhirInspectionPagingController getDiagnosticReportPathologyPage condition :::: {}",
				JsonUtil.toJsonString(condition));
		log.info("FhirInspectionPagingController getDiagnosticReportPathologyPage provideInstitutionCode :::: {}",
				provideInstitutionCode);

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (!AgreementUtil.checkAgreement(agreementList, DatasetCode.PATHOLOGY.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.PATHOLOGY);
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return pathologyService.getResourcePage(client, parser, provideInstitutionCode, condition);
	}
}
