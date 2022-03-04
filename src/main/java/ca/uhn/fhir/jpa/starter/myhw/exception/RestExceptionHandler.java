package ca.uhn.fhir.jpa.starter.myhw.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		
		ApiError apiError = new ApiError(BAD_REQUEST, request.getDescription(false));
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError, request);
	} 
	
	@ExceptionHandler(ca.uhn.fhir.jpa.starter.myhw.exception.DuplicateException.class)
	protected ResponseEntity<Object> handleDuplicate(ca.uhn.fhir.jpa.starter.myhw.exception.DuplicateException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		
		ApiError apiError = new ApiError(CONFLICT, request.getDescription(false));
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError, request);
	}
	
	@ExceptionHandler(ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFound(ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		
		ApiError apiError = new ApiError(NOT_FOUND, request.getDescription(false));
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError, request);
	}
	
	@ExceptionHandler(ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException.class)
	protected ResponseEntity<Object> handleAgreement(ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		
		ApiError apiError = new ApiError(ex.getSatuts(), request.getDescription(false));
		
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError, request);
	}
	
	@ExceptionHandler({Exception.class, RuntimeException.class})
	protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, request.getDescription(false));
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError, request);
	}
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError, WebRequest request) {
		ServletWebRequest req = (ServletWebRequest) request;
		apiError.setHttpMethod(req.getHttpMethod());
		return ResponseEntity.status(apiError.getStatus()).body(apiError);
	}
	
}
