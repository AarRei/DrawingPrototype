package xv.GUI;

import java.awt.Dialog;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LayerConfigWindow extends JDialog {
	
	private String[] connected_users = {"User1", "User2", "User3"};
	JLabel lbl_txt = new JLabel("Select user who will be able to edit the layer:");

	public LayerConfigWindow() {
		int x = 200, y = 400;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
		
	}

}
