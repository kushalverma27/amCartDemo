package com.nagarro.nagp.model;

public class Tracking {
	
	private String id;
	private String orderTime;
	private String deliveryTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	public String toString() {
		return "ID: "+getId()+" Order Time: "+getOrderTime()+ " Delivery TIme: "+getDeliveryTime();
	}

}
