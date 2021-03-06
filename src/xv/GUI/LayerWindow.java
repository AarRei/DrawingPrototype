package xv.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import xv.Canvas.Canvas;
import xv.GUI.Listener.ListenerHandler;
import xv.Tools.Tools;

public class LayerWindow extends JFrame implements MouseListener{
	
	Canvas canvas;
	DrawWindow win;
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
	
	public LayerWindow(DrawWindow win, Canvas canvas, final ListenerHandler listener){
		this.canvas = canvas;
		this.win = win;
		int x = 200, y = 500;
		
		this.setLayout(null);
		this.setIconImage(listener.makeImageIcon("/icons/Layers.png").getImage());
		
		listModel = new DefaultListModel();
		for(int i = 0; i < canvas.layerList.size();i++)
			listModel.add(i,canvas.layerList.get(i).getName());
		
		list = new JList(listModel); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		
		if(listener.win.net != null) {
			btn_setting.setBounds(150, 410, 50, 50);
			btn_setting.addActionListener(listener);
			btn_setting.setIcon(new ImageIcon(getClass().getResource("/icons/Settings.png")));
		}
		
		list.addMouseListener(this);
		
		add(listScroller);
		add(btn_add);
		add(btn_remove);
		add(btn_setting);
		
		
		this.setTitle("Layers");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	listener.win.window_layers.setSelected(false);
            	setVisible(false);
            }
		});
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
		canvas.setSelectedLayer(listModel.size()-1-list.getSelectedIndex());
		if(win.net != null){
			if(!win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getOwner().equals(win.net.username) && !win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).getOwner().equals("")){
				boolean allowed=false;
				for (String coll : win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).collaborateurList){
					if(coll.equals(win.net.username))
						allowed = true;
				}
				if(!allowed){
					win.toolWindow.setDisabled();
					if(win.tools.getSelectedTool() != Tools.NOTHING)
						win.tools.setPreviousTool(win.tools.getSelectedTool());
					win.tools.setSelectedTool(Tools.NOTHING);
				}
				else{
					win.toolWindow.setEnabled();
					if(win.tools.getSelectedTool() == Tools.NOTHING){
						win.tools.setSelectedTool(win.tools.getPreviousTool());
					}
				}
			}
			else{
				win.toolWindow.setEnabled();
				if(win.tools.getSelectedTool() == Tools.NOTHING){
					win.tools.setSelectedTool(win.tools.getPreviousTool());
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Toggles Window visibility
	 * 
	 * @return true if visible, false if not  
	 */
	public boolean toggle() {
		this.setVisible(!this.isVisible());
		return this.isVisible();		
	}
}
