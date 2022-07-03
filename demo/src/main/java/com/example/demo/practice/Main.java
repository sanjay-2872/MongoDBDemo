package com.example.demo.practice;


import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader bis = new BufferedReader(new FileReader("C:\\Users\\SanjayKumar\\OneDrive - M2P Solutions Private Limited\\Desktop\\movie.json"));
	
		String line ;
		while((line=bis.readLine())!=null) {
			System.out.println(line);
		}
		
	}
	

}
