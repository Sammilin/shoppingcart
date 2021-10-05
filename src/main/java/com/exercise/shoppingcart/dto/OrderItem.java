package com.exercise.shoppingcart.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class OrderItem {

	private long productId;
	private double salePrice;
	private int quantity;
}
