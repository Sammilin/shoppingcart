package com.exercise.shoppingcart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exercise.shoppingcart.repository.model.ShoppingOrder;

@Repository
public interface ShoppingOrderRepository extends CrudRepository<ShoppingOrder, Long> {

}
