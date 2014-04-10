package xv.Tools;

import java.awt.Color;

public class PenSettings {
	
	int thickness;
	Color color;

	public PenSettings(){
		thickness = 1;
		color = new Color(0f, 0f, 0f, 1f);
	}
	
	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
