package com.example.demo.encrptionAndDecription.mac;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;

public class MACTesting {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		System.out.println(keyGen);
		SecureRandom ran = new SecureRandom();
		System.out.println(keyGen);
		System.out.println("Ran : {" + ran + "}");
		keyGen.init(ran);

		System.out.println(keyGen);

		Key key = keyGen.generateKey();
		System.out.println(key);
		Mac mac = Mac.getInstance("HmacSHA256");

		mac.init(key);

		String msg = new String("Hi how are you");
		byte[] bytes = msg.getBytes();
		System.out.println(bytes.length);
		byte[] macResult = mac.doFinal(bytes);

		System.out.println("Mac result:");
		System.out.println(new String(macResult));
	}
}
