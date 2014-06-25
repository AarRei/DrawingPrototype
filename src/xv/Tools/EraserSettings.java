package xv.Tools;

import java.awt.Color;

public class EraserSettings {
	
	int thickness;
	
	/**
	 * Creates a PenSettings object.
	 */
	public EraserSettings(){
		thickness = 1;
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
}
