package xv.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xv.Canvas.Canvas;
import xv.Canvas.Component.Painter;
import xv.GUI.Listener.ListenerHandler;
import xv.Network.Client.ClientMain;
import xv.Tools.PenSettings;

public class DrawWindow extends JFrame{
	
	public Canvas canvas;
	public ClientMain net;
	
	public DrawPanel drawPanel;
	public LayerWindow layerWindow;
	public ToolWindow toolWindow;
	public ChatWindow chatWindow;
	public ListenerHandler listener = new ListenerHandler(this);
	JMenuBar menu = new JMenuBar();
	JMenu file = new JMenu("File"),
			view = new JMenu("View"),
			window = new JMenu("Window"),
			network = new JMenu("Network");
	public JMenuItem newc = new JMenuItem("New...");
	public JMenuItem save = new JMenuItem("Save...");
	public JMenuItem exit = new JMenuItem("Exit");
	public JCheckBox backg = new JCheckBox("Display Background Grid");
	public JMenuItem connect = new JMenuItem("Connect...");
	public JMenuItem host = new JMenuItem("Host Server...");
	
	public PenSettings pen = new PenSettings();
	public JPanel backgroundPanel = new JPanel();
	public JScrollPane scrollPane;
	
	public boolean webcolors;

	public int x = 1280, y=720;
	
	public double zoom = 1;
	
	/**
	 * Constructor of the DrawWindow
	 * 
	 * Builds the DrawWindow. Adds the MenuBar and its elements. Adds the ScrollPane and the background panel. 
	 * Sets listeners for all menu options and panels. Starts the painter thread.
	 */
	
	public DrawWindow(){
		
		setJMenuBar(menu);

		file.add(newc);
		file.add(save);
		file.add(exit);
		view.add(backg);
		network.add(connect);
		network.add(host);
		
		backg.setSelected(true);

		newc.addActionListener(listener);
		save.addActionListener(listener);
		exit.addActionListener(listener);
		backg.addActionListener(listener);
		connect.addActionListener(listener);
		host.addActionListener(listener);
		
		menu.add(file);
		menu.add(view);
		menu.add(window);
		menu.add(network);
				
		backgroundPanel.setBackground(new Color(52,52,52));
		backgroundPanel.setLayout(null);		
		addKeyListener(listener);

		scrollPane = new JScrollPane(backgroundPanel);
		
		this.add(scrollPane);

		Thread t = new Painter(this,listener);
		t.setDaemon( true );
	    t.start();
	    
		
		this.setTitle("ExtraVisual");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //TODO: Custom close operation (close socket, kill stuff)
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-x/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28 + 21); //6 windows border, 28 windows frame, 21 Menu Bar
		this.setVisible(true);
	}
	
	/**
	 * Creates the canvas object.
	 * 
	 * Creates the canvas object with a specified width and height. Furthermore the drawPanel, 
	 * the tool window and the layer window are initialized and the drawPanel added to the background panel.
	 * 
	 * @param width width of the canvas
	 * @param height height of the canvas
	 */
	
	public void createCanvas(int width, int height){
		x = width;
		y = height;
		canvas = new Canvas(width,height);
		layerWindow = new LayerWindow(canvas,listener);
		toolWindow = new ToolWindow(this);
		
		drawPanel = new DrawPanel(width,height,canvas,this);

		drawPanel.addMouseListener(listener);
		drawPanel.addMouseMotionListener(listener);
		drawPanel.setSize(width, height);
		drawPanel.setBackground(new Color(255, 255, 255));

		backgroundPanel.addComponentListener(listener);

		backgroundPanel.setPreferredSize(new Dimension(width, height));
		
		backgroundPanel.add(drawPanel);
		
	    backgroundPanel.addMouseWheelListener(listener);

	    centerCanvas();
		repaint();

	}
	
	/**
	 * Centers the drawPanel
	 * 
	 * The method centers the drawPanel within the bounds of the background panel.
	 */
	public void centerCanvas(){
		drawPanel.setBounds(backgroundPanel.getWidth()/2-drawPanel.getWidth()/2, backgroundPanel.getHeight()/2-drawPanel.getHeight()/2, drawPanel.getWidth(), drawPanel.getHeight());
	}
	
	/**
	 * Establishes a connection to a server.
	 * 
	 * Initializes the ClientMain object with the passed parameters. Throws an UnknownHostException if unsuccessful. 
	 * Also opens the chat window.
	 * 
	 * @param host the IP address or hostname of the host 
	 * @param port the port to which the client connects
	 * @param username the desired username
	 */
	public void establishConnection(String host, int port, String username){
		try {
			chatWindow = new ChatWindow(listener);
			net = new ClientMain(InetAddress.getByName(host), port, username,this);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
