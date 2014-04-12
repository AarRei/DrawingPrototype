package xv.Network.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import xv.Canvas.Component.Layer;
import xv.Canvas.Component.Painter;

public class Server {

	// private HashMap<String, ClientThread> clientList;
	public List<ClientThread> clientList = Collections.synchronizedList(new ArrayList<ClientThread>());
	public List<String> messageList = Collections.synchronizedList(new ArrayList<String>());
	public List<String> actionList = Collections.synchronizedList(new ArrayList<String>());

	public Server(int port, boolean webcolor, int width, int height) {
		// clientList = new HashMap<>();
		String serversettings = "{\"action\": \"SVCF\","
				+ "\"width\": "+width+","
				+ "\"height\": "+height+","
				+ "\"webcolors\": "+Boolean.toString(webcolor)+"}";
		/*String firstlayer = "{\"action\": \"ADDL\","
				+ "\"user\": \"Host\","
				+ "\"layer_id\": 0,"
				+ "\"layer_position\": 0}";*/
		
		actionList.add(serversettings);
		//actionList.add(firstlayer);
		
		openServer(port);
	}

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
				System.out.println("Accepted "+ clientSocket.getInetAddress().getHostAddress()+" ...");
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	/*
	 * public void registerClient(ClientThread clientThread, String hans) {
	 * if(!getClientList().containsKey(hans)) getClientList().put(hans,
	 * clientThread); }
	 */

	public void blockingTest(int thread) {
		System.out.println(thread + " Durchläufe ");
		System.out.println("Blocking....");
		new Scanner(System.in).next();
		System.exit(1);
	}

	/*
	 * public HashMap<String, ClientThread> getClientList() { return clientList;
	 * }
	 */
}
