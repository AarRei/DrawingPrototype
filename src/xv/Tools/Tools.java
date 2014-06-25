package xv.Tools;

import java.awt.Color;

public class Tools {

	public static final int PEN = 0;
	public static final int BRUSH = 1;
	public static final int BEZIER = 2;
	public static final int FILL = 3;
	public static final int ERASER = 4;
	public static final int PICKER = 5;
	public static final int NOTHING = 99;

	private int selectedTool = 0;
	private int previousTool = 0;
	
	private Color color;

	public Tools() {
		color = new Color(0f, 0f, 0f, 1f);
	}
	
	public int getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedTool(int selectedTool) {
		this.selectedTool = selectedTool;
	}
	
	public int getPreviousTool() {
		return previousTool;
	}

	public void setPreviousTool(int previousTool) {
		this.previousTool = previousTool;
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
