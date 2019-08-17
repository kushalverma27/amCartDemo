package com.nagarro.nagp.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.nagp.model.RequestHeaderDTO;
import com.nagarro.nagp.delegate.OrderServiceDelegate;

@RestController
public class OrderHystrixController {
     
    @Autowired
    OrderServiceDelegate orderServiceDelegate;
 
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendOrder(@RequestBody RequestHeaderDTO request) {
        System.out.println("Going to call student service to get data!");
        return orderServiceDelegate.sendDelegate(request);
    }
    
    @RequestMapping(value = "/getbyId", method = RequestMethod.POST)
    public String getOrderbyId(@RequestBody RequestHeaderDTO request) throws URISyntaxException {
        System.out.println("Going to call student service to get data!");
        return orderServiceDelegate.getbyIdDelegate(request);
    }
    
    
}