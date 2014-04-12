package xv.Network.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClientThread extends Thread{
	Socket clientSocket;
	public PrintWriter out;
	List<String> messageList;
	List<String> actionList;
	public String username;
	
	public ClientThread(Socket socket, List<String> list, List<String> listA){
		clientSocket = socket;
		messageList = list;
		actionList = listA;
	}
	
	public void run() {
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine, outputLine = "{\"action\": \"CHAT\",\"user\": \"SERVER\",\"text\": \"Verbindung hergestellt.\"}";

			// Initiate conversation with client
			out.println(outputLine);
			
			inputLine = in.readLine();
			if(inputLine.substring(0, 4).equals("JOIN")){
				username = inputLine.substring(5,inputLine.length());
				System.out.println(username+" ("+clientSocket.getInetAddress().getHostAddress()+") has joined the Server.");
			}
			for(String i : actionList){
				out.println(i);
			}
				
			while ((inputLine = in.readLine()) != null) {
				
				messageList.add(inputLine);
				actionList.add(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
