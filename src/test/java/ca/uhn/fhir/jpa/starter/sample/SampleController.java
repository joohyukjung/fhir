package ca.uhn.fhir.jpa.starter.sample;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;


/**
 * @ClassName : SampleController.java
 * @Description : FHIR 응용 Controller Sample
 * @Modification
 * 
 * <pre>
 *  수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.17 최철호     최초작성
 * </pre>
 * 
 * @author Cheol-Ho Choi
 * @since 2022-01-17
 * @version 1.0
 */

@Controller
public class SampleController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SampleController.class);

	/**
	 * Call JPA Server Client Example(Patient)
	 */
	@ResponseBody
	@PostMapping(value = { "/client" }, produces="application/fhir+json")
	public String selectPatientListClient(HttpServletRequest theServletRequest, HomeRequest theRequest,
										  HttpServletResponse theResponse, @RequestBody DatasetSearchVO condition) throws Exception {
		LOGGER.info("SampleController selectPatientListClient Start :::: {}", LocalDateTime.now());
		LOGGER.info("SampleController selectPatientListClient condition :::: {}", condition.toString());

		// 1. Client 생성
		CaptureInterceptor interceptor = new CaptureInterceptor();
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		// 2. 데이터 조회(Search by 주민번호)
		// ID 기반으로 조회하는 경우 -> /fhir/Patient/{id}
		// Patient patient = client.read().resource(Patient.class).withId("1").execute();
		Bundle response = (Bundle) client
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().identifier(condition.getResidentRegistrationNumber()))
				.encodedJson().returnBundle(Bundle.class).execute();

		// if 결과가 없는 경우 예외처리
		if (response.getTotal() == 0) {
			return "NO DATA";
		}
		
		// 3. Parser 생성
		IParser parser = getContext(theRequest).newJsonParser();
		
		// 리스트 결과에서 값을 하나씩 추출하는 경우
		for (BundleEntryComponent component : response.getEntry()) {
			Patient patient = (Patient) component.getResource();
			String patientStr = parser.setPrettyPrint(true).encodeResourceToString(component.getResource());
			LOGGER.info("SampleController selectPatientListClient patientStr :::: {}", patientStr);
		}
		
		// TODO response 값에서 Patient 데이터 추출 후 DataSet 생성
		
		// response.getEntryFirstRep().getResource() -> List 결과 중 첫번째 Entry에서 Resource 추출
		return parser.setPrettyPrint(true).encodeResourceToString(response.getEntryFirstRep().getResource());
	}
}
