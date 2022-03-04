package ca.uhn.fhir.jpa.starter.myhw.message;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName : MyhwMessageUtil.java
 * @Description :
 * @Modification 
 * 수정일 수정자 수정내용
 *  --------- ------- ------------------------ 
 *  2022. 1. 4 iteyes-hskim 최초작성
 *
 * @author iteyes-hskim
 * @since 2022. 1. 4
 * @version 1.0
 * @see
 */
@Component
public class MyhwMessageUtil {

	@Autowired
	private MyhwMessageSource myhwMessageSource;

	private static MyhwMessageSource sMyhwMessageSource;

	@PostConstruct
	public void instance() {
		MyhwMessageUtil.sMyhwMessageSource = myhwMessageSource;
	}

	public static String getMessage(String code) {

		String strMsg = sMyhwMessageSource.getMessage(code);
		return strMsg != null ? strMsg : null;
	}

	public static String getMessage(String code, String obj) {

		String strMsg = sMyhwMessageSource.getMessageArgs(code, new String[] { obj });

		return strMsg;
	}

	public static String getMessage(String code, String[] args) {

		String strMsg = sMyhwMessageSource.getMessageArgs(code, args);

		return strMsg;
	}

	public static String generateMessage(String errorCode, Object[] params) {
		String errorMessage = getMessage(errorCode);
		if (errorMessage == null)
			return null;

		if (params != null) {
			errorMessage = applyFormat(errorMessage, params);
		}

		return errorMessage;
	}

	private static String applyFormat(String errorMessage, Object[] params) {
		MessageFormat mf = new MessageFormat(errorMessage);
		return mf.format(params);
	}

}
