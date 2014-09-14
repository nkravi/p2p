package com.p2p.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.p2p.util.Debug;

/*
 * This is used by Client to process the request
 */
public class ClientProcessThread extends Thread {
	
	private Socket clientSocket;
	private int purpose;
	private String initialMsgToServer;
	private List<Long> searchTime;
	
	
	ClientProcessThread(Socket clientSocket,int purpose,String initialMsgToServer){
		this.clientSocket = clientSocket;
		this.purpose = purpose;
		this.initialMsgToServer = initialMsgToServer;
	}
	ClientProcessThread(Socket clientSocket,int purpose,String initialMsgToServer,List<Long> searchTime){
		this(clientSocket,purpose,initialMsgToServer);
		this.searchTime = searchTime;
	}
	/*
	 * Used to register a file
	 * Which file to register is appended to initialMsgToServer
	 */
	private void registerAFile(){
		String serverString = initialMsgToServer;
		try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 out.println(serverString);
			 String serverMessage = in.readLine();
			 processServerMessage(serverMessage);
			 in.close();
			 out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 * Used to search a file in 
	 * the server
	 */
	private List<String> searchAFile(){
		long Starttime = System.nanoTime();
		String serverString = initialMsgToServer;
		List<String> ipList = null;
		try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 out.println(serverString);
			 String serverMessage = in.readLine();
			 if(processServerMessage(serverMessage)){
				 ipList = new ArrayList<String>();
				 String iplist[]= serverMessage.split("_");
				 for(int i=2 ;i<iplist.length;i++){
					 Debug.printMsg(iplist[i]);
					 ipList.add(iplist[i]);
				 }
			 }
			 in.close();
			 out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			long timeLapsed = System.nanoTime() -Starttime;
			searchTime.add(timeLapsed);
		}
		return ipList;
	}
	/*
	 * Used to download a file
	 */
	private boolean downloadAFile(){
		String serverString = initialMsgToServer;
		try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 out.println(serverString);
			 String serverMessage = in.readLine();		
			 if(processServerMessage(serverMessage)){
				 File file = new File("./Downloads/"+initialMsgToServer.split("_")[1]); //to get file name
				 if (!file.exists()) 
						file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				serverMessage = serverMessage.replaceFirst("SUCCESS_FILEDOWNLOADED_", "");//remove server message
				 while(!serverMessage.equals("EOF")){
					 bw.write(serverMessage+"\n");
					 serverMessage = in.readLine();
				 }
				 bw.close();
				 in.close();
				 out.close();
				 return true;
			 }
			 
			 //processServerMessage(serverMessage);
			 in.close();
			 out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/*
	 *used to search and download files 
	 */
	private void searchAndDownloadFile(){
		List<String> ipList = this.searchAFile();
		boolean retVal = false;
		if (ipList == null){
			Debug.printErrMsg("Search file not found");
			return;
		}
		this.initialMsgToServer = this.initialMsgToServer.replaceFirst("LOOKUP_","GETFILE_");
		for (String ip:ipList){
			Debug.printMsg("Trying to download From "+ ip);
			String serverName = ip.split(":")[0];
			int portNumber = Integer.parseInt(ip.split(":")[1]);
			Socket clientSocket = openSocket(serverName,portNumber);
			if(clientSocket == null || clientSocket.isClosed()){
				continue;
			}
			this.clientSocket = clientSocket;
			retVal = this.downloadAFile();
			if(retVal){
				break;
			}
		}
		if(false==retVal){
			Debug.printErrMsg("tried all servers . unable to download file");
		}
		
	}
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
			Debug.printErrMsg("host server down");
			return null;
		}
	}
	/*
	 * This is used to process message from server
	 * decodes server message
	 */
	private boolean processServerMessage(String message){
		
		String msg[] = message.split("_");
		boolean retval = false;
		if (msg[0].equals("SUCCESS")){
			retval = true;
		}
		switch(msg[1]){
		case "INVALIDMESSAGELENGTH":
			Debug.printErrMsg("Incorrect message sent from us");
			break;
		case "FILEALREADYREGISTERED":
			Debug.printErrMsg("File aready registered");
			break;
		case "FILEREGISTERED":
			Debug.printMsg("File registered successfully");
			break;
		case "MESSAGEEMPTY":
			Debug.printErrMsg("Empty message sent from us");
			break;
		case "FILEFOUND":
			Debug.printMsg("Seach file  found");
			break;
		case "FILENOTFOUND":
			Debug.printErrMsg("Search file not found");
			break;
		case "FILEDOWNLOADED":
			Debug.printMsg("File downloaded");
			break;
		case "IOEXCEPTIONEOF":
			Debug.printErrMsg("Io exception while downloading file");
			break;
		case "FILENOTFOUNDEOF":
			Debug.printErrMsg("Download File not found");
			break;
		}
		
		return retval;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * this is where thread starts
	 * uses purpose to run corresponding method
	 */
	public void run(){
		switch(this.purpose){
		case 1:
			registerAFile();
			break;
		case 2:
			searchAFile();
			break;
		case 3:
			downloadAFile();
			break;
		case 4:
			searchAndDownloadFile();
			break;
		default:
			Debug.printErrMsg("Invalid input in ClientProcess thread");
			break;
			
		}
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
