package ca.uhn.fhir.jpa.starter.util;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;

public class AgreementUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgreementUtil.class);
	
	// 단일 코드 체크
	public static boolean checkAgreement(List<AgreementDatasetVO> agreementList, String datasetCode) {
		List<String> datsetList = agreementList.stream().filter(data -> data.getAgreementStatus().equals(AgreementCode.AGREEMENT.getCode()))
								 .map(AgreementDatasetVO :: getDatasetCode).collect(Collectors.toList());
		
		if (datsetList.contains(datasetCode)) {
			return true;
		}
		return false;
	}
	
	// 코드 리스트 체크
	public static boolean checkAgreement(List<AgreementDatasetVO> agreementList, List<String> datasetCode) {
		List<String> datsetList = agreementList.stream().filter(data -> data.getAgreementStatus().equals(AgreementCode.AGREEMENT.getCode()))
								 .map(AgreementDatasetVO :: getDatasetCode).collect(Collectors.toList());
		
		boolean check = true;
		for (String code : datasetCode) {
			if (! datsetList.contains(code)) {
				check = false;
			}
		}
		
		return check;
	}
}
