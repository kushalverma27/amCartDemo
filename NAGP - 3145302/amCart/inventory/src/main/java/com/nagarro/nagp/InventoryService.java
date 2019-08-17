package com.nagarro.nagp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@ComponentScan("com.nagarro.nagp")
public class InventoryService {
	
	@Autowired
	ProductJDBCRepository jdbcRepo;
	
    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
            http.headers().frameOptions().disable();
        }
    }
    
    
    public static void main(String[] args) throws IOException, TimeoutException, Exception {
        SpringApplication.run(InventoryService.class, args);
    }
    }
    

