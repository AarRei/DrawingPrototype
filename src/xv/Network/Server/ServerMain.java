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

public class ServerMain {

	// private HashMap<String, ClientThread> clientList;
	public List<ClientThread> clientList = Collections.synchronizedList(new ArrayList<ClientThread>());
	public List<String> messageList = Collections.synchronizedList(new ArrayList<String>());

	public ServerMain(int port) {
		// clientList = new HashMap<>();
		openServer(port);
	}

	public static void main(String[] args) {
		if(args.length > 0)
			new ServerMain(Integer.parseInt(args[0]));
		else
			new ServerMain(7777);
	}

	public void openServer(int port) {
		int portNumber = port;

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				
		) {
			Thread message = new MessageThread(clientList,messageList);
			message.setDaemon( true );
			message.start();
			while(true){
				Socket clientSocket = serverSocket.accept();
				Thread t = new ClientThread(clientSocket,messageList);
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
