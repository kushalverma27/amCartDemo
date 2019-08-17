package com.nagarro.nagp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class TrackingService {
	public static Channel channel;


    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
            http.headers().frameOptions().disable();
        }
    }
	
	
	public static void main(String[] args) throws IOException, TimeoutException {
		SpringApplication.run(TrackingService.class, args);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
/*		channel.queueDeclare("jsa.queue", true, false, false, null);
		channel.queueDeclare("jsa.queue1", true, false, false, null);
		channel.queueDeclare("jsa.queue2", true, false, false, null);
		channel.queueDeclare("jsa.queue3", true, false, false, null);
		
		channel.exchangeDeclare("jsa.direct", "fanout", true);
		channel.exchangeDeclare("jsa.direct1", "fanout", true);
		channel.exchangeDeclare("jsa.direct2", "fanout", true);
		channel.exchangeDeclare("jsa.direct3", "fanout", true);*/
		
		channel.queueBind("jsa.queue", "jsa.direct", "jsa.routingkey");
		channel.queueBind("jsa.queue1", "jsa.direct1", "jsa.routingkey1");
		channel.queueBind("jsa.queue2", "jsa.direct2", "jsa.routingkey2");
		channel.queueBind("jsa.queue3", "jsa.direct3", "jsa.routingkey3");

	}
	
/*	private static void insertInventoryData(Delivery delivery) {
		System.out.println("insertInventoryData");
		
	}

	public static void insertTrackingIntoDB(Delivery delivery) throws IOException {
		System.out.println("insertTrackingIntoDB");
 	    
	}*/
	
	}


