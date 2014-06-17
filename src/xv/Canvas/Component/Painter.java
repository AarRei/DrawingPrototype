package xv.Canvas.Component;

import xv.GUI.DrawWindow;
import xv.GUI.Listener.ListenerHandler;

public class Painter extends Thread {
	DrawWindow win;
	ListenerHandler listener;
	
	/**
	 * Creates a Painter thread.
	 * 
	 * @param win parent DrawWindow
	 * @param listener the ListenerHandler
	 */
	public Painter(DrawWindow win, ListenerHandler listener) {
		this.win = win;
		this.listener = listener;
	}
	
	/**
	 * Repaints the DrawPanel.
	 * 
	 * Repaints the DrawPanel at 30 frames per second.
	 */
	public void run() {
		try {
			while (true) {
				if(listener.isMouseDown())
					win.drawPanel.repaint(listener.leftmost-5-win.pen.getThickness()/2, 
							listener.highest-5-win.pen.getThickness()/2, 
							listener.rightmost-listener.leftmost+10+win.pen.getThickness(),
							listener.lowest-listener.highest+10+win.pen.getThickness());
				sleep(1000/30);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}