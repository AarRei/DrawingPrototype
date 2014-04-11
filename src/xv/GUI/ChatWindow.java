package xv.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import xv.Canvas.Canvas;
import xv.GUI.Listener.ListenerHandler;
import xv.Network.Client.ClientMain;

public class ChatWindow extends JFrame{
	Canvas canvas;
	public JList list;
	DefaultListModel listModel;
	
	public JButton btn_send = new JButton("Send");
	public JTextField txt_send = new JTextField();
	JScrollPane listScroller;
	
	public ChatWindow(ListenerHandler listener){
		this.canvas = canvas;
		int x = 300, y = 340;

		this.setLayout(null);
		
		listModel = new DefaultListModel();
		/*for(int i = 0; i < canvas.layerList.size();i++)
			listModel.add(i,canvas.layerList.get(i).getName());*/
		
		list = new JList(listModel); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(300, 300));
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listModel.addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
				// TODO Auto-generated method stub
				listScroller.getVerticalScrollBar().setValue(350);
				
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		btn_send.setBounds(220, 310, 80, 25);
		btn_send.addActionListener(listener);
		
		txt_send.setBounds(0, 310, 215, 25);
		
		add(listScroller);
		add(btn_send);
		add(txt_send);
		
		//pack();
		
		this.setTitle("Chat");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}

	public void addMessage(String message){
		listModel.addElement(message);
	}
}
