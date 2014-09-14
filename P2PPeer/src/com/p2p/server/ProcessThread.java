package com.p2p.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.p2p.util.Debug;


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
	
	
	ProcessThread(Socket clientSocket){
		this.clientSocket = clientSocket;
		
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
			//Debug.printMsg(returnMsgToClient);
			out.print(returnMsgToClient);
			out.println("EOF");
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
		case "GETFILE":
			retMessage = this.getAFile(clientMsg);
			break;
		}
		
		return retMessage;
	}
	/*
	 * used to get a file from
	 * on success returns a list of all
	 * system that has it
	 */
	private String getAFile(String clientMsg){
		//format GETFILE_FILENAME
		String msg[] = clientMsg.split("_");
		if(msg.length != 2){
			//System.out.println("\nInvalid message format");
			return "ERROR_INVALIDMESSAGELENGTH";
		}
		String fileName = msg[1];
		return readFile("./Files/"+fileName);
		
	}
	String readFile(String fileName) {
		StringBuilder sb = null;
		BufferedReader br = null;
		try {
		FileReader f = new FileReader(fileName);
		br = new BufferedReader(f);
		sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
		    sb.append("\n");
		     line = br.readLine();
		}
		 	br.close();      
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "ERROR_FILENOTFOUND";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "ERROR_IOEXCEPTION";
		}
		return "SUCCESS_FILEDOWNLOADED_"+sb.toString();
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		this.handleClient();
	}
	
}
