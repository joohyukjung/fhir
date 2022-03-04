package ca.uhn.fhir.jpa.starter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import ca.uhn.fhir.jpa.starter.vo.common.DatasetPagingSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;

public class ValidationUtil {
	private static final String residentRegistrationNumberPattern = "^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|[3][01])\\-[1-4][0-9]{6}$";
	private static final String datePattern = "^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|[12][0-9]|3[01])$";
	private static final SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

	// 의료데이터 조회 정합성 체크(리스트)
	public static boolean isConditionCheck(DatasetSearchVO condition) throws ParseException {
		// 제공기관 코드
		if (!isListNotEmpty(condition.getProvideInstitutionCode())) {
			return false;
		}

		// MH User ID
		if (!isNotBlank(condition.getUserId())) {
			return false;
		}

		// 주민번호
		if (!isPattern(condition.getResidentRegistrationNumber(), residentRegistrationNumberPattern)) {
			return false;
		}

		// 조회 시작일
		if (isNotNull(condition.getBeginningDate())) {
			if (!isPattern(condition.getBeginningDate(), datePattern)) {
				return false;
			}
		}

		// 조회 종료일
		if (isNotNull(condition.getEndDate())) {
			if (!isPattern(condition.getEndDate(), datePattern)) {
				return false;
			}
		}
		
		// 조회 기간 비교 (시작일 <= 종료일)
		if (isNotNull(condition.getBeginningDate()) && isNotNull(condition.getEndDate())) {
			Date beginningDate = transFormat.parse(condition.getBeginningDate());
			Date endDate = transFormat.parse(condition.getEndDate());
			if (!isMaxDate(beginningDate, endDate)) {
				return false;
			}
		}

		return true;
	}

	// 의료데이터 조회 정합성 체크(페이징)
	public static boolean isConditionCheck(DatasetPagingSearchVO condition) throws ParseException {
		// MH User ID
		if (!isNotBlank(condition.getUserId())) {
			return false;
		}

		// 주민번호
		if (!isPattern(condition.getResidentRegistrationNumber(), residentRegistrationNumberPattern)) {
			return false;
		}

		// 조회 시작일
		if (isNotNull(condition.getBeginningDate())) {
			if (!isPattern(condition.getBeginningDate(), datePattern)) {
				return false;
			}
		}

		// 조회 종료일
		if (isNotNull(condition.getEndDate())) {
			if (!isPattern(condition.getEndDate(), datePattern)) {
				return false;
			}
		}

		// 조회 기간 비교 (시작일 <= 종료일)
		if (isNotNull(condition.getBeginningDate()) && isNotNull(condition.getEndDate())) {
			Date beginningDate = transFormat.parse(condition.getBeginningDate());
			Date endDate = transFormat.parse(condition.getEndDate());
			if (!isMaxDate(beginningDate, endDate)) {
				return false;
			}
		}
		
		// 페이지 번호
		if (!isNotNull(condition.getInquiryPageNumber())) {
			return false;
		} else if (!isMin(condition.getInquiryPageNumber(), 0)) {
			return false;
		}
		
		return true;
	}

	// 조회 검색 키
	public static boolean isSearchKey(String inquiryPeriodSearchKey, String code) {
		if (isNotBlank(inquiryPeriodSearchKey)) {
			if (!inquiryPeriodSearchKey.equals(code)) {
				return false;
			}
		}

		return true;
	}
	
	// Not Empty
		public static <T> boolean isListNotEmpty(List<T> strList) {
			if (strList != null) {
				if (!strList.isEmpty())
					return true;
			}
			return false;
		}
		
	// Not Null
	private static <T> boolean isNotNull(T data) {
		if (data != null) {
			return true;
		}
		return false;
	}

	// Not Blank
	private static boolean isNotBlank(String str) {
		if (str != null) {
			if (str != "") {
				return true;
			}
		}
		return false;
	}


	// Min
	private static boolean isMin(int value, int min) {
		if (value >= min) {
			return true;
		}
		return false;
	}

	// Pattern
	private static boolean isPattern(String str, String pattern) {
		if (str != null) {
			if (Pattern.matches(pattern, str)) {
				return true;
			}
		}
		return false;
	}

	// Max Date
	private static boolean isMaxDate(Date date, Date maxDate) {
		if (date.before(maxDate)) {
			return true;
		} else if (date.equals(maxDate)) {
			return true;
		}

		return false;
	}
}
