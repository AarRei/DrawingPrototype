package xv.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import xv.Canvas.Canvas;

public class DrawPanel extends JPanel{
	BufferedImage buffer;
	BufferedImage chess;
	Canvas canvas;
	DrawWindow win;
	
	int width, height;
	
	/**
	 * Constructor of the DrawPanel
	 * 
	 * Creates a DrawPanel with the given parameters.
	 * 
	 * @param width width of the canvas
	 * @param height height of the canvas
	 * @param canvas the canvas object containing the layers
	 * @param win the parent DrawWindow
	 */
	public DrawPanel(int width, int height ,Canvas canvas,DrawWindow win){
		this.win = win;
		this.width = width;
		this.height = height;
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		chess = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		chessBG();
		this.canvas = canvas;
	}
	
	/**
	 * Generates the background for the canvas.
	 * 
	 * The method chessBG generates the checkered image for the background of the canvas via Graphics2D.
	 */
	private void chessBG(){
		Graphics2D g2 = chess.createGraphics();
		for(int i = 0; i < width/20+1; i++){
			for(int j = 0; j < height/20+1 ; j++){
				if(i%2 == 0 && j%2 == 0){
					g2.setColor(Color.WHITE);
					g2.fillRect(0+i*20, 0+j*20, 0+i*20+20,0+j*20+20);
				}else if (i%2 == 1 && j%2 == 1){
					g2.setColor(Color.WHITE);
					g2.fillRect(0+i*20, 0+j*20, 0+i*20+20,0+j*20+20);
				}else{
					g2.setColor(new Color(200,200,200));
					g2.fillRect(0+i*20, 0+j*20, 0+i*20+20,0+j*20+20);
				}
			}
		}

		g2.dispose();
	}
	
	/**
	 * Draws the content of the canvas on the panel.
	 * 
	 * The paint method draws the background image and the content of the layers onto the panel.
	 */
	
	public void paint(Graphics g) {
		Graphics2D g2 = buffer.createGraphics();

		/* Hier Rendern */
		
		//Hintergrund Schachmuster oder weißer hintergrund als Alternative
		if(win.backg.isSelected()){
			g2.drawImage(chess, 0, 0, this);
		}else{
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, width, height);
		}

		for(int i = canvas.layerList.size()-1;i >= 0 ;i--){
			g2.drawImage(canvas.layerList.get(i), 0, 0, this);
		}
		
		
		/* Hier nicht mehr Rendern */
		
		g2.dispose();

		g.drawImage(buffer, 0, 0,(int) (width*win.zoom), (int) (height*win.zoom), 0, 0, width, height, this);
	}
	
}
