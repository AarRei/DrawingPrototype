package xv.Network.Client;

import java.io.BufferedReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import xv.GUI.DrawWindow;
import xv.Network.Server.ClientThread;

public class MessageReceiveThread extends Thread{
	
	BufferedReader in;
	DrawWindow win;
	
	public MessageReceiveThread(BufferedReader in,DrawWindow win){
		this.win = win;
		this.in = in;
	}
	
	public void run(){
		String fromServer;
		
		JSONParser parser = new JSONParser();
        Object unitsObj = null;
        
        JSONObject unitsJson;
		try {
			while ((fromServer = in.readLine()) != null) {
				unitsObj = parser.parse(fromServer);
				unitsJson  = (JSONObject) unitsObj;
				switch((String)unitsJson.get("action")){
					case "CHAT":
						win.chatWindow.addMessage("<"+(String) unitsJson.get("user")+">: "+(String) unitsJson.get("text"));
						System.out.println("<"+(String) unitsJson.get("user")+">: "+(String) unitsJson.get("text"));
						break;
					case "LINE":
						new ReceivingDrawer(unitsJson, win).start();
						break;
					case "ADDL":
						if(!((String) unitsJson.get("user")).equals(win.net.username)){
							win.canvas.addLayer();
							win.layerWindow.fillList();
						}
						break;
					case "SVCF":
						Long w = (Long)unitsJson.get("width"), h = (Long)unitsJson.get("height");
						win.createCanvas(w.intValue(), h.intValue());
						break;
					default:
						break;
				}
	            if (fromServer.equals("Bye."))
	            	break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
