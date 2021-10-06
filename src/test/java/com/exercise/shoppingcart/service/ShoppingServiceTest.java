package com.exercise.shoppingcart.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.exercise.shoppingcart.dto.OrderItem;
import com.exercise.shoppingcart.dto.ProductResponse;
import com.exercise.shoppingcart.repository.CustomerRepository;
import com.exercise.shoppingcart.repository.ProductRepository;
import com.exercise.shoppingcart.repository.ShoppingOrderRepository;
import com.exercise.shoppingcart.repository.model.Customer;
import com.exercise.shoppingcart.repository.model.Product;
import com.exercise.shoppingcart.repository.model.ShoppingOrder;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ShoppingServiceTest {

	private static final long ORDER_ITEM_ID2 = 10222L;

	private static final long ORDER_ITEM_ID1 = 10111L;

	@InjectMocks
	private ShoppingServiceImpl shoppingService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private ShoppingOrderRepository shoppingOrderRepository;

	@Captor
	ArgumentCaptor<ShoppingOrder> shoppingOrderCaptor = null;

	@Test
	void getAllProductsSuccess() {

		List<Product> list = Arrays.asList(new Product(1L, "Product1", "Product1Detail", 2.99));
		when(productRepository.findAll()).thenReturn(list);

		List<ProductResponse> products = shoppingService.getAllProducts();

		assertTrue(products.size() == 1);
		verify(productRepository, times(1)).findAll();

	}

	@Test
	void saveOrderWithCustIdSuccess() {

		List<OrderItem> orderItems = setOrderItems();

		long custId = 100111;
		double requestAmount = 6.99;

		mockOrderItems();

		Customer customer = new Customer(custId, "a", "b", "ab", "123123123", "123 St.");
		when(customerRepository.findById(eq(custId))).thenReturn(Optional.of(customer));

		mockShoppingOrderSave();

		shoppingService.saveOrder("Joe Linch", "abc St", orderItems, custId, requestAmount);

		verify(shoppingOrderRepository, times(1)).save(shoppingOrderCaptor.capture());
		ShoppingOrder createOrder = shoppingOrderCaptor.getValue();

		assertTrue(createOrder.amount() == requestAmount);

		double validateAmount = createOrder.shoppingOrderItem().stream().mapToDouble(o -> o.salePrice() * o.quantity())
				.sum();

		assertTrue(validateAmount == requestAmount);

	}

	@Test
	void saveOrderWithoutCustIdSuccess() {

		long custId = 100111;
		double requestAmount = 6.99;

		Customer customer = new Customer(custId, "Joe", "Week", null, "123123123", "123 St.");

		mockOrderItems();

		mockShoppingOrderSave();

		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		shoppingService.saveOrder("Joe", "Week", "123123123", "123 St.", setOrderItems(), requestAmount);

		verify(shoppingOrderRepository, times(1)).save(shoppingOrderCaptor.capture());
		ShoppingOrder createOrder = shoppingOrderCaptor.getValue();

		assertTrue(createOrder.amount() == requestAmount);

		double validateAmount = createOrder.shoppingOrderItem().stream().mapToDouble(o -> o.salePrice() * o.quantity())
				.sum();

		assertTrue(validateAmount == requestAmount);

	}

	@Test
	void saveOrderWithCustIdFailCannotFindProduct() {

		long custId = 100111L;
		double amount = 6.99;

		Customer customer = new Customer(custId, "a", "b", "ab", "123123123", "123 St.");
		when(customerRepository.findById(eq(custId))).thenReturn(Optional.of(customer));

		Assertions.assertThrows(RuntimeException.class, () -> {
			shoppingService.saveOrder("Joe Linch", "abc St", setOrderItems(), custId, amount);
		});

		verify(shoppingOrderRepository, times(0)).save(any());

	}

	@Test
	void saveOrderWithoutCustIdFailWithCannotFindProduct() {

		long custId = 100111;
		double requestAmount = 6.99;

		Customer customer = new Customer(custId, "Joe", "Week", null, "123123123", "123 St.");

		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Assertions.assertThrows(RuntimeException.class, () -> {
			shoppingService.saveOrder("Joe", "Week", "123123123", "123 St.", setOrderItems(), requestAmount);
		});

		verify(shoppingOrderRepository, times(0)).save(any());

	}

	private void mockShoppingOrderSave() {
		ShoppingOrder shoppingOrder = new ShoppingOrder();
		shoppingOrder.id(20211003001L);

		when(shoppingOrderRepository.save(any(ShoppingOrder.class))).thenReturn(shoppingOrder);
	}

	private void mockOrderItems() {
		when(productRepository.findById(eq(10111L)))
				.thenReturn(Optional.of(new Product(10111L, "Product1", "Product1Detail", 2.99)));

		when(productRepository.findById(eq(10222L)))
				.thenReturn(Optional.of(new Product(10222L, "Product2", "Product1Detail", 3.99)));
	}

	private List<OrderItem> setOrderItems() {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();

		OrderItem o1 = new OrderItem(ORDER_ITEM_ID1, 2.99, 1);
		OrderItem o2 = new OrderItem(ORDER_ITEM_ID2, 2.00, 2);
		orderItems.add(o1);
		orderItems.add(o2);
		return orderItems;
	}
}
