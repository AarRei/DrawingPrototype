package xv.Canvas.Component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import xv.GUI.DrawWindow;
import xv.GUI.Listener.ListenerHandler;

public class Drawer extends Thread {
	DrawWindow win;
	ListenerHandler listener;

	public Drawer(DrawWindow win, ListenerHandler listener) {
		this.win = win;
		this.listener = listener;
		System.out.println("thread constructor");
	}
	
	/*while ((count = in.read(buffer)) > 0) {
		  out.write(buffer, 0, count);
		}*/
	
	public void run() {
		System.out.println("run");
		try {
			System.out.println("try");
			Graphics2D g2;
			while (listener.isMouseDown() || win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()>=2) {
				//System.out.println(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size());
				if(win.layerWindow.list.getSelectedIndex() != -1)
					g2 = win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).createGraphics();
				else
					g2 = win.canvas.layerList.get(0).createGraphics();
				
				g2.setColor(win.pen.getColor());
				//g2.fillOval(win.canvas.layerList.get(0).pointList.get(0).width-5, win.canvas.layerList.get(0).pointList.get(0).height-5, 10, 10);
				if(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()>=2){
					g2.drawLine(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).height, 
							win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).height);
					win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.remove(0);
				}
				//win.canvas.layerList.get(0).pointList.remove(0);
				
				g2.dispose();

				sleep(1);
			}
			win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}