package ca.uhn.fhir.jpa.starter.resource;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name="PatientDatasetResource")
public class PatientDatasetResource extends DomainResource implements Comparable<PatientDatasetResource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Child(name="provideInstitutionCode", min=1, max=1, order=0)
	private StringType provideInstitutionCode;
	
	@Child(name="patient", type={ Patient.class })
	private Patient patient;
	
	@Override
	public PatientDatasetResource copy() {
		//
		PatientDatasetResource retVal = new PatientDatasetResource();
		
		retVal.provideInstitutionCode = provideInstitutionCode;
		retVal.patient = patient;
		
		return retVal;
	}

	@Override
	public ResourceType getResourceType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public void setProvideInstitutionCode(StringType provideInstitutionCode) {
		this.provideInstitutionCode = provideInstitutionCode;
	}

	@Override
	public int compareTo(PatientDatasetResource o) {
		//
		return this.provideInstitutionCode.toString().compareTo(o.provideInstitutionCode.toString());
	}
}
