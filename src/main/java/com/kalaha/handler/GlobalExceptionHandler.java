package com.kalaha.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.kalaha.exception.GameNotFoundException;
import com.kalaha.exception.KalahaClientException;
import com.kalaha.exception.KalahaServerException;
import com.kalaha.model.response.ErrorResponse;

/**
 * Global Handler for Handling RestController Errors
 * and return and standard error format
 * @author Praveen
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Custom error handler for game id not found
	 *
	 * @param exception
	 * @param request
	 * @return the response entity
	 */
	public ResponseEntity<ErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
				new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Custom error handler for Client side error
	 *
	 * @param exception
	 * @param request
	 * @return the response entity
	 */
	@ExceptionHandler(KalahaClientException.class)
	public ResponseEntity<ErrorResponse> customHandleClient(Exception ex, WebRequest request) {
		if (ex instanceof GameNotFoundException)
			return customHandleNotFound(ex, request);
		return new ResponseEntity<>(
				new ErrorResponse(LocalDateTime.now(), HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage()),
				HttpStatus.PRECONDITION_FAILED);
	}

	/**
	 * Custom error handler for Server side error
	 *
	 * @param exception
	 * @param request
	 * @return the response entity
	 */
	@ExceptionHandler(KalahaServerException.class)
	public ResponseEntity<ErrorResponse> customHandleServer(Exception ex, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				String.format("Error in server : %s", ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> customHandleGenericServerError(Exception ex, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Error in Server: Kindly reach out to application owner for more information"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
