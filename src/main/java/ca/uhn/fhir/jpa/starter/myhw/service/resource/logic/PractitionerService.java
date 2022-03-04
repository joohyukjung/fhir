package ca.uhn.fhir.jpa.starter.myhw.service.resource.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.FhirResourceService;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.PagingUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.StringUtil;
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
public class PractitionerService implements FhirResourceService{
	
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
			
			List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
			Map<String, Object> resource	 		= new HashMap<String, Object>();
			
			// Patient Loop
			patientIdList.stream().forEach(id -> {
				// Encounter 조회
				List<BundleEntryComponent> encounterEntry = ResourceUtil.getEncounterBundleEntry(client, id);
				
				// Result Resource Loop
				for (BundleEntryComponent component : encounterEntry) {
					Encounter encounter = (Encounter) component.getResource();
					List<String> practitionerRoleIdList = encounter.getParticipant().stream().map(participant -> participant.getIndividual().getReference()).collect(Collectors.toList());
				
					for (String practitionerRoleId : practitionerRoleIdList) {
						// PractitionerRole 추출
						Map<String, Object> practitionerRoleMap = new HashMap<String, Object>();
						practitionerRoleId = StringUtil.substr(practitionerRoleId, "/");
						PractitionerRole practitionerRole = ResourceUtil.getResourceById(client, PractitionerRole.class, practitionerRoleId);
						practitionerRoleMap.put("practitionerRole", ResourceUtil.fromResource(parser, practitionerRole, Map.class));
						
						// Practitioner 추출
						Map<String, Object> practitionerMap = new HashMap<String, Object>();
						String practitionerId = StringUtil.substr(practitionerRole.getPractitioner().getReference(), "/");
						Practitioner practitioner = ResourceUtil.getResourceById(client, Practitioner.class, practitionerId);
						practitionerMap.put("practitioner", ResourceUtil.fromResource(parser, practitioner, Map.class));
						
						practitionerMap.putAll(practitionerRoleMap);
						resourceList.add(practitionerMap);
					}
				}
			});
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
			List<BundleEntryComponent> encounterEntry = ResourceUtil.getEncounterBundleEntry(client, index.getFhirPatientId());
			
			for (BundleEntryComponent component : encounterEntry) {
				Encounter encounter = (Encounter) component.getResource();
				List<String> practitionerRoleIdList = encounter.getParticipant().stream().map(participant -> participant.getIndividual().getReference()).collect(Collectors.toList());
			
				for (String practitionerRoleId : practitionerRoleIdList) {
					// PractitionerRole 추출
					Map<String, Object> practitionerRoleMap = new HashMap<String, Object>();
					practitionerRoleId = StringUtil.substr(practitionerRoleId, "/");
					try {
						PractitionerRole practitionerRole = ResourceUtil.getResourceById(client, PractitionerRole.class, practitionerRoleId);
						practitionerRoleMap.put("practitionerRole", ResourceUtil.fromResource(parser, practitionerRole, Map.class));
						
						// Practitioner 추출
						Map<String, Object> practitionerMap = new HashMap<String, Object>();
						String practitionerId = StringUtil.substr(practitionerRole.getPractitioner().getReference(), "/");
						Practitioner practitioner = ResourceUtil.getResourceById(client, Practitioner.class, practitionerId);
						practitionerMap.put("practitioner", ResourceUtil.fromResource(parser, practitioner, Map.class));
						
						practitionerMap.putAll(practitionerRoleMap);
						resourceList.add(practitionerMap);
					} catch (ResourceNotFoundException e) {
						// FIXME Resource PractitionerRole/{id} is not known
						// FIXME Resource Practitioner/{id} is not known
					}
				}
			}
		}
		// 페이징
		int totalPages = 1;
		int totalItemCount = resourceList.size();
		if (! PagingUtil.checkAllSearch(condition.getInquiryPageNumber())) {
			totalPages = PagingUtil.getTotalPages(resourceList.size(), condition.getOnePageInquiryCase());
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
