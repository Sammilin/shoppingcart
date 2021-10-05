package com.exercise.shoppingcart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exercise.shoppingcart.repository.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
