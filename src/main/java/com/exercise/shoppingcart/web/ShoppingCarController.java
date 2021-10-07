package com.exercise.shoppingcart.web;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.shoppingcart.dto.ProductResponse;
import com.exercise.shoppingcart.service.ShoppingService;
import com.exercise.shoppingcart.web.model.GetAllProductisResponse;
import com.exercise.shoppingcart.web.model.ShoppingOrderRequest;
import com.exercise.shoppingcart.web.model.ShoppingOrderResponse;

@RestController
@RequestMapping("/v1/api")
public class ShoppingCarController {

	@Autowired
	ShoppingService shoppingService;

	@GetMapping("/products")
	public GetAllProductisResponse getProducts() {

		return new GetAllProductisResponse(shoppingService.getAllProducts());
	}

	@GetMapping("/products/{productId}")
	public ProductResponse getProduct(@PathVariable(value = "productId", required = true) String id) {

		return shoppingService.getProductById(Long.valueOf(id));
	}

	@PostMapping("/orders")
	public ShoppingOrderResponse createOrder(@Valid @RequestBody ShoppingOrderRequest shoppingOrderRequest) {

		long orderId;

		if (shoppingOrderRequest.custId() == 0 || Objects.isNull(shoppingOrderRequest.custId())) {
			orderId = shoppingService.saveOrder(shoppingOrderRequest.firstName(), shoppingOrderRequest.lastName(),
					shoppingOrderRequest.phone(), shoppingOrderRequest.address(), shoppingOrderRequest.orderItems(),
					shoppingOrderRequest.amount());
		} else {
			orderId = shoppingService.saveOrder(
					shoppingOrderRequest.firstName() + " " + shoppingOrderRequest.lastName(),
					shoppingOrderRequest.address(), shoppingOrderRequest.orderItems(), shoppingOrderRequest.custId(),
					shoppingOrderRequest.amount());
		}

		return new ShoppingOrderResponse(orderId, "Success");
	}

}
