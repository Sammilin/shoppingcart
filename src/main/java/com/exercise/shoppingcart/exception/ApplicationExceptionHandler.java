package com.exercise.shoppingcart.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ShoppingServiceException.class })
	protected ResponseEntity<Object> handleServiceException(ShoppingServiceException ex, WebRequest request) {
		System.out.println("InternalErr:" + ex.getErrorMessage());

		CustomException exceptionRs = new CustomException(ex.getErrorMessage(), request.getDescription(false),
				new Date());
		HttpStatus httpStatus = (ex.getHttpStatus() != null) ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

		return handleExceptionInternal(ex, exceptionRs, new HttpHeaders(), httpStatus, request);
	}

	@ExceptionHandler(value = { NumberFormatException.class })
	protected ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
		System.out.println("Bad Request:" + ex.getMessage());

		CustomException exceptionRs = new CustomException(ex.getMessage(), request.getDescription(false), new Date());
		return handleExceptionInternal(ex, exceptionRs, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		System.out.println("Validation Error Method getting executed!!!!");
		final List<String> details = new ArrayList<>();
		for (final ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		return new ResponseEntity("Validation Error", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleAllException(RuntimeException ex, WebRequest request) {

		CustomException exceptionRs = new CustomException(ex.getMessage(), request.getDescription(false), new Date());

		return handleExceptionInternal(ex, exceptionRs, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
