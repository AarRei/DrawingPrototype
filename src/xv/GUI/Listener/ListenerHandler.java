package xv.GUI.Listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xv.Canvas.Component.Drawer;
import xv.GUI.CanvasCreationDialog;
import xv.GUI.DrawWindow;
import xv.GUI.NetworkConnectionDialog;
import xv.Network.Server.GUI.ServerConfigWindow;

public class ListenerHandler extends MouseMotionAdapter implements MouseListener, KeyListener, ActionListener, ChangeListener, CaretListener, MouseMotionListener, ItemListener, AdjustmentListener, ComponentListener, MouseWheelListener {

	DrawWindow win;
	Timer drawing = new Timer(1000/200,this);
	List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());

	String message;
	
	int increment = 0;
	
	private boolean mouseDown = false;
	private boolean controlDown = false;

	public boolean isMouseDown() {
		return mouseDown;
	}

	public ListenerHandler(DrawWindow win) {
		this.win = win;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(drawing)){
			if(mouseDown){
				
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int)((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom);
				int y = (int)((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom);
				if(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size() == 0){
					win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.add(new Dimension(x, y));
					message+="{\"x\": "+x+", \"y\": "+y+"},";
				}else if(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()-1).width == x &&
						win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.get(win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.size()-1).height == y){	
					//do nothing
				}else{
					win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.add(new Dimension(x, y));
					message+="{\"x\": "+x+", \"y\": "+y+"},";
				}

				//System.out.println(win.canvas.layerList.get(0).pointList.size());
				
				
				/*Graphics2D g2 = win.canvas.layerList.get(0).createGraphics();
	
				/* Hier Rendern */
				/*
				System.out.println(x+" "+y);
				g2.setColor(Color.BLACK);
				g2.fillOval(x-5, y-5, 10, 10);
				
				g2.dispose();
				
				win.drawPanel.repaint();*/
			}
		}
		/*if(e.getSource().equals(drawer)){
			Graphics2D g2 = win.canvas.layerList.get(0).createGraphics();
			
			/* Hier Rendern */
			/*while(!win.canvas.layerList.get(0).pointList.isEmpty()){
				g2.setColor(Color.BLACK);
				g2.fillOval(win.canvas.layerList.get(0).pointList.get(0).width-5, win.canvas.layerList.get(0).pointList.get(0).height-5, 10, 10);
				win.canvas.layerList.get(0).pointList.remove(0);
				if(!mouseDown && win.canvas.layerList.get(0).pointList.isEmpty())
					drawer.stop();
			}
			g2.dispose();
			win.drawPanel.repaint();
		}*/
		
		else if(e.getSource().equals(win.exit)){
			System.exit(0);
		}else if(e.getSource().equals(win.connect)){
			new NetworkConnectionDialog(win);
		}else if(e.getSource().equals(win.newc)){
			new CanvasCreationDialog(win);
		}else if(e.getSource().equals(win.host)){
		}else if(e.getSource().equals(win.backg)){
			win.drawPanel.repaint();
		}
		
		else if(e.getSource().equals(win.layerWindow.btn_add)){
			int selected= win.layerWindow.list.getSelectedIndex();
			win.canvas.addLayer(selected);
			if(win.net != null){
				win.net.sendMessage("{\"action\": \"ADDL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"layer_id\": 0,"
						+ "\"layer_position\": "+win.layerWindow.list.getSelectedIndex()+"}");
			}
			win.layerWindow.fillList();
			win.layerWindow.list.setSelectedIndex(selected);
			win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
		}else if (e.getSource().equals(win.layerWindow.btn_remove)){
			if(win.layerWindow.list.getSelectedIndex() != -1 && win.canvas.layerList.size()>1){
				int selected= win.layerWindow.list.getSelectedIndex();
				win.canvas.removeLayer(win.layerWindow.list.getSelectedIndex());
				win.layerWindow.fillList();
				if(selected > win.canvas.layerList.size()-1)
					win.layerWindow.list.setSelectedIndex(selected-1);
				else
					win.layerWindow.list.setSelectedIndex(selected);
				win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
					
				win.drawPanel.repaint();
			}
		}else if(win.chatWindow != null){
			if (e.getSource().equals(win.chatWindow.btn_send)){
				if(!win.chatWindow.txt_send.getText().equals("")){
					//win.net.sendMessage("CHAT " + win.chatWindow.txt_send.getText());
					win.net.sendMessage("{\"action\": \"CHAT\",\"user\": \""+win.net.username+"\",\"text\": \""+win.chatWindow.txt_send.getText()+"\"}");
					win.chatWindow.txt_send.setText("");
					win.chatWindow.txt_send.grabFocus();
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		/*if (e.getKeyCode() == KeyEvent.VK_A) {
			left = true;
		}*/
		if(e.getKeyCode()== KeyEvent.VK_CONTROL){
			controlDown = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()== KeyEvent.VK_CONTROL){
			controlDown = false;
		}
		if(e.getKeyCode()== KeyEvent.VK_PLUS){
			System.out.println("MÖÖP");
			win.pen.setThickness(win.pen.getThickness()+1);
		}
		if(e.getKeyCode()== KeyEvent.VK_MINUS){
			if(win.pen.getThickness() > 1)
				win.pen.setThickness(win.pen.getThickness()-1);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// (x - center_x)^2 + (y - center_y)^2 < radius^2
		// TODO Auto-generated method stub
		mouseDown = true;
		message ="{\"action\": \"LINE\",\"user\": \""+((win.net==null)?"user":win.net.username)+"\",\"layer_id\": "+win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId()+", \"color\": {"
				+ "\"R\": "+win.pen.getColor().getRed()+","
				+ "\"G\": "+win.pen.getColor().getGreen()+","
				+ "\"B\": "+win.pen.getColor().getBlue()+","
				+ "\"A\": "+win.pen.getColor().getAlpha()+"},\"points\": [";
		drawing.start();
		Thread t = new Drawer(win,this);
		t.setDaemon( true );
	    t.start();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseDown = false;
		drawing.stop();
		message = message.substring(0, message.length()-1);
		message += "]}";
		
		if(win.net != null){
			win.net.sendMessage(message);
		}
		win.drawPanel.repaint();
		
		//Graphics2D g2 = win.canvas.layerList.get(0).createGraphics();
		
		/* Hier Rendern 
		while(!win.canvas.layerList.get(0).pointList.isEmpty()){
			g2.setColor(Color.BLACK);
			//g2.fillOval(win.canvas.layerList.get(0).pointList.get(0).width-5, win.canvas.layerList.get(0).pointList.get(0).height-5, 10, 10);
			if(win.canvas.layerList.get(0).pointList.size()>1)
				g2.drawLine(win.canvas.layerList.get(0).pointList.get(0).width, win.canvas.layerList.get(0).pointList.get(0).height, win.canvas.layerList.get(0).pointList.get(1).width, win.canvas.layerList.get(0).pointList.get(1).height);
			win.canvas.layerList.get(0).pointList.remove(0);
			if(!mouseDown && win.canvas.layerList.get(0).pointList.isEmpty())
				drawer.stop();
		}
		g2.dispose();
		
		win.drawPanel.repaint();*/
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		win.centerCanvas();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if(e.isControlDown()){
			if(e.getWheelRotation() > 0){
				//down - zoom out
				if(win.zoom >0.15){
					if(win.zoom > 4)
						win.zoom -= 0.4;
					else if(win.zoom > 2)
						win.zoom -= 0.2;
					else
						win.zoom -= 0.1;
					win.drawPanel.setSize((int)(win.x * win.zoom),(int)( win.y*win.zoom));
					win.backgroundPanel.setPreferredSize(new Dimension((int)(win.x * win.zoom), (int)( win.y*win.zoom)));
					win.scrollPane.repaint();
					win.centerCanvas();
					win.repaint();
				}
			}else if(e.getWheelRotation() < 0){
				//up - zoom in
				if(win.zoom > 4)
					win.zoom += 0.4;
				else if(win.zoom > 2)
					win.zoom += 0.2;
				else
					win.zoom += 0.1;
				
				win.drawPanel.setSize((int)(win.x * win.zoom),(int)( win.y*win.zoom));
				win.backgroundPanel.setPreferredSize(new Dimension((int)(win.x * win.zoom), (int)( win.y*win.zoom)));
				win.scrollPane.repaint();
				win.centerCanvas();
				win.repaint();
			}
			
		}
	}

	public ImageIcon makeImageIcon(String relative_path) {
		URL imgURL = getClass().getResource(relative_path);
		return new ImageIcon(imgURL);
	}

}
