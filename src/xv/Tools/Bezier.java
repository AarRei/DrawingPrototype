package xv.Tools;

import java.awt.Point;

public class Bezier {

	/**
	 * Speichert die Punkte der Bezier Kurve und deren Hilfspunkte
	 */
	private Point[] points;
	private int numpoints = 0;
	
	public Bezier() {
		
		points = new Point[4];		
		
	}
	
	public void setFirst(int x, int y) {
			
		Point point = new Point(x,y);
		Point point2 = new Point(x,y);

		points[0] = point;
		points[1] = point2;
		
		numpoints = 2;
		
	}
	
	public int getNumpoints() {
		return numpoints;
	}

	public void setSecond(int x, int y) {
		
		Point point = new Point(x,y);
		Point point2 = new Point(x,y);
	
		points[2] = point;
		points[3] = point2;
		
		numpoints = 4;
		
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}
	
	
	
		
}
