package ca.uhn.fhir.jpa.starter.myhw.service.resource.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
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
public class PatientService implements FhirResourceService {

	private final TfnMdsidxServiceLogic indexService;
	
	@Override
	public List<Map<String, Object>> getResourceList(IGenericClient client, IParser parser, DatasetSearchVO condition)
			throws JsonProcessingException {
		// TODO Auto-generated method stub
		List<FhirVisitPatientDatasetVO> indexList = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), condition.getProvideInstitutionCode());

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (String provideInstitutionCode : condition.getProvideInstitutionCode()) {
			List<String> patientIdList = indexList.stream()
												  .filter(data -> (data.getPvsnInstCd().equals(provideInstitutionCode) && data.getFhirPatientId() != null))
												  .map(FhirVisitPatientDatasetVO::getFhirPatientId)
												  .collect(Collectors.toList());

			List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
			Map<String, Object> resource 			= new HashMap<String, Object>();
			List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
					
			patientIdList.stream().forEach(id -> {
				try {
					Patient patient = ResourceUtil.getResourceById(client, Patient.class, id);
					Map<String, Object> patientMap = ResourceUtil.fromResource(parser, patient, Map.class);
					resourceList.add(patientMap);
				} catch (ResourceNotFoundException e) {		
					// FIXME Resource Patient/{id} is not known
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
		
		FhirVisitPatientDatasetVO index = indexService.getFhirVisitPatientIndexByCondition(condition.getUserId(), provideInstitutionCode);
		
		Map<String, Object> result 				= new HashMap<String, Object>();
		List<Map<String, Object>> entry 		= new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resourceList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> resource 			= new HashMap<String, Object>();

		if (index != null) {
			try {
				Patient patient = ResourceUtil.getResourceById(client, Patient.class, index.getFhirPatientId());
				Map<String, Object> patientMap = ResourceUtil.fromResource(parser, patient, Map.class);
				resourceList.add(patientMap);
				
			} catch (ResourceNotFoundException e) {
				// FIXME Resource Patient/{id} is not known
				throw new ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException("Resource Patient/" + index.getFhirPatientId() + "is not known");
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
