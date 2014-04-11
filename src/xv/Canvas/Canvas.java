package xv.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xv.Canvas.Component.Layer;

public class Canvas {
	public List<Layer> layerList = Collections.synchronizedList(new ArrayList<Layer>());
	
	int selectedLayer = 0;
	int layernumber = 0;
	int width, height;

	public Canvas(int width, int height){
		this.width = width;
		this.height = height;
		layerList.add(new Layer(width,height,"Layer 0"));
		layernumber++;
		//layerList.get(0).setRGB(200, 200, 0xffff0000);
	}
	
	public void addLayer(){
		layerList.add(new Layer(width, height,"Layer "+layernumber));
		layernumber++;
	}
	
	public void removeLayer(int index){
		layerList.remove(index);
	}
	
	public int getSelectedLayer() {
		return selectedLayer;
	}

	public synchronized void setSelectedLayer(int selectedLayer) {
		this.selectedLayer = selectedLayer;
	}
}
