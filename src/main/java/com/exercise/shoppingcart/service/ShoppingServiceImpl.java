package com.exercise.shoppingcart.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.shoppingcart.dto.OrderItem;
import com.exercise.shoppingcart.dto.ProductResponse;
import com.exercise.shoppingcart.repository.CustomerRepository;
import com.exercise.shoppingcart.repository.ProductRepository;
import com.exercise.shoppingcart.repository.ShoppingOrderRepository;
import com.exercise.shoppingcart.repository.model.Customer;
import com.exercise.shoppingcart.repository.model.Product;
import com.exercise.shoppingcart.repository.model.ShoppingOrder;
import com.exercise.shoppingcart.repository.model.ShoppingOrderItem;

@Service
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ShoppingOrderRepository shoppingOrderRepository;

	@Override
	public List<ProductResponse> getAllProducts() {
		List<ProductResponse> result = new ArrayList<ProductResponse>();

		Iterable<Product> products = productRepository.findAll();
		products.forEach(p -> {
			result.add(new ProductResponse(p.id(), p.name(), p.description(), p.price()));
		});
		return result;
	}

	@Override
	public long saveOrder(String recipient, String address, List<OrderItem> orderItems, long custId, double amount) {
		Customer customer = null;
		Optional<Customer> existCustomer = customerRepository.findById(custId);

		if (existCustomer.isPresent()) {
			customer = existCustomer.get();
		}

		ShoppingOrder order = setCreateOrder(recipient, address, orderItems, amount, customer);
		return order.id();
	}

	@Override
	public long saveOrder(String firstName, String lastName, String phone, String address, List<OrderItem> orderItems,
			double amount) {

		Customer customer = customerRepository.save(new Customer(firstName, lastName, null, phone, address));

		ShoppingOrder order = setCreateOrder(firstName + " " + lastName, address, orderItems, amount, customer);
		return order.id();
	}

	private ShoppingOrder setCreateOrder(String recipient, String address, List<OrderItem> orderItems, double amount,
			Customer customer) {
		final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

		Set<ShoppingOrderItem> items = new HashSet<ShoppingOrderItem>();
		for (OrderItem o : orderItems) {
			items.add(setOrderItem(o.getProductId(), o.getQuantity(), o.getSalePrice()));
		}
		ShoppingOrder order = new ShoppingOrder(recipient, address, timestamp, timestamp, items);

		if (customer != null) {
			order.customer(customer);
		}
		order.amount(amount);
		shoppingOrderRepository.save(order);
		return order;
	}

	private ShoppingOrderItem setOrderItem(long productId, int quantity, double salePrice) {

		ShoppingOrderItem item = new ShoppingOrderItem();
		productRepository.findById(productId).ifPresentOrElse(p -> {
			item.product(p);
		}, () -> {
			// TODO: change by exception handler
			throw new RuntimeException("Can't find product");
		});
		item.quantity(quantity);
		item.salePrice(salePrice);
		return item;
	}

}
