package kema.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;

import www.kema.Astar.Astar;
import www.kema.GameAI.GameAI;
import www.kema.TankState.TankState;
/**
 * 坦克类
 * @author 赵聪
 *
 */
public class Tank {
	//坦克当前所在的位置
	private int x,y;
	
	public static final int FINDHOMESTATE = 1;  //找老家
	public static final int FINDEDHOME= 2;    //找到老家
	public static final int FINDMYTANK = 3;	//发现我方坦克，，开枪
	public static final int AVOIDMISSILE1 = 4; // 躲避左邊或者右边來的子弹
	public static final int AVOIDMISSILE2 = 5; // 躲避左上或者右下來的子弹
	public static final int AVOIDMISSILE3 = 6; // 躲避上面或者下边的子弹
	public static final int AVOIDMISSILE4 = 7; // 躲避右上左下的子弹
	
	
	
/*	//老家所在地
	private static int ENDX = 119;
	private static int ENDY = 80;*/
	
	//坦克的速度
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	//坦克的大小
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	//是否活着
	private boolean live = true;
	
	TankClient tc;
	//血块条
	bloodBar bb = new bloodBar();

	//判断是否为我方坦克，，若是，，good == true;
	private boolean good;
	//坦克上一时刻所在的位置
	private int oldX, oldY;
	//生命值
	private int life = 100;
	
	private static Map<String,Image> imgs = new HashMap<String ,Image>();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// 坦克
	private static Image[] tankImgs = null;
	
	private List<Integer> list = new ArrayList<Integer>();
	
	TankState tankState ;
	
	private GameAI gameAI  ;
	/**
	 * 画出八个方向的坦克，，，并且加入Map键值对
	 */
	static {
		tankImgs= new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
			};
		
		imgs.put("L",tankImgs[0]);
		imgs.put("LU",tankImgs[1]);
		imgs.put("U",tankImgs[2]);
		imgs.put("RU",tankImgs[3]);
		imgs.put("R",tankImgs[4]);
		imgs.put("RD",tankImgs[5]);
		imgs.put("D",tankImgs[6]);
		imgs.put("LD",tankImgs[7]);
		
		
		
	}
	
	//随机数,,表示该方向所走的步数
	private static Random r =new Random();
	//四个方位
	private boolean bL = false ,bU = false, bR =false,bD = false;
	
	//追逐坦克时的方向集合
	private List<Integer> closeDir = new ArrayList<Integer>();
	//判断是否在追逐过程中
	private boolean isChase = false;
	
	//追逐的步数:
	private int chaseStep = 0;
	
	//发现老家，，攻城的方向集合
	private List<Integer> findHomeDir = new ArrayList<Integer>();
	//是否已发现
	private boolean isfind = false;
	
	//找老家的步数
	private int findHomeStep =0;
		
	
	//设置初始状态有stop
	private Direction dir = Direction.STOP;
	
	private Direction ptdir = Direction.U;
	
	//随机出的步数
	private int step = 0;
	
	private int path = r.nextInt(12)+3;
	
	public int pathState = r.nextInt(12)+3;
	
	private int pathLenth = 0;
	//构造函数
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
		
		 gameAI = new GameAI(x,y);
		 list = gameAI.getAns();
		 this.pathLenth = list.size();
		/* System.out.println("list_size:"+pathLenth);*/
		 /*for(int lis : list){
			 System.out.print(lis);
		 }
		 System.out.println();*/
	}
	
	/*public void init(){
		// list = GameAI.GA();
		
	}*/
	//getAndSet 方法
		public int getLife() {
			return life;
		}

		public void setLife(int life) {
			this.life = life;
		}

		public boolean isGood() {
			return good;
		}
	//停止状态
	public void stay(){
		x = oldX;
		y = oldY;
	}
	//构造函数
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		//调用上面的构造函数
		 this(x,y,good);
		 this.dir = dir;
		 this.tc=tc;
	}
	//画tank
	public void draw(Graphics g){
		//如果坦克死了。。擦出掉
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
/*		//得到颜色
		Color c = g.getColor();
		//如果是我方坦克颜色为红色,否则为黄色
		if(good){
			g.setColor(Color.RED);
			}
		else{
			g.setColor(Color.YELLOW);
		}
		//画出坦克
		g.fillOval(x, y, WIDTH,HEIGHT);
		//再把颜色换回去
		g.setColor(c);*/
		//给坦克画血块
		
			bb.draw(g);
		
		//判断什么方向，以及操作
			switch(ptdir){
			case L:
				g.drawImage(imgs.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(imgs.get("LU"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("U"), x, y, null);
				break;
			case RU:
				g.drawImage(imgs.get("RU"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("R"), x, y, null);
				break;
			case RD:
				g.drawImage(imgs.get("RD"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("D"), x, y, null);
				break;
			case LD:
				g.drawImage(imgs.get("LD"), x, y, null);
				break;
			}
		move();
	}
	//移动的具体操作
	void move(){
		
		this.oldX = x;
		this.oldY = y;
		
		switch(dir){
		case L:
			x -=XSPEED;
			break;
		case LU:
			x -=XSPEED;
			y -=YSPEED;
			break;
		case U:
			y -=YSPEED;
			break;
		case RU:
			x +=XSPEED;
			y -=YSPEED;
			break;
		case R:
			x +=XSPEED;
			break;
		case RD:
			x +=XSPEED;
			y +=YSPEED;
			break;
		case D:
			y +=YSPEED;
			break;
		case LD:
			x -=XSPEED;
			y +=YSPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP){
			this.ptdir = this.dir;
		}
		//边界处理
		if(x < 0) x = 0;
		if(y < 20) y = 20;
		//边界处理
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.WIDTH;
		//敌方坦克
		if(!good){
			Direction[] dirs = Direction.values();
			
			tankState = new TankState(FINDHOMESTATE,this,tc);
			
			
			int currenState = tankState.update();
			/*System.out.println("current state =" +currenState);*/
			switch(currenState){
				
				case FINDHOMESTATE:		//当step == 0 的时候，，说明AI方向已经走完了，，，再重新随机出来一个方向
				/*	System.out.println("finding home");*/
					/*if(pathLenth -1 == step){
						this.fire();
						step = r.nextInt(12) + 3;
						//随机出朝那个方向移动
						int rn = r.nextInt(dirs.length);
						dir = dirs[rn];
						
						step --;
					}
					else{*/
					//方向做完了，，重新再走
					if(pathLenth -1 == step){
						
						step = 0;
					}
						if(0 == path){
							this.fire();
							path = r.nextInt(12)+3;
							dir = dirs[list.get(step)];
							step++;
						}
						
						path -- ;
					
					
					break;
				case FINDEDHOME:
					System.out.println("fire your home");
					//获取当前坦克的做坐标
					int currentX = y / XSPEED;
					int currentY = x / YSPEED;
					/*//坦克在老家的左边
					if(currentY <(tc.getGameWidth()-50) / YSPEED){
						System.out.println("in left,turn right");
						if(this.collidesWithTanks(tc.tanks)){
							break;
						}
						//在wall2下面，，说明接近老家
						if(currentY > tc.getWallTwo().getY() / YSPEED){
							this.setDir(Direction.R);
							x += XSPEED;
						}else if(currentX < (tc.getWallTwo().getX() - 5)/YSPEED){
							this.setDir(Direction.RD);
							x +=XSPEED;
							y +=YSPEED;
						}else{
							this.setDir(Direction.L);
							x -=XSPEED;
						}
						
						if(pathState == 0){
							pathState = r.nextInt(12)+3;
						}
						pathState --;
						if(r.nextInt(40) > 30){
							this.fire(Direction.RD);
						}
					
						break;
					}
					//坦克在老家右边
					if(currentY > (tc.getGameWidth()-50) / YSPEED){
						
						System.out.println("in right,turn left");
						if(this.collidesWithTanks(tc.tanks));
						
						if(currentY > tc.getWallTwo().getY() / YSPEED){
							this.setDir(Direction.L);
							x -= XSPEED;
						}else if(currentX > (tc.getWallTwo().getX() + tc.getWallTwo().getWidth() + 5)/YSPEED){
							this.setDir(Direction.LD);
							x -=XSPEED;
							y +=YSPEED;
						}else{
							this.setDir(Direction.R);
							x+=XSPEED;
						}
						
						this.setDir(Direction.LD);
						if(pathState == 0){
							pathState = r.nextInt(12)+3;
						}
						pathState --;
						if(r.nextInt(40) > 30){
							this.fire(Direction.LD);
						}
						
						break;
					}*/
					if(collidesWall(tc.getWallTwo())){
						System.out.println("撞墙啦");
						break;
					}
					else{
					if(!isfind || findHomeDir.size() ==0){
						fireMyhome();
						isfind = true;
						findHomeStep = 0;
					}
					if(findHomeDir.size() >0){
						if(findHomeStep >= findHomeDir.size()){
							break;
						}
						int findDir = findHomeDir.get(findHomeStep);
						
						this.dir = dirs[findDir];
						
						findHomeStep ++;
					}
					int chaseX = r.nextInt(40);
					if(chaseX > 35){
						this.fire();
					}
					if(chaseStep >= findHomeDir.size()){
						isfind = false;
						findHomeStep = 0;
					}
					break;
					}
				
				case FINDMYTANK:
					System.out.println("chase myTank");
					if(!isChase || closeDir.size() == 0){
						chaseMyTank();
						isChase = true;
						chaseStep = 0;
						 
					}
					if(closeDir.size() > 0){
						int chaseDir = closeDir.get(chaseStep);
						
						this.dir = dirs[chaseDir];
						
						chaseStep++;
					}
					int chaseX = r.nextInt(40);
					if(chaseX > 35){
						this.fire();
					}
					if(chaseStep >= closeDir.size()){
						isChase = false;
						chaseStep = 0;
					}
					break;
				case AVOIDMISSILE1:
					System.out.println("躲避左边或者右邊来的子弹");
					this.setDir(Direction.U);
					if(this.collidesWall(tc.wallOne) || this.collidesWall(tc.wallTwo) 
							|| this.x < 10 || this.y < 10 ||x + Tank.WIDTH >= TankClient.GAME_WIDTH-10
							|| y + Tank.HEIGHT >= TankClient.GAME_HEIGHT -10){
						System.out.println("change dir");
						this.setDir(Direction.L);
					}
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					y -=YSPEED;
					break;
				case AVOIDMISSILE2:
					System.out.println("躲避左上或者右下來的子弹");
					this.setDir(Direction.LD);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					y -=YSPEED;
					break;
				case AVOIDMISSILE3:
					System.out.println("躲避上面或者下边的子弹");
					this.setDir(Direction.L);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					x -=XSPEED;
					break;
				case AVOIDMISSILE4:
					System.out.println("躲避右上左下的子弹");
					this.setDir(Direction.RD);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					x +=XSPEED;
					break;
				default:
					break;
			}
		/*	//当step == pathLenth 的时候，，说明该AI方向已经走完了，，，再重新随机出来一个方向
			if(pathLenth -1 == step){
				this.fire();
				step = r.nextInt(12) + 3;
				//随机出朝那个方向移动
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
				
				step --;
			}
			else{
				
				if(0 == path){
					this.fire();
					path = r.nextInt(12)+3;
					dir = dirs[list.get(step)];
					step++;
				}
				
				path -- ;
			}
			*/
			
			//随机出的数（40以内）大于38，，发子弹
			if(r.nextInt(40) >38){
				this.fire();
			}	
		}
	}
	/**
	 * 找到老家后，执行的ASTAR算法
	 */
	private void fireMyhome() {
		int myHomeX = tc.getGameHeight() / XSPEED;
		int myHomeY = (tc.getGameWidth()/2) / XSPEED;
		Astar astar = new Astar(y/XSPEED, x/YSPEED, myHomeX, myHomeY, tc);
		astar.init();
		
		findHomeDir = astar.getCloseDir();
	}
	/**
	 * 发现我的坦克后，，执行的ASTAR算法
	 */
	private void chaseMyTank() {
		int mytankX = tc.myTank.getY() / XSPEED;
		int mytankY = tc.myTank.getX() / YSPEED;
		
		Astar astar = new Astar(y/XSPEED, x/YSPEED, mytankX, mytankY, tc);
		/*Astar astar = new Astar(5, 60, 5, 50, tc);*/
		astar.init();
		
		closeDir = astar.getCloseDir();
		System.out.println("closeDir size in chaseMyTank:"+closeDir.size());
	}

	//键盘的操作
	public void KyePressed(KeyEvent e){
		int key = e.getKeyCode();
			switch(key){
			//按下F2，如果我方坦克挂了，，复活
			case KeyEvent.VK_F2:
				if(!this.live){
					this.live = true;
					this.life =100;
				}
				break;
			//坦克的移动
			case KeyEvent.VK_LEFT:
			 bL = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break; 
		}
		locateDirection();
	}
	//执行的操作
	void locateDirection(){
		if(bL && !bU && !bR &&!bD) dir= Direction.L;
		else if(bL && bU && !bR &&!bD) dir= Direction.LU;
		else if(!bL && bU && !bR &&!bD) dir= Direction.U;
		else if(!bL && bU && bR &&!bD) dir= Direction.RU;
		else if(!bL && !bU && bR &&!bD) dir= Direction.R;
		else if(!bL && !bU && bR &&bD) dir= Direction.RD;
		else if(!bL && !bU && !bR &&bD) dir= Direction.D;
		else if(bL && !bU && !bR &&bD) dir= Direction.LD;
		else if(!bL && !bU && !bR &&!bD) dir= Direction.STOP;
	}
	//键盘释放的操作,防外挂
	public void KeyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		//发子弹
		case KeyEvent.VK_X:
			fire();
			break;
		case KeyEvent.VK_LEFT:
		 bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break; 
		//外挂，，八个方向的子弹
		case KeyEvent.VK_A:
			superFire();
			break;
	}
	locateDirection();
	}
	/**
	 * 发子弹
	 * @return
	 */
	public Missile fire(){
		//如果已经死了，，就不能发子弹
		if(!live)
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 -Missile.HEIGHT/2;
		Missile m =new Missile(x,y,good,ptdir,this.tc);
		PlaySound PlaySound = new PlaySound("fire.wav");
		PlaySound.play();
		tc.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir){
		if(!live)
			return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 -Missile.HEIGHT/2;
		Missile m =new Missile(x,y,good,dir,this.tc);
		PlaySound PlaySound = new PlaySound("fire.wav");
		PlaySound.play();
		tc.missiles.add(m);
		return m;
	}
	//超级外挂
	public void superFire(){
		Direction [] dir = Direction.values(); 
		for(int i = 0 ; i < 8 ;i++){
			fire(dir[i]);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	//撞墙了
	public boolean collidesWall(Wall w){
		//碰撞检测
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			this.stay();
			return true;
		}
		return false;
	}
	//坦克间撞上了
	public boolean collidesWithTanks(java.util.List<Tank> tanks){
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			//与其他坦克
			if(this != t){
				//碰撞检测
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	//与Myhome撞上了
	public boolean collidesWithMyHome(MyHome myHome){
			//碰撞检测
			if(this.live && myHome.isLive() && this.getRect().intersects(myHome.getRect())){
				this.stay();
				return true;
			}
			return false;
		}
	//血块
	private class bloodBar {
		public void draw(Graphics g){
			Color c = g.getColor();
			//把我方坦克血块的颜色设为红色
			if(good){
				g.setColor(Color.RED);
				g.drawRect(x, y-10,WIDTH, 10);
				//可视化
				int w = WIDTH * life / 100 ;
				g.fillRect(x, y - 10, w, 10);
				g.setColor(c);
			}else{
				g.setColor(Color.MAGENTA);
				g.drawRect(x, y-10,WIDTH, 10);
				//可视化
				int w = WIDTH * life / 100 ;
				g.fillRect(x, y - 10, w, 10);
				g.setColor(c);
			}
		}
	}
	//吃血块
	public boolean eat(Blood b){
		//如果我方坦克还活着，并且血块还在，而且撞上了
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			//满血复活
			this.life = 100 ;
			//吃完以后，，把血块消失掉
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public GameAI getGameAI() {
		return gameAI;
	}

	
	
}

