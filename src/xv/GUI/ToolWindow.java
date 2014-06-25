package xv.GUI;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import xv.Tools.Tools;

public class ToolWindow extends JFrame implements ActionListener{
	JToggleButton btn_pen = new JToggleButton();
	JToggleButton btn_bezier = new JToggleButton();
	JToggleButton btn_fill = new JToggleButton();
	JButton btn_color = new JButton();
	JToggleButton btn_colorpicker = new JToggleButton();
	JToggleButton btn_eraser = new JToggleButton();
	DrawWindow win;
	
	/**
	 * Constructs the ToolWindow.
	 * 
	 * Builds the ToolWindow and adds listener to the buttons.
	 * 
	 * @param win the parent DrawWindow
	 */
	public ToolWindow(final DrawWindow win){
		this.win = win;
		int x = 80, y = 120;
	
		this.setIconImage(win.listener.makeImageIcon("/icons/Graphic_tools.png").getImage());
		this.setLayout(new GridLayout(3, 2, 0, 0));
		
		btn_pen.addActionListener(this);
		btn_pen.setFocusPainted(false);
		btn_pen.setIcon(win.listener.makeImageIcon("/icons/Pen.png"));
		
		btn_bezier.addActionListener(this);
		btn_bezier.setFocusPainted(false);
		btn_bezier.setIcon(win.listener.makeImageIcon("/icons/Curve_points.png"));
		
		btn_fill.addActionListener(this);
		btn_fill.setFocusPainted(false);
		btn_fill.setIcon(win.listener.makeImageIcon("/icons/Fill.png"));
		
		btn_color.addActionListener(this);
		btn_color.setFocusPainted(false);
		btn_color.setIcon(win.listener.makeImageIcon("/icons/Color.png"));
		
		btn_colorpicker.addActionListener(this);
		btn_colorpicker.setFocusPainted(false);
		btn_colorpicker.setIcon(win.listener.makeImageIcon("/icons/Pickcolor.png"));

		btn_eraser.addActionListener(this);
		btn_eraser.setFocusPainted(false);
		btn_eraser.setIcon(win.listener.makeImageIcon("/icons/Eraser.png"));
		
		add(btn_pen);
		add(btn_bezier);
		add(btn_fill);
		add(btn_colorpicker);
		add(btn_eraser);
		add(btn_color);

		btn_pen.setSelected(true);
		
		this.setTitle("Tools");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	win.window_tools.setSelected(false);
            	setVisible(false);
            }
		});
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2+1280/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-y/2);
		this.setVisible(true);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btn_color)){
			if(win.net != null){
				if(win.webcolors == false){
					win.tools.setColor(JColorChooser.showDialog(this, "Palette", win.tools.getColor()));
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
		            win.tools.setColor(JColorChooser.showDialog(this, "Palette", win.tools.getColor()));
		           // win.pen.setColor(cc.showDialog(this, "Palette", win.pen.getColor()));
				}
			}else{
				win.tools.setColor(JColorChooser.showDialog(this, "Palette", win.tools.getColor()));
			}
			
		} else if(e.getSource().equals(btn_pen)) {
			
			win.tools.setSelectedTool(Tools.PEN);
			untoggle();
			btn_pen.setSelected(true);
			if(win.net != null){
				String tool = "{\"action\": \"TOOL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"tool\": \"Pen\"}";
				win.net.sendMessage(tool);
			}

			win.pen_size.setValue(win.pen.getThickness());
			win.l_pen_size.setText("Pen Size: "+win.pen_size.getValue());
		} else if(e.getSource().equals(btn_bezier)) {
			win.tools.setSelectedTool(Tools.BEZIER);
			untoggle();
			btn_bezier.setSelected(true);
			if(win.net != null){
				String tool = "{\"action\": \"TOOL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"tool\": \"Bézier-Tool\"}";
				win.net.sendMessage(tool);
			}
		}else if(e.getSource().equals(btn_fill)) {
			win.tools.setSelectedTool(Tools.FILL);
			untoggle();
			btn_fill.setSelected(true);
			if(win.net != null){
				String tool = "{\"action\": \"TOOL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"tool\": \"Fill-Tool\"}";
				win.net.sendMessage(tool);
			}
		}else if(e.getSource().equals(btn_colorpicker)) {
			win.tools.setSelectedTool(Tools.PICKER);
			untoggle();
			btn_colorpicker.setSelected(true);
			if(win.net != null){
				String tool = "{\"action\": \"TOOL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"tool\": \"Pick-Tool\"}";
				win.net.sendMessage(tool);
			}
		}else if(e.getSource().equals(btn_eraser)) {
			
			win.tools.setSelectedTool(Tools.ERASER);
			untoggle();
			btn_eraser.setSelected(true);
			if(win.net != null){
				String tool = "{\"action\": \"TOOL\","
						+ "\"user\": \""+win.net.username+"\","
						+ "\"user_id\": 0,"
						+ "\"tool\": \"Eraser\"}";
				win.net.sendMessage(tool);
			}

			win.pen_size.setValue(win.eraser.getThickness());
			win.l_pen_size.setText("Eraser Size: "+win.pen_size.getValue());
		}
	}
	
	private void untoggle(){
		btn_pen.setSelected(false);
		btn_bezier.setSelected(false);
		btn_fill.setSelected(false);
		btn_colorpicker.setSelected(false);
		btn_eraser.setSelected(false);
		win.drawPanel.repaint();
	}
	
	public void setDisabled(){
		btn_pen.setEnabled(false);
		btn_bezier.setEnabled(false);
		btn_fill.setEnabled(false);
		btn_eraser.setEnabled(false);
	}
	
	public void setEnabled(){
		btn_pen.setEnabled(true);
		btn_bezier.setEnabled(true);
		btn_fill.setEnabled(true);
		btn_eraser.setEnabled(true);
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
