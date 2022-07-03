package com.example.demo.practice;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileOutputStreamExample {
	public static void main(String[] args) throws IOException {
		FileOutputStream fout = new FileOutputStream("src//main//resources\\key\\TestingFile_2 - Copy.txt");
		
		String data = "This is a testing data which is inserted using File Output Stream";		
		byte[] b = data.getBytes();
		System.out.println(Arrays.toString(b));
		fout.write(b);
		fout.close();
	}

}
