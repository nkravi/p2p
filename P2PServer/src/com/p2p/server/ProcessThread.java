package com.p2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 * ProcessThread -  started by Server thread
 * to fullfil the client request
 * Request types:
 * Register - to register a file
 * Search - to search for a file
 * Dummy - call made to check server is alive
 * server replies nothing to this call
 *
 */
public class ProcessThread extends Thread {
	private Socket clientSocket;
	private ConcurrentHashMap<String, List<String>> fileList; 
	
	ProcessThread(Socket clientSocket,ConcurrentHashMap<String, List<String>> fileList){
		this.clientSocket = clientSocket;
		this.fileList = fileList;
	}
	/*
	 * First method called to server client
	 */
	private void handleClient(){
		if(!clientSocket.isClosed()){
			talkToClient();
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Debug.printErrMsg("Client Socket is closed");
		}
	}
	/*
	 * Talks to client
	 * Client server communication happens through this 
	 * method
	 * 
	 */
	private void talkToClient(){
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String returnMsgToClient;
			String clientMsg = in.readLine();
			if(clientMsg.equals("DUMMY")){
				out.close();
				in.close();
				//clientSocket.close();
				return;
			}
			returnMsgToClient = processClientMessage(clientMsg);
			out.println(returnMsgToClient);
			out.close();
			in.close();
			//clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Process the client request
	 * If SUCCESS - returns SUCCESS_<Sucess Message>
	 * else - returns ERROR_<Error Message>
	 */
	private String processClientMessage(String clientMsg){
		String  retMessage = "ERROR_MESSAGEEMPTY"; 
		if (clientMsg == null){
			return retMessage;
		}
		String msg = clientMsg.split("_")[0];
		
		switch(msg){
		case "REGISTER":
			retMessage = this.registerFile(clientMsg);
			break;
		case "LOOKUP":
			//System.out.println("Look up for a file");
			retMessage = this.lookupAFile(clientMsg);
			break;
		}
		
		return retMessage;
	}
	/*
	 * Register a given file to 
	 * the client If file alredy registerd
	 * returns a error message
	 */
	private String registerFile(String clientMsg){
		//format REGISTER_IP:PORT_FILENAME
		String msg[] = clientMsg.split("_");
		if(msg.length != 3){
			//System.out.println("\nInvalid message format");
			return "ERROR_INVALIDMESSAGELENGTH";
		}
		
		String ipPort  = msg[1];
		String fileName = msg[2];
		List<String> iplist = this.fileList.get(fileName);
		if( iplist != null ){
			if(iplist.contains(ipPort)){
				System.out.println("\nFile already registered");
				return "ERROR_FILEALREADYREGISTERED";
			}else{
				iplist.add(ipPort);
				this.fileList.put(fileName, iplist);
			}
		}else{
			iplist = new ArrayList<String>();
			iplist.add(ipPort);
			this.fileList.put(fileName, iplist);
		}
		
		return "SUCCESS_FILEREGISTERED";
	}
	/*
	 * used to look up a given file
	 * on success returns a list of all
	 * system that has it
	 */
	private String lookupAFile(String clientMsg){
		//format LOOKUP_FILENAME
		String msg[] = clientMsg.split("_");
		if(msg.length != 2){
			//System.out.println("\nInvalid message format");
			return "ERROR_INVALIDMESSAGELENGTH";
		}
		String fileName = msg[1];
		List<String> iplist = this.fileList.get(fileName);
		if(iplist != null){
			String retMsg = "SUCCESS_FILEFOUND";
			for(String ip:iplist){
				retMsg += "_"+ip;
			}
			return retMsg;
		}
		return "ERROR_FILENOTFOUND";
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		this.handleClient();
	}
	
}
