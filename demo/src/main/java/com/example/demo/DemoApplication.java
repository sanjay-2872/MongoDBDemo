package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.mongolearning.Learning1;
import com.mongodb.client.MongoClient;

@SpringBootApplication
public class DemoApplication {

	public static MongoClient mgClient;
	public static void main(String[] args) {
		ConfigurableApplicationContext ac =	SpringApplication.run(DemoApplication.class, args);
		
		//mgClient = ac.getBean(Learning1.class).getDataSource();
	}

}
