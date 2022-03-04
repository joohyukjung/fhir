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
public class AllergyIntoLeranceService implements FhirResourceService{
	
	private final TfnMdsidxServiceLogic indexService;
	
	@Override
	public List<Map<String, Object>> getResourceList(IGenericClient client, IParser parser, DatasetSearchVO condition)
			throws JsonProcessingException {
		//
		List<FhirVisitPatientDatasetVO> indexList = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), condition.getProvideInstitutionCode());
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (String provideInstitutionCode : condition.getProvideInstitutionCode()) {
			List<String> patientIdList = indexList.stream().filter(data -> (data.getPvsnInstCd().equals(provideInstitutionCode) && data.getFhirPatientId() != null))
					.map(FhirVisitPatientDatasetVO :: getFhirPatientId).collect(Collectors.toList());

			List<Map<String, Object>> entry = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resourceList = new ArrayList<Map<String, Object>>();
			Map<String, Object> resource = new HashMap<String, Object>();

			// Patient Loop
			for (String id : patientIdList) {
				Bundle allergyIntoleranceEntry = ResourceUtil.getAllergyIntoleranceBundle(client, id);

				// Result Resource Loop
				for (BundleEntryComponent component : allergyIntoleranceEntry.getEntry()) {
					Map<String, Object> allergyIntoleranceStrMap = ResourceUtil.fromResource(parser, component, Map.class);
					resourceList.add(allergyIntoleranceStrMap);
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
		
		Map<String, Object> result 				= new HashMap<String, Object>();
		List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> resource 			= new HashMap<String, Object>();
		
		if (index != null) {
			Bundle allergyIntolerance = ResourceUtil.getAllergyIntoleranceBundle(client, index.getFhirPatientId());
			// Result Resource Loop
			for (BundleEntryComponent component : allergyIntolerance.getEntry()) {
				Map<String, Object> allergyIntoleranceStrMap = ResourceUtil.fromResource(parser, component, Map.class);
				resourceList.add(allergyIntoleranceStrMap);
			}
		}
		
		// 페이징
		int totalPages 		= 1;
		int totalItemCount  = resourceList.size();
		if (! PagingUtil.checkAllSearch(condition.getInquiryPageNumber())) {
			totalPages   = PagingUtil.getTotalPages(resourceList.size(), condition.getOnePageInquiryCase());
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
