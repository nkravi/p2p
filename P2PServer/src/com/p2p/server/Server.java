package com.p2p.server;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Server class is used to create server
 * It launches the server as a separate thread
 * A single server thread will be launched each time
 * and that thread can be controlled via this class
 */
public class Server {
	private int portNumber;
	private ServerThread serverThread;
	private ConcurrentHashMap<String, List<String>> fileList; 
	
	Server(int portNumber){
		this.portNumber = portNumber;
		fileList = new ConcurrentHashMap<String,List<String>>();
		this.serverThread = new ServerThread(portNumber,fileList);
	}
	/*
	 * startServer method is used to start the server
	 * uses serverThread class to launch 
	 * the server
	 */
	public void startServer(){
		Debug.printMsg("Starting Server....");
		if(serverThread.isAlive()){
			Debug.printErrMsg("Server is already up and running @ "+ this.portNumber);
			return;
		}
		this.serverThread.start();
	}
	/*
	 * stopServer method is used to stop the server
	 * If the server is already up and running 
	 * it will stop the server
	 */
	public void stopServer(){
		Debug.printMsg("Stopping Server....");
		if(!serverThread.isAlive()){
			Debug.printErrMsg("Server is already stopped ");
			return;
		}
		this.serverThread.stopMe();
	}
	public void isServerAlive(){
		if(this.serverThread.isAlive()){
			Debug.printMsg("Yes");
			
		}else{
			Debug.printMsg("No");
		}
	}
}
