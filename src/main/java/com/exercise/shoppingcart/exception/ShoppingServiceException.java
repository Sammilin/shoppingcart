package com.exercise.shoppingcart.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ShoppingServiceException extends RuntimeException {

	private String errorMessage;
	private HttpStatus httpStatus;

	public ShoppingServiceException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
}
