package ca.uhn.fhir.jpa.starter.myhw.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
	private final Date timestamp;
	private int status;
	private String error;
	private String path;
	private String message;
	private HttpMethod httpMethod;
	private List<ApiSubError> subErrors;
	
	public ApiError(HttpStatus status, String path) {
		this.status = status.value();
		this.error = status.getReasonPhrase();
		this.path = path;
		this.timestamp = new Date();
	}
	
	public String getPath() {
		if(path == null) return "";
		return path.substring(path.indexOf("/"));
	}
	
	public String getMessage() {
		return (message == null) ? "No message available" : message;
	}
	
	private void addValidationError(ConstraintViolation<?> cv) {
		this.addValidationError(cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
				cv.getInvalidValue(),
				cv.getMessage());
	}
	
	public void addValidationError(String object, String field, Object rejectedValue, String message) {
		addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}
	
	private void addSubError(ApiSubError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<ApiSubError>();
		}
		subErrors.add(subError);
	}

	public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addValidationError);
	}
}
