package com.p2p.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/*
 * Main Method where program starts
 */
public class P2PAPI {

	
	static void showOptions(){
		Debug.printMsg("1 - Start local Server");
		Debug.printMsg("2 - stop local server");
		Debug.printMsg("3 - Register File on Central Server");
		Debug.printMsg("4 - Look for file on Central Server");
		Debug.printMsg("5 - Download A File");
		Debug.printMsg("0 - Exit");
	}
	
	static void testMode(Peer peer,List<String> searchFiles,int noOfHops){
		//peer.startMyServer();
		/*Register all the files in Files Directory*/
		File dir = new File("./Files/");
		for (File file : dir.listFiles()) {
			Debug.printMsg(file.getName());
			peer.registerAFileOnServer(file.getName());
		}
		/*Looks for File in Server for given Number of hops*/
		for(String searchFile : searchFiles){
			for(int i=0 ;i<noOfHops;i++){
				peer.lookForAFileOnServer(searchFile);
			}
		}
		//Debug.printMsg("Total Search time in milli-seconds\t"+peer.getSearchTimeinMilliSecond());
		/*Download given search File*/
		//peer.stopMyServer();
	}
	
	
	
	static void interactiveMode(Peer peer){
		int choice ;
		String fileName;
		showOptions();
		choice = Input.getIntegerInput();
		while(choice != 0){
			switch(choice){
			case 1:
				peer.startMyServer();
				break;
			case 2:
				peer.stopMyServer();
				break;
			case 3:
				Debug.printMsg("Enter the File Name");
				fileName = Input.getStringInput();
				peer.registerAFileOnServer(fileName);
				break;
			case 4:
				Debug.printMsg("Enter the File Name");
				fileName = Input.getStringInput();
				peer.lookForAFileOnServer(fileName);
				break;
			case 5:
				Debug.printMsg("Enter the File Name");
				fileName = Input.getStringInput();
				peer.downloadAFile(fileName);
				break;
			default:
				Debug.printErrMsg("Invalid option Entered");
				
			}
			showOptions();
			choice = Input.getIntegerInput();	
		}
	}
	public static void main(String[] args) {
		String serverIP = null ;
		int serverPort = 0;
		int localServerPort =0;
		boolean isInteractiveMode = true;
		//int im=-1;
		Debug.printMsg("Enter Server IP(Register/Search) Files");
		serverIP = Input.getStringInput();
		Debug.printMsg("Enter Server IP's Port");
		serverPort = Input.getIntegerInput();
		Debug.printMsg("Enter My local Server Port");
		localServerPort = Input.getIntegerInput();
		/*Debug.printMsg("To Run in interactive mode press 1 else 0");
		im = Input.getIntegerInput();
		if(im == 1){
			isInteractiveMode= true;
		}*/
		if(serverIP == null || serverPort ==0 || localServerPort==0 ){
			Debug.printErrMsg("Some Input enered wrongly. Try Again");
			return;
		}
		Peer peer = new Peer(serverIP,serverPort,localServerPort);
		if(isInteractiveMode){
			interactiveMode(peer);
		}else{
			List<String> serachFiles = new ArrayList<String>();
			for(int i=1 ;i<=10 ;i++){
				serachFiles.add("c"+"1"+"f"+1);
			}
			
			testMode(peer,serachFiles,1000);
		}	
	}

}
