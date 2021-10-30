package com.exercise.shoppingcart.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.exercise.shoppingcart.dto.OrderItem;
import com.exercise.shoppingcart.repository.CustomerRepository;
import com.exercise.shoppingcart.repository.ProductRepository;
import com.exercise.shoppingcart.repository.ShoppingOrderRepository;
import com.exercise.shoppingcart.repository.model.Product;
import com.exercise.shoppingcart.web.model.ShoppingOrderRequest;
import com.exercise.shoppingcart.web.model.ShoppingOrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/before.sql") })
//		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/after.sql") })
public class ShoppingCarControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ShoppingOrderRepository shoppingOrderRepository;

	@AfterEach
	void after() {
		shoppingOrderRepository.deleteAll();
		customerRepository.deleteAll();
		productRepository.deleteAll();
	}

	@Test
	@Order(1)
	void getAllProducts() throws Exception {

		mockMvc.perform(get("/v1/api/products").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.products[0].id", is(100001)))
				.andExpect(jsonPath("$.products.length()", is((int) productRepository.count())));

	}

	@Test
	@Order(2)
	@Transactional
	void createOrder() throws Exception {

		List<Product> products = (List<Product>) productRepository.findAll();

		List<OrderItem> orderItems = Arrays.asList(new OrderItem(products.get(0).id(), products.get(0).price(), 1),
				new OrderItem(products.get(2).id(), 1.25, 2));

		double amount = orderItems.stream().mapToDouble(o -> o.getQuantity() * o.getSalePrice()).sum();

		ShoppingOrderRequest request = new ShoppingOrderRequest("Allen", "Wang", "(647)111-2133",
				"55 Queens Quay W, Toronto", orderItems, 0, amount);

		MvcResult mvcResult = mockMvc.perform(post("/v1/api/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andDo(print()).andReturn();
		assertTrue(mvcResult.getResponse().getStatus() == 200);
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Success"));

		ShoppingOrderResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				ShoppingOrderResponse.class);
		long orderId = response.getOrderId();
		shoppingOrderRepository.findById(orderId).ifPresentOrElse(s -> {
			assertTrue(s.amount() == amount);
		}, () -> {
			fail("New OrderId Not Found");
		});

	}

	@Test
	@Order(3)
	void getProdcutById() throws Exception {

		mockMvc.perform(get("/v1/api/products/{productId}", 100001).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(100001)));

	}

	@Test
	void createOrderFailByNonExistingProduct() throws JsonProcessingException, Exception {

		List<OrderItem> orderItems = Arrays.asList(new OrderItem(1L, 2.44, 1), new OrderItem(2L, 1.25, 2));

		double amount = orderItems.stream().mapToDouble(o -> o.getQuantity() * o.getSalePrice()).sum();

		ShoppingOrderRequest request = new ShoppingOrderRequest("Allen", "Wang", "(647)111-2133",
				"55 Queens Quay W, Toronto", orderItems, 0, amount);

		mockMvc.perform(post("/v1/api/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.message", is("Order - Can't find product")));

	}
}
