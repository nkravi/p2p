package com.p2p.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



/*
 * ServerThread is the server thread launched 
 * by Server class 
 * It serves the client via ProcessThread
 */
public class ServerThread extends Thread {
	
	private int portNumber;
	private ConcurrentHashMap<String, List<String>> fileList; 
	private boolean stopFlag;
	private ServerSocket serverSocket;
	private InetAddress myIp;
	
	ServerThread(int portNumber,ConcurrentHashMap<String, List<String>> fileList){
		this.portNumber = portNumber;
		this.fileList = fileList;
		this.stopFlag = false;
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
			serverSocket = new ServerSocket(portNumber,50,myIp);
			while(!stopFlag){
				Debug.printMsg("Server listening at  "+myIp.getHostAddress()+" port "+this.portNumber);
				Socket clientSocket = serverSocket.accept();
				Debug.printMsg("Incomming Client Connection Launching Thread");
				(new ProcessThread(clientSocket,this.fileList)).start();
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
