package xv.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import xv.Canvas.Canvas;
import xv.Canvas.Component.Painter;
import xv.GUI.Listener.ListenerHandler;
import xv.Network.Client.ClientMain;
import xv.Tools.PenSettings;
import xv.Tools.Tools;

public class DrawWindow extends JFrame{
	
	public Canvas canvas;
	public ClientMain net;
	public Tools tools = new Tools();
	
	public DrawPanel drawPanel;
	public LayerWindow layerWindow;
	public ToolWindow toolWindow;
	public ChatWindow chatWindow;
	public ListenerHandler listener = new ListenerHandler(this);
	JMenuBar menu = new JMenuBar();
	JMenu file = new JMenu("File"),
			settings = new JMenu("Settings"),
			view = new JMenu("View"),
			window = new JMenu("Window"),
			network = new JMenu("Network");
	public JMenuItem newc = new JMenuItem("New...");
	public JMenuItem save = new JMenuItem("Save...");
	public JMenuItem exit = new JMenuItem("Exit");
	public JLabel l_pen_size = new JLabel("Pen Size: 1");
	public JSlider pen_size = new JSlider(1,50);
	public JCheckBox backg = new JCheckBox("Display Background Grid");
	public JCheckBox dmouse = new JCheckBox("Display Mouse Pointers");
	public JCheckBox window_tools = new JCheckBox("Show Tool Window");
	public JCheckBox window_layers = new JCheckBox("Show Layer Window");
	public JCheckBox window_chat = new JCheckBox("Show Chat Window");
	public JMenuItem connect = new JMenuItem("Connect...");
	public JCheckBox smouse = new JCheckBox("Send Mouse Pointers");
	public JMenuItem host = new JMenuItem("Host Server...");
	
	public PenSettings pen = new PenSettings();
	public PenSettings eraser = new PenSettings();
	public JPanel backgroundPanel = new JPanel();
	public JScrollPane scrollPane;
	
	public boolean webcolors;

	public int x = 1280, y=720;
	
	public double zoom = 1;
	private WindowListener exitListener;
	
	
	/**
	 * Constructor of the DrawWindow
	 * 
	 * Builds the DrawWindow. Adds the MenuBar and its elements. Adds the ScrollPane and the background panel. 
	 * Sets listeners for all menu options and panels. Starts the painter thread.
	 */
	
	public DrawWindow(){
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 1 ), new JLabel("1") );
		labelTable.put( new Integer( 10 ), new JLabel("10") );
		labelTable.put( new Integer( 20 ), new JLabel("20") );
		labelTable.put( new Integer( 30 ), new JLabel("30") );
		labelTable.put( new Integer( 40 ), new JLabel("40") );
		labelTable.put( new Integer( 50 ), new JLabel("50") );
		
		pen_size.setMajorTickSpacing(10);
		pen_size.setLabelTable(labelTable);
		pen_size.setValue(1);
		pen_size.setPaintLabels(true);
		pen_size.addChangeListener(listener);

		setJMenuBar(menu);

		file.add(newc);
		file.add(save);
		file.add(exit);
		settings.add(l_pen_size);
		settings.add(pen_size);
		window.add(window_tools);
		window.add(window_layers);
		window.add(window_chat);
		view.add(backg);
		view.add(dmouse);
		network.add(connect);
		network.add(smouse);
		network.add(host);
		
		window_tools.setEnabled(false);
		window_layers.setEnabled(false);
		window_chat.setEnabled(false);
		
		backg.setSelected(true);

		newc.addActionListener(listener);
		save.addActionListener(listener);
		exit.addActionListener(listener);
		backg.addActionListener(listener);
		dmouse.addActionListener(listener);
		window_tools.addActionListener(listener);
		window_layers.addActionListener(listener);
		window_chat.addActionListener(listener);
		connect.addActionListener(listener);
		smouse.addActionListener(listener);
		host.addActionListener(listener);
		
		menu.add(file);
		menu.add(settings);
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
	    
	    exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                	if(net != null) {
                		net.sendMessage("quit");
                		net.closeSocket();
                	}
                	System.exit(0);
                }
            }
        };
		
		this.setTitle("eXtraVisual");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(exitListener);
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
		layerWindow = new LayerWindow(this,canvas,listener);
		toolWindow = new ToolWindow(this);
		
		window_tools.setEnabled(true);
		window_layers.setEnabled(true);
		window_chat.setEnabled(true);
		window_tools.setSelected(true);
		window_layers.setSelected(true);
		window_chat.setSelected(true);
		
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
			e.printStackTrace();
		}
	}
}
