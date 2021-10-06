package com.exercise.shoppingcart.exception;

import java.util.Date;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomException {

	private String message;
	private String path;
	private Date timestamp;

	public CustomException(String message, String path, Date timestamp) {
		super();
		this.message = message;
		this.path = path;
		this.timestamp = new Date(timestamp.getTime());
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public Date getTimestamp() {
		return new Date(timestamp.getTime());
	}

}
