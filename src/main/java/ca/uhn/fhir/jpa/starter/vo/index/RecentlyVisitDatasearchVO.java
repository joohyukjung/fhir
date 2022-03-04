package ca.uhn.fhir.jpa.starter.vo.index;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RecentlyVisitDatasearchVO {
	// MH사용자ID
	@Size(min = 0, max = 9)
	private String userId;
	
	 /** 사용자이름 */
	private String userName; 
	
	// 활용서비스ID
	@Size(min = 0, max = 10)
	private String utilizationServiceCode;
	
	// 기준일자
	@Size(min = 8, max = 8)
	@NotBlank(message = "baseDate must not be blank")
	private String baseDate;
	
	// 제공기관코드
	private List<String> provideInstitutionCode;

	// 사용자 주민번호
	private String residentRegistrationNumber; // ???

}
