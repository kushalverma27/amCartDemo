package com.nagarro.nagp.model;

import java.io.Serializable;

public class Product implements Serializable{
  /**
	 * 
	 */

private static final long serialVersionUID = 9108206774818085442L;
private String name;
private int id;
private int quantity;
private int amount;
  
  
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}

public String toString() {
	 return "Details:ID:" +getId()+" Name "+getName()+" Amount: "+getAmount()+" Quantity "+getQuantity();
	 
}
}
