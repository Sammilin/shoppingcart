package com.exercise.shoppingcart.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.exercise.shoppingcart.repository.model.Customer;
import com.exercise.shoppingcart.repository.model.Product;
import com.exercise.shoppingcart.repository.model.ShoppingOrder;
import com.exercise.shoppingcart.repository.model.ShoppingOrderItem;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/before.sql")
//,@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/after.sql") 
})
public class ShoppingOrderRepositoryTest {

	private static final String PRODUCT1_NAME = "product1-test";
	private static final String PRODUCT2_NAME = "product2-test";
	private static final String PRODUCT3_NAME = "product3-test";

	private Product product1;
	private Product product2;
	private Product product3;

	@Resource
	private ProductRepository productRepository;
	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private ShoppingOrderRepository shoppingOrderRepository;

	@Test
	@Order(1)
	public void findAllOrders() {
		List<ShoppingOrder> orders = (List<ShoppingOrder>) shoppingOrderRepository.findAll();
		assertTrue(orders.size() == 2);
	}

	@Test
	@Order(2)
	public void createOrder() {
		mockProducts();
		List<Product> products = (List<Product>) productRepository.findAll();
		assertTrue(products.size() == 6);

		Customer cust = mockCustomer();

		ShoppingOrder shoppingOrder = mockCreateOrder(cust);

		assertEquals(2, shoppingOrder.shoppingOrderItem().size());

		shoppingOrder.shoppingOrderItem().stream().filter(p -> PRODUCT1_NAME.equals(p.product().name())).findAny()
				.ifPresentOrElse(o -> {
					assertTrue(9.99 == o.product().price());
					assertTrue(1 == o.quantity());
					assertTrue(7.99 == o.salePrice());

				}, () -> {
					fail("New order should be found PRODUCT1 ITEM");
				});
		List<ShoppingOrder> orders = (List<ShoppingOrder>) shoppingOrderRepository.findAll();
		assertTrue(orders.size() == 3);
	}

	private Customer mockCustomer() {
		Customer customer = new Customer("Mimi", "Geeks", "MG", "6571221111", "111 Young St.");
		return customerRepository.save(customer);
	}

	private ShoppingOrder mockCreateOrder(Customer customer) {
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		Set<ShoppingOrderItem> items = mockOrderItems();

		ShoppingOrder order = new ShoppingOrder("John Smith", "123 King St.", timestamp, timestamp, items, customer);

		double amount = 0;
		for (ShoppingOrderItem item : items) {
			amount += item.quantity() * item.salePrice();
		}
		order.amount(amount);
		order.discount(0);
		return shoppingOrderRepository.save(order);

	}

	private Set<ShoppingOrderItem> mockOrderItems() {
		ShoppingOrderItem itme1 = new ShoppingOrderItem().product(product1).quantity(1).salePrice(7.99);
		ShoppingOrderItem itme2 = new ShoppingOrderItem().product(product2).quantity(1).salePrice(product2.price());
		Set<ShoppingOrderItem> result = new HashSet<ShoppingOrderItem>();
		result.add(itme1);
		result.add(itme2);
		return result;

	}

	private void mockProducts() {
		product1 = new Product(PRODUCT1_NAME, "product1 detail", Double.valueOf(9.99));
		product2 = new Product(PRODUCT2_NAME, "product2 detail", Double.valueOf(2.99));
		product3 = new Product(PRODUCT3_NAME, "product3 detail", Double.valueOf(3.99));
		productRepository.saveAll(Arrays.asList(product1, product2, product3));

	}
}
