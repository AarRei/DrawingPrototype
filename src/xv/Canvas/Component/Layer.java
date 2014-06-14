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
	/**
	 * List of actions.
	 */
	public List<String> actionList = Collections.synchronizedList(new ArrayList<String>());
	
	String name;
	int id;

	/**
	 * Creates a Layer.
	 * 
	 * Creates a Layer with a given width and height by calling the super method.
	 * The BufferedImage type is set to ARGB.
	 * 
	 * @param width width of the canvas
	 * @param height height of the canvas
	 * @param name name of the layer
	 * @param id id of the layer
	 */
	public Layer(int width, int height, String name, int id){
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Line algorithm by Bresenham
	 * 
	 * Draws a line onto the layer according to the line algorithm by Bresenham.
	 * 
	 * @param x1 x component of the source coordinate
	 * @param y1 y component of the source coordinate
	 * @param x2 x component of the destination coordinate
	 * @param y2 y component of the destination coordinate
	 * @param thickness thickness of the pen
	 * @param color color of the pen
	 */
	public void bresenham(int x1, int y1, int x2, int y2, int thickness, Color color){
		int dx, dy, sx, sy, err, e2;
		dx = Math.abs(x2 -x1);
		dy = Math.abs(y2 -y1);
		sx = (x1 < x2)? 1 : -1;
		sy = (y1 < y2)? 1 : -1;
		err = dx - dy;
		if(x1 < 0 || x1 >= this.getWidth() || x2 < 0 || x2 >= this.getWidth() || y1 < 0 || y1 >= this.getHeight() || y2 < 0 || y2 >= this.getHeight()){ //thickness / 2
			while(true){
				if(x1 < this.getWidth() && x1 >= 0 && y1 < this.getHeight() && y1 >= 0){
					setRGB(x1, y1, color.getRGB());
					if(thickness != 1){
						if(thickness == 2){
							setRGB(x1, y1-1, color.getRGB());
						}
						else{
							for(int i = 1; i <= (thickness-1)/2;i++ ){
								setRGB(x1, y1-i, color.getRGB());
								setRGB(x1, y1+i, color.getRGB());

								setRGB(x1-i, y1, color.getRGB());
								setRGB(x1+i, y1, color.getRGB());
							}
							if(thickness % 2 == 0){
								setRGB(x1, y1-thickness / 2, color.getRGB());
								setRGB(x1 - thickness / 2, y1, color.getRGB());
							}
						}						
					}
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
				if(thickness != 1){
					if(thickness == 2){
						setRGB(x1, y1-1, color.getRGB());
						setRGB(x1+1, y1, color.getRGB());
						setRGB(x1+1, y1-1, color.getRGB());
					}
					else if(thickness % 2 == 0){
						for(int i = 1; i <= (thickness-1)/2;i++ ){
							setRGB(x1, y1-i, color.getRGB());
							setRGB(x1, y1+i, color.getRGB());

							setRGB(x1-i, y1, color.getRGB());
							setRGB(x1+i, y1, color.getRGB());
						}
						setRGB(x1, y1-thickness / 2, color.getRGB());
					}else{
						for(int i = 1; i <= (thickness-1)/2;i++ ){
							setRGB(x1, y1-i, color.getRGB());
							setRGB(x1, y1+i, color.getRGB());

							setRGB(x1-i, y1, color.getRGB());
							setRGB(x1+i, y1, color.getRGB());
						}
					}
					
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
	
	/**
	 * Draws a line.
	 * 
	 * Test of the Xiaolin Wu line algorithm.
	 * 
	 * @param x0 source x
	 * @param y0 source y
	 * @param x1 destination x
	 * @param y1 destination y
	 * @param color pen color
	 */
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
	
	/*private void plotGS(int x, int y, double c, Color color){
		System.out.println((int)(color.getAlpha() * (1-(c*2/3) )));
		setRGB(x, y, new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(color.getAlpha() * (1-(c*2/3) ))).getRGB());
	}*/
	
	/**
	 * Draws a line (or not).
	 * 
	 * Test line algorithm by Gutpa Sproull.
	 * 
	 * @param x0 source x
	 * @param y0 source y
	 * @param x1 destination x
	 * @param y1 destination y
	 * @param color pen color
	 */
	private void IntensifyPixel(int x, int y, double c, Color color){
		//double intensity = Filter(Math.round(Math.abs(c)));
		System.out.println(c);
		double intensity = Math.abs(c);
		//System.out.println(intensity);
		setRGB(x, y, new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(color.getAlpha() * intensity)).getRGB());
	}
	
	public void guptasproull(int x0, int y0, int x1, int y1, Color color){
		int dx = x1-x0; 
		int dy = y1-y0; 
		int d=2*dy-dx;
		int incrE = 2*dy;
		int incrNE = 2*(dy-dx);
		int two_v_dx = 0;
		double invDenom = 1.0/(2.0*Math.sqrt(dx*dx+dy*dy));
		double two_dx_invDemon = 2.0*dx*invDenom;
		int x = x0;
		int y = y0;
		IntensifyPixel(x,y,0,color);
		IntensifyPixel(x,y+1,two_dx_invDemon,color);
		IntensifyPixel(x,y-1,two_dx_invDemon,color);
		while (x<x1) { 
			if (d<0) {
				two_v_dx = d+dx;
				x++;
			}else { 
				two_v_dx = d-dx;
				d += incrNE;
				x++;
				y++;
			} 
			IntensifyPixel(x,y,two_v_dx*invDenom,color);
			IntensifyPixel(x,y+1,two_dx_invDemon - two_v_dx*invDenom,color);
			IntensifyPixel(x,y-1,two_dx_invDemon + two_v_dx*invDenom,color);
		}
	}
	
	public void plotLineWidth(int x0, int y0, int x1, int y1, float wd, Color color) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx - dy, e2, x2, y2; /* error value e_xy */
		double ed = dx + dy == 0 ? 1 : Math.sqrt((double) dx * dx + (double) dy * dy);

		for (wd = (wd + 1) / 2;;) { /* pixel loop */
			int col = (int) (255*(Math.abs(err-dx+dy)/ed-wd+1));
			setRGB(x0, y0, new Color(color.getRed(), color.getGreen(), color.getBlue(),(col < 0)?255:255-col).getRGB());
			// setPixelColor(x0,y0,Math.max(0,255*(Math.abs(err-dx+dy)/ed-wd+1));
			//System.out.println(255*(Math.abs(err-dx+dy)/ed-wd+1));
			e2 = err;
			x2 = x0;
			if (2 * e2 >= -dx) { /* x step */
				for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx){
					// setPixelColor(x0, y2 += sy,
					// Math.max(0,255*(Math.abs(e2)/ed-wd+1));
					int temp = (int) (255 * (Math.abs(e2) / ed - wd + 1));
					setRGB(x0, y2 += sy, new Color(color.getRed(), color.getGreen(), color.getBlue(),(temp < 0)?255:255-temp ).getRGB());
				}
				if (x0 == x1)
					break;
				e2 = err;
				err -= dy;
				x0 += sx;
			}
			if (2 * e2 <= dy) { /* y step */
				for (e2 = dx - e2; e2 < ed * wd && (x1 != x2 || dx < dy); e2 += dy){
					// setPixelColor(x2 += sx, y0,
					// Math.max(0,255*(Math.abs(e2)/ed-wd+1));
					int temp = (int) (255 * (Math.abs(e2) / ed - wd + 1));
					setRGB(x2 += sx, y0, new Color(color.getRed(), color.getGreen(), color.getBlue(),(temp < 0)?255:255-temp ).getRGB());
				}
				if (y0 == y1)
					break;
				err += dx;
				y0 += sy;
			}
		}
	}

	/*public void guptasproull(int x0, int y0, int x1, int y1, Color color){
		int addr = (y0*640*x0)*4;
		int dx = x1 - x0;
		int dy = y1 - y0;
		
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		
		int du, dv, u, v, uincr, vincr;
		
		if(Math.abs(dx)>Math.abs(dy)){
			du = Math.abs(dx);
			dv = Math.abs(dy);
			u = x1;
			v = y1;
			uincr = 1;
			vincr = 1;
			if(dx < 0)
				uincr = -uincr;
			if(dy < 0)
				vincr = -vincr;
		}else{
			du = Math.abs(dy);
			dv = Math.abs(dx);
			u = y1;
			v = x1;
			uincr = 1;
			vincr = 1;
			if(dy < 0)
				uincr = -uincr;
			if(dx < 0)
				vincr = -vincr;
		}
		int uend = u + 2 *du;
		int d = (2 * dv) - du;
		int incrS = 2 * dv;	
		int incrD = 2 * (dv - du);	
		int twovdu = 0;	
		double invD = 1.0 / (2.0*Math.sqrt(du*du + dv*dv));   
		double invD2du = 2.0 * (du*invD);  
		do{
			if(steep){
				plotGS(x0, y0, twovdu*invD, color);
				plotGS(x0 + uincr, y0, invD2du - twovdu*invD, color);
				plotGS(x0 - uincr, y0, invD2du + twovdu*invD, color);
			}else{
				plotGS(x0, y0, twovdu*invD, color);
				plotGS(x0, y0+vincr, invD2du - twovdu*invD, color);
				plotGS(x0, y0-vincr, invD2du + twovdu*invD, color);
			}
			
			if(d < 0){
				twovdu = d + du;
				d = d + incrS;
			}else{
				twovdu = d - du;
				d = d + incrD;
				v = v+1;
				y0 = y0 + vincr;
			}
			u = u+1;
			x0 = x0 + uincr;
		}while(u < uend);
	}*/
	
	/**
	 * Returns the name of the layer.
	 * 
	 * @return layer name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the layer.
	 * 
	 * @param name layer name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the ID of the layer.
	 * 
	 * @return layer ID
	 */
	public int getId() {
		return id;
	}

}
