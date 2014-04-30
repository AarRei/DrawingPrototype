package xv.Canvas.Component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Layer extends BufferedImage{
	
	public List<Dimension> pointList = Collections.synchronizedList(new ArrayList<Dimension>());
	
	String name;
	int id;


	public Layer(int width, int height, String name, int id){
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		this.id = id;
		this.name = name;
		int a = 4, b = 6;
	}
	
	public void bresenham(int x1, int y1, int x2, int y2, Color color){
		int dx, dy, sx, sy, err, e2;
		dx = Math.abs(x2 -x1);
		dy = Math.abs(y2 -y1);
		sx = (x1 < x2)? 1 : -1;
		sy = (y1 < y2)? 1 : -1;
		err = dx - dy;
		if(x1 < 0 || x1 >= this.getWidth() || x2 < 0 || x2 >= this.getWidth() || y1 < 0 || y1 >= this.getHeight() || y2 < 0 || y2 >= this.getHeight()){
			while(true){
				if(x1 < this.getWidth() && x1 >= 0 && y1 < this.getHeight() && y1 > 0){
					setRGB(x1, y1, color.getRGB());
				}
				if(x1 == x2 && y1 == y2){
					break;
				}
				e2 = 2 * err;
				if(e2 > -dy){
					err = err - dy;
					x1 = x1 + sx;
				}
				if(e2 < dx){
					err = err + dx;
					y1 = y1 + sy;
				}
			}
		}else{
			while(true){
				setRGB(x1, y1, color.getRGB());
				if(x1 == x2 && y1 == y2){
					break;
				}
				e2 = 2 * err;
				if(e2 > -dy){
					err = err - dy;
					x1 = x1 + sx;
				}
				if(e2 < dx){
					err = err + dx;
					y1 = y1 + sy;
				}
			}
		}
	}
	
	private void plot(int x, int y, float c, Color color){
		setRGB(x, y, new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(color.getAlpha() * c)).getRGB());
	}
	
	private int ipart(float x){
		return (int) x;
	}
	
	private int round (float x){
		return (int) ((float)x + 0.5f);
	}
	
	private float fpart(float x){
		return x - (float) ipart(x);
	}
	
	private float rfpart(float x){
		return 1.0f - fpart(x);
	}
	
	public void xiaolinwu(int x0, int y0, int x1, int y1, Color color){

		float dx = (float)x1 - (float)x0;
		float dy = (float)y1 - (float)y0;
		if(Math.abs(dx)>Math.abs(dy)){
			if(x1 < x0){
				int temp = x0;
				x0 = x1;
				x1 = temp;
				
				temp = y0;
				y0 = y1;
				y1 = temp;
			}
			float gradient = dy/dx;
			float xend = round(x0);
			float yend = y0 + gradient*(xend - x0);
			float xgap = rfpart(x0 + 0.5f);
			int xpxl1 = (int)xend;
			int ypxl1 = ipart(yend);
			//plot(xpxl1, ypxl1, rfpart(yend)*xgap, color);
			//plot(xpxl1, ypxl1+1, fpart(yend)*xgap, color);
			float intery = yend +gradient;
			
			xend = round(x1);
			yend = y1 + gradient*(xend - x1);
			xgap = fpart(x1+0.5f);
			int xpxl2 = (int)xend;
			int ypxl2 = ipart(yend);
			//plot(xpxl2, ypxl2, rfpart(yend)*xgap, color);
			//plot(xpxl2, ypxl2+1, fpart(yend)*xgap, color);
			
			int x;
			for(x = xpxl1 + 1; x <= xpxl2 /*- 1*/;x++){
				plot(x, ipart(intery),rfpart(intery),color);
				plot(x, ipart(intery)+1,fpart(intery),color);
				intery += gradient;
			}
		}else{
			if(y1 < y0){
				int temp = y0;
				y0 = y1;
				y1 = temp;
				
				temp = x0;
				x0 = x1;
				x1 = temp;
			}
			float gradient = dx/dy;
			float yend = round(y0);
			float xend = x0 + gradient*(yend - y0);
			float ygap = rfpart(y0 + 0.5f);
			int ypxl1 = (int)yend;
			int xpxl1 = ipart(xend);
			//plot(xpxl1, ypxl1, rfpart(xend)*ygap, color);
			//plot(xpxl1, ypxl1+1, fpart(xend)*ygap, color);
			float interx = xend +gradient;
			
			yend = round(y1);
			xend = x1 + gradient * (yend -y1);
			ygap = fpart(y1+0.5f);
			int ypxl2 = (int)yend;
			int xpxl2 = ipart(xend);
			//plot(xpxl2, ypxl2, rfpart(xend)*ygap, color);
			//plot(xpxl2, ypxl2+1, fpart(xend)*ygap, color);
			
			int y;
			for(y = ypxl1 + 1; y <=ypxl2 /*- 1*/; y++){
				plot(ipart(interx),y,rfpart(interx),color);
				plot(ipart(interx)+1,y,fpart(interx),color);
				interx += gradient;
			}
			
		}
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	/*
	int x, y, dx, dy, e, i, dx2, dy2;
		x = x1;
		y = y1;
		dx = x2 - x1;
		dy = y2 - y1;
		dx2 = 2 * dx;
		dy2 = 2 * dy;
		e = dy2 - dx;
		for(i = 1; i < dx;i++){
			setRGB(x, y, color.getRGB());
			if(e >= 0){
				y++;
				e -= dx2;
			}
			x++;
			e += dy2;
		}
		setRGB(x, y, color.getRGB());
	 */

}
