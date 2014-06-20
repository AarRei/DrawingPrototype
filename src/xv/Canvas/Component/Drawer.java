package xv.Canvas.Component;

import java.awt.Color;
import xv.GUI.DrawWindow;
import xv.GUI.Listener.ListenerHandler;
import xv.Tools.Tools;

public class Drawer extends Thread {
	DrawWindow win;
	ListenerHandler listener;

	/**
	 * Creates a Drawer thread.
	 * 
	 * @param win parent DrawWindow
	 * @param listener the ListenerHandler
	 */
	public Drawer(DrawWindow win, ListenerHandler listener) {
		this.win = win;
		this.listener = listener;
	}
	
	/**
	 * Draws the lines.
	 * 
	 * Draws the pen stroke by connecting received coordinates with lines. 
	 * The lines are drawn in the currently chosen color.
	 */
	public void run() {
		try {
			//Graphics2D g2;
			while (listener.isMouseDown() || win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()>=2) {
				
				if(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()>=2){
					if(win.tools.getSelectedTool() == Tools.PEN)
						win.canvas.layerList.get(win.canvas.getSelectedLayer()).bresenham(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).height, 
								win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).height,win.pen.getThickness() ,win.pen.getColor());
					else
						win.canvas.layerList.get(win.canvas.getSelectedLayer()).bresenham(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(0).height, 
								win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).width, win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(1).height,win.pen.getThickness() ,new Color(0,0,0,0));
					win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.remove(0);
				}

				sleep(5);
			}
			win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}