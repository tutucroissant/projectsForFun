package cn.tedu.shoot_wy;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Award{
		int step;
	static BufferedImage[] images = new BufferedImage[5];
	static{
		for(int i=0;i<images.length;i++) 
		{
			images[i]=loadImage("airplane"+i+".png");
		}
	}
	
	public Airplane(){
		width =57;
		height = 53;
		x= (int)(Math.random()*(World.WIDTH+1-width));
		y= -height;
		step = 3;

	}

	@Override
	public BufferedImage getImage() {
		if(STATE==LIFE)
			return images[0];
		else if(STATE==REMOVE)
			return null;
		else return images[0];
	}

	@Override
	public void step() {
		y+=step;
	}

	@Override
	public int getScores() {
		return 15;
	}
		
	

}
