package com.exercise.shoppingcart.web.model;

import java.util.List;

import com.exercise.shoppingcart.dto.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingOrderRequest {

	@JsonProperty(value = "firstName", required = true)
	private String firstName;

	@JsonProperty(value = "lastName", required = true)
	private String lastName;

	@JsonProperty(value = "phone")
	private String phone;

	@JsonProperty(value = "shippingAddress", required = true)
	private String address;
	@JsonProperty(value = "orderItems", required = true)
	private List<OrderItem> orderItems;

	@JsonProperty(value = "custId", required = false)
	private long custId;

	@JsonProperty(value = "amount")
	private double amount;

}
