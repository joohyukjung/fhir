package ca.uhn.fhir.jpa.starter.myhw.exception;

import org.springframework.http.HttpStatus;

import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AgreementException extends RuntimeException {
	
	/**	UID */
	private static final long serialVersionUID = 3195772478341842175L;

	private HttpStatus satuts = HttpStatus.INTERNAL_SERVER_ERROR;
	private String message;
	private Throwable cause;
	
	public AgreementException() {}

	public AgreementException(HttpStatus satuts) {
		this.satuts = satuts;
	}
	
	public AgreementException(String message) {
		this.message = message;
	}

	public AgreementException(String userId, DatasetCode dataSet) {
		this.message = createNotAgreementMessage(userId, dataSet);
	}
	
	public AgreementException(HttpStatus satuts, String message) {
		this.satuts = satuts;
		this.message = message;
	}


	public AgreementException(HttpStatus satuts, String message, Throwable cause) {
		this.satuts = satuts;
		this.message = message;
		this.cause = cause;
	}

	public AgreementException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}
	
	private static String createNotAgreementMessage(String userId, DatasetCode dataSet) {
		return "ID(" + userId + ") did not agree to the " + dataSet.getDatasetEngName() + " information.";
	}
}
