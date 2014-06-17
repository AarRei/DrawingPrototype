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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xv.Canvas.Component.Drawer;
import xv.GUI.CanvasCreationDialog;
import xv.GUI.DrawWindow;
import xv.GUI.NetworkConnectionDialog;
import xv.Network.Client.ReceivingDrawer;
import xv.Network.Server.GUI.ServerConfigWindow;
import xv.Tools.Bezier;
import xv.Tools.Tools;

public class ListenerHandler extends MouseMotionAdapter implements MouseListener, KeyListener, ActionListener, ChangeListener, CaretListener, MouseMotionListener, ItemListener, AdjustmentListener, ComponentListener, MouseWheelListener {

	DrawWindow win;
	Timer drawing = new Timer(1000/200,this);
	List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());
	public List<Bezier> bezierList = Collections.synchronizedList(new ArrayList<Bezier>());
	
	PointerInfo a;
	Point b;
	/**
	 * Message to be send to the server.
	 */
	String message;
	
	int increment = 0;
	
	public int leftmost = 0, rightmost = 0, highest = 0, lowest = 0;
	
	/**
	 * Shows if the left mouse button is currently being pressed.
	 */
	private boolean mouseDown = false;
	/**
	 * Shows if the added layer has already been received.
	 */
	public boolean layerAdded = true;
	/**
	 * Shows if the removed layer has already been received.
	 */
	public boolean layerRemoved = true;
	
	/**
	 * Returns if the mouse button is currently pressed.
	 * 
	 * @return mouse button state
	 */
	public boolean isMouseDown() {
		return mouseDown;
	}
		
	/**
	 * Anzahl der Punkte
	 */
	private boolean newBezier = true;
	
	/**
	 * 5 wenn neue Punkte hinzugefügt werden oder ein Punkt zum verschieben ausgewählt wird.
	 * 1-4 wenn der jeweilige Punkt verschoben wird.
	 */
	private int moveflag = 5;
	
	/**
	 * Creates a ListenerHandler object.
	 * 
	 * @param win parent DrawWindow
	 */
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
	
	/**
	 * Controls all actions.
	 * 
	 * Controls the actions of the elements in the menu bar.
	 * 
	 * Reacts to the pressing of the Layer Add and Layer Remove button.
	 * 
	 * Records the coordinates while drawing a line.
	 * 
	 * Reacts to the send button of the ChatWindow, by sending the message.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(drawing)){
			if(mouseDown){
				a = MouseInfo.getPointerInfo();
				b = a.getLocation();
				int x = (int)((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom);
				int y = (int)((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom);
				
				if(x < leftmost)
					leftmost = x;
				if(x > rightmost)
					rightmost = x;
				if(y < highest)
					highest = y;
				if(y > lowest)
					lowest = y;
				
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
			}
		}
				
		else if(e.getSource().equals(win.exit)){
			System.exit(0);
		}else if(e.getSource().equals(win.connect)){
			new NetworkConnectionDialog(win);
		}else if(e.getSource().equals(win.newc)){
			new CanvasCreationDialog(win);
		}else if(e.getSource().equals(win.save)){
			saveImage();
		}else if(e.getSource().equals(win.host)){
		}else if(e.getSource().equals(win.backg)){
			win.drawPanel.repaint();
		}
		
		else if(e.getSource().equals(win.layerWindow.btn_add) && layerAdded){
			if(win.layerWindow.list.getSelectedIndex() != -1){
				if(win.net == null){
					int selected= win.layerWindow.list.getSelectedIndex();
					win.canvas.addLayer(selected);
					win.layerWindow.fillList();
					win.layerWindow.list.setSelectedIndex(selected);
					win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
				}
				
				if(win.net != null){
					win.net.sendMessage("{\"action\": \"ADDL\","
							+ "\"user\": \""+win.net.username+"\","
							+ "\"layer_id\": 0,"
							+ "\"layer_position\": "+win.layerWindow.list.getSelectedIndex()+"}");
					layerAdded = false;
					win.layerWindow.fillList();
				}
				win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
			}
		}else if (e.getSource().equals(win.layerWindow.btn_remove) && layerRemoved){
			if(win.layerWindow.list.getSelectedIndex() != -1 && win.canvas.layerList.size()>1){
				int selected= win.layerWindow.list.getSelectedIndex();
				if(win.net != null){
					win.net.sendMessage("{\"action\": \"RMVL\","
							+ "\"user\": \""+win.net.username+"\","
							+ "\"layer_id\": 0,"
							+ "\"layer_position\": "+win.layerWindow.list.getSelectedIndex()+"}");

					win.layerWindow.fillList();
					layerRemoved = false;
				}else{
					win.canvas.removeLayer(selected);
					win.layerWindow.fillList();
					if(selected > win.canvas.layerList.size()-1)
						win.layerWindow.list.setSelectedIndex(selected-1);
					else
						win.layerWindow.list.setSelectedIndex(selected);
					win.canvas.setSelectedLayer(win.layerWindow.list.getSelectedIndex());
				}				
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
	}
	
	/**
	 * Testing of line thickness. Pressing + makes it thicker, - makes it thinner.
	 * Undo function.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()== KeyEvent.VK_PLUS){
			System.out.println("MÖÖP");
			win.pen.setThickness(win.pen.getThickness()+1);
		}
		if(e.getKeyCode()== KeyEvent.VK_MINUS){
			if(win.pen.getThickness() > 1)
				win.pen.setThickness(win.pen.getThickness()-1);
		}
		if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()){
			if(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).actionList.size() > 0){
				Color clear = new Color(0,0,0,0);
				for(int i = 0; i < win.drawPanel.width;i++){
					for(int j = 0; j < win.drawPanel.height; j++){
						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).setRGB(i, j,clear.getRGB());
					}
				}
				win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).actionList.remove(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).actionList.size()-1);
				JSONParser parser = new JSONParser();
		        Object unitsObj = null;
		        
		        JSONObject unitsJson;
				for(String f : win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).actionList){
					try {
						unitsObj = parser.parse(f);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					unitsJson  = (JSONObject) unitsObj;
					new ReceivingDrawer(unitsJson, win).start();
				}
				win.drawPanel.repaint();
			}
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
	
	

	/**
	 * Starts the Drawer thread and establishes the message to be send to the server.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// (x - center_x)^2 + (y - center_y)^2 < radius^2
		// TODO Auto-generated method stub
		if(win.tools.getSelectedTool()==Tools.PEN){
			mouseDown = true;
			message ="{\"action\": \"LINE\",\"user\": \""+((win.net==null)?"user":win.net.username)+"\",\"layer_id\": "+win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId()+", \"color\": {"
					+ "\"R\": "+win.pen.getColor().getRed()+","
					+ "\"G\": "+win.pen.getColor().getGreen()+","
					+ "\"B\": "+win.pen.getColor().getBlue()+","
					+ "\"A\": "+win.pen.getColor().getAlpha()+"},\"points\": [";
			
			a = MouseInfo.getPointerInfo();
			b = a.getLocation();
			int x = (int)((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom);
			int y = (int)((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom);
			leftmost = x;
			rightmost = x;
			highest = y;
			lowest = y;
			
			drawing.start();
			Thread t = new Drawer(win,this);
			t.setDaemon( true );
		    t.start();
		}else if(win.tools.getSelectedTool()==Tools.BEZIER){
			a = MouseInfo.getPointerInfo();
			b = a.getLocation();
			if(newBezier) {
				Bezier bezier = new Bezier();
				bezier.setFirst((int) ((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom),(int) (int) ((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom));
				newBezier = !newBezier;
				bezierList.add(bezier);
			} else {
				bezierList.get(bezierList.size()-1).setSecond((int) ((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom),(int) (int) ((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom));
				newBezier = !newBezier;
			}
			win.drawPanel.repaint();
		}
	}

	/**
	 * Sends the message to the to the server, stops the Drawer thread and repaints the DrawPanel.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(win.tools.getSelectedTool()==Tools.PEN){
			mouseDown = false;
			drawing.stop();
			message = message.substring(0, message.length()-1);
			message += "]}";
			
			System.out.println(win.layerWindow.list.getSelectedIndex());
			win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).actionList.add(message);
			if(win.net != null){
				win.net.sendMessage(message);
			}
			win.drawPanel.repaint();
		}
		
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
	
	/**
	 * Zoom test. Hold CTRL and spin the mouse wheel.
	 */
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
					win.backgroundPanel.revalidate();
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
				win.backgroundPanel.revalidate();
				win.scrollPane.repaint();
				win.centerCanvas();
				win.repaint();
			}
			
		}
	}
	
	/**
	 * Creates an ImageIcon.
	 * 
	 * Gets an image file from a relative path and creates and returns an ImageIcon.
	 * 
	 * @param relative_path relative path of the image file
	 * @return created ImageIcon
	 */
	
	private void saveImage(){
		BufferedImage buffer = new BufferedImage(win.drawPanel.width, win.drawPanel.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buffer.createGraphics();

		/* Hier Rendern */
				for(int i = win.canvas.layerList.size()-1;i >= 0 ;i--){
			g2.drawImage(win.canvas.layerList.get(i), 0, 0, win);
		}
		
		
		/* Hier nicht mehr Rendern */
		
		g2.dispose();

	    Date date = new Date();
		try {
            if (ImageIO.write(buffer, "png", new File("./Test"+date.toString().replace(':','-').substring(4, 19)+".png")))
            {
                System.out.println("-- saved");
            }
	    } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	    }
	}
	
	public ImageIcon makeImageIcon(String relative_path) {
		URL imgURL = getClass().getResource(relative_path);
		return new ImageIcon(imgURL);
	}

}
