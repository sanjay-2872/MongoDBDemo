package com.example.demo.encrptionAndDecription.practice1;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AseEncry {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		keygen.init(256);
		SecretKey skey = keygen.generateKey();
		System.out.println(skey);
		byte[] iv = new byte[64/8];
		SecureRandom srandom = new SecureRandom();
		srandom.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
	}

}
