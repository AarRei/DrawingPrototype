package xv.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
	ListenerHandler listener = new ListenerHandler(this);
	JMenuBar menu = new JMenuBar();
	JMenu file = new JMenu("File"),
			network = new JMenu("Network");
	public JMenuItem newc = new JMenuItem("New...");
	public JMenuItem exit = new JMenuItem("Exit");
	public JMenuItem connect = new JMenuItem("Connect...");
	
	public PenSettings pen = new PenSettings();
	JPanel backgroundPanel = new JPanel();
	JScrollPane scrollPane;

	int x = 1280, y=720;
	
	public DrawWindow(){
		
		setJMenuBar(menu);

		file.add(newc);
		file.add(exit);
		network.add(connect);

		newc.addActionListener(listener);
		exit.addActionListener(listener);
		connect.addActionListener(listener);
		
		menu.add(file);
		menu.add(network);
		
		//this.setLayout(null);
		
		backgroundPanel.setBackground(new Color(52,52,52));
		//backgroundPanel.setSize(x, y);
		backgroundPanel.setLayout(null);		

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
		canvas = new Canvas(width,height);
		layerWindow = new LayerWindow(canvas,listener);
		toolWindow = new ToolWindow(this);
		
		drawPanel = new DrawPanel(width,height,canvas);

		drawPanel.addMouseListener(listener);
		drawPanel.addMouseMotionListener(listener);
		drawPanel.setSize(width, height);
		drawPanel.setBackground(new Color(255, 255, 255));

		backgroundPanel.addComponentListener(listener);

		backgroundPanel.setPreferredSize(new Dimension(width, height));
		
		backgroundPanel.add(drawPanel);
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
