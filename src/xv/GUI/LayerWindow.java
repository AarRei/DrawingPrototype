package xv.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import xv.Canvas.Canvas;
import xv.GUI.Listener.ListenerHandler;

public class LayerWindow extends JFrame implements MouseListener{
	
	Canvas canvas;
	public JList list;
	DefaultListModel listModel;
	
	public JButton btn_add = new JButton();
	public JButton btn_remove = new JButton();
	public JButton btn_setting = new JButton();
	JScrollPane listScroller;
	
	/**
	 * Constructs the LayerWindow
	 * 
	 * Builds the LayerWindow, creates and fills the layer list and adds listener to the buttons.
	 * 
	 * @param canvas the current working canvas
	 * @param listener the ListenerHandler
	 */
	
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
		list.setSelectedIndex(0);
		
		listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(200, 400));
		
		btn_add.setBounds(0, 410, 50, 50);
		btn_add.addActionListener(listener);
		btn_add.setIcon(new ImageIcon(getClass().getResource("/icons/Add.png")));
		
		btn_remove.setBounds(75, 410, 50, 50);
		btn_remove.addActionListener(listener);
		btn_remove.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.png")));
		
		btn_setting.setBounds(150, 410, 50, 50);
		btn_setting.addActionListener(listener);
		btn_setting.setIcon(new ImageIcon(getClass().getResource("/icons/Settings.png")));
		
		list.addMouseListener(this);
		
		add(listScroller);
		add(btn_add);
		add(btn_remove);
		add(btn_setting);
		
		//pack();
		
		this.setTitle("Layers");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}
	
	/**
	 * Fills the list with Layers.
	 * 
	 * Clears the list of all current Layers and fills it again. Used to update the list upon change.
	 */
	public void fillList(){
		listModel.clear();
		for(int i = 0; i < canvas.layerList.size();i++)
			listModel.add(i,canvas.layerList.get(i).getName());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		canvas.setSelectedLayer(list.getSelectedIndex());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
