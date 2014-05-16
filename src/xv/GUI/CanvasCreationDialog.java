package xv.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CanvasCreationDialog extends JDialog implements ActionListener{
	DrawWindow win;
	
	JTextField height = new JTextField(), width = new JTextField();
	JLabel heightL = new JLabel("Height"), widthL = new JLabel("Width");
	JButton ok = new JButton("Ok");
	
	/**
	 * Creates the CanvasCreationDialog.
	 * 
	 * @param win parent DrawWindow
	 */
	public CanvasCreationDialog(DrawWindow win){
		this.win = win;
		setLayout(new GridLayout(0,1));
		
		ok.addActionListener(this);
		
		add(widthL);
		add(width);
		add(heightL);
		add(height);
		add(ok);
		setSize(250,150);
		setVisible(true);
	}
	
	/**
	 * Calls createCanvas method with entered parameters.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(ok)){
			win.createCanvas(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
			this.dispose();
		}
	}
}
