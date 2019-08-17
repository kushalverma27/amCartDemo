package com.nagarro.nagp.delegate;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nagarro.nagp.model.RequestHeaderDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class OrderServiceDelegate {

   @Autowired
   RestTemplate restTemplate;
    
   @HystrixCommand(fallbackMethod = "orderService_Fallback")
   public String sendDelegate(RequestHeaderDTO request) {

       //System.out.println("Getting details for " + id);

	   String response = restTemplate.postForObject("http://localhost:8080/send", request, String.class);
      // System.out.println("Response Received as " + response);
       return response;
   }
    
	@HystrixCommand(fallbackMethod = "orderService_Fallback")
	public String getbyIdDelegate(RequestHeaderDTO request) throws URISyntaxException {
		 
		String response = restTemplate.postForObject("http://localhost:8080/getbyId", request, String.class);
	     return response;
	}
	
	public String orderService_Fallback(RequestHeaderDTO request) {
	System.out.println("Order Service is down!!! fallback route enabled...");
	return "CIRCUIT BREAKER ENABLED!!! No Response From Order Service at this moment. " +
	                   " Service will be back shortly - ";
	}
   
   @Bean
   public RestTemplate restTemplate() {
     return new RestTemplate();
       

 
   }
}