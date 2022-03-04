package ca.uhn.fhir.jpa.starter.vo.agreement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2022-01-24 최초작성
 * @author smlee
 *
 */
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class UserAgreementVO {

	private String userId;	// 사용자ID
	
	private String agreementStatus;	// 동의상태
	
	private String residentRegistrationNumber;	// 사용자 주민번호

}
