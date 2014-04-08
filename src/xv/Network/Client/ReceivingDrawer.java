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

	public ReceivingDrawer(JSONObject json_line, DrawWindow win) {
		this.json_line = json_line;
		this.win = win;
	}

	public void run() {
		if (((String) json_line.get("user")).equals(win.net.username)) {
			System.out.println("Own line");
			return;
		}

		JSONParser parser = new JSONParser();
		Object test = null;
		try {
			test = parser.parse(json_line.get("points").toString());
		} catch (ParseException e) {
			e.printStackTrace(); 
		}
		JSONArray points = (JSONArray) test;
		Graphics2D g2;
		g2 = win.canvas.layerList.get(0).createGraphics();
		for(int i = 0; i < points.size()-1;i++){
			JSONObject temp = (JSONObject)points.get(i);
			JSONObject temp2 = (JSONObject)points.get(i+1);
			Long x1 = (Long)temp.get("x"), y1=(Long)temp.get("y"), x2=(Long)temp2.get("x"), y2=(Long)temp2.get("y");
			g2.setColor(Color.BLACK);
			g2.drawLine(x1.intValue(),y1.intValue(),x2.intValue(),y2.intValue());
		}
		g2.dispose();
		win.drawPanel.repaint();
		return;
	}

}
