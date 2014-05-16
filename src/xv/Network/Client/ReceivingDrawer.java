package xv.Network.Client;

import java.awt.Color;
import java.awt.Graphics2D;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xv.GUI.DrawWindow;

public class ReceivingDrawer extends Thread {
	JSONObject json_line;
	DrawWindow win;

	/**
	 * Creates a ReceivingDrawer.
	 * 
	 * Creates a ReceivingDrawer thread.
	 * 
	 * @param json_line the JSONObject containing the command
	 * @param win parent DrawWindow
	 */
	public ReceivingDrawer(JSONObject json_line, DrawWindow win) {
		this.json_line = json_line;
		this.win = win;
	}

	/**
	 * Draws a received line.
	 * 
	 * Reads line data from the json_line object and draws a line in the specified format with the specified points.
	 */
	public void run() {
		if (((String) json_line.get("user")).equals(win.net.username)) {
			System.out.println("Own line");
			return;
		}

		JSONParser parser = new JSONParser();
		Object test = null;
		
        JSONObject color = (JSONObject) json_line.get("color");
        
		try {
			test = parser.parse(json_line.get("points").toString());
		} catch (ParseException e) {
			e.printStackTrace(); 
		}
		JSONArray points = (JSONArray) test;
		Long p=(Long)json_line.get("layer_id");
		Graphics2D g2;
		g2 = win.canvas.layerIDList.get(p.intValue()).createGraphics();
		Long r=(Long)color.get("R"), g=(Long)color.get("G"), b=(Long)color.get("B"), a=(Long)color.get("A");
		g2.setColor(new Color(r.intValue(), g.intValue(),b.intValue(),a.intValue()));
		for(int i = 0; i < points.size()-1;i++){
			JSONObject temp = (JSONObject)points.get(i);
			JSONObject temp2 = (JSONObject)points.get(i+1);
			Long x1 = (Long)temp.get("x"), y1=(Long)temp.get("y"), x2=(Long)temp2.get("x"), y2=(Long)temp2.get("y");
			g2.drawLine(x1.intValue(),y1.intValue(),x2.intValue(),y2.intValue());
		}
		g2.dispose();
		win.drawPanel.repaint();
		return;
	}

}
