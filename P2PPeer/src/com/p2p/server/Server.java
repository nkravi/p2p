package com.p2p.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.p2p.util.Debug;



/*
 * Server class is used to create server
 * It launches the server as a separate thread
 * A single server thread will be launched each time
 * and that thread can be controlled via this class
 */
public class Server {
	private int portNumber;
	private ServerThread serverThread;
	private InetAddress myIp;
	
	public Server(int portNumber){
		this.portNumber = portNumber;
		this.serverThread = new ServerThread(portNumber);
		try {
			this.myIp  = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	public boolean isServerAlive(){
		return this.serverThread.isAlive();
	}
	public String getServerIp(){
		return myIp.getHostAddress();
	}
	public int getServerPort(){
		return this.portNumber;
	}
}
