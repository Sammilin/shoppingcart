package com.exercise.shoppingcart.web.model;

import java.util.List;

import com.exercise.shoppingcart.dto.ProductResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllProductisResponse {

	@JsonProperty(value = "products")
	private List<ProductResponse> products;
}
