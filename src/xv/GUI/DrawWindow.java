package xv.GUI;

import java.awt.Color;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xv.Canvas.Canvas;
import xv.Canvas.Component.Drawer;
import xv.Canvas.Component.Painter;
import xv.GUI.Listener.ListenerHandler;
import xv.Network.Client.ClientMain;

public class DrawWindow extends JFrame{
	
	public Canvas canvas;
	public ClientMain net;
	
	public DrawPanel drawPanel;
	public LayerWindow layerWindow;
	public ChatWindow chatWindow;
	ListenerHandler listener = new ListenerHandler(this);
	JMenuBar menu = new JMenuBar();
	JMenu file = new JMenu("File"),
			network = new JMenu("Network");
	public JMenuItem exit = new JMenuItem("Exit");
	public JMenuItem connect = new JMenuItem("Connect...");
	
	public DrawWindow(Canvas canvas){
		
		setJMenuBar(menu);
		
		file.add(exit);
		network.add(connect);
		
		exit.addActionListener(listener);
		connect.addActionListener(listener);
		
		menu.add(file);
		menu.add(network);
		
		this.canvas = canvas;
		layerWindow = new LayerWindow(canvas,listener);
		drawPanel = new DrawPanel(canvas);
		int x = 1280, y=720;
		
		this.setLayout(null);
		drawPanel.setSize(x, y);
		// gamePanel.setPreferredSize(new Dimension(852,480));
		drawPanel.setBackground(new Color(255, 255, 255));
		drawPanel.addMouseListener(listener);
		drawPanel.addMouseMotionListener(listener);
	
		this.add(drawPanel);
		// pack();

		Thread t = new Painter(this,listener);
		t.setDaemon( true );
	    t.start();
		
		this.setTitle("ExtraVisual");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //TODO: Custom close operation (close socket, kill stuff)
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-x/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28 + 21); //6 windows border, 28 windows frame, 21 Menu Bar
		this.setVisible(true);
	}
	
	public void establishConnection(String host, int port, String username){
		System.out.println("bop1");
		try {
			System.out.println("bop");
			chatWindow = new ChatWindow(listener);
			net = new ClientMain(InetAddress.getByName(host), port, username,this);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
