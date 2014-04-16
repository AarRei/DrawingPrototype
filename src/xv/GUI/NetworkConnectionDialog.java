package xv.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NetworkConnectionDialog extends JDialog implements ActionListener{
	
	DrawWindow win;
	
	JTextField host = new JTextField("127.0.0.1"), 
			port = new JTextField("7777"), 
			name = new JTextField("User");
	JLabel hostL = new JLabel("Host"), portL = new JLabel("Port"), nameL = new JLabel("Username");
	JButton ok = new JButton("Connect");
	
	public NetworkConnectionDialog(DrawWindow win){
		this.win = win;
		setLayout(new GridLayout(0,1));
		
		ok.addActionListener(this);
		
		add(nameL);
		add(name);
		add(hostL);
		add(host);
		add(portL);
		add(port);
		add(ok);
		setSize(250,200);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(ok)){
			System.out.println("mööp");
			win.establishConnection(host.getText(), Integer.parseInt(port.getText()), name.getText());
			this.dispose();
		}
	}
}
