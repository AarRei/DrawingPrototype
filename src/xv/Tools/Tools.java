package xv.Tools;

public class Tools {

	public static final int PEN = 0;
	public static final int BRUSH = 1;
	public static final int BEZIER = 2;
	public static final int ERASER = 3;

	private int selectedTool = 0;

	public Tools() {
		
	}
	
	public int getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedTool(int selectedTool) {
		this.selectedTool = selectedTool;
	}
	
	
}
