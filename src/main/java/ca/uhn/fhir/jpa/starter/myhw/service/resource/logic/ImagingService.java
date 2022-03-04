package ca.uhn.fhir.jpa.starter.myhw.service.resource.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportMediaComponent;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.FhirResourceService;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.PagingUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.code.CategoryCode;
import ca.uhn.fhir.jpa.starter.vo.code.InquiryPeriodSearchCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourceListVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourcePageVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagingService implements FhirResourceService{
	
	private final TfnMdsidxServiceLogic indexService;
	
	@Override
	public List<Map<String, Object>> getResourceList(IGenericClient client, IParser parser, DatasetSearchVO condition)
			throws JsonProcessingException {
		//
		List<FhirVisitPatientDatasetVO> indexList = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), condition.getProvideInstitutionCode());
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		// Search Date Setting
		String beginningDate = "";
		String endDate 		 = "";
		if (ValidationUtil.isSearchKey(condition.getInquiryPeriodSearchKey(), InquiryPeriodSearchCode.INSPECTION_DATE.getCode())) {
			if (condition.getInquiryPeriodSearchKey() != "") {
				beginningDate = condition.getBeginningDate() != null ? condition.getBeginningDate() : "";
				endDate		  = condition.getEndDate() != null ? condition.getEndDate() : "";
			}
		} else {
			throw new ConstraintViolationException("Check Request Parameter['inquiryPeriodSearchKey'] Value",
					new HashSet<ConstraintViolation<?>>());
		}
		
		for (String provideInstitutionCode : condition.getProvideInstitutionCode()) {
			List<String> patientIdList = indexList.stream().filter(
					data -> (data.getPvsnInstCd().equals(provideInstitutionCode) && data.getFhirPatientId() != null))
					.map(FhirVisitPatientDatasetVO::getFhirPatientId).collect(Collectors.toList());

			List<Map<String, Object>> entry = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resourceList = new ArrayList<Map<String, Object>>();
			Map<String, Object> resource = new HashMap<String, Object>();

			// Patient ID Loop
			for (String id : patientIdList) {
				Bundle diagnosticReportBundle = ResourceUtil.getDiagnosticReportBundle(client,
						id, beginningDate, endDate, CategoryCode.IMAGING.getCode());

				for (BundleEntryComponent component : diagnosticReportBundle.getEntry()) {
					DiagnosticReport diagnosticReport = (DiagnosticReport) component.getResource();

					Map<String, Object> mediaMap 		= new HashMap<String, Object>();
					Map<String, Object> imagingStudyMap = new HashMap<String, Object>();
					// Imaging Study 추출
					if (diagnosticReport.hasImagingStudy()) {
						List<Map<String, Object>> imagingStudyList = new ArrayList<>();
						for (Reference reference : diagnosticReport.getImagingStudy()) {
							String imagingStudyId = StringUtil.substr(reference.getReference(), "/");
							try {
								ImagingStudy imagingStudy = ResourceUtil.getResourceById(client, ImagingStudy.class, imagingStudyId);
								imagingStudyList.add(ResourceUtil.fromResource(parser, imagingStudy, Map.class));
							} catch (ResourceNotFoundException e) {
								// FIXME Resource ImagingStudy/{id} is not known
							}
						}
						imagingStudyMap.put("imagingStudy", imagingStudyList);
					}

					// Media 추출
					if (diagnosticReport.hasMedia()) {
						List<Map<String, Object>> mediaList = new ArrayList<>();
						for (DiagnosticReportMediaComponent reference : diagnosticReport.getMedia()) {
							String mediaId = StringUtil.substr(reference.getLink().toString(), "/");
							try {
								Media media = ResourceUtil.getResourceById(client, Media.class, mediaId);
								mediaList.add(ResourceUtil.fromResource(parser, media, Map.class));
							} catch (ResourceNotFoundException e) {
								// FIXME Resource Media/{id} is not known
							}
						}
						mediaMap.put("media", mediaList);
					}

					// DiagnosticReport 추출
					Map<String, Object> diagnosticReportMap = new HashMap<String, Object>();
					diagnosticReportMap.put("diagnosticReport", ResourceUtil.fromResource(parser, component, Map.class));

					diagnosticReportMap.putAll(imagingStudyMap);
					diagnosticReportMap.putAll(mediaMap);

					resourceList.add(diagnosticReportMap);
				}
			}
			resource.put("resource", resourceList);
			entry.add(resource);

			ResultResourceListVO<Map<String, Object>> resultResourceListVO = new ResultResourceListVO<Map<String, Object>>();
			resultResourceListVO.setSuccess(condition.getServiceUID(), provideInstitutionCode, entry);
			result.add(JsonUtil.fromJson(JsonUtil.toJsonString(resultResourceListVO), Map.class));
		}

		return result;
	}

	@Override
	public Map<String, Object> getResourcePage(IGenericClient client, IParser parser, String provideInstitutionCode, DatasetPagingSearchVO condition)
			throws JsonProcessingException {
		// 
		FhirVisitPatientDatasetVO index = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), provideInstitutionCode);
		
		// Search Date Setting
		String beginningDate = "";
		String endDate 		 = "";
		if (ValidationUtil.isSearchKey(condition.getInquiryPeriodSearchKey(), InquiryPeriodSearchCode.INSPECTION_DATE.getCode())) {
			if (condition.getInquiryPeriodSearchKey() != "") {
				beginningDate = condition.getBeginningDate() != null ? condition.getBeginningDate() : "";
				endDate = condition.getEndDate() != null ? condition.getEndDate() : "";
			}
		} else {
			throw new ConstraintViolationException("Check Request Parameter['inquiryPeriodSearchKey'] Value",
					new HashSet<ConstraintViolation<?>>());
		}
		
		Map<String, Object> result 				= new HashMap<String, Object>();
		List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> resource 			= new HashMap<String, Object>();
		
		if (index != null) {
			Bundle diagnosticReportBundle = ResourceUtil.getDiagnosticReportBundle(client, index.getFhirPatientId(),
					beginningDate, endDate, CategoryCode.IMAGING.getCode());
	
			for (BundleEntryComponent component : diagnosticReportBundle.getEntry()) {
				DiagnosticReport diagnosticReport = (DiagnosticReport) component.getResource();
	
				Map<String, Object> mediaMap 		= new HashMap<String, Object>();
				Map<String, Object> imagingStudyMap = new HashMap<String, Object>();
				// Imaging Study 추출
				if (diagnosticReport.hasImagingStudy()) {
					List<Map<String, Object>> imagingStudyList = new ArrayList<>();
					for (Reference reference : diagnosticReport.getImagingStudy()) {
						String imagingStudyId = StringUtil.substr(reference.getReference(), "/");
						try {
							ImagingStudy imagingStudy = ResourceUtil.getResourceById(client, ImagingStudy.class, imagingStudyId);
							imagingStudyList.add(ResourceUtil.fromResource(parser, imagingStudy, Map.class));
						} catch (ResourceNotFoundException e) {
							// FIXME Resource ImagingStudy/{id} is not known
						}
					}
					imagingStudyMap.put("imagingStudy", imagingStudyList);
				}
	
				// Media 추출
				if (diagnosticReport.hasMedia()) {
					List<Map<String, Object>> mediaList = new ArrayList<>();
					for (DiagnosticReportMediaComponent reference : diagnosticReport.getMedia()) {
						String mediaId = StringUtil.substr(reference.getLink().toString(), "/");
						try {
							Media media = ResourceUtil.getResourceById(client, Media.class, mediaId);
							mediaList.add(ResourceUtil.fromResource(parser, media, Map.class));
						} catch (ResourceNotFoundException e) {
							// FIXME Resource Media/{id} is not known
						}
					}
					mediaMap.put("media", mediaList);
				}
	
				// DiagnosticReport 추출
				Map<String, Object> diagnosticReportMap = new HashMap<String, Object>();
				diagnosticReportMap.put("diagnosticReport", ResourceUtil.fromResource(parser, component, Map.class));
	
				diagnosticReportMap.putAll(imagingStudyMap);
				diagnosticReportMap.putAll(mediaMap);
	
				resourceList.add(diagnosticReportMap);
			}
		}

		// 페이징
		int totalPages 		= 1;
		int totalItemCount  = resourceList.size();
		if (!PagingUtil.checkAllSearch(condition.getInquiryPageNumber())) {
			totalPages 	 = PagingUtil.getTotalPages(resourceList.size(), condition.getOnePageInquiryCase());
			resourceList = PagingUtil.pagingList(resourceList, condition.getInquiryPageNumber(), condition.getOnePageInquiryCase());
		}

		resource.put("resource", resourceList);
		entry.add(resource);

		ResultResourcePageVO<Map<String, Object>> resultResourcePageVO = new ResultResourcePageVO<Map<String, Object>>();
		resultResourcePageVO.setSuccess(condition.getServiceUID(), provideInstitutionCode, totalItemCount, totalPages, entry);
		result = JsonUtil.fromJson(JsonUtil.toJsonString(resultResourcePageVO), Map.class);

		return result;
	}
}
