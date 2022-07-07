package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		//mapper.readTree("{'name':'sanjay','age':22}");
		
//		System.out.println();
//		mapper.readTree("{\"name\":\"sanjay\",\"age\":22}").fieldNames().forEachRemaining(System.out::println);
		
		List<String> data = new ArrayList<String>();
		
		
		
	}

}
