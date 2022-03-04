package ca.uhn.fhir.jpa.starter.myhw.service.resource;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * @InterfaceName : FhirResourceService.java
 * @Description : Search FHIR Resource Data to MYHW Response Data
 * @Modification
 * 
 *               <pre>
 * 수정일      	  수정자          수정내용
 * --------- ------- ------------------------
 * 2022.02.28 최철호     최초작성
 *               </pre>
 *
 * @author Cheol-Ho Choi
 * @since 2022.02.28
 * @version 1.0
 * @see
 */
public interface FhirResourceService {

	// Resource Search by Condition
	List<Map<String, Object>> getResourceList(IGenericClient client, IParser parser, DatasetSearchVO condition) throws JsonProcessingException;
	
	// Resource Search by Condition to Pages
	Map<String, Object> getResourcePage(IGenericClient client, IParser parser, String provideInstitutionCode , DatasetPagingSearchVO condition) throws JsonProcessingException;
}
