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
	public String username;
	
	public ClientThread(Socket socket, List<String> list){
		clientSocket = socket;
		messageList = list;
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
				
			while ((inputLine = in.readLine()) != null) {
				messageList.add(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
