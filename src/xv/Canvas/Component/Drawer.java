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
			if(win.tools.getSelectedTool() == Tools.PEN){
				while (listener.isMouseDown() || win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()>=2) {
					
					if(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()>=2){

						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).bresenham(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(0).width, win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(0).height, 
									win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(1).width, win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(1).height,win.pen.getThickness() ,win.tools.getColor());
						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.remove(0);
					}
	
					sleep(5);
				}
			}
			else if(win.tools.getSelectedTool() == Tools.ERASER){
				while (listener.isMouseDown() || win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()>=2) {
					if(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()>=2){
						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).bresenham(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(0).width, win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(0).height, 
									win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(1).width, win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(1).height,win.eraser.getThickness() ,new Color(0,0,0,0));
						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.remove(0);
					}
					sleep(5);
				}
			}
			
			win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}