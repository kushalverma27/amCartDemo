package com.nagarro.nagp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.nagarro.nagp.ribbon.RibbonConfiguration;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@RibbonClient(name = "server", configuration = RibbonConfiguration.class)
public class OrderService {
	
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
        SpringApplication.run(OrderService.class, args);
    }
    
}
