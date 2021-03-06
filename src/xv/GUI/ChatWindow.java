package xv.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import xv.Canvas.Canvas;
import xv.GUI.Listener.ListenerHandler;

public class ChatWindow extends JFrame{
	Canvas canvas;
	public JList list;
	DefaultListModel listModel;
	
	JScrollPane listScroller2;
	public JList list2;
	DefaultListModel listModel2;
	
	public JButton btn_send = new JButton("Send");
	public JTextField txt_send = new JTextField();
	JLabel lbl_connected = new JLabel("Connected User:");
	JTextField txt_user = new JTextField();
	JScrollPane listScroller;
	private ListenerHandler listener;
	
	/**
	 * Constructs the ChatWindow.
	 * 
	 * Builds the ChatWindow and adds listener to send button.
	 * 
	 * @param listener the ListenerHandler
	 */
	public ChatWindow(final ListenerHandler listener){
		this.canvas = canvas;
		int x = 500, y = 340;
		
		this.listener = listener;
		this.setIconImage(listener.makeImageIcon("/icons/Comment.png").getImage());

		this.setLayout(null);
		
		listModel = new DefaultListModel();
		
		list = new JList(listModel); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		
		listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(300, 300));
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		
		
		listModel2 = new DefaultListModel();
		
		list2 = new JList(listModel2);
		list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list2.setVisibleRowCount(-1);
		list2.setCellRenderer(new UserListRenderer());
		
		listScroller2 = new JScrollPane(list2);
		listScroller2.setSize(new Dimension(200, 300));
		listScroller2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		btn_send.setBounds(220, 310, 80, 25);
		btn_send.addActionListener(listener);
		
		txt_send.setBounds(0, 310, 215, 25);
		
		lbl_connected.setBounds(310, 0, 190, 30);
		lbl_connected.setText("Connected user:");
		listScroller2.setBounds(310, 30, 190, 270);
		
		add(listScroller);
		add(btn_send);
		add(txt_send);
		add(listScroller2);
		add(lbl_connected);
		
		
		this.setTitle("Chat");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	listener.win.window_chat.setSelected(false);
            	setVisible(false);
            }
		});
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}
	
	/**
	 * Adds a message.
	 * 
	 * Adds a message at the end of the chat.
	 * 
	 * @param message the message to be added
	 */
	public void addMessage(String message){
		listModel.addElement(message);
		
		list.ensureIndexIsVisible(listModel.getSize()-1);

	}
	
	public void refreshUsers(){
		listModel2.removeAllElements();
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	for(String name: listener.win.net.usersList) {
					String tool = listener.win.net.toolsList.get(name);
		        	if(name.matches(listener.win.net.username)) {
		        		
		        		listModel2.addElement(new ListItem(tool, name + " (you)"));
		        	} else {
		        		listModel2.addElement(new ListItem(tool, name));
		        	}
		        }
		    }
		  });
		
	}

	/**
	 * Toggles Window visibility
	 * 
	 * @return true if visible, false if not  
	 */
	public boolean toggle() {
		this.setVisible(!this.isVisible());
		return this.isVisible();		
	}
}
