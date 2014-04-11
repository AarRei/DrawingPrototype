package xv.Canvas.Component;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Layer extends BufferedImage{
	
	public List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());
	
	String name;

	public Layer(int width, int height, String name){
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
