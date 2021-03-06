package xv.GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LayerConfigWindow extends JDialog implements ActionListener {
	
	DrawWindow win;
	JLabel lbl_txt = new JLabel();
	JScrollPane userScroll;
	JPanel toppanel = new JPanel();
	JButton btn_apply = new JButton("Apply");
	JButton btn_cancel = new JButton("Cancel");
	JPanel buttonpanel = new JPanel();
	Integer indexer = 0;
	Integer layer_id;
    List<JCheckBox> listOfCheckBox = new ArrayList<JCheckBox>();
    
	public LayerConfigWindow(DrawWindow win, int id) {
		
		this.layer_id = id;
		this.win = win;
		
		lbl_txt.setText("Select users who will be able to edit Layer " + id);
		
		
		this.setLayout(new BorderLayout(5, 5));
		
		toppanel.setLayout(new GridBagLayout());
		toppanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.NORTHWEST;
        gbc1.gridwidth = 2;
        gbc1.gridy = 0;
        
        btn_apply.addActionListener(this);
        btn_cancel.addActionListener(this);
       
        toppanel.add(lbl_txt, gbc1);
		
        for(String name: win.net.usersList) {
        	JCheckBox c = new JCheckBox(name);
        	if(win.canvas.layerIDList.get(id).collaborateurList.contains(name)) {
        		c.setSelected(true);
        	}
        	listOfCheckBox.add(c);
        	if(!win.canvas.layerIDList.get(id).getOwner().equals(win.net.username)){
        		listOfCheckBox.get(indexer).setEnabled(false);
        	}
        	else{
        		if(name.matches(win.net.username)) {
            		listOfCheckBox.get(indexer).setText(name + " (you)");
            		listOfCheckBox.get(indexer).setSelected(true);
            		listOfCheckBox.get(indexer).setEnabled(false);
            	}
        	}
		    indexer++;
        }

        GridBagConstraints checkBoxConstraints = new GridBagConstraints();
        checkBoxConstraints.anchor = GridBagConstraints.NORTHWEST;

        for(int i = 0; i < indexer; i++)
        {
            checkBoxConstraints.gridx = 0;
            checkBoxConstraints.gridy = 1 + i;
            
            toppanel.add(listOfCheckBox.get(i), checkBoxConstraints);
            
        }
        
        buttonpanel.add(btn_apply);
        buttonpanel.add(btn_cancel);
        
        add(toppanel, BorderLayout.CENTER);
        add(buttonpanel, BorderLayout.PAGE_END);
        
        pack();

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btn_apply)) {
			win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).collaborateurList = getChecked();
			String rightsSend = "{\"action\": \"LAYR\","
					+ "\"user\": \""+win.net.username+"\","
					+ "\"user_id\": 0,"
					+ "\"layer_id\": "+layer_id+","
					+ "\"users\": [";
			
			for(String coll :win.canvas.layerList.get(win.layerWindow.list.getSelectedIndex()).collaborateurList){
				rightsSend += "{\"user\": \""+coll+"\"},";
			}
			rightsSend = rightsSend.substring(0, rightsSend.length()-1);
			rightsSend += "]}";
			win.net.sendMessage(rightsSend);
			this.dispose();
		} else if(e.getSource().equals(btn_cancel)) {
			this.dispose();
		}
	}
	
	public List<String> getChecked() {
		List<String> users = new ArrayList<String>();
		for(JCheckBox c: listOfCheckBox) {
			if(c.isSelected()) {
				users.add(c.getText().replace(" (you)", ""));
			}
		}
		return users;
	}

}
