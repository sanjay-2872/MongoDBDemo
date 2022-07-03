package com.example.demo.practice;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamExample {

	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("C:\\\\DECRYPT\\\\TestingFile_2 - Copy.txt");
//		int i =fis.read();
//		int j =fis.read();
////		System.out.println(i);
//		System.out.println((char)i);
//		System.out.println((char)j);
		
		int i =0;
		while((i=fis.read())!=-1) {
			System.out.print((char)i);
		}
	}
}
