package com.nagarro.nagp.model;

import java.io.Serializable;

public class RequestHeaderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3811460097762390978L;
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name;
	private int quantity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
