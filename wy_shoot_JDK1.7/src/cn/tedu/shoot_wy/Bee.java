package cn.tedu.shoot_wy;

import java.awt.image.BufferedImage;

public class Bee extends FlyingObject implements Award{
	int step,xstep;
	static BufferedImage[] images = new BufferedImage[5];
	static {for(int i =0 ;i<images.length;i++)
	images[i] = loadImage("bee"+i+".png");}
	
	public Bee(){
		width=60;
		height =50;
		x=(int)(Math.random()*(World.WIDTH-width-1));
		y= -height;
		step=2; 
		xstep=2;
	}
	
		public BufferedImage getImage() {
		if(STATE==LIFE)
			return images[0];
		else if(STATE==REMOVE)
			return null;
		else return images[0];
	}

		@Override
		public void step() {
			
			if(x>=(World.WIDTH-width)||x<=0)
			xstep=-xstep;
			y+=step;
			x+=xstep;
		}

		@Override
		public int getScores() {
			return 30;
		}
}
