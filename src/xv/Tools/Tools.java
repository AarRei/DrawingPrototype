package xv.Tools;

public class Tools {

	public static final int PEN = 0;
	public static final int BRUSH = 1;
	public static final int BEZIER = 2;
	public static final int FILL = 3;
	public static final int ERASER = 4;
	public static final int NOTHING = 99;

	private int selectedTool = 0;
	
	private int previousTool = 0;

	public Tools() {
		
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

}
