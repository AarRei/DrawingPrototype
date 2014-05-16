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
						Long id = (Long)unitsJson.get("layer_position");
						win.canvas.addLayer(id.intValue());
						win.layerWindow.fillList();
						if(((String) unitsJson.get("user")).equals(win.net.username)){
							win.layerWindow.list.setSelectedIndex(id.intValue());
							win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
							win.listener.layerAdded = true;
						}
						break;
					case "RMVL":
						Long pos_r = (Long)unitsJson.get("layer_position");
						win.canvas.removeLayer(pos_r.intValue());
						win.layerWindow.fillList();
						if(((String) unitsJson.get("user")).equals(win.net.username)){
							if(pos_r.intValue() > win.canvas.layerList.size()-1)
								win.layerWindow.list.setSelectedIndex(pos_r.intValue()-1);
							else
								win.layerWindow.list.setSelectedIndex(pos_r.intValue());
							win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
							win.listener.layerRemoved = true;
						}
						break;
					case "SVCF":
						Boolean b = (Boolean) unitsJson.get("webcolors");
						win.webcolors = b.booleanValue();
						System.out.println(Boolean.toString(win.webcolors));
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
