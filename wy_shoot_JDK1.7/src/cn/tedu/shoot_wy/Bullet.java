package cn.tedu.shoot_wy;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	int step;
	static BufferedImage image= loadImage("bullet.png");
	public Bullet(){
		width =8;
		height =14;
		x=World.WIDTH/2;
		y= 200;
		step =2;
	}
	@Override
	public BufferedImage getImage() {
		if(STATE==LIFE)
			return image;
		else if(STATE==REMOVE)
			return null;
		else return image;
	}
	@Override
	public void step() {
		y-=step;
		
	}

	
	
}
