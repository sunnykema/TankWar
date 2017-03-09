package kema.tank;
import java.awt.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

import javax.xml.stream.events.StartDocument;

import com.zhangzg.MenuComponent.MenuBarList;

import www.kema.GameAI.GameAI;
/**
 * 坦克游戏主窗口
 * @author kema
 *
 */
public class TankClient extends Frame{
	//定义窗口大小
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	//游戏刚刚开始，放音乐用的
	private static boolean isStart = false;
	
	//加载游戏的控制器
	private static boolean isLoding = false;
	
	//加载游戏的控制器
	private static boolean isLodingF = false;
	
	//地方坦克的代數
	private static int generation =0;
	
	//游戏是否结束了
	private static boolean isOver = false;

	// 老家
	MyHome myHome = new MyHome(GAME_WIDTH/2,GAME_HEIGHT,this);
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//游戏结束的图
	private static Image gameOver = tk.getImage(Tank.class.getClassLoader().getResource("images/over.gif"));
	
	//我方坦克
	Tank myTank = new Tank(GAME_WIDTH / 2 -100 ,GAME_HEIGHT - 50,true,Direction.STOP,this);
	
	//血块
	Blood blood = new  Blood(this);
	
	//计时器
	int times = 0;
	
	public Tank getMyTank() {
		return myTank;
	}
	
	public List<Missile> getMissiles() {
		return missiles;
	}


	//墙
	Wall wallOne = new Wall(100,150,200,20,false,this);
	Wall wallTwo = new Wall(300,450,20,300,false,this);
	Wall wallThree = new Wall(375,550,50,20,true,this);
		
	Wall wallFour = new Wall(375,520,20,91,true,this);
		
	Wall wallFive = new Wall(465,550,50,20,true,this);

	//爆炸
	List<Explode> explodes = new ArrayList<Explode>();
	//子弹
	List<Missile> missiles = new ArrayList<Missile>();
	//敌方坦克
	List<Tank> tanks = new ArrayList<Tank>();
	
	
	//子弹
	Missile m = null;
	//当前屏幕
	Image offScreenImage = null;
	
	
	
	public static int getGameWidth() {
		return GAME_WIDTH;
	}

	public static int getGameHeight() {
		return GAME_HEIGHT;
	}

		
	public Wall getWallOne() {
		return wallOne;
	}



	public Wall getWallTwo() {
		return wallTwo;
	}

	public static boolean isStart() {
		return isStart;
	}

	public static void setStart(boolean isStart) {
		TankClient.isStart = isStart;
	}

	public static boolean isLoding() {
		return isLoding;
	}

	public static void setLoding(boolean isLoding) {
		TankClient.isLoding = isLoding;
	}
	
	public Wall getWallThree() {
		return wallThree;
	}

	public void setWallThree(Wall wallThree) {
		this.wallThree = wallThree;
	}

	public Wall getWallFour() {
		return wallFour;
	}

	public void setWallFour(Wall wallFour) {
		this.wallFour = wallFour;
	}

	public Wall getWallFive() {
		return wallFive;
	}

	public void setWallFive(Wall wallFive) {
		this.wallFive = wallFive;
	}

	public void setWallOne(Wall wallOne) {
		this.wallOne = wallOne;
	}

	public void setWallTwo(Wall wallTwo) {
		this.wallTwo = wallTwo;
	}

	/**
	 * 构造主窗体  覆盖windows中的paint
	 */
		public void paint(Graphics g){
			if(!isLodingF){
				if(isLoding){
					//AePlayer aePlayer = new AePlayer("start.wav");
					if(isStart){
					PlaySound playSound = new PlaySound("start.wav");
					playSound.play();
					isStart = false;
					}
					while(times < 50)
					{
						g.setColor(Color.BLACK);
						g.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
						//提示信息
						if(times%2==0)
						{
							g.setColor(Color.yellow);
							//开关信息的字体
							Font myFont=new Font("华文新魏",Font.BOLD,30);
							g.setFont(myFont);
							g.drawString("loading...",GAME_WIDTH / 2 - 50,GAME_HEIGHT / 2);
						}
						//休眠
						try {
							Thread.sleep(100);
							times++;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.repaint();
					}
					isLoding = false; // 表示正在加载界面的控制开关已经关闭
					isLodingF = true; // 表示首页已经加载完毕
					//System.out.println("!!!!!!");
				}else{
					int size = 50;
					Color c  = g.getColor();
					g.setColor(Color.GREEN);
					g.setFont(new Font("楷体", Font.LAYOUT_LEFT_TO_RIGHT, size));
					g.drawString("TankWar", GAME_WIDTH / 2 - (size * 3 / 2), GAME_HEIGHT / 2);
					new Tank(50+ 300,50,false,Direction.D,this);
				}
			}else{
				//当前屏幕里面子弹的数量
				Color c  = g.getColor();
				g.setColor(Color.GREEN);
				g.drawString("Missiles count:" + missiles.size(), 5, 40);
				//当前屏幕的爆炸数
				g.drawString("explodes count:"+explodes.size(),5,60);
				//敌方坦克的数目
				g.drawString("Tanks    count:"+tanks.size(), 5, 80);
				//我方坦克的生命值
				g.drawString("Tank      life:" + myTank.getLife(), 5, 100);
				
				g.drawString("Tank generation :"+generation, 5, 120);
				g.setColor(c);
				//如果地方坦克全部被消灭， 重新生成出坦克
				if(tanks.size() <= 0){
				/*	new Thread(new tankThread()).start();*/
					generation += 200;
					for(int i=0;i<3;i++){
						tanks.add(new Tank(50+ 300*i,50,false,Direction.D,this));
						tanks.add(new Tank(100+ 400*i,50,false,Direction.D,this));
					}
					/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
				}
				//检查子弹击中坦克的情况，，碰撞检测。所有的子弹
				for(int i=0;i<missiles.size();i++){
					Missile m = missiles.get(i);
					//击中敌方坦克
					m.hitTanks(tanks);
					//我方坦克被击中
					m.hitTank(myTank);
					//子弹击中墙
					m.hitWall(wallOne);
					m.hitWall(wallTwo);
					m.hitWall(wallThree);
					m.hitWall(wallFour);
					m.hitWall(wallFive);
					//击中myhome
					boolean ans = false;
					ans = m.hitMyHome(myHome);
					if(ans){
						isOver = true;
					}
					m.draw(g);
				}
				//处理爆炸
				for(int i = 0;i < explodes.size(); i++){
					Explode e = explodes.get(i);
					e.draw(g);
				}
				//敌方坦克，，的事务
				for(int i = 0;i < tanks.size();i++){
					//获得每一个坦克
					Tank t = tanks.get(i);
					//处理撞墙事件
					t.collidesWall(wallOne);
					t.collidesWall(wallTwo);
					t.collidesWall(wallThree);
					t.collidesWall(wallFour);
					t.collidesWall(wallFive);
					//坦克间碰撞事件
					t.collidesWithTanks(tanks);
					//与myhome相撞
					t.collidesWithMyHome(myHome);
					t.draw(g);
				}
				
				//画出我方坦克
				myTank.draw(g);
				//吃血块
				myTank.eat(blood);
				//画出墙
				wallOne.draw(g);
				wallTwo.draw(g);
				wallThree.drawNR(g);
				wallFour.drawNR(g);
				wallFive.drawNR(g);
				// 画出血块
				blood.draw(g);
				//画出老家
				myHome.draw(g);
			}
		}
		/**
		 * 跟新  覆盖container 中的 update
		 */
		public void update(Graphics g){
			//如果为空，，创建本窗体
			if(offScreenImage == null){
				offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
			}
			if(!isOver){
				//设置背景
				Graphics gOffScreen = offScreenImage.getGraphics();
				Color c = gOffScreen.getColor();
				gOffScreen.setColor(Color.BLACK);
				gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
				gOffScreen.setColor(c);
				
				paint(gOffScreen);
				g.drawImage(offScreenImage,0,0,null);
			}
			else{
				/*System.out.println("********");*/
				g.drawImage(gameOver, GAME_WIDTH/2, GAME_HEIGHT/2, null);
			
				
			}
		}
		/**
		 * 画出窗口里面的东西，，
		 */
		public void lauchFrame(){
			//敌方坦克
			//new Thread(new tankThread()).start();
			//this.setLocation(400, 300);
			for(int i=0;i<3;i++){
				
				tanks.add(new Tank(50+ 300*i,50,false,Direction.D,this));
				tanks.add(new Tank(100+ 400*i,50,false,Direction.D,this));
			}
			/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
			//基本信息，大小，，标题
			this.setSize(GAME_WIDTH, GAME_HEIGHT);
			this.setTitle("TankWar");
			//监听器，，，监听窗口关闭
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
					
				}	
			});
			this.setMenuBar(new MenuBarList(this).getMb());
			//这置为false，，让游戏窗口不能缩放
			this.setResizable(false);
			//背景
			this.setBackground(Color.BLACK);
			
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
			//键盘监听器
			this.addKeyListener(new KeyMonitor());
			//可见
			setVisible(true);
			//开启线程。。
			new Thread(new PaintThread()).start();
		}
		
		/**
		 * 画出窗口里面的东西，，
		 */
		public void lauchFrameStart(){
			//敌方坦克
			//new Thread(new tankThread()).start();
			//this.setLocation(400, 300);
			/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
			//基本信息，大小，，标题
			this.setSize(GAME_WIDTH, GAME_HEIGHT);
			this.setTitle("TankWar");
			//监听器，，，监听窗口关闭
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
					
				}	
			});
			this.setMenuBar(new MenuBarList(this).getMb());
			//这置为false，，让游戏窗口不能缩放
			this.setResizable(false);
			//背景
			this.setBackground(Color.BLACK);
			
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
			//键盘监听器
			this.addKeyListener(new KeyMonitor());
			//可见
			setVisible(true);
		}
		
		/**
		 * 主函数
		 * @param args
		 */
	public static void main(String[] args) {
		//初始化，，tankclient
		TankClient tc = new TankClient();
		//调用lanchframe
		tc.lauchFrameStart();
	}
	
	/**
	 *创建坦克的线程,,
	 *可惜效果不理想
	 * @author 赵聪
	 *
	 */
/*	private class tankThread implements Runnable  {
		
		public void  run(){
			
				tanks.add(new Tank(50+ 300,50,false,Direction.D,TankClient.this));
				tanks.add(new Tank(100+ 400,50,false,Direction.D,TankClient.this));
			
		}
	}*/
	/**
	 * paintThread 线程 
	 * @author 赵聪
	 *
	 */
	private class PaintThread implements Runnable{
		public void run() {
			//死循环
			while(true){
				//重画
				repaint();
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * 键盘监听
	 * @author 赵聪
	 *
	 */
	private class KeyMonitor extends KeyAdapter{
		//监听键盘抬起
		public void keyReleased(KeyEvent e) {
			myTank.KeyReleased(e);
		}
		//监听键盘下压
		public void keyPressed(KeyEvent e) {
				myTank.KyePressed(e);
				myHome.KyePressed(e);
			}
		}
		public static boolean isOver() {
			return isOver;
		}
		public static void setOver(boolean isOver) {
			TankClient.isOver = isOver;
		}
	}
