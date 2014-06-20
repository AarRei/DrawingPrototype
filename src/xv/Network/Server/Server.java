package xv.Network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	/**
	 * List of all connected clients.
	 */
	public List<ClientThread> clientList = Collections.synchronizedList(new ArrayList<ClientThread>());
	/**
	 * List of messages which yet have to be relayed.
	 */
	public List<String> messageList = Collections.synchronizedList(new ArrayList<String>());
	/**
	 * List of all received actions.
	 */
	public List<String> actionList = Collections.synchronizedList(new ArrayList<String>());

	/**
	 * Creates the Server object.
	 * 
	 * Creates the Server object and establishes the server settings with the given parameters.
	 * 
	 * @param port the server port
	 * @param webcolor true if only webcolors should be allowed
	 * @param width width of the canvas
	 * @param height height of the canvas
	 */
	public Server(int port, boolean webcolor, int width, int height) {
		String serversettings = "{\"action\": \"SVCF\","
				+ "\"width\": "+width+","
				+ "\"height\": "+height+","
				+ "\"webcolors\": "+Boolean.toString(webcolor)+"}";
		
		actionList.add(serversettings);
		
		openServer(port);
	}
	
	/**
	 * Starts the server.
	 * 
	 * Opens a server socket on the specified port, listens for connecting clients and starts the MessageThread.
	 * Throws an IOException when the port is already being listened on. 
	 * 
	 * @param port port of the server socket
	 */
	public void openServer(int port) {
		int portNumber = port;

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				
		) {
			Thread message = new MessageThread(clientList,messageList,actionList);
			message.setDaemon( true );
			message.start();
			while(true){
				Socket clientSocket = serverSocket.accept();
				Thread t = new ClientThread(clientSocket,messageList,actionList);
				t.setDaemon( true );
			    t.start();
			    clientList.add((ClientThread)t);
			    
			    String users = "{\"action\": \"USER\"," + "\"users\": [";
			    for(ClientThread c : clientList){
			    	users += "\"user\": \""+c.username+"\",";
			    }
			    users = users.substring(0, users.length()-1);
			    users += "]}";
			    messageList.add(users);
			    
				System.out.println("Accepted "+ clientSocket.getInetAddress().getHostAddress()+" ...");
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

}
