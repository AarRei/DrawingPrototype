package xv.GUI;

import javax.swing.*;

public class ListItem {

	private ImageIcon icon = null;
	private String text;

	public ListItem(String tool, String text) {
		String toolpic = "/icons/Nothing.png";
		if(tool != null) {
			switch(tool) {
			case "Pen":
				toolpic = "/icons/Brush.png";
				break;
			case "Bézier-Tool":
				toolpic = "/icons/Curve_points.png";
				break;
			case "Fill-Tool":
				toolpic = "/icons/Fill.png";
				break;
			}
		}
		this.icon = new ImageIcon(getClass().getResource(toolpic));
		this.text = text;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public String getText() {
		return text;
	}
}
