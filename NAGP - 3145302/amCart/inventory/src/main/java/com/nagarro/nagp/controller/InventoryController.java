package com.nagarro.nagp.controller;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.nagp.InventoryImpl;
import com.nagarro.nagp.ProductJDBCRepository;
import com.nagarro.nagp.model.RequestHeaderDTO;

@RestController
@RefreshScope
public class InventoryController {


	@Autowired
	ProductJDBCRepository jdbcRepo;
	
	@Value("${name:unknown}")
	private String msg;
	
	public InventoryController() {
		System.out.println("initialized");
	}
	
	@Autowired
	InventoryImpl consumer;
	
	
	@RequestMapping(value= "/inventory",  method = RequestMethod.POST)
	 public String sendMsg(@RequestBody RequestHeaderDTO request) throws InterruptedException, JSONException{
		//request.setId(getRandomHexString(16));
		Message msg = new Message(SerializationUtils.serialize(request), null);
	 consumer.recievedMessage(msg);
	 return "Done";
	 }
	
	@RequestMapping(value= "/getbyId",  method = RequestMethod.POST)
	 public String sendId(@RequestBody RequestHeaderDTO request) throws InterruptedException{
	 return consumer.getObject(request.getId());
	 }
	
	
	
}
