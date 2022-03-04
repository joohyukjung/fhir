package ca.uhn.fhir.jpa.starter.myhw.message;

import org.springframework.stereotype.Component;

import ca.uhn.fhir.jpa.starter.myhw.exception.MyhwBizException;

/**
 * 
 * @ClassName : ExMessageUtil.java
 * @Description :
 * @Modification 
 * 수정일 수정자 수정내용 
 * --------- ------- ------------------------ 
 * 2022.1. 3 iteyes-hskim 최초작성
 *
 * @author iteyes-hskim
 * @since 2022. 1. 3
 * @version 1.0
 * @see
 */
@Component
public class ExMessageUtil {

	public static MyhwBizException createMyhwBizException(String errorCode) {
		return new MyhwBizException(errorCode, generateMessage(errorCode));
	}

	public static MyhwBizException createMyhwBizExceptionn(String errorCode, Object[] params) {
		return new MyhwBizException(errorCode, generateMessage(errorCode, params));
	}

	private static String generateMessage(String errorCode) {
		String errorMessage = MyhwMessageUtil.getMessage(errorCode);
		return errorMessage != null ? errorMessage : null;
	}

	private static String generateMessage(String errorCode, Object[] params) {
		String errorMessage = MyhwMessageUtil.generateMessage(errorCode, params);
		return errorMessage != null ? errorMessage : null;
	}

}