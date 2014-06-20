package xv.Network.Server.GUI;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import xv.Network.Server.Server;

public class ServerConfigWindow extends JFrame implements ActionListener{
	
	JLabel lb_port = new JLabel("Port");
	JTextField txt_port = new JTextField("7777");
	JLabel lb_width = new JLabel("Width");
	JTextField txt_width = new JTextField("700");
	JLabel lb_height = new JLabel("Height");
	JTextField txt_height = new JTextField("700");
	JButton btn_ok = new JButton("Ok");
	JCheckBox cb_webcolors = new JCheckBox("Allow only webcolors.");
	
	/**
	 * Creates a ServerConfigWindow.
	 */
	public ServerConfigWindow(){
		int x = 300, y = 210;

		setLayout(new GridLayout(0,1));
				
		btn_ok.addActionListener(this);

		add(lb_port);
		add(txt_port);
		add(cb_webcolors);
		add(lb_width);
		add(txt_width);
		add(lb_height);
		add(txt_height);
		add(btn_ok);
		
		this.setTitle("Server Setup");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}
	
	/**
	 * Starts a server with the entered parameters.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btn_ok)){
			this.dispose();
			Server s = new Server(Integer.parseInt(txt_port.getText()), cb_webcolors.isSelected(), Integer.parseInt(txt_width.getText()), Integer.parseInt(txt_height.getText()));
			new ServerClose(s);
		}
	}
}
