package xv.Tools;

import java.awt.Color;
import java.awt.Point;

public class Bezier {

	/**
	 * Speichert die Punkte der Bezier Kurve und deren Hilfspunkte
	 */
	private Point[] points;
	private int numpoints = 0;
	private int layerId;
	private int id;
	private Color color;
	
	public Bezier(int layerId) {
		
		points = new Point[4];
		this.layerId = layerId;
		this.color = Color.BLACK;
		
	}
	
	public Bezier(int layerId, Color color, Point p1, Point p2, Point p3, Point p4){
		points = new Point[4];
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		points[3] = p4;
		this.color = color;
		this.layerId = layerId;
		numpoints = 4;
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
	
	public int getLayerId() {
		return layerId;
	}

	public void setColor(Color c) {
		this.color = c;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	
		
}
