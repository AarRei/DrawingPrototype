package xv.GUI;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import xv.Tools.Tools;

public class ToolWindow extends JFrame implements ActionListener{
	JToggleButton btn_pen = new JToggleButton();
	JToggleButton btn_bezier = new JToggleButton();
	JToggleButton btn_fill = new JToggleButton();
	JButton btn_color = new JButton();
	JButton btn_color_alt = new JButton();
	DrawWindow win;
	private WindowListener exitListener;
	
	/**
	 * Constructs the ToolWindow.
	 * 
	 * Builds the ToolWindow and adds listener to the buttons.
	 * 
	 * @param win the parent DrawWindow
	 */
	public ToolWindow(final DrawWindow win){
		this.win = win;
		int x = 120, y = 120;
	
		this.setLayout(new GridLayout(2, 2, 5, 5));
		
		//btn_pen.setBounds(0, 0, 60, 40);
		btn_pen.addActionListener(this);
		btn_pen.setFocusPainted(false);
		btn_pen.setIcon(win.listener.makeImageIcon("/icons/Brush.png"));
		
		//btn_bezier.setBounds(61, 0, 60, 40);
		btn_bezier.addActionListener(this);
		btn_bezier.setFocusPainted(false);
		btn_bezier.setIcon(win.listener.makeImageIcon("/icons/Curve_points.png"));
		
		//btn_fill.setBounds(0, 41, 60, 40);
		btn_fill.addActionListener(this);
		btn_fill.setFocusPainted(false);
		btn_fill.setIcon(win.listener.makeImageIcon("/icons/Fill.png"));
		
		//btn_color.setBounds(0, 82, 60, 40);
		btn_color.addActionListener(this);
		btn_color.setFocusPainted(false);
		btn_color.setIcon(win.listener.makeImageIcon("/icons/Color.png"));
		
		//btn_color_alt.setBounds(61, 82, 60, 40);
		btn_color_alt.addActionListener(this);
		btn_color_alt.setFocusPainted(false);
		
		
		add(btn_pen);
		add(btn_bezier);
		add(btn_fill);
		add(btn_color);
		//add(btn_color_alt);
		
		//pack();

		btn_pen.setSelected(true);
		
		this.setTitle("Tools");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.addWindowListener(exitListener);
		exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	win.window_tools.setSelected(false);
            }
        };
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		//this.setSize(x + 6, y + 28);
		this.setVisible(true);
		pack();
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
		}else if(e.getSource().equals(btn_fill)) {
			win.tools.setSelectedTool(Tools.FILL);
			untoggel();
			btn_fill.setSelected(true);
		}
	}
	
	private void untoggel(){
		btn_pen.setSelected(false);
		btn_bezier.setSelected(false);
		btn_fill.setSelected(false);
		win.drawPanel.repaint();
	}

	/*
	 * Toggles Window visibility
	 * 
	 * @return true if visible, false if not  
	 */
	public boolean toggle() {
		this.setVisible(!this.isVisible());
		return this.isVisible();		
	}
}
