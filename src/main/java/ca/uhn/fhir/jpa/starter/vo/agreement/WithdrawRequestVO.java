package ca.uhn.fhir.jpa.starter.vo.agreement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2022-02-03 최초작성
 * 
 * @author smlee
 *
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class WithdrawRequestVO {

	private String userId; 	/** MH사용자 ID */
	
	private String userName; /** 사용자이름 */

	private String utilizationServiceCode; /** 활용서비스 코드 */
	
	private String residentRegistrationNumber; /** 주민번호 */

	private String withdrawDateTime; /** 철회 일시 */

}
