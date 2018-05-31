package cn.tedu.shoot_wy;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	int step;
	public int lifePoints=1;
	static BufferedImage[] images = new BufferedImage[6];
	static {
		for(int i =0;i<images.length;i++)
			images[i] = loadImage("hero"+i+".png");
	}
	public Hero(){
		width =97;
		height =124;
		x= 140;
		y= 300;
	}

	private int deadCount = 10;
	public BufferedImage getImage() {//创建方法使飞机每过50ms换一次图片！
		if(STATE == LIFE) return images[0];
		else deadCount++;
		if(STATE == REMOVE && deadCount<60) //控制时间让爆炸明显点
			return images[deadCount/10];
		else {World.state = World.GAME_OVER; return null;}
	}
	@Override
	public void step() {};
}
