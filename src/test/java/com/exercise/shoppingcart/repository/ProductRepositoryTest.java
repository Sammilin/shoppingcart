package com.exercise.shoppingcart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.exercise.shoppingcart.repository.model.Product;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

	private static final String PRODUCT1_NAME = "product1";
	private static final String PRODUCT2_NAME = "product2";
	private static final String PRODUCT3_NAME = "product3";

	@Resource
	private ProductRepository productRepository;

	@BeforeEach
	public void start() {
		mockProducts();
	}

	@AfterEach
	public void teardown() {
		productRepository.deleteAll();
	}

	@Test
	public void findAllProdcuts() {

		List<Product> products = (List<Product>) productRepository.findAll();
		assertTrue(products.size() == 3);

	}

	@Test
	public void findProductByNameAndId() {
		productRepository.findByName(PRODUCT1_NAME).ifPresentOrElse(p -> {
			long pid = p.id();
			productRepository.findById(pid).ifPresentOrElse(prod -> {
				assertEquals(PRODUCT1_NAME, prod.name());
			}, () -> {
				fail("Product Result is not match");
			});
		}, () -> {
			fail("Can't find the product");
		});
	}

	private void mockProducts() {

		Product p1 = new Product(PRODUCT1_NAME, "product1 detail", Double.valueOf(9.99));
		Product p2 = new Product(PRODUCT2_NAME, "product2 detail", Double.valueOf(2.99));
		Product p3 = new Product(PRODUCT3_NAME, "product3 detail", Double.valueOf(3.99));
		productRepository.saveAll(Arrays.asList(p1, p2, p3));

	}
}
