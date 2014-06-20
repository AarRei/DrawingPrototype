package xv.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LayerConfigWindow extends JDialog implements ActionListener {
	
	List<String> connected_users = new ArrayList<String>();
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
		
//		connected_users.add("User1");
//		connected_users.add("User2");
//		connected_users.add("User3");
		
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
        	if(!name.matches(win.net.username)) {
		        listOfCheckBox.add(new JCheckBox(name));
		        indexer++;
        	}
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
			System.out.println(getChecked());
			this.dispose();
		} else if(e.getSource().equals(btn_cancel)) {
			this.dispose();
		}
	}
	
	public List<String> getChecked() {
		List<String> users = new ArrayList<String>();
		for(JCheckBox c: listOfCheckBox) {
			if(c.isSelected()) {
				users.add(c.getText());
			}
		}
		return users;
	}

}
