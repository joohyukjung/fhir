package ca.uhn.fhir.jpa.starter.myhw.service.resource.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.FhirResourceService;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
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
public class OrganizationService implements FhirResourceService{
	
	private final TfnMdsidxServiceLogic indexService;
	
	@Override
	public List<Map<String, Object>> getResourceList(IGenericClient client, IParser parser, DatasetSearchVO condition)
			throws JsonProcessingException {
		//
		List<FhirVisitPatientDatasetVO> indexList = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), condition.getProvideInstitutionCode());
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (String provideInstitutionCode : condition.getProvideInstitutionCode()) {
			List<String> organizationIdList = indexList.stream()
													   .filter(data -> (data.getPvsnInstCd().equals(provideInstitutionCode) && data.getFhirOrganizationId() != null))
													   .map(FhirVisitPatientDatasetVO :: getFhirOrganizationId)
													   .collect(Collectors.toList());
			
			List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
			Map<String, Object> resource 			= new HashMap<String, Object>();
			
			organizationIdList.stream().forEach(id -> {
				try {
					Organization organization = ResourceUtil.getResourceById(client, Organization.class, id);
					Map<String, Object> organizationMap = ResourceUtil.fromResource(parser, organization, Map.class);
					resourceList.add(organizationMap);
				} catch (ResourceNotFoundException e) {
					// FIXME Resource Organization/{id} is not known
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
	public Map<String, Object> getResourcePage(IGenericClient client, IParser parser, String provideInstitutionCode,
			DatasetPagingSearchVO condition) throws JsonProcessingException {
		// 
		FhirVisitPatientDatasetVO index = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), provideInstitutionCode);
		
		Map<String, Object> result 				= new HashMap<String, Object>();
		List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> resource 			= new HashMap<String, Object>();
		
		if (index != null) {
			try {
				Organization organization = ResourceUtil.getResourceById(client, Organization.class, index.getFhirOrganizationId());
				Map<String, Object> organizationMap = ResourceUtil.fromResource(parser, organization, Map.class);
				resourceList.add(organizationMap);
			} catch (ResourceNotFoundException e) {
				// FIXME Resource Organization/{id} is not known
				throw new ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException("Resource Organization/" + index.getFhirOrganizationId() + "is not known");
			}
		}
		resource.put("resource", resourceList);
		entry.add(resource);
		
		ResultResourcePageVO<Map<String, Object>> resultResourcePageVO = new ResultResourcePageVO<Map<String, Object>>();
		resultResourcePageVO.setSuccess(condition.getServiceUID(), provideInstitutionCode, 1, 1, entry);
		result = JsonUtil.fromJson(JsonUtil.toJsonString(resultResourcePageVO), Map.class);
		
		return result;
	}
}
