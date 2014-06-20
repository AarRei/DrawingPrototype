package xv.Network.Client;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xv.GUI.DrawWindow;
import xv.Network.Server.ClientThread;
import xv.Tools.Bezier;

public class MessageReceiveThread extends Thread{
	
	BufferedReader in;
	DrawWindow win;
	
	/**
	 * Creates the MessageReceiveThread.
	 * 
	 * Creates the MessageReceiveThread and sets the input stream.
	 * 
	 * @param in BufferedReader as input stream 
	 * @param win parent DrawWindow
	 */
	public MessageReceiveThread(BufferedReader in,DrawWindow win){
		this.win = win;
		this.in = in;
	}
	
	/**
	 * Reads from the input stream.
	 * 
	 * Reads data from the input stream until it returns null (the connection is closed).
	 * Converts received data into JSON Objects and handles the commands depending on the action type:
	 * CHAT: chat message
	 * LINE: line drawing action
	 * ADDL: adding a layer
	 * RMVL: removing a layer
	 * SVCF: server configuration data
	 */
	public void run(){
		String fromServer;
		
		JSONParser parser = new JSONParser();
        Object unitsObj = null;
        
        JSONObject unitsJson;
		try {
			while ((fromServer = in.readLine()) != null) {
				unitsObj = parser.parse(fromServer);
				unitsJson  = (JSONObject) unitsObj;
				System.out.println(fromServer);
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
						int tempLayer = 0;
						if(!((String) unitsJson.get("user")).equals(win.net.username)){
							tempLayer = win.layerWindow.list.getSelectedIndex();
						}
						win.canvas.addLayer(id.intValue());
						win.layerWindow.fillList();
						if(((String) unitsJson.get("user")).equals(win.net.username)){
							win.layerWindow.list.setSelectedIndex(id.intValue());
							win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
							win.listener.layerAdded = true;
						}else{
							if(id.intValue() <= tempLayer){
								win.layerWindow.list.setSelectedIndex(tempLayer+1);
								win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
							}else{
								win.layerWindow.list.setSelectedIndex(tempLayer);
								win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
							}
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
						win.drawPanel.repaint();
						break;
					case "SVCF":
						Boolean b = (Boolean) unitsJson.get("webcolors");
						win.webcolors = b.booleanValue();
						System.out.println(Boolean.toString(win.webcolors));
						Long w = (Long)unitsJson.get("width"), h = (Long)unitsJson.get("height");
						win.createCanvas(w.intValue(), h.intValue());
						break;
					case "BEZR":
						Long layerId, p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y, red, green, blue, alpha;
						layerId = (Long) unitsJson.get("layer_id");
						p1x = (Long) unitsJson.get("start_x");
						p1y = (Long) unitsJson.get("start_y");
						p2x = (Long) unitsJson.get("end_x");
						p2y = (Long) unitsJson.get("end_y");
						p3x = (Long) unitsJson.get("start_man_x");
						p3y = (Long) unitsJson.get("start_man_y");
						p4x = (Long) unitsJson.get("end_man_x");
						p4y = (Long) unitsJson.get("end_man_y");
						Point p1 = new Point(p1x.intValue(),p1y.intValue());
						Point p2 = new Point(p2x.intValue(),p2y.intValue());
						Point p3 = new Point(p3x.intValue(),p3y.intValue());
						Point p4 = new Point(p4x.intValue(),p4y.intValue());
						JSONObject color = (JSONObject) unitsJson.get("color");
						red = (Long) color.get("R");
						green  = (Long) color.get("G");
						blue = (Long) color.get("B");
						alpha = (Long) color.get("A");
						
						win.listener.bezierList.add(new Bezier(layerId.intValue(), new Color(red.intValue(),green.intValue(),blue.intValue(),alpha.intValue()), p1, p2, p3, p4));
						win.listener.bezierList.get(win.listener.bezierList.size()-1).setID(win.listener.bezierList.size()-1);
						win.drawPanel.repaint();
						if(win.net.username.equals((String) unitsJson.get("user")));
							win.listener.tempBezier = null;
						break;
					case "BEZC":
						Long p1xc, p1yc, p2xc, p2yc, p3xc, p3yc, p4xc, p4yc, bezierId;
						bezierId = (Long) unitsJson.get("bezier_id");
						p1xc = (Long) unitsJson.get("start_x");
						p1yc = (Long) unitsJson.get("start_y");
						p2xc = (Long) unitsJson.get("end_x");
						p2yc = (Long) unitsJson.get("end_y");
						p3xc = (Long) unitsJson.get("start_man_x");
						p3yc = (Long) unitsJson.get("start_man_y");
						p4xc = (Long) unitsJson.get("end_man_x");
						p4yc = (Long) unitsJson.get("end_man_y");
						win.listener.bezierList.get(bezierId.intValue()).getPoints()[0].move(p1xc.intValue(),p1yc.intValue());
						win.listener.bezierList.get(bezierId.intValue()).getPoints()[1].move(p2xc.intValue(),p2yc.intValue());
						win.listener.bezierList.get(bezierId.intValue()).getPoints()[2].move(p3xc.intValue(),p3yc.intValue());
						win.listener.bezierList.get(bezierId.intValue()).getPoints()[3].move(p4xc.intValue(),p4yc.intValue());
						win.drawPanel.repaint();
						break;
					case "FILL":
						break;
					case "USER":
						win.net.usersList.clear();
						Object usersParser = null;
						try {
							usersParser = parser.parse(unitsJson.get("users").toString());
						} catch (ParseException e) {
							e.printStackTrace(); 
						}
						JSONArray users = (JSONArray) usersParser;
						for(int i = 0;i< users.size();i++){
							JSONObject temp = (JSONObject)users.get(i);
							win.net.usersList.add((String)temp.get("user"));
							win.chatWindow.refreshUsers();
						}
						break;
					case "TOOL":
						String user = (String) unitsJson.get("user");
						String tool = (String) unitsJson.get("tool");
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
