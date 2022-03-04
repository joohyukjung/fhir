package ca.uhn.fhir.jpa.starter.util;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;

import ca.uhn.fhir.jpa.starter.vo.code.CategoryCode;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class ResourceUtil {

	/**
	 * Resource read by Id
	 * 
	 * @return FHIR Resource<T>
	 */
	public static <T extends IBaseResource> T getResourceById(IGenericClient client, Class<T> clazz, String id) {
		T resource = client
						.read()
						.resource(clazz)
						.withId(id)
						.execute();
						
		return resource;
	}
	
	/**
	 * Check ResidentRegistrationNumber by Patient Resource
	 * 
	 * @return
	 */
	public static boolean checkResidentRegistrationNumberByPatient(IGenericClient client, String id, String residentRegistrationNumber) {
		Patient patient = client.read()
				.resource(Patient.class)
				.withId(id)
				.execute();
		
		if (! patient.getIdentifier().get(0).getValue().equals(residentRegistrationNumber)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 리소스 조회일 추출
	 * 
	 * @return SearchKey와 코드가 일치하는 경우 return
	 */
	public static String getResourceSearchDate(String InquiryPeriodSearchKey, String date, String code) {
		if (! InquiryPeriodSearchKey.equals(code)) {
			return "";
		}
		return date;
	}
	
	/**
	 * Select Encounter Resource by Patient ID
	 * 
	 * @param client
	 * @param id
	 * @return
	 */
	public static List<BundleEntryComponent> getEncounterBundleEntry(IGenericClient client, String patientId) {
		
		Bundle bundle = client.search().forResource(Encounter.class)
				.where(Encounter.PATIENT.hasId(patientId))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(Encounter.class)
				.where(Encounter.PATIENT.hasId(patientId))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle.getEntry();
	}
	
	/**
	 * Select Condition Resource by Patient ID & Date
	 * 
	 * @param client
	 * @param id
	 * @param beginningDate
	 * @param endDate
	 * @return
	 */
	public static Bundle getConditionBundle(IGenericClient client, String patientId, String beginningDate, String endDate) {
		
		Bundle bundle = client.search().forResource(Condition.class)
				.where(Condition.PATIENT.hasId(patientId))
				.and(Condition.RECORDED_DATE.afterOrEquals().day(beginningDate))
				.and(Condition.RECORDED_DATE.beforeOrEquals().day(endDate))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(Condition.class)
				.where(Condition.PATIENT.hasId(patientId))
				.and(Condition.RECORDED_DATE.afterOrEquals().day(beginningDate))
				.and(Condition.RECORDED_DATE.beforeOrEquals().day(endDate))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select MedicationRequest Resource by Patient ID & Date
	 * 
	 * @param client
	 * @param id
	 * @param beginningDate
	 * @param endDate
	 * @return
	 */
	public static Bundle getMedicationRequestBundle(IGenericClient client, String patientId, String beginningDate, String endDate) {
		
		Bundle bundle = client.search().forResource(MedicationRequest.class)
				.where(MedicationRequest.PATIENT.hasId(patientId))
				.and(MedicationRequest.AUTHOREDON.afterOrEquals().day(beginningDate))
				.and(MedicationRequest.AUTHOREDON.beforeOrEquals().day(endDate))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(MedicationRequest.class)
				.where(MedicationRequest.PATIENT.hasId(patientId))
				.and(MedicationRequest.AUTHOREDON.afterOrEquals().day(beginningDate))
				.and(MedicationRequest.AUTHOREDON.beforeOrEquals().day(endDate))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select Procedure Resource by Patient ID & Date
	 * 
	 * @param client
	 * @param id
	 * @param beginningDate
	 * @param endDate
	 * @return
	 */
	public static Bundle getProcedureBundle(IGenericClient client, String patientId, String beginningDate, String endDate) {
		Bundle bundle = client.search().forResource(Procedure.class)
				.where(Procedure.PATIENT.hasId(patientId))
				.and(Procedure.DATE.afterOrEquals().day(beginningDate))
				.and(Procedure.DATE.beforeOrEquals().day(endDate))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);

		bundle = client.search().forResource(Procedure.class)
				.where(Procedure.PATIENT.hasId(patientId))
				.and(Procedure.DATE.afterOrEquals().day(beginningDate))
				.and(Procedure.DATE.beforeOrEquals().day(endDate))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select Observation Resource by Patient ID & Date & code
	 * 
	 * @param client
	 * @param id
	 * @param beginningDate
	 * @param endDate
	 * @param code
	 * @return
	 */
	public static Bundle getObservationBundle(IGenericClient client, String patientId, String beginningDate, String endDate, String code) {
		Bundle bundle = client.search().forResource(Observation.class)
				.where(Observation.CATEGORY.exactly().code(code))
				.and(Observation.PATIENT.hasId(patientId))
				.and(Observation.DATE.afterOrEquals().day(beginningDate))
				.and(Observation.DATE.beforeOrEquals().day(endDate))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(Observation.class)
				.where(Observation.CATEGORY.exactly().code(code))
				.and(Observation.PATIENT.hasId(patientId)).and(Observation.DATE.afterOrEquals().day(beginningDate))
				.and(Observation.DATE.beforeOrEquals().day(endDate))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select DiagnosticReport Resource by Patient ID & Date & code
	 * 
	 * @param client
	 * @param id
	 * @param beginningDate
	 * @param endDate
	 * @param code
	 * @return
	 */
	public static Bundle getDiagnosticReportBundle(IGenericClient client, String patientId, String beginningDate, String endDate, String code) {
		Bundle bundle = client.search().forResource(DiagnosticReport.class)
				.where(DiagnosticReport.CATEGORY.exactly().code(code))
				.and(DiagnosticReport.PATIENT.hasId(patientId))
				.and(DiagnosticReport.DATE.afterOrEquals().day(beginningDate))
				.and(DiagnosticReport.DATE.beforeOrEquals().day(endDate))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(DiagnosticReport.class)
				.where(DiagnosticReport.CATEGORY.exactly().code(code))
				.and(DiagnosticReport.PATIENT.hasId(patientId))
				.and(DiagnosticReport.DATE.afterOrEquals().day(beginningDate))
				.and(DiagnosticReport.DATE.beforeOrEquals().day(endDate))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select AllergyIntolerance Resource by Patient ID
	 * 
	 * @param client
	 * @param id
	 * @return
	 */
	public static Bundle getAllergyIntoleranceBundle(IGenericClient client, String patientId) {
		Bundle bundle = client.search().forResource(AllergyIntolerance.class)
				.where(AllergyIntolerance.PATIENT.hasId(patientId))
				.returnBundle(Bundle.class)
				.execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(AllergyIntolerance.class)
				.where(AllergyIntolerance.PATIENT.hasId(patientId))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	/**
	 * Select DocumentReference Resource by Patient ID
	 * 
	 * @param client
	 * @param id
	 * @return
	 */
	public static Bundle getDocumentReferenceBundle(IGenericClient client, String patientId) {
		Bundle bundle = client.search().forResource(DocumentReference.class)
				.where(DocumentReference.PATIENT.hasId(patientId))
				.returnBundle(Bundle.class).execute();
		
		int total = getBundleTotal(bundle);
		
		bundle = client.search().forResource(DocumentReference.class)
				.where(DocumentReference.PATIENT.hasId(patientId))
				.count(total)
				.returnBundle(Bundle.class)
				.execute();
		
		return bundle;
	}
	
	// Bundle Item Count
	private static int getBundleTotal(Bundle bundle) {
		if (bundle.hasTotal()) {
			return bundle.getTotal();
		}
		
		return 0;
	}
	
	/**
	 * Bundle Entry Component To Parameter Class
	 * 
	 * @param <T>
	 * @param parser
	 * @param component
	 * @param clazz
	 * @return
	 */
	public static <T> T fromResource(IParser parser, BundleEntryComponent component, Class<T> clazz) {
		String resourceStr = parser.encodeResourceToString(component.getResource());
		
		return JsonUtil.fromJson(resourceStr, clazz);
	}
	
	/**
	 * Resource To Parameter Class
	 * 
	 * @param <T>
	 * @param parser
	 * @param resource
	 * @param clazz
	 * @return
	 */
	public static<T> T fromResource(IParser parser, IBaseResource resource, Class<T> clazz) {
		String resourceStr = parser.encodeResourceToString(resource);
		
		return JsonUtil.fromJson(resourceStr, clazz);
	}
}
