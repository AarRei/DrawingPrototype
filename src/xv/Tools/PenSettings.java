package xv.Tools;

import java.awt.Color;

public class PenSettings {
	
	int thickness;
	Color color;
	
	/**
	 * Creates a PenSettings object.
	 */
	public PenSettings(){
		thickness = 1;
		color = new Color(0f, 0f, 0f, 1f);
	}
	
	/**
	 * Returns the thickness of the pen.
	 * 
	 * @return pen thickness
	 */
	public int getThickness() {
		return thickness;
	}
	
	/**
	 * Sets the thickness of the pen.
	 * 
	 * @param thickness pen thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	/**
	 * Returns the color of the pen.
	 * 
	 * @return pen color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color of the pen.
	 * 
	 * @param color pen color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
}
