package ca.uhn.fhir.jpa.starter.myhw.exception;

import lombok.Data;

/**
 * @ClassName   : MyhwBizException.java
 * @Description : Myhw에서 사용하는 Exception
 * @Modification
 * @
 * @ 수정일	    수정자	    수정내용
 * @--------- ------- ------------------------
 * 2021.11.24 hblee   최초작성
 *
 * @author hblee
 * @since 2021. 11. 24
 * @version 1.0
 * @see
 */
@Data
public class MyhwBizException extends RuntimeException {

	/** UID */
	private static final long serialVersionUID = -5354793725111923383L;
	
	private String code;
	private String message;
	private Throwable cause;

	public MyhwBizException(String code) {
		this.code = code;
	}

	public MyhwBizException(String code, String param) {
		this.code = code;
		this.message = param;
	}

	public MyhwBizException(String code, String param, Throwable cause) {
		this.code = code;
		this.message = param;
		this.cause = cause;
	}

	public MyhwBizException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}

}