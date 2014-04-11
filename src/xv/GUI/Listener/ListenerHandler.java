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
import xv.GUI.DrawWindow;
import xv.GUI.NetworkConnectionDialog;

public class ListenerHandler extends MouseMotionAdapter implements MouseListener, KeyListener, ActionListener, ChangeListener, CaretListener, MouseMotionListener, ItemListener, AdjustmentListener, ComponentListener {

	DrawWindow win;
	Timer drawing = new Timer(1000/200,this);
	List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());

	String message;
	
	int increment = 0;
	
	private boolean mouseDown = false;

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
				int x = (int) b.getX()-win.drawPanel.getLocationOnScreen().x;
				int y = (int) b.getY()-win.drawPanel.getLocationOnScreen().y;
				
				win.canvas.layerList.get(win.canvas.getSelectedLayer()).pointList.add(new Dimension(x, y));
				message+="{\"x\": "+x+", \"y\": "+y+"},";

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
		
		if(e.getSource().equals(win.exit)){
			System.exit(0);
		}else if(e.getSource().equals(win.connect)){
			new NetworkConnectionDialog(win);
		}
		
		if(e.getSource().equals(win.layerWindow.btn_add)){
			win.canvas.addLayer();
			win.layerWindow.fillList();
		}else if (e.getSource().equals(win.layerWindow.btn_remove)){
			if(win.layerWindow.list.getSelectedIndex() != -1){
				win.canvas.removeLayer(win.layerWindow.list.getSelectedIndex());
				win.layerWindow.fillList();
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
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
		message ="{\"action\": \"LINE\",\"user\": \""+((win.net==null)?"user":win.net.username)+"\",\"layer_id\": 0, \"color\": {"
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
		win.drawPanel.setBounds(e.getComponent().getWidth()/2-win.drawPanel.getWidth()/2, e.getComponent().getHeight()/2-win.drawPanel.getHeight()/2, win.drawPanel.getWidth(), win.drawPanel.getHeight());
		;
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public ImageIcon makeImageIcon(String relative_path) {
		URL imgURL = getClass().getResource(relative_path);
		return new ImageIcon(imgURL);
	}



}
