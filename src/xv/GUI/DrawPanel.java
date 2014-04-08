package xv.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import xv.Canvas.Canvas;

public class DrawPanel extends JPanel{
	BufferedImage buffer = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
	BufferedImage chess	= new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
	Canvas canvas;
	
	public DrawPanel(Canvas canvas){
		chessBG();
		this.canvas = canvas;
	}
	
	public void chessBG(){
		Graphics2D g2 = chess.createGraphics();
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 36; j++){
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
	
	public void paint(Graphics g) {
		Graphics2D g2 = buffer.createGraphics();

		/* Hier Rendern */
		
		//Hintergrund Schachmuster
		g2.drawImage(chess, 0, 0, this);

		for(int i = 0;i < canvas.layerList.size();i++){
			g2.drawImage(canvas.layerList.get(i), 0, 0, this);
		}
		
		//g2.drawImage(image, 400, 400, this);
		
		/* Hier nicht mehr Rendern */
		
		g2.dispose();

		g.drawImage(buffer, 0, 0, 1280, 720, 0, 0, 1280, 720, this);
	}
	
}
