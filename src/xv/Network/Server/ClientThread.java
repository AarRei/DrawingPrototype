package xv.Network.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClientThread extends Thread{
	Socket clientSocket;
	public PrintWriter out;
	List<String> messageList;
	List<String> actionList;
	List<ClientThread> clientList;
	public String username;
	
	/**
	 * Creates a client thread.
	 * 
	 * Creates a client thread with the respective Socket.
	 * 
	 * @param socket socket of the client
	 * @param list list of messages to be relayed
	 * @param listA list of all actions
	 */
	public ClientThread(Socket socket, List<String> list, List<String> listA,List<ClientThread> listC){
		clientSocket = socket;
		messageList = list;
		actionList = listA;
		clientList = listC;
	}
	
	/**
	 * Listens for messages from the client.
	 * 
	 * Initializes input- and output stream. Sends the client all received actions and listens 
	 * for messages from the client.
	 */
	public void run() {
		try {			
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine, outputLine = "{\"action\": \"CHAT\",\"user\": \"SERVER\",\"text\": \"Verbindung hergestellt.\"}";
			
			out.println(outputLine);
			
			inputLine = in.readLine();
			if(inputLine.substring(0, 4).equals("JOIN")){
				username = inputLine.substring(5,inputLine.length());
				System.out.println(username+" ("+clientSocket.getInetAddress().getHostAddress()+") has joined the Server.");
			}
			
			
			
			for(String i : actionList){
				out.println(i);
			}
			
			for(int i = 0; i < clientList.size();i++){
		    	if(!clientList.get(i).isAlive()){
		    		clientList.remove(i);
		    	}
		    }
			
			String users = "{\"action\": \"USER\"," + "\"users\": [";
		    for(ClientThread c : clientList){
		    	users += "{\"user\": \""+c.username+"\"},";
		    }
		    users = users.substring(0, users.length()-1);
		    users += "]}";
		    messageList.add(users);
		    
		    JSONParser parser = new JSONParser();
	        Object unitsObj = null;
	        JSONObject unitsJson;

			while ((inputLine = in.readLine()) != null) {
				if(inputLine.equals("quit")){
					break;
				}
				unitsObj = parser.parse(inputLine);
				unitsJson  = (JSONObject) unitsObj;
				
				if(((String)unitsJson.get("action")).equals("TOOL")){
					messageList.add(inputLine);
				}
				else{
					messageList.add(inputLine);
					actionList.add(inputLine);
				}
			}
			
			users = "{\"action\": \"USER\"," + "\"users\": [";
		    for(ClientThread c : clientList){
		    	if(!c.username.equals(username))
		    		users += "{\"user\": \""+c.username+"\"},";
		    }
		    users = users.substring(0, users.length()-1);
		    users += "]}";
		    messageList.add(users);
		    
			System.out.println("User "+username+" ("+ clientSocket.getInetAddress().getHostAddress()+") has quit. Disconnect by user.");
		    
		    in.close();
		    out.close();
		    clientSocket.close();
		    
		}catch(SocketException e){
			System.out.println("User "+username+" ("+ clientSocket.getInetAddress().getHostAddress()+") has quit. Interrupted connection.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
