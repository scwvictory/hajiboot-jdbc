package com.example;

import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@EnableAutoConfiguration
@ComponentScan
public class App implements CommandLineRunner {
    @Autowired
    CustomerRepository customerRepository;
	
    @Override
    public void run(String... strings) throws Exception {
    	//Add Customer
    	Customer createdCustomer = customerRepository.save(
    			new Customer(null, "Hidetoshi", "Dekisugi"));
    	System.out.println(createdCustomer + " is created!");
    	
    	//find Customer
    	Customer findOneCustomer = customerRepository.findOne(3);
    	System.out.println(findOneCustomer + " is find!");
    	
    	//find All Customers
    	customerRepository.findAll().forEach(System.out::println);
    	
    	//Delete Customer
    	customerRepository.delete(5);
    	
    	//find All Customers
    	customerRepository.findAll().forEach(System.out::println);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
