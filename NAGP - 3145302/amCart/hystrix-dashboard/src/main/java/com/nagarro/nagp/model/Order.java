package com.nagarro.nagp.model;

public class Order {
	private String id;
	private String name;
	private int amount;
	private int quantity;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String toString() {
		return "ID: "+getId()+ " Name: "+getName()+ " Amount: "+getAmount()+" Quantity: "+getQuantity();
	}
}
