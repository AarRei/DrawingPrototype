package xv.Network.Server.GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import xv.Network.Server.Server;

public class ServerClose implements ActionListener {

	public ServerClose(Server server) {
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		JButton btn = new JButton("Close Server");
		btn.addActionListener(this);
		frame.add(btn);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		System.exit(1);
		
	}

}
