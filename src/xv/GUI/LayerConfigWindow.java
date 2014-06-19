package xv.GUI;

import java.awt.Dialog;

import javax.swing.JDialog;

public class LayerConfigWindow extends JDialog {
	
	private String[] connected_users;

	public LayerConfigWindow() {
		int x = 200, y = 500;
		
		//TODO need method
		//connected_users = getConnectedUsers();
		
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
	}

}
