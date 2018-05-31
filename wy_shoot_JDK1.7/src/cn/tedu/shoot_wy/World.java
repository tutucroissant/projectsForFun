package cn.tedu.shoot_wy;
/**
 *@author WangYang
 *	The project was complished on 2/15 / 2018.
 * enemies bee,Airplane,BigAirplane
 * bullets bullet
 */
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3624001485369709269L;
	public final static int WIDTH = 400;
	public final static int HEIGHT = 700;
	
	FlyingObject[] enemies = {};
	FlyingObject[] bullets ={};
	Sky sky = new Sky();
	Hero hero = new Hero();
	public final static int PAUSE =0;
	public final static int START =1;
	public final static int GAME_OVER =2;
	public final static int RUNNING =3;
	public static int state = START;
	private int score = 0;
	private int firePoints =1;
	private static 	BufferedImage start =FlyingObject.loadImage("start.png");
	private static 	BufferedImage pause = FlyingObject.loadImage("pause.png");
	private static 	BufferedImage game_over =FlyingObject.loadImage("gameover.png");

	public static void main(String[] args) {

		JFrame frame = new JFrame("airplane");
		World world = new World();
		frame.add(world);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		world.action();
	//---------------------------------------------------------------------		
	}
	
	public void action(){
		//设置鼠标和飞机坐标的同步
		MouseAdapter ma = new MouseAdapter(){ 
			@Override
		public void mouseMoved(MouseEvent e) {
			if(state==RUNNING)
			{hero.x=e.getX()-hero.width/2;
			hero.y=e.getY()-hero.height/2;}
		}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(state==START)
				state = RUNNING;	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE)
				state = RUNNING;
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING)
				state = PAUSE;
			}
		};
		this.addMouseListener(ma);
		this.addMouseMotionListener(ma);

		
		int mills =10;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
			if(state==RUNNING)//用这一步来防止对象进入
			{	
				storeObjectClass();
				stepAction();
				heroBangAction();
				shootAction();
				scoreBonus();
				bangAction();//这一步里加了分数
				outOfBoundDelete();
				repaint();	
			}
			else repaint();
		}			
	}, mills, mills);	
	}


	

	public FlyingObject nextOne(){
		int i =(int)(Math.random()*100);
		if(i<20) return new Bee();		
		else if(i<55) return new BigAirplane();
		else return new Airplane();
	}

	private static int count=0;
	public void storeObjectClass(){
		count++;
		if(count%20==0)//每隔10ms*30 出现一个敌人，并储存在数组里
		{
			FlyingObject enemy = nextOne();
			enemies=Arrays.copyOf(enemies, enemies.length+1);
			enemies[enemies.length-1]=enemy;
			if(count==100) count=0;
		}
	}
	
	private static int bulletscount=0;
	protected void shootAction() {	//子弹的发射位置
		bulletscount++;
		int flag=0,unit =hero.width/4;//5根线
		if(bulletscount%20==0)
		{
			bullets=Arrays.copyOf(bullets, bullets.length+firePoints);
			flag =bullets.length-firePoints;
			if(firePoints==1){
				bullets[flag] = new Bullet(); 
				bullets[flag].x = hero.x+2*unit;
				bullets[flag].y = hero.y;
			}
			else if(firePoints==2){
				for(int i = 0;i<2;i++)
				{
					bullets[flag+i]= new Bullet();
					bullets[flag+i].y=hero.y;
				}
				bullets[flag].x=1*unit+hero.x;
				bullets[flag].x=3*unit+hero.x;
			}
			if(bulletscount==120) bulletscount=0;
		}
	}
	
	public void outOfBoundDelete(){
		int num=0;
		FlyingObject[] tep1= {};
		for(int i =0 ; i < enemies.length;i++)
			if(enemies[i].y<HEIGHT+enemies[i].height)
			{
				tep1 = Arrays.copyOf(tep1, tep1.length+1);
				tep1[num++]= enemies[i];
			}
		num=0;
		enemies = Arrays.copyOf(tep1, tep1.length);
		FlyingObject[] tep2= {};
		for(int i=0; i<bullets.length;i++)
		{
			if(bullets[i].y>-bullets[i].height)
			{
				tep2 = Arrays.copyOf(tep2, tep2.length+1);
				tep2[num++]= bullets[i];
			}
		}
		bullets = Arrays.copyOf(tep2, tep2.length);
	}
	protected void stepAction() {
		for(int i =0;i<bullets.length;i++)
		{
			bullets[i].step();
		}
			for(int i =0;i<enemies.length;i++)
			enemies[i].step();
		sky.step();
	}
	
	protected void heroBangAction() {	//这一块每10ms就刷新一次
		//在这里注意，调用了这个类，this还是hero那个类的对象引用。。。
		for(int i =0 ; i < enemies.length;i++)
		{
			if(hero.ifhit(enemies[i])&&enemies[i].isLife()){//true是撞击到了
				enemies[i].goDead();
				if(--hero.lifePoints>0) break;
				else {
					hero.lifePoints =0;
					hero.goDead();
					score=0;break;		
				}							
			}
		}		
	}
	public void paint(Graphics g){
		sky.paint(g);
		hero.paint(g);
		for(int i=0;i<enemies.length;i++)
			enemies[i].paint(g);
	
		for(int i=0;i<bullets.length;i++)
			{
			// 子弹坐标的修改不能写这里
				bullets[i].paint(g);
			}
		g.drawString("分数： "+score, 10, 10);
		g.drawString("命值： "+hero.lifePoints, 10, 30);
		g.drawString("火力值： "+ firePoints, 10, 50);
		switch(state){
		case PAUSE : g.drawImage(pause, 0, 0, null);break;
		case START : g.drawImage(start, 0, 0, null);break;
		case GAME_OVER : g.drawImage(game_over, 0, 0, null);break;
		}

	}

	protected void bangAction() {
		//判断是否撞击，进行互相遍历，
		for(int i =0 ; i <enemies.length;i++)
		{
			Award enemy = (Award)enemies[i];
			for(int m =0 ; m <bullets.length ;m++)
			{
				if((bullets[m].ifhit(enemies[i])&&bullets[m].isLife())
						&&enemies[i].isLife())//此语句必须执行 true是撞上
				{
					if(enemies[i] instanceof Bee && Math.random()<0.2)
						hero.lifePoints++;
					score +=enemy.getScores();
					enemies[i].goDead();bullets[m].goDead();break;
				}
			}}}
	protected void scoreBonus() {
		if(score<1000) 
			firePoints=1;
		else 
			firePoints=2;
	}
	
}
