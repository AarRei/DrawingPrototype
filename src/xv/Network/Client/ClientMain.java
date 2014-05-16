package xv.Network.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import xv.GUI.DrawWindow;
import xv.Network.Server.ClientThread;

public class ClientMain {
	
	PrintWriter out;
	public String username;
	DrawWindow win;
	
	/**
	 * Creates the ClientMain.
	 * 
	 * Creates the ClientMain object and calls connectToServer.
	 * 
	 * @param inetAddress hostname or host IP address
	 * @param port port to which the client connects
	 * @param username the desired username
	 * @param win the parent DrawWindow
	 */
    public ClientMain(InetAddress inetAddress, int port, String username, DrawWindow win) {
    	this.win = win;
        this.username = username;
        connectToServer(inetAddress,port);
    }
    
    /**
     * Establishes a connection to the Server.
     * 
     * Opens a socket to the Server with the given IP and port. Opens an input- and an output stream.
     * Starts the MessageReceiveThread and transmits the username to the server.
     * 
     * @param ip IP address or hostname of the host
     * @param port port to which the client connects
     */
    private void connectToServer(InetAddress ip,int port) {

        InetAddress hostName = ip;
        int portNumber = port;

        try{
        	Socket socket = new Socket(hostName, portNumber);
         	out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            /*BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromUser;*/
            
            Thread t = new MessageReceiveThread(in,win);
			t.setDaemon( true );
		    t.start();
		    
		    sendMessage("JOIN "+username);
		    //sendMessage("{\"action\": \"JOIN\", \"username\": \""+username+"\"}");
		    
            /*while (true) {                
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    //System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }*/
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
    
    /**
     * Sends a message to the Server.
     * 
     * Sends a message to the Server via the PrintWriter.
     * 
     * @param message the message to be send
     */
    public void sendMessage(String message){
    	out.println(message);
    }

    /*public void pingTest(String ip) throws Exception {
        String ipAddress = "127.0.0.1";
        InetAddress inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByName(ip);

        System.out.println("Sending Ping Request to " + ip);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
    }*/
}
