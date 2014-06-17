package xv.GUI;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import xv.Tools.Tools;

public class ToolWindow extends JFrame implements ActionListener{
	JToggleButton btn_pen = new JToggleButton("Pen");
	JToggleButton btn_bezier = new JToggleButton("Bezier");
	JButton btn_color = new JButton("Color");
	JButton btn_color_alt = new JButton();
	DrawWindow win;
	
	/**
	 * Constructs the ToolWindow.
	 * 
	 * Builds the ToolWindow and adds listener to the buttons.
	 * 
	 * @param win the parent DrawWindow
	 */
	public ToolWindow(DrawWindow win){
		this.win = win;
		int x = 120, y = 90;
	
		this.setLayout(null);
		
		btn_pen.setBounds(0, 0, 60, 40);
		btn_pen.addActionListener(this);
		
		btn_bezier.setBounds(61, 0, 60, 40);
		btn_bezier.addActionListener(this);
		
		btn_color.setBounds(0, 41, 60, 40);
		btn_color.addActionListener(this);
		
		btn_color_alt.setBounds(61, 41, 60, 40);
		btn_color_alt.addActionListener(this);
		
		add(btn_pen);
		add(btn_bezier);
		add(btn_color);
		add(btn_color_alt);
		
		//pack();
		
		this.setTitle("Tools");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setSize(x + 6, y + 28);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btn_color)){
			if(win.net != null){
				if(win.webcolors == false){
					win.pen.setColor(JColorChooser.showDialog(this, "Palette", win.pen.getColor()));
				}
				else{
					JColorChooser cc = new JColorChooser();
					AbstractColorChooserPanel[] panels = cc.getChooserPanels();
		            for (AbstractColorChooserPanel accp : panels) {
		                if (!accp.getDisplayName().equals("Swatches")) {
		                   /* AbstractColorChooserPanel[] finalp = {accp};
		                    cc.setChooserPanels(finalp);
		                    break;*/
		                	cc.removeChooserPanel(accp);
		                }
		            }
		            win.pen.setColor(JColorChooser.showDialog(this, "Palette", win.pen.getColor()));
		           // win.pen.setColor(cc.showDialog(this, "Palette", win.pen.getColor()));
				}
			}else{
				win.pen.setColor(JColorChooser.showDialog(this, "Palette", win.pen.getColor()));
			}
			
		} else if(e.getSource().equals(btn_pen)) {
			win.tools.setSelectedTool(Tools.PEN);
			untoggel();
			btn_pen.setSelected(true);
		} else if(e.getSource().equals(btn_bezier)) {
			win.tools.setSelectedTool(Tools.BEZIER);
			untoggel();
			btn_bezier.setSelected(true);
		}
	}
	
	private void untoggel(){
		btn_pen.setSelected(false);
		btn_bezier.setSelected(false);
		win.drawPanel.repaint();
	}
}
