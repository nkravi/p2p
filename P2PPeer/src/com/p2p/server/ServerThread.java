package com.p2p.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.p2p.util.Debug;

/*
 * ServerThread is the server thread launched 
 * by Server class 
 * It serves the client via ProcessThread
 */
public class ServerThread extends Thread {
	
	private int portNumber;
	private boolean stopFlag;
	private ServerSocket serverSocket;
	private InetAddress myIp;
	
	ServerThread(int portNumber){
		this.portNumber = portNumber;
		this.stopFlag = false;
	}
	public String getMyIp(){
		return myIp.getHostAddress();
	}
	/*
	 * stopMe used to stop the server
	 */
	public void stopMe(){
		this.stopFlag = true;
		try {
			Socket clientSocket = new Socket(myIp, portNumber);
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 out.println("DUMMY");
			 out.close();
			 clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/*
	 * startMe used to start the server
	 */
	public void startMe(){
		try {
			this.myIp  = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(portNumber,20,myIp);
			while(!stopFlag){
				Debug.printMsg("Server listening at  "+myIp.getHostAddress()+" port "+this.portNumber);
				Socket clientSocket = serverSocket.accept();
				(new ProcessThread(clientSocket)).start();
			}
			
		}catch (SocketTimeoutException e){
			Debug.printErrMsg("Server timmed out");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void run(){
		this.startMe();
		
	}
}
