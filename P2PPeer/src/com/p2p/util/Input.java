package com.p2p.util;
/*
 * Helper Class to get Input
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
	public static String getStringInput(){
		String input = "";
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); 
		try {
			input = stdin.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	public static int getIntegerInput(){
		int input = -1;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); 
		try {
			input = Integer.parseInt(stdin.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
