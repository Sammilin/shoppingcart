package com.exercise.shoppingcart.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingOrderResponse {

	@JsonProperty(value = "orderId")
	private long orderId;

	@JsonProperty(value = "message", required = true)
	private String message;
}
