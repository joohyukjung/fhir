package ca.uhn.fhir.jpa.starter.vo.code;

import java.util.Arrays;

import lombok.Getter;

/**
 * FHIR Resource DataSet Code 
 * 
 * @author Cheol-Ho Choi
 */

@Getter
public enum DatasetCode {

	PATIENT("DS01", "Patient", "환자정보")
  , ORGANIZATION("DS02", "Organization", "의료기관정보")
  , PRACTITIONER("DS03", "Practitioner", "진료의정보")
  , CONDITION("DS04", "Condition", "진단내역")
  , MEDICATIONREQUEST("DS05", "MedicationRequest", "약물처방내역")
  , LABORATORY("DS06", "Laboratory", "진단검사")
  , IMAGING("DS07", "Imaging", "영상검사")
  , PATHOLOGY("DS08", "Pathology", "병리검사")
  , VITAL_SIGNS("DS09", "VitalSigns", "기타검사")
  , PROCEDURE("DS10", "Procedure", "수술내역")
  , ALLERGYINTOLERANCE("DS11", "AllergyIntolerance", "알러지 및 부작용")
  , DOCUMENTREFERENCE("DS12", "DocumentReference", "진료기록")
  
  ;
	
	private String datasetCode;
	private String datasetEngName;
	private String datasetKorName;
	
	DatasetCode(String datasetCode, String datasetEngName, String datasetKorName) {
		this.datasetCode = datasetCode;
		this.datasetEngName = datasetEngName;
		this.datasetKorName = datasetKorName;
	}
	
	public static DatasetCode find(String code) {
		return Arrays.stream(values()).filter(dataSet -> dataSet.datasetCode.equals(code)).findAny().orElse(null);
	}
}
