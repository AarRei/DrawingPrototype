package xv.Network.Server;

import java.util.Scanner;

import xv.Network.Server.GUI.ServerClose;
import xv.Network.Server.GUI.ServerConfigWindow;

/**
 * 
 * @author Sascha Rothkopf, Aaron Reiher
 * @version 0.0.2
 *
 */
public class ServerMain {

	static Scanner scan = new Scanner(System.in);
	
	/**
	 * Starts the program. 
	 * 
	 * Starts the program. Depending on the options it will start with GUI or without.
	 * 
	 * @param args options
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			new ServerConfigWindow();
		}else if(args[0].equals("-nogui")){
			//java -jar Server.jar -nogui -7777 -wc/nowc -1280 -720
			if(args.length < 5){
				int port, width, height;
				boolean webcolors;
				System.out.println("Which port do you want the server to run on?");
				System.out.print("Port: ");
				port = getInt();
				System.out.print("\nImage width:");
				width = getInt();
				System.out.print("\nImage height:");
				height = getInt();
				System.out.println("\nOnly allow webcolors? [y/n]");
				String temp = scan.next();
				if(temp.equals("y")){
					webcolors = true;
				}else{
					webcolors = false;
				}
				
				new Server(port,webcolors,width,height);				
			}else{
				new Server(Integer.parseInt(args[1].substring(1)), (args[2].equals("-wc"))?true:false,  Integer.parseInt(args[3].substring(1)), Integer.parseInt(args[4].substring(1)));
			}
		}else{
			new ServerConfigWindow();
		}
	}
	
	/**
	 * Returns an Integer value.
	 * 
	 * return entered int
	 */
	public static int getInt(){
		try{
			return Integer.parseInt(scan.next());
		}catch(Exception e){
			System.out.println("\nIncorrect input!");
			return getInt();
		}
		
	}
	
}
