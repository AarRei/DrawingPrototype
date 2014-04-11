package xv.Canvas.Component;

import xv.GUI.DrawWindow;
import xv.GUI.Listener.ListenerHandler;

public class Painter extends Thread {
	DrawWindow win;
	ListenerHandler listener;

	public Painter(DrawWindow win, ListenerHandler listener) {
		this.win = win;
		this.listener = listener;
	}
	
	/*while ((count = in.read(buffer)) > 0) {
		  out.write(buffer, 0, count);
		}*/
	
	public void run() {
		try {
			while (true) {
				if(listener.isMouseDown())
					win.drawPanel.repaint();
				sleep(1000/60);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}