package ca.uhn.fhir.jpa.starter.controller.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportMediaComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.CategoryCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.common.BundleSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourceListVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirResourceController.java
 * @Description : 의료데이터 Bundle 조회
 * @Modification
 * 
 *               <pre>
 *  수정일		수정자	수정내용
 * ---------- ------- ------------------------
 * 2022.02.07	최철호	최초작성
 *               </pre>
 * 
 * @author Cheol-Ho Choi
 * @since 2022-02-07
 * @version 1.0
 */

@Validated
@RestController
@RequiredArgsConstructor
public class FhirResourceController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FhirResourceController.class);

	private final TfnMdsidxServiceLogic indexService;
	private final TfnDynagreServiceLogic agreementService;

	private CaptureInterceptor interceptor = new CaptureInterceptor();

	/**
	 * 의료데이터 Bundle 조회 by 환자, 의료기관
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @return
	 * @throws JsonProcessingException
	 */
	@PostMapping(value = { "/bundle" })
	public Map<String, Object> getResourceBundleByPatient(HttpServletRequest theServletRequest, HomeRequest theRequest,
			HttpServletResponse theResponse, @Valid @RequestBody BundleSearchVO condition) throws JsonProcessingException {

		LOGGER.info("FhirResourceController getResourceBundleByPatient Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirResourceController getResourceBundleByPatient condition :::: {}", JsonUtil.toJsonString(condition));

		Map<String, Object> result = new HashMap<>();
		ResultResourceListVO<Map<String, Object>> resultVO = new ResultResourceListVO<>();

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		for (String code : condition.getDataSetCodes()) {
			if (!AgreementUtil.checkAgreement(agreementList, code)) {
				throw new AgreementException(condition.getUserId(), DatasetCode.find(code));
			}
		}

		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		// Search Date Setting
		String beginningDate = condition.getBeginningDate() != null ? condition.getBeginningDate() : "";
		String endDate = condition.getEndDate() != null ? condition.getEndDate() : "";

		// Request Mapping Index Mapping
		FhirVisitPatientDatasetVO index = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(),
				condition.getProvideInstitutionCode());
		String patientId = index.getFhirPatientId();
		String organizationId = index.getFhirOrganizationId();

		Bundle resultBundle = new Bundle();
		List<Map<String, Object>> resourceList = new ArrayList<>();
		Map<String, Object> resource = new HashMap<>();
		List<Map<String, Object>> entry = new ArrayList<>();
		for (String dataSetCode : condition.getDataSetCodes().stream().distinct().collect(Collectors.toList())) {
			// DS01 환자 정보
			if (DatasetCode.PATIENT.getDatasetCode().equals(dataSetCode)) {
				try {
					resultBundle.addEntry().setResource(ResourceUtil.getResourceById(client, Patient.class, patientId));
					Patient patient = ResourceUtil.getResourceById(client, Patient.class, patientId);
					resourceList.add(ResourceUtil.fromResource(parser, patient, Map.class));
				} catch (ResourceNotFoundException e) {
					// FIXME Resource Patient/{id} is not known
					throw new ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException("Resource Patient/" + index.getFhirPatientId() + " is not known");
				}
				continue;
			}
			// DS02 의료기관 정보
			if (DatasetCode.ORGANIZATION.getDatasetCode().equals(dataSetCode)) {
				try {
					resultBundle.addEntry()
							.setResource(ResourceUtil.getResourceById(client, Organization.class, organizationId));
					Organization organization = ResourceUtil.getResourceById(client, Organization.class,
							organizationId);
					resourceList.add(ResourceUtil.fromResource(parser, organization, Map.class));
				} catch (ResourceNotFoundException e) {
					// FIXME Resource Organization/{id} is not known
					throw new ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException("Resource Organization/" + index.getFhirOrganizationId() + " is not known");
				}
				continue;
			}
			// DS03 의료진 정보
			if (DatasetCode.PRACTITIONER.getDatasetCode().equals(dataSetCode)) {
				List<BundleEntryComponent> encounterEntry = ResourceUtil.getEncounterBundleEntry(client, patientId);

				// Result Resource Loop
				for (BundleEntryComponent component : encounterEntry) {
					Encounter encounter = (Encounter) component.getResource();
					List<String> practitionerRoleIdList = encounter.getParticipant().stream()
							.map(participant -> participant.getIndividual().getReference())
							.collect(Collectors.toList());

					for (String practitionerRoleId : practitionerRoleIdList) {
						// PractitionerRole 추출
						practitionerRoleId = StringUtil.substr(practitionerRoleId, "/");
						PractitionerRole practitionerRole = ResourceUtil.getResourceById(client, PractitionerRole.class,
								practitionerRoleId);
						resultBundle.addEntry().setResource(practitionerRole);
						resourceList.add(ResourceUtil.fromResource(parser, practitionerRole, Map.class));

						// Practitioner 추출
						String practitionerId = StringUtil.substr(practitionerRole.getPractitioner().getReference(),
								"/");
						Practitioner practitioner = ResourceUtil.getResourceById(client, Practitioner.class,
								practitionerId);
						resultBundle.addEntry().setResource(practitioner);
						resourceList.add(ResourceUtil.fromResource(parser, practitioner, Map.class));
					}
				}
				continue;
			}
			// DS04 진단내역
			if (DatasetCode.CONDITION.getDatasetCode().equals(dataSetCode)) {
				Bundle conditionBundle = ResourceUtil.getConditionBundle(client, patientId,
						beginningDate, endDate);

				// Result Resource Loop
				for (BundleEntryComponent component : conditionBundle.getEntry()) {
					Condition conditionResource = (Condition) component.getResource();
					resultBundle.addEntry().setResource(conditionResource);
					resourceList.add(ResourceUtil.fromResource(parser, conditionResource, Map.class));

					// Encounter 추출
					String encounterId = StringUtil.substr(conditionResource.getEncounter().getReference(), "/");
					Encounter encounter = ResourceUtil.getResourceById(client, Encounter.class, encounterId);
					resultBundle.addEntry().setResource(encounter);
					resourceList.add(ResourceUtil.fromResource(parser, encounter, Map.class));
				}
				continue;
			}
			// DS05 약물처방내역
			if (DatasetCode.MEDICATIONREQUEST.getDatasetCode().equals(dataSetCode)) {
				Bundle medicationRequest = ResourceUtil.getMedicationRequestBundle(client,
						patientId, beginningDate, endDate);

				for (BundleEntryComponent component : medicationRequest.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
				continue;
			}
			// DS06 진단검사
			if (DatasetCode.LABORATORY.getDatasetCode().equals(dataSetCode)) {
				Bundle observation = ResourceUtil.getObservationBundle(client, patientId,
						beginningDate, endDate, CategoryCode.LABORATORY.getCode());

				for (BundleEntryComponent component : observation.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
			}
			// DS07 영상검사
			if (DatasetCode.IMAGING.getDatasetCode().equals(dataSetCode)) {
				Bundle diagnosticReportBundle = ResourceUtil.getDiagnosticReportBundle(client,
						patientId, beginningDate, endDate, CategoryCode.IMAGING.getCode());

				for (BundleEntryComponent component : diagnosticReportBundle.getEntry()) {
					DiagnosticReport diagnosticReport = (DiagnosticReport) component.getResource();
					resultBundle.addEntry().setResource(diagnosticReport);
					resourceList.add(ResourceUtil.fromResource(parser, diagnosticReport, Map.class));
					// Imaging Study 추출
					if (diagnosticReport.hasImagingStudy()) {
						for (Reference reference : diagnosticReport.getImagingStudy()) {
							String imagingStudyId = StringUtil.substr(reference.getReference(), "/");
							ImagingStudy imagingStudy = ResourceUtil.getResourceById(client, ImagingStudy.class,
									imagingStudyId);
							resultBundle.addEntry().setResource(imagingStudy);
							resourceList.add(ResourceUtil.fromResource(parser, imagingStudy, Map.class));
						}
					}

					// Media 추출
					if (diagnosticReport.hasMedia()) {
						List<Map<String, Object>> mediaList = new ArrayList<>();
						for (DiagnosticReportMediaComponent reference : diagnosticReport.getMedia()) {
							String mediaId = StringUtil.substr(reference.getLink().toString(), "/");
							Media media = ResourceUtil.getResourceById(client, Media.class, mediaId);
							resultBundle.addEntry().setResource(media);
							resourceList.add(ResourceUtil.fromResource(parser, media, Map.class));
						}
					}
				}
			}
			// DS08 병리검사
			if (DatasetCode.PATHOLOGY.getDatasetCode().equals(dataSetCode)) {
				Bundle diagnosticReportBundle = ResourceUtil.getDiagnosticReportBundle(client,
						patientId, beginningDate, endDate, CategoryCode.PATHOLOGY.getCode());

				for (BundleEntryComponent component : diagnosticReportBundle.getEntry()) {
					DiagnosticReport diagnosticReport = (DiagnosticReport) component.getResource();
					resultBundle.addEntry().setResource(diagnosticReport);
					resourceList.add(ResourceUtil.fromResource(parser, diagnosticReport, Map.class));
					// Imaging Study 추출
					if (diagnosticReport.hasImagingStudy()) {
						for (Reference reference : diagnosticReport.getImagingStudy()) {
							String imagingStudyId = StringUtil.substr(reference.getReference(), "/");
							ImagingStudy imagingStudy = ResourceUtil.getResourceById(client, ImagingStudy.class,
									imagingStudyId);
							resultBundle.addEntry().setResource(imagingStudy);
							resourceList.add(ResourceUtil.fromResource(parser, imagingStudy, Map.class));
						}
					}

					// Media 추출
					if (diagnosticReport.hasMedia()) {
						for (DiagnosticReportMediaComponent reference : diagnosticReport.getMedia()) {
							String mediaId = StringUtil.substr(reference.getLink().toString(), "/");
							Media media = ResourceUtil.getResourceById(client, Media.class, mediaId);
							resultBundle.addEntry().setResource(media);
							resourceList.add(ResourceUtil.fromResource(parser, media, Map.class));
						}
					}
				}
			}
			// DS09 기타검사
			if (DatasetCode.VITAL_SIGNS.getDatasetCode().equals(dataSetCode)) {
				Bundle observationBundle = ResourceUtil.getObservationBundle(client, patientId,
						beginningDate, endDate, CategoryCode.VITAL_SIGNS.getCode());

				for (BundleEntryComponent component : observationBundle.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
			}
			// DS10 수술내역
			if (DatasetCode.PROCEDURE.getDatasetCode().equals(dataSetCode)) {
				Bundle procedure = ResourceUtil.getProcedureBundle(client, patientId, beginningDate, endDate);

				for (BundleEntryComponent component : procedure.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
			}
			// DS11 알러지 및 부작용
			if (DatasetCode.ALLERGYINTOLERANCE.getDatasetCode().equals(dataSetCode)) {
				Bundle allergyIntolerance = ResourceUtil.getAllergyIntoleranceBundle(client, patientId);

				// Result Resource Loop
				for (BundleEntryComponent component : allergyIntolerance.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
			}
			// DS12 진료기록
			if (DatasetCode.DOCUMENTREFERENCE.getDatasetCode().equals(dataSetCode)) {
				Bundle documentReference = ResourceUtil.getDocumentReferenceBundle(client, patientId);

				// Result Resource Loop
				for (BundleEntryComponent component : documentReference.getEntry()) {
					resultBundle.addEntry().setResource(component.getResource());
					resourceList.add(ResourceUtil.fromResource(parser, component.getResource(), Map.class));
				}
			}
		}

		resource.put("resource", resourceList);
		entry.add(resource);
		resultVO.setSuccess(condition.getServiceUID(), condition.getProvideInstitutionCode(), entry);
		result = JsonUtil.fromJson(parser.encodeResourceToString(resultBundle), Map.class);

		return result;
	}
}
