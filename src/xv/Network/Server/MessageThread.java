package xv.Network.Server;

import java.util.List;

public class MessageThread extends Thread{
	public List<ClientThread> clientList;
	public List<String> messageList;
	public List<String> actionList;
	
	public MessageThread(List<ClientThread> listC,List<String> listS, List<String> listA){
		clientList = listC;
		messageList = listS;
		actionList = listA;
	}
	
	public void run(){
		try {
			while (true) {
				if(messageList.size()>0){
					for(ClientThread c : clientList){
						c.out.println(messageList.get(0));
					}
					messageList.remove(0);
				}
				sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
