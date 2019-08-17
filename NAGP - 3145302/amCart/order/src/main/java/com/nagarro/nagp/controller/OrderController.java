package com.nagarro.nagp.controller;

import java.util.Random;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.nagp.dao.OrderImpl;
import com.nagarro.nagp.dao.OrderJdbcRepository;
import com.nagarro.nagp.model.RequestHeaderDTO;

@RestController
@RefreshScope
public class OrderController {

	
	@Value("${name:unknown}")
	private String name;
	
	@Autowired
     OrderJdbcRepository jdbcRepo;
	
	String req;
	
	 @Autowired
	 OrderImpl producer;
	 
	 @Autowired
	 Environment environment;
	
	
	@RequestMapping(value= "/send",  method = RequestMethod.POST)
	 public String sendMsg(@RequestBody RequestHeaderDTO request) throws InterruptedException{
		request.setId(getRandomHexString(16));
	 producer.produceMsg(SerializationUtils.serialize(request));
	 return "Done";
	 }
	
	@RequestMapping(value= "/checkPort",   method = RequestMethod.GET)
	 public String checkPort() throws InterruptedException{
	 return environment.getProperty("local.server.port");
	 }
	
	@RequestMapping(value= "/getbyId",  method = RequestMethod.POST)
	 public String sendId(@RequestBody RequestHeaderDTO request) throws InterruptedException{
	 return producer.getObject(request.getId());
	 }
	
	
	  private String getRandomHexString(int numchars){
	        Random r = new Random();
	        StringBuffer sb = new StringBuffer();
	        while(sb.length() < numchars){
	            sb.append(Integer.toHexString(r.nextInt()));
	        }

	        return sb.toString().substring(0, numchars);
	    }
}
