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
	JTextField txt_port = new JTextField();
	JLabel lb_width = new JLabel("Width");
	JTextField txt_width = new JTextField();
	JLabel lb_height = new JLabel("Height");
	JTextField txt_height = new JTextField();
	JButton btn_ok = new JButton("Ok");
	JCheckBox cb_webcolors = new JCheckBox("Allow only webcolors.");
	
	public ServerConfigWindow(){
		int x = 300, y = 210;

		//this.setLayout(null);
		setLayout(new GridLayout(0,1));
				
		//btn_ok.setBounds(0, 410, 100, 25);
		btn_ok.addActionListener(this);

		add(lb_port);
		add(txt_port);
		add(cb_webcolors);
		add(lb_width);
		add(txt_width);
		add(lb_height);
		add(txt_height);
		add(btn_ok);
		
		//pack();
		
		this.setTitle("Layers");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btn_ok)){
			System.out.println("oioi");
			this.dispose();
			new Server(Integer.parseInt(txt_port.getText()), cb_webcolors.isSelected(), Integer.parseInt(txt_width.getText()), Integer.parseInt(txt_height.getText()));
		}
	}
}
