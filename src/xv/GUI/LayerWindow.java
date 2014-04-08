package xv.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import xv.Canvas.Canvas;
import xv.GUI.Listener.ListenerHandler;

public class LayerWindow extends JFrame{
	
	Canvas canvas;
	public JList list;
	DefaultListModel listModel;
	
	public JButton btn_add = new JButton("+ Layer");
	public JButton btn_remove = new JButton("- Layer");
	JScrollPane listScroller;
	
	public LayerWindow(Canvas canvas, ListenerHandler listener){
		this.canvas = canvas;
		int x = 200, y = 500;

		this.setLayout(null);
		
		listModel = new DefaultListModel();
		for(int i = 0; i < canvas.layerList.size();i++)
			listModel.add(i,canvas.layerList.get(i).getName());
		
		list = new JList(listModel); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(200, 400));
		
		btn_add.setBounds(0, 410, 100, 25);
		btn_add.addActionListener(listener);
		
		btn_remove.setBounds(100, 410, 100, 25);
		btn_remove.addActionListener(listener);
		
		add(listScroller);
		add(btn_add);
		add(btn_remove);
		
		//pack();
		
		this.setTitle("Layers");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}
	
	public void fillList(){
		listModel.clear();
		for(int i = 0; i < canvas.layerList.size();i++)
			listModel.add(i,canvas.layerList.get(i).getName());
	}
}
