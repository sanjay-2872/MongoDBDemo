package com.example.demo.practice;

import java.io.IOException;

public class IOPractice {
	public static void main(String[] args) throws IOException {
		
		int i=System.in.read();//returns ASCII code of 1st character  
		System.out.println("Value of "+i+" is :"+(char)i);//will print the character  
		System.out.println("\n--Next--\n");
		
		while(i<65) {
			System.out.println("Value of "+i+" is :"+(char)i);
			i++;
		}
		
		
		System.out.println("\n--Next--\n");
		System.out.println();
	}

}
