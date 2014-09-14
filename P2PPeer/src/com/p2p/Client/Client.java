package com.p2p.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.p2p.util.Debug;

public class Client {
	
	private String myServer; //stores server information to which this client is binded to"IP:port"
	//private List<Long> searchTime;//diffrent thread enters search time in this
	
	public Client(String myServer){
		this.myServer = myServer;
		//this.searchTime = Collections.synchronizedList(new ArrayList<Long>());;
	}
	
	/*
	 * Used to register a given file
	 * to the specified server
	 */
	public void registerAFile(String serverName,int portNumer,String fileName){
		Socket clientSocket = openSocket(serverName,portNumer);
		//format REGISTER_IP:PORT_FILENAME
		String serverMsg = "REGISTER_"+this.myServer+"_"+fileName;
		(new ClientProcessThread(clientSocket,1,serverMsg)).start();
	}
	/*
	 * Used to search for a given file on server
	 */
	public void searchAFile(String serverName,int portNumer,String fileName){
		Socket clientSocket = openSocket(serverName,portNumer);
		//format LOOKUP_FILENAME
		String serverMsg = "LOOKUP_"+fileName;
		(new ClientProcessThread(clientSocket,2,serverMsg)).start();
	}
	/*
	 * Used to search for a given file on server
	 */
	public void downLoadAFile(String serverName,int portNumer,String fileName){
		Socket clientSocket = openSocket(serverName,portNumer);
		//format GETFILE_FILENAME
		String serverMsg = "GETFILE_"+fileName;
		(new ClientProcessThread(clientSocket,3,serverMsg)).start();
	}
	/*
	 * Used to Find and download file
	 * If file found then it will try to download
	 */
	public void findAndDownloadFile(String serverName,int portNumer,String fileName){
		Socket clientSocket = openSocket(serverName,portNumer);
		//format GETFILE_FILENAME
		String serverMsg = "LOOKUP_"+fileName;
		(new ClientProcessThread(clientSocket,4,serverMsg)).start();
	}
	/*
	public double getSearchTime(){
		
		if (searchTime.size() == 0){
			return 0.00;
		}
		Object time[] = searchTime.toArray();
		long totalTime =0 ;
		for(Object t:time){
			totalTime = totalTime + (long)t;
		}
		return (totalTime/((double)this.searchTime.size()));
	}
	*/
	/*
	 * used to open a socket given server name and port
	 */
	private Socket openSocket(String hostName,int portNumber){
		Socket clientSocket;
		try {
				clientSocket = new Socket(hostName, portNumber);
				return clientSocket;
			
		} catch (UnknownHostException e) {
			Debug.printErrMsg("host server down");
			return null;
			//e.printStackTrace();
		} catch (IOException e) {
			Debug.printErrMsg("Io Excertion");
			return null;
		}
	}
	

}
