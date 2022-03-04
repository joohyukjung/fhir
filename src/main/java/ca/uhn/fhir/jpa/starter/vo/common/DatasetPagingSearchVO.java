package ca.uhn.fhir.jpa.starter.vo.common;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class DatasetPagingSearchVO {
	
	/** 제공기관 코드 */
//	private String provideInstitutionCode;
	
	/** MH사용자 ID */
	@NotBlank
	private String userId;

	/** 주민번호 */
	@NotNull
	@Pattern(regexp="^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|[3][01])\\-[1-4][0-9]{6}$",
			 message="Check residentRegistrationNumber Pattern.")
	private String residentRegistrationNumber;
	
	/** 서비스 번호 */
	private String serviceUID;
	
	/** 서비스 구분자 */
	private String serviceDivision;
	
	/** 조회기간 검색 Key */
	private String inquiryPeriodSearchKey;
	
	/** 조회시작일 yyyy-MM-dd */
	@Pattern(regexp="^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|[12][0-9]|3[01])$",
			 message="Check beginningDate Pattern.")
	private String beginningDate;
	
	/** 조회종료일 yyyy-MM-dd */
	@Pattern(regexp="^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|[12][0-9]|3[01])$",
			 message="Check endDate Pattern.")
	private String endDate;
	
	/** 페이징 번호 (0 : 전체조회)*/
	@Min(0)
	private int inquiryPageNumber = 0;
	
	/** 페이징 조회 건수 
	 * default : 10
	 */
	private int onePageInquiryCase = 10;
	
}
