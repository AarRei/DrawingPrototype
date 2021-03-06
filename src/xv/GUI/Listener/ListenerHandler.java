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
import javax.swing.JCheckBox;
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
import xv.GUI.LayerConfigWindow;
import xv.GUI.NetworkConnectionDialog;
import xv.Network.Client.ReceivingDrawer;
import xv.Tools.Bezier;
import xv.Tools.Tools;

public class ListenerHandler extends MouseMotionAdapter implements MouseListener, KeyListener, ActionListener, ChangeListener, CaretListener, MouseMotionListener, ItemListener, AdjustmentListener, ComponentListener, MouseWheelListener {

	public DrawWindow win;
	Timer drawing = new Timer(1000/200,this);
	List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());
	public List<Bezier> bezierList = Collections.synchronizedList(new ArrayList<Bezier>());
	public Bezier tempBezier;
	
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
	 * 5 wenn neue Punkte hinzugef�gt werden oder ein Punkt zum verschieben ausgew�hlt wird.
	 * 1-4 wenn der jeweilige Punkt verschoben wird.
	 */
	private int moveflag = 5;
	private Bezier choosenBezier;
	
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
		if(e.getSource().equals(win.pen_size)){
			if(win.tools.getSelectedTool() == Tools.PEN){
				win.l_pen_size.setText("Pen Size: "+win.pen_size.getValue());
				win.pen.setThickness(win.pen_size.getValue());
			}else{
				win.l_pen_size.setText("Eraser Size: "+win.pen_size.getValue());
				win.eraser.setThickness(win.pen_size.getValue());
			}
		}
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
				
				if(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size() == 0){
					win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.add(new Dimension(x, y));
					message+="{\"x\": "+x+", \"y\": "+y+"},";
				}else if(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()-1).width == x &&
						win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.get(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.size()-1).height == y){	
					//do nothing
				}else{
					win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).pointList.add(new Dimension(x, y));
					message+="{\"x\": "+x+", \"y\": "+y+"},";
				}
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
		}else if(e.getSource().equals(win.window_tools)){
			if(e.getSource() instanceof JCheckBox) {
				((JCheckBox) e.getSource()).setSelected(win.toolWindow.toggle());
			}
		}else if(e.getSource().equals(win.window_layers)){
			if(e.getSource() instanceof JCheckBox) {
				((JCheckBox) e.getSource()).setSelected(win.layerWindow.toggle());
			}
		}else if(e.getSource().equals(win.window_chat)){
			if(e.getSource() instanceof JCheckBox) {
				((JCheckBox) e.getSource()).setSelected(win.chatWindow.toggle());
			}
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
		}else if (e.getSource().equals(win.layerWindow.btn_setting)) {
			new LayerConfigWindow(win, win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId());			
		}else if(win.chatWindow != null){
			if (e.getSource().equals(win.chatWindow.btn_send)){
				if(!win.chatWindow.txt_send.getText().equals("")){
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
			System.out.println("M��P");
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
		if(win.tools.getSelectedTool()==Tools.PEN ||win.tools.getSelectedTool()==Tools.ERASER){
			mouseDown = true;
			message ="{\"action\": \"LINE\","
					+ "\"user\": \""+((win.net==null)?"user":win.net.username)
					+"\",\"layer_id\": "+win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId()
					+", \"type\": "+win.tools.getSelectedTool()
					+", \"thickness\": "+ ((win.tools.getSelectedTool() == Tools.PEN)?win.pen.getThickness():win.eraser.getThickness())
					+", \"color\": {"
					+ "\"R\": "+win.tools.getColor().getRed()+","
					+ "\"G\": "+win.tools.getColor().getGreen()+","
					+ "\"B\": "+win.tools.getColor().getBlue()+","
					+ "\"A\": "+win.tools.getColor().getAlpha()+"},\"points\": [";
			
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
			Point point = new Point((int) ((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom),(int) (int) ((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom));
			if(newBezier) {
				for(Bezier b: bezierList)
					for(int i=0;i<b.getNumpoints();i++)
						for(int j=-5;j<6;j++)
						for(int l=-5;l<6;l++)
						if(point.equals(new Point(b.getPoints()[i].x+j, b.getPoints()[i].y+l))) {
							moveflag = i;
							choosenBezier = b;
							break;
						}
				if(moveflag == 5) {
					System.out.println(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId());
					Bezier bezier = new Bezier(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId());
					bezier.setColor(win.tools.getColor());
					bezier.setFirst(point.x, point.y);
					newBezier = !newBezier;
					if(win.net != null){
						tempBezier = bezier;
					}else{
						bezierList.add(bezier);
					}
				}
			} else {
				if(win.net != null){
					tempBezier.setSecond(point.x, point.y);
				}else{
					bezierList.get(bezierList.size()-1).setSecond(point.x, point.y);
				}
				
				newBezier = !newBezier;
				if(win.net != null){
					String bezierSend = "{\"action\": \"BEZR\","
							+ "\"user\": \""+win.net.username+"\","
							+ "\"user_id\": 0,"
							+ "\"layer_id\": "+tempBezier.getLayerId()+","
							+ "\"color\": "
									+ "{\"R\": "+tempBezier.getColor().getRed()+","
									+ "\"G\": "+tempBezier.getColor().getGreen()+","
									+ "\"B\": "+tempBezier.getColor().getBlue()+","
									+ "\"A\": "+tempBezier.getColor().getAlpha()+"},"
							+ "\"start_x\": "+tempBezier.getPoints()[0].x+","
							+ "\"start_y\": "+tempBezier.getPoints()[0].y+","
							+ "\"end_x\": "+tempBezier.getPoints()[1].x+","
							+ "\"end_y\": "+tempBezier.getPoints()[1].y+","
							+ "\"start_man_x\": "+tempBezier.getPoints()[2].x+","
							+ "\"start_man_y\": "+tempBezier.getPoints()[2].y+","
							+ "\"end_man_x\": "+tempBezier.getPoints()[3].x+","
							+ "\"end_man_y\": "+tempBezier.getPoints()[3].y+"}";
					win.net.sendMessage(bezierSend);
					System.out.println("curve send");
				}
			}
			win.drawPanel.repaint();
		}else if(win.tools.getSelectedTool()==Tools.PICKER){
			a = MouseInfo.getPointerInfo();
			b = a.getLocation();
			Point point = new Point((int) ((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom),(int) (int) ((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom));
			Color picked = new Color(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getRGB(point.x, point.y));
			System.out.println(picked);
			win.tools.setColor(picked);
		}else if(win.tools.getSelectedTool()==Tools.FILL){
			a = MouseInfo.getPointerInfo();
			b = a.getLocation();
			int x = (int)((int) (b.getX()-win.drawPanel.getLocationOnScreen().x)/win.zoom);
			int y = (int)((int) (b.getY()-win.drawPanel.getLocationOnScreen().y)/win.zoom);
			System.out.println("x: "+x+" y: "+y);
			
			win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).fill(x, y, new Color(win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getRGB(x, y),true), win.tools.getColor());
			
			if(win.net != null){
				String fillSend = "{ \"action\": \"FILL\", "
						+ "\"user\": \""+win.net.username+"\", "
						+ "\"user_id\": 0, "
						+ "\"layer_id\":"+win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getId()+", "
						+ "\"color\": {  "
							+ "\"R\": "+win.tools.getColor().getRed()+",  "
							+ "\"G\": "+win.tools.getColor().getGreen()+",  "
							+ "\"B\": "+win.tools.getColor().getBlue()+",  "
							+ "\"A\": "+win.tools.getColor().getAlpha()+" }, "
						+ "\"x\": "+x+", "
						+ "\"y\": "+y+"  }";
				win.net.sendMessage(fillSend);
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
		if(win.tools.getSelectedTool()==Tools.PEN||win.tools.getSelectedTool()==Tools.ERASER){
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
		else if(win.tools.getSelectedTool()==Tools.BEZIER){
			if(moveflag < 5){
				if(win.net != null){
					String bezierChangeSend = "{\"action\": \"BEZC\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"layer_id\": "+choosenBezier.getLayerId()+","
						+ "\"bezier_id\": "+choosenBezier.getID()+","
						+ "\"start_x\": "+choosenBezier.getPoints()[0].x+","
						+ "\"start_y\": "+choosenBezier.getPoints()[0].y+","
						+ "\"end_x\": "+choosenBezier.getPoints()[1].x+","
						+ "\"end_y\": "+choosenBezier.getPoints()[1].y+","
						+ "\"start_man_x\": "+choosenBezier.getPoints()[2].x+","
						+ "\"start_man_y\": "+choosenBezier.getPoints()[2].y+","
						+ "\"end_man_x\": "+choosenBezier.getPoints()[3].x+","
						+ "\"end_man_y\": "+choosenBezier.getPoints()[3].y+"}";
					win.net.sendMessage(bezierChangeSend);
				}
			}
			moveflag = 5;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(win.tools.getSelectedTool()==Tools.BEZIER){
			if(moveflag < 5) {
				
				int posX = e.getX();
				int posY = e.getY();
				
				if(e.getX() < 0)
					posX = 0;
				if(e.getX() > win.drawPanel.width)
					posX = win.drawPanel.width;
				if(e.getY() < 0)
					posY = 0;
				if(e.getY() > win.drawPanel.height)
					posY = win.drawPanel.height;
				choosenBezier.getPoints()[moveflag].move(posX, posY);
	
				win.drawPanel.repaint();
			}	
		}
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
					
					System.out.println("Zoom: "+win.zoom);
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
				
				System.out.println("Zoom: "+win.zoom);
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
