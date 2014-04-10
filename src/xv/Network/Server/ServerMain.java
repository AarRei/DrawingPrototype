package xv.Network.Server;

public class ServerMain {
	
	public static void main(String[] args) {
		if(args.length > 0)
			new Server(Integer.parseInt(args[0]));
		else
			new Server(7777);
	}
	
}
