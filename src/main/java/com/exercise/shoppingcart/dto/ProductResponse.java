package com.exercise.shoppingcart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	private long id;

	private String name;

	private String description;

	private double price;

}
