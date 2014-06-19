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
	
	private String[] connected_users = {"User1", "User2", "User3"};
	JLabel lbl_txt = new JLabel("Select users who will be able to edit the layer:");
	JScrollPane userScroll;
	JPanel toppanel = new JPanel();
	JButton btn_apply = new JButton("Apply");
	JButton btn_cancel = new JButton("Cancel");
	JPanel buttonpanel = new JPanel();
	static Integer indexer = 0;
    static List<JLabel> listOfLabels = new ArrayList<JLabel>();
    static List<JCheckBox> listOfCheckBox = new ArrayList<JCheckBox>();

	public LayerConfigWindow() {
		
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
		
        for(String name: connected_users) {
	        listOfCheckBox.add(new JCheckBox());
	        listOfLabels.add(new JLabel(name));
	        indexer++;
        }

        GridBagConstraints checkBoxConstraints = new GridBagConstraints();
        GridBagConstraints labelConstraints = new GridBagConstraints();
        checkBoxConstraints.anchor = GridBagConstraints.NORTHWEST;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;
        labelConstraints.weightx = 1;

        for(int i = 0; i < indexer; i++)
        {
            checkBoxConstraints.gridx = 0;
            checkBoxConstraints.gridy = 1 + i;

            labelConstraints.gridx = 1;
            labelConstraints.gridy = 1 + i;
            
            toppanel.add(listOfLabels.get(i), labelConstraints);
            toppanel.add(listOfCheckBox.get(i), checkBoxConstraints);
            
        }
        
        buttonpanel.add(btn_apply);
        buttonpanel.add(btn_cancel);
        
        add(toppanel, BorderLayout.CENTER);
        add(buttonpanel, BorderLayout.PAGE_END);
        
        pack();

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btn_apply)) {
			
		} else if(e.getSource().equals(btn_cancel)) {
			this.dispose();
		}
	}

}
