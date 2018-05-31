package cn.tedu.shoot_wy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
		int step,y1;
	/*public void getImage(){
		BufferedImage image = new BufferedImage(); 
		BufferedImage image;
		image =loadImage("background.png");
	}*/

	static BufferedImage image;
	static{
		image = loadImage("background.png");
	}
	
	public Sky(){
		width = World.WIDTH;
		height= World.HEIGHT;
		x=0;
		y=0;
		y1=-height;
		step =2;
	}

	@Override
	public BufferedImage getImage() {
		
		return image;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(getImage(), x, y, null);
		g.drawImage(getImage(), x, y1, null);
	}
	@Override
	public void step() {
		y+=step;
		y1+=step;
		if(y==World.HEIGHT) y=-y;
		if(y1==World.HEIGHT) y1=-y1;
		
	}
	
	
}











