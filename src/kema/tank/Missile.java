package kema.tank;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子弹
 * @author 赵聪
 *
 */
public class Missile {
	//子弹的速度
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	//子弹的大小
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	//位置
	int x, y;
private static Map<String,Image> imgs = new HashMap<String ,Image>();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// 坦克
	private static Image[] missileImgs = null;
	
	private static Image gameOver = null;
	/**
	 * 画出八个方向的子弹，，，并且加入Map键值对
	 */
	static {
		missileImgs= new Image[]{
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/over.gif"))
			};
		//子弹上下图片P反了
		imgs.put("L",missileImgs[0]);
		imgs.put("LU",missileImgs[1]);
		imgs.put("U",missileImgs[2]);
		imgs.put("RU",missileImgs[3]);
		imgs.put("R",missileImgs[4]);
		imgs.put("RD",missileImgs[5]);
		imgs.put("D",missileImgs[6]);
		imgs.put("LD",missileImgs[7]);
	}
	
	//方向
	Direction dir;
	
	//是否为我方坦克
	private boolean good;
	//子弹的生命,初始值为true
	private boolean live = true;
	//
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}
	//构造函数
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x, int y,boolean good, Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
		
	}
	//画子弹
	public void draw(Graphics g){
		//判断是都或者，，子弹死了，去除掉
		if(!live){
			tc.missiles.remove(this);
			return;
		}
		//画出子弹
		switch(dir) {
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
		//移动子弹
		move();
	}
	//让子弹飞
  	private void move(){
		//判断哪个方向，，然后操作如何移动
		switch(dir){
		case L:
			x -= XSPEED;
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
		}
		//子弹飞出窗体，，就把子弹的生命值设为false
		if(x < 0|| y < 0||x > TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			live = false;
		}
	}
	//返回子弹
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	//处理击中坦克的事件
	public boolean hitTank(Tank t){
		//碰撞检测   并且不打自己人
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() &&this.good != t.isGood()){
			PlaySound playSound = new PlaySound("blast.wav");
			playSound.play();
			//击中的坦克,有生命值，，所以，可以抗几下死不了，把生命值减掉20,,
			
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0){
					t.setLive(false);
				}
			
			//同时子弹的生命也没了
			this.live = false;
			//播放爆炸过程
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
			
		return false;
	}
	//是否击中坦克 击中返回true，
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i < tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	//判断是否撞墙，撞墙放回true
	public boolean hitWall (Wall w){
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			PlaySound playSound = new PlaySound("hit.wav");
			playSound.play();
			this.live =false;
			//是土墙，，则使他消失
			if(w.isTu() && !this.good){
				w.setLive(false);
			}
			
			return true;
		}
		return false;
	}
	/**
	 * 判断是否击中myhome
	 * @param mh
	 * @return
	 */
	public boolean hitMyHome(MyHome mh){
		if(this.live &&  mh.isLive() && this.getRect().intersects(mh.getRect()) && !good){
			MyHome.setLive(false);
			this.live = false;
			PlaySound playSound = new PlaySound("blast.wav");
			playSound.play();
			//播放爆炸过程
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		
		return false;
	}
	public boolean isGood() {
		return good;
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
	
	
}

