package com.p2p.util;

import com.p2p.Client.Client;
import com.p2p.server.Server;

public class Peer {
	private String mainServerIp;
	private int mainServerPort;
	private Server myServer;
	private Client myClient;
	public Peer(String mainServerIp,int mainServerPort,int myServerPort ){
		this.mainServerIp = mainServerIp;
		this.mainServerPort = mainServerPort;
		myServer = new Server(myServerPort);
		myClient = new Client(myServer.getServerIp()+":"+myServer.getServerPort());
	}
	
	public void startMyServer(){
		myServer.startServer();
	}
	public void stopMyServer(){
		myServer.stopServer();
	}
	public void registerAFileOnServer(String fileName){
		myClient.registerAFile(mainServerIp, mainServerPort, fileName);
	}
	public void lookForAFileOnServer(String fileName){
		myClient.searchAFile(mainServerIp, mainServerPort, fileName);
	}
	public void downloadAFile(String fileName){
		myClient.findAndDownloadFile(mainServerIp, mainServerPort, fileName);
	}
	/*public double getSearchTimeinMilliSecond(){
		return (myClient.getSearchTime()*1.0e-6);
	}*/
}
