package com.exercise.shoppingcart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exercise.shoppingcart.dto.OrderItem;
import com.exercise.shoppingcart.dto.ProductResponse;

@Service
public interface ShoppingService {
	List<ProductResponse> getAllProducts();

	long saveOrder(String recipient, String address, List<OrderItem> orderItems, long custId, double amount);

	long saveOrder(String firstName, String lastName, String phone, String address, List<OrderItem> orderItems,
			double amount);

}
