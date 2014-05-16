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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import xv.Canvas.Canvas;
import xv.Canvas.Component.Drawer;
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
	
	public DrawWindow(){
		
		setJMenuBar(menu);

		file.add(newc);
		file.add(exit);
		view.add(backg);
		network.add(connect);
		network.add(host);
		
		backg.setSelected(true);

		newc.addActionListener(listener);
		exit.addActionListener(listener);
		backg.addActionListener(listener);
		connect.addActionListener(listener);
		host.addActionListener(listener);
		
		menu.add(file);
		menu.add(view);
		menu.add(window);
		menu.add(network);
		
		//this.setLayout(null);
		
		backgroundPanel.setBackground(new Color(52,52,52));
		//backgroundPanel.setSize(x, y);
		backgroundPanel.setLayout(null);		
		addKeyListener(listener);

		scrollPane = new JScrollPane(backgroundPanel);
		//scrollPane.setSize(x, y);

		
		this.add(scrollPane);
		
		//this.add
		// pack();

		Thread t = new Painter(this,listener);
		t.setDaemon( true );
	    t.start();
	    
		
		this.setTitle("ExtraVisual");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //TODO: Custom close operation (close socket, kill stuff)
		//this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-x/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28 + 21); //6 windows border, 28 windows frame, 21 Menu Bar
		this.setVisible(true);
	}
	
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
	
	public void centerCanvas(){
		drawPanel.setBounds(backgroundPanel.getWidth()/2-drawPanel.getWidth()/2, backgroundPanel.getHeight()/2-drawPanel.getHeight()/2, drawPanel.getWidth(), drawPanel.getHeight());
	}
	
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
