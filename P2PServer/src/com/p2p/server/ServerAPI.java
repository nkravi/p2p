package com.p2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ServerAPI {

	/**
	 * @param args
	 */
	public static int show(){
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); 
		
		int x = -1;
		System.out.println("1- Start Server");
		System.out.println("2- Stop Server");
		System.out.println("0 - To Exit");
		try {
			x = Integer.parseInt(stdin.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return x;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int serverPort = -1;
		Debug.printMsg("Enter the Server Port");
		serverPort = Input.getIntegerInput();
		if(serverPort == -1){
			Debug.printErrMsg("I/o Error. Try Again");
			return;
		}
		Server s = new Server(serverPort);
		int input = show();
		while(input != 0){
			switch(input){
			case 1:
				s.startServer();
				break;
			case 2:
				s.stopServer();
				break;
			default:
				System.out.println("\nInvalid input");
			}
			input = show();
		}
		//s.isServerAlive();
		
	}

}
