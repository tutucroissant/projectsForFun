package cn.tedu.shoot_wy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class FlyingObject {
	int x,y,height,width;
	public static int LIFE =0;
	public static int DEAD =1;
	public static int REMOVE =2;	
	
	protected int STATE = LIFE;//这里注意,就是所有的子类都一开始就是LIFE状态
	
	public static BufferedImage loadImage(String name){
		try{BufferedImage image 
		= ImageIO.read(FlyingObject.class.getResource(name));
				return image;
		}catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		 
	}	
	public abstract void step();
	
	public boolean ifhit(FlyingObject enemy){
		if((this.y+this.height<enemy.y)||(this.y>enemy.y+enemy.height))//没撞上
			return false;
		else if((this.x+this.width<enemy.x)||(this.x>enemy.x+enemy.width))
			return false;
		else return true;
	}
	
	public abstract BufferedImage getImage();
	
	public void paint(Graphics g) {
		/**
		 * //这个方法确定了图片的出现位置
		 * 只需要知道X，y就行了，width，height都是object的固有属性
		 */
		g.drawImage(getImage(), x, y, null);
			
	}
	public void goDead(){
		STATE = REMOVE;
	}
	public void goHead(){
		STATE = LIFE;
	}
	
	public boolean isLife(){
		if(this.STATE==LIFE) return true;
		else return false;
	}
	
}
