package xv.Canvas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xv.Canvas.Component.Layer;

public class Canvas {
	public List<Layer> layerList = Collections.synchronizedList(new ArrayList<Layer>());
	public List<Layer> layerIDList = Collections.synchronizedList(new ArrayList<Layer>());
	
	int selectedLayer = 0;
	int layernumber = 0;
	int width, height;

	/**
	 * Creates a Canvas object.
	 * 
	 * @param width width of canvas
	 * @param height height of canvas
	 */
	public Canvas(int width, int height){
		this.width = width;
		this.height = height;
		layerList.add(new Layer(width,height,"Layer 0", layernumber));
		layerIDList.add(layerList.get(0));
		layernumber++;
	}
	
	/**
	 * Adds a layer.
	 * 
	 * Adds a layer at the desired position.
	 * 
	 * @param position position of the new layer
	 */
	public void addLayer(int position){
		layerList.add(position, new Layer(width, height,"Layer "+layernumber, layernumber));
		layerIDList.add(layerList.get(position));
		layernumber++;
	}
	
	/**
	 * Removes a layer.
	 * 
	 * Removes a layer at the specified index.
	 * 
	 * @param index index of the layer
	 */
	public void removeLayer(int index){
		layerIDList.set(layerList.get(index).getId(), null);
		layerList.remove(index);
	}
	
	/**
	 * Returns the currently selected layer
	 * 
	 * @return selected layer
	 */
	public int getSelectedLayer() {
		return selectedLayer;
	}
	
	/**
	 * Sets the currently selected layer.
	 * 
	 * @param selectedLayer selected layer
	 */
	public synchronized void setSelectedLayer(int selectedLayer) {
		this.selectedLayer = selectedLayer;
	}
}
