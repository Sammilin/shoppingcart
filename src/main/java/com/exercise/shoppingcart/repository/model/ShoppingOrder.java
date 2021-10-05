package com.exercise.shoppingcart.repository.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SHOPPINGORDER")
public class ShoppingOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORDER_ID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "RECIPIENT")
	private String recipient;
	@Column(name = "SHIPPING_ADDR")
	private String shippingAddress;

	@Column(name = "ORDER_DATETIME")
	private Timestamp orderDatetime;

	@Column(name = "LASTMODIFIED")
	private Timestamp lastmodified;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "shoppingOrder")
	private Set<ShoppingOrderItem> shoppingOrderItem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUST_ID")
	private Customer customer;

	@Column(name = "AMOUNT")
	private double amount;
	@Column(name = "DISCOUNT")
	private double discount;

	public ShoppingOrder(String recipient, String shippingAddress, Timestamp orderDatetime, Timestamp lastmodified,
			Set<ShoppingOrderItem> shoppingOrderItem, Customer customer) {
		super();
		this.recipient = recipient;
		this.shippingAddress = shippingAddress;
		this.orderDatetime = orderDatetime;
		this.lastmodified = lastmodified;
		this.shoppingOrderItem = shoppingOrderItem;
		this.customer = customer;
	}

	public ShoppingOrder(String recipient, String shippingAddress, Timestamp orderDatetime, Timestamp lastmodified,
			Set<ShoppingOrderItem> shoppingOrderItem) {
		super();
		this.recipient = recipient;
		this.shippingAddress = shippingAddress;
		this.orderDatetime = orderDatetime;
		this.lastmodified = lastmodified;
		this.shoppingOrderItem = shoppingOrderItem;
	}

	public ShoppingOrder(String recipient, String shippingAddress, Timestamp orderDatetime, Timestamp lastmodified,
			Set<ShoppingOrderItem> shoppingOrderItem, Customer customer, double amount, double discount) {
		super();
		this.recipient = recipient;
		this.shippingAddress = shippingAddress;
		this.orderDatetime = orderDatetime;
		this.lastmodified = lastmodified;
		this.shoppingOrderItem = shoppingOrderItem;
		this.customer = customer;
		this.amount = amount;
		this.discount = discount;
	}

}
